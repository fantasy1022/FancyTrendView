package com.fantasy1022.fancytrendapp.injection;

import com.fantasy1022.fancytrendapp.data.TrendRepository;
import com.fantasy1022.fancytrendapp.data.TrendRepositoryImpl;
import com.fantasy1022.fancytrendapp.data.remote.FancyTrendRestService;
import com.fantasy1022.fancytrendapp.data.remote.MockFancyTrendRestServiceImpl;


/**
 * Created by fantasy1022 on 2017/2/8.
 */

public class Injection {

    private static FancyTrendRestService googleTrendRestService;

    public static TrendRepository provideTrendRepo() {
        return new TrendRepositoryImpl(provideFancyTrendRestService());
    }

    private static FancyTrendRestService provideFancyTrendRestService() {
        if (googleTrendRestService == null) {
            googleTrendRestService = new MockFancyTrendRestServiceImpl();
        }
        return googleTrendRestService;
    }

}



