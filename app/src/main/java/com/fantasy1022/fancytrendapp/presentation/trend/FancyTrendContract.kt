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

import com.fantasy1022.fancytrendapp.common.SPUtils
import com.fantasy1022.fancytrendapp.presentation.base.MvpPresenter
import com.fantasy1022.fancytrendapp.presentation.base.MvpView

/**
 * Created by fantasy1022 on 2017/2/7.
 */

interface FancyTrendContract {

    interface View : MvpView {
        fun showTrendResult(trendList: List<String>)
        fun changeTrend(trendList: List<String>, position: Int)
        fun showErrorScreen()
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter : MvpPresenter<View> {
        var defaultCountryCode: String
        var defaultCountryIndex: Int
        var clickBehavior: SPUtils.ClickBehavior
        fun getAllCountryNames():List<String>
        fun retrieveAllTrend()
        fun retrieveSingleTrend(countryName: String, position: Int)
        fun cancelJob()
    }

}
