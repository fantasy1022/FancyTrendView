package com.fantasy1022.fancytrendapp;

import com.fantasy1022.fancytrendapp.injection.FancyTrendComponent;
import com.fantasy1022.fancytrendapp.injection.FancyTrendPresenterModule;
import com.fantasy1022.fancytrendapp.injection.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by fantasy_apple on 2017/6/25.
 */


@Singleton
@Component(modules = {NetworkModule.class})
public interface AppComponent {
    FancyTrendComponent plus(FancyTrendPresenterModule fancyTrendPresenterModule);
}
