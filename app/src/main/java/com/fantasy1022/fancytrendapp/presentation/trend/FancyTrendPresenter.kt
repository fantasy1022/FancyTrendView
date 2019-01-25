/*
 * Copyright 2017 Fantasy Fang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fantasy1022.fancytrendapp.presentation.trend

import android.util.Log
import com.fantasy1022.fancytrendapp.common.Constant
import com.fantasy1022.fancytrendapp.common.SPUtils
import com.fantasy1022.fancytrendapp.data.TrendRepository
import com.fantasy1022.fancytrendapp.presentation.base.BasePresenter
import io.reactivex.Scheduler
import kotlinx.coroutines.*
import java.util.*

/**
 * Created by fantasy1022 on 2017/2/7.
 */

class FancyTrendPresenter(private val spUtils: SPUtils, private val trendRepository: TrendRepository,
                          private val ioScheduler: Scheduler, private val mainScheduler: Scheduler)
    : BasePresenter<FancyTrendContract.View>(), FancyTrendContract.Presenter {
    private val TAG = javaClass.simpleName
    private var trendArrayMap: Map<String, List<String>> = emptyMap()
    private var myJob: Job? = null

    override var defaultCountryIndex: Int
        get() {
            val result = spUtils.getInt(Constant.SP_DEFAULT_COUNTRY_INDEX_KEY, -1)
            return if (result != -1) {
                result
            } else {//Use system language for first time
                val country = Locale.getDefault().language
                when (country) {
                    "zh" -> 41
                    else -> 46
                }
            }
        }
        set(value) {
            spUtils.putInt(Constant.SP_DEFAULT_COUNTRY_INDEX_KEY, value)
        }

    override var defaultCountryName: String
        get() {
            val result = spUtils.getString(Constant.SP_DEFAULT_COUNTRY_NAME_KEY, "")
            return if (result.isNotEmpty()) {
                result
            } else {//Use system language for first time
                val country = Locale.getDefault().language
                Log.d(TAG, "country:$country")
                when (country) {
                    "zh" -> "Taiwan"
                    else -> "United States"
                }
            }
        }
        set(value) {
            spUtils.putString(Constant.SP_DEFAULT_COUNTRY_NAME_KEY, value)
        }

    override fun retrieveAllTrend() {
        checkViewAttached()

        myJob = CoroutineScope(Dispatchers.IO).launch {
            val result = trendRepository.getAllTrendCoroutine()
            withContext(Dispatchers.Main) {
                Log.d(TAG, "Get trend result successful")
                this@FancyTrendPresenter.trendArrayMap = result
                view?.showTrendResult(trendArrayMap[defaultCountryName]?.toList() ?: emptyList())
            }
        }
    }

    override fun getAllCountryNames(): List<String> = trendArrayMap.keys.toList().sorted()

    override fun retrieveSingleTrend(countryName: String, position: Int) {
        trendArrayMap?.let {
            view?.changeTrend(it[countryName] ?: emptyList(), position)
        }
    }

    override fun cancelJob() {
        myJob?.cancel()
    }
}
