package com.fantasy1022.googletrendapp.injection;

import com.fantasy1022.googletrendapp.data.TrendRepository;
import com.fantasy1022.googletrendapp.data.TrendRepositoryImpl;
import com.fantasy1022.googletrendapp.data.remote.GoogleTrendRestService;
import com.fantasy1022.googletrendapp.data.remote.MockGoogleTrendRestServiceImpl;

/**
 * Created by fantasy1022 on 2017/2/8.
 */

public class Injection {

    private static GoogleTrendRestService googleTrendRestService;

    public static TrendRepository provideTrendRepo() {
        return new TrendRepositoryImpl(provideGoogleTrendRestService());
    }

    private static GoogleTrendRestService provideGoogleTrendRestService() {
        if (googleTrendRestService == null) {
            googleTrendRestService = new MockGoogleTrendRestServiceImpl();
        }
        return googleTrendRestService;
    }

}



