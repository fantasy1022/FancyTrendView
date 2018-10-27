package com.fantasy1022.fancytrendapp.injection;

import com.fantasy1022.fancytrendapp.common.SPUtils;
import com.fantasy1022.fancytrendapp.data.TrendRepository;
import com.fantasy1022.fancytrendapp.presentation.trend.FancyTrendContract;
import com.fantasy1022.fancytrendapp.presentation.trend.FancyTrendPresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

/**
 * Created by fantasy_apple on 2017/6/25.
 */

@Module
public class FancyTrendPresenterModule {
    private Scheduler ioScheduler;
    private Scheduler mainScheduler;
    private SPUtils spUtils;

    public FancyTrendPresenterModule(Scheduler ioScheduler, Scheduler mainScheduler, SPUtils spUtils) {
        this.ioScheduler = ioScheduler;
        this.mainScheduler = mainScheduler;
        this.spUtils = spUtils;
    }

    @Provides
    @Named("ioScheduler")
    public Scheduler provideioScheduler() {
        return ioScheduler;
    }

    @Provides
    @Named("mainScheduler")
    public Scheduler providemainScheduler() {
        return mainScheduler;
    }

    @Provides
    public SPUtils provideSPUtils() {
        return spUtils;
    }


    @Provides
    public FancyTrendContract.Presenter provideGoogleTrendPresenter(TrendRepository trendRepository, @Named("ioScheduler") Scheduler ioScheduler, @Named("mainScheduler") Scheduler mainScheduler, SPUtils spUtils) {
        return new FancyTrendPresenter(spUtils, trendRepository, ioScheduler, mainScheduler);
    }
}
