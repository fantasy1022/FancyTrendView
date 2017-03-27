package com.fantasy1022.googletrendapp.data;

import android.support.v4.util.ArrayMap;


import java.util.List;

import io.reactivex.Single;

/**
 * Created by fantasy1022 on 2017/2/7.
 */

public interface TrendRepository {

    Single<ArrayMap<String, List<String>>> getAllTrend();
}
