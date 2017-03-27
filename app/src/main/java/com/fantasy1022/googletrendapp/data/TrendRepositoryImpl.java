package com.fantasy1022.googletrendapp.data;

import android.support.v4.util.ArrayMap;

import com.fantasy1022.googletrendapp.data.remote.GoogleTrendRestService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;


/**
 * Created by fantasy1022 on 2017/2/7.
 */

public class TrendRepositoryImpl implements TrendRepository {

    private GoogleTrendRestService googleTrendRestService;

    public TrendRepositoryImpl(GoogleTrendRestService googleTrendRestService) {
        this.googleTrendRestService = googleTrendRestService;
    }

    @Override
    public Single<ArrayMap<String, List<String>>> getAllTrend() {
        return Single.defer(() -> googleTrendRestService.getGoogleTrend())
                .retry(1)
                .timeout(3, TimeUnit.SECONDS);
        //TODO:Check  Retry mechanisms, backoff mechanisms and error handling
    }

}
