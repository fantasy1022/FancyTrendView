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

package com.fantasy1022.fancytrendapp


import android.app.Application
import com.fantasy1022.fancytrendapp.common.SPUtils
import com.fantasy1022.fancytrendapp.injection.FancyTrendComponent
import com.fantasy1022.fancytrendapp.injection.FancyTrendPresenterModule
import com.fantasy1022.fancytrendapp.injection.NetworkModule

/**
 * Created by fantasy1022 on 2017/3/15.
 */

class FancyTrendApplication : Application() {

    lateinit var fancyTrendComponent: FancyTrendComponent
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
                .networkModule(NetworkModule)
                .build()

        fancyTrendComponent = appComponent.plus(FancyTrendPresenterModule(SPUtils(this)))
    }
}
