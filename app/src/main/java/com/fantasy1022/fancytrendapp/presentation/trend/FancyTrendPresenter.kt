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
import com.fantasy1022.fancytrendapp.presentation.base.BasePresenter
import com.fantasy1022.fancytrendapp.data.TrendRepository
import java.util.Locale

import io.reactivex.Scheduler

/**
 * Created by fantasy1022 on 2017/2/7.
 */

class FancyTrendPresenter(private val spUtils: SPUtils, private val trendRepository: TrendRepository,
                          private val ioScheduler: Scheduler, private val mainScheduler: Scheduler)
    : BasePresenter<FancyTrendContract.View>(), FancyTrendContract.Presenter {
    private val TAG = javaClass.simpleName
    private var trendArrayMap: Map<String, List<String>> = emptyMap()


    override var defaultCountryCode: String
        get() {
            val result = spUtils.getString(Constant.SP_DEFAULT_COUNTRY_KEY, "")
            return if (!result.isEmpty()) {
                result
            } else {//Use system language for first time
                val country = Locale.getDefault().language
                Log.d(TAG, "country:$country")
                when (country) {
                    "zh" -> "12"
                    "en" -> "1"
                    else//TODO:map other country
                    -> "1"
                }
            }
        }
        set(value) {
            spUtils.putString(Constant.SP_DEFAULT_COUNTRY_KEY, value)
        }

    override var defaultCountryIndex: Int
        get() {
            val result = spUtils.getInt(Constant.SP_DEFAULT_COUNTRY_INDEX_KEY, -1)
            return if (result != -1) {
                result
            } else {//Use system language for first time
                val country = Locale.getDefault().language
                Log.d(TAG, "country:$country")
                when (country) {
                    "zh" -> 40
                    "en" -> 45
                    else//TODO:map other country
                    -> 45
                }
            }
        }
        set(value) {
            spUtils.putInt(Constant.SP_DEFAULT_COUNTRY_INDEX_KEY, value)
        }
    override var clickBehavior: Int
        get() = spUtils.getInt(Constant.SP_DEFAULT_CLICK_BEHAVIOR_KEY, 0)
        set(value) {
            spUtils.putInt(Constant.SP_DEFAULT_CLICK_BEHAVIOR_KEY, value)
        }

    override fun generateCountryCodeMapping() {
        Constant.generateCountryCodeMapping()
    }

    override fun retrieveAllTrend() {
        checkViewAttached()
        addSubscription(trendRepository.allTrend
                .doOnSubscribe { view?.showLoading() }
                .doFinally { view?.hideLoading() }
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe({ trendArrayMap ->
                    Log.d(TAG, "Get trend result successful")
                    this@FancyTrendPresenter.trendArrayMap = trendArrayMap
                    //TODO:Use last choice
                    view?.showTrendResult(trendArrayMap["taiwan"]?.toList() ?: emptyList())//Taiwan
                }, {
                    Log.d(TAG, "Get trend result failure")
                    view?.showErrorScreen()
                }))
    }

    override fun retrieveSingleTrend(countryCode: String, position: Int) {
        //Use init data to find countryCode and show
        val rr = trendArrayMap["12"]
        //讀完collection，再來改
        trendArrayMap?.let {
            view?.changeTrend(rr ?: emptyList(), position)
        }
    }
}
