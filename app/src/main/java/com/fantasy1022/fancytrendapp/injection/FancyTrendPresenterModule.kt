package com.fantasy1022.fancytrendapp.injection

import com.fantasy1022.fancytrendapp.common.SPUtils
import com.fantasy1022.fancytrendapp.data.TrendRepository
import com.fantasy1022.fancytrendapp.presentation.trend.FancyTrendContract
import com.fantasy1022.fancytrendapp.presentation.trend.FancyTrendPresenter

import javax.inject.Named

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler

/**
 * Created by fantasy_apple on 2017/6/25.
 */

@Module
class FancyTrendPresenterModule(private val ioScheduler: Scheduler,
                                private val mainScheduler: Scheduler,
                                private val spUtils: SPUtils) {

    @Provides
    @Named("ioScheduler")
    fun provideioScheduler(): Scheduler {
        return ioScheduler
    }

    @Provides
    @Named("mainScheduler")
    fun providemainScheduler(): Scheduler {
        return mainScheduler
    }

    @Provides
    fun provideSPUtils(): SPUtils {
        return spUtils
    }


    @Provides
    fun provideGoogleTrendPresenter(trendRepository: TrendRepository, @Named("ioScheduler") ioScheduler: Scheduler,
                                    @Named("mainScheduler") mainScheduler: Scheduler, spUtils: SPUtils)
            : FancyTrendContract.Presenter {
        return FancyTrendPresenter(spUtils, trendRepository, ioScheduler, mainScheduler)
    }
}
