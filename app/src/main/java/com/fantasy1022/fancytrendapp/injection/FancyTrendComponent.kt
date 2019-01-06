package com.fantasy1022.fancytrendapp.injection

import com.fantasy1022.fancytrendapp.presentation.trend.FancyTrendActivity
import com.fantasy1022.fancytrendapp.scope.ActivityScope

import dagger.Subcomponent

/**
 * Created by fantasy_apple on 2017/6/25.
 */
@ActivityScope
@Subcomponent(modules = [FancyTrendPresenterModule::class])
interface FancyTrendComponent {
    fun inject(activity: FancyTrendActivity)
}
