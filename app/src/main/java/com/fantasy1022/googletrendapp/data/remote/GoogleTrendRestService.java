package com.fantasy1022.googletrendapp.data.remote;


import android.support.v4.util.ArrayMap;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

/**
 * Created by fantasy1022 on 2017/2/7.
 */

public interface GoogleTrendRestService {

    @GET("/api/terms/")
    Single<ArrayMap<String, List<String>>> getGoogleTrend();
}
