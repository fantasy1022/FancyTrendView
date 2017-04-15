package com.fantasy1022.fancytrendapp.data.remote;

import android.support.v4.util.ArrayMap;

import com.fantasy1022.fancytrendapp.common.Constant;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by fantasy1022 on 2017/2/22.
 */

public class MockFancyTrendRestServiceImpl implements FancyTrendRestService {
    private static ArrayMap<String, List<String>> countryMap;
    private static Single dummyGoogleTrendResult = null;

    public MockFancyTrendRestServiceImpl() {
        countryMap = Constant.generateTrendMap();
    }

    @Override
    public Single<ArrayMap<String, List<String>>> getGoogleTrend() {
        if (dummyGoogleTrendResult != null) {
            return dummyGoogleTrendResult;
        }
        return Single.just(countryMap);
    }

    public static void setDummyGoogleTrendResult(Single result) {
        dummyGoogleTrendResult = result;
    }

}
