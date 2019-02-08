package com.fantasy1022.fancytrendapp.injection

import com.fantasy1022.fancytrendapp.common.SPUtils
import com.fantasy1022.fancytrendapp.data.TrendRepository
import com.fantasy1022.fancytrendapp.presentation.trend.FancyTrendContract
import com.fantasy1022.fancytrendapp.presentation.trend.FancyTrendPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by fantasy_apple on 2017/6/25.
 */

@Module
class FancyTrendPresenterModule(private val spUtils: SPUtils) {

    @Provides
    fun provideSPUtils(): SPUtils {
        return spUtils
    }

    @Provides
    fun provideGoogleTrendPresenter(trendRepository: TrendRepository, spUtils: SPUtils)
            : FancyTrendContract.Presenter {
        return FancyTrendPresenter(spUtils, trendRepository)
    }
}
