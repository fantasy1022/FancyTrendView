/*
 * Copyright 2017 Fantasy Fang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fantasy1022.googletrendapp.presentation.trend;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.fantasy1022.googletrendapp.common.Constant;
import com.fantasy1022.googletrendapp.data.TrendRepository;
import com.fantasy1022.googletrendapp.presentation.base.BasePresenter;


import java.util.List;

import io.reactivex.Scheduler;

/**
 * Created by fantasy1022 on 2017/2/7.
 */

public class GoogleTrendPresenter extends BasePresenter<GoogleTrendContract.View> implements GoogleTrendContract.Presenter {
    private final String TAG = getClass().getSimpleName();
    private final Scheduler mainScheduler, ioScheduler;
    private TrendRepository trendRepository;
    private ArrayMap<String, List<String>> trendArrayMap;

    public GoogleTrendPresenter(TrendRepository trendRepository, Scheduler ioScheduler, Scheduler mainScheduler) {
        this.trendRepository = trendRepository;
        this.ioScheduler = ioScheduler;
        this.mainScheduler = mainScheduler;
    }

    @Override
    public void retrieveAllTrend() {
        checkViewAttached();
        addSubscription(trendRepository.getAllTrend()
                .doOnSubscribe(a -> getView().showLoading())
                .doFinally(() -> getView().hideLoading())
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe(trendArrayMap -> {
                    Log.d(TAG,"Get trend result successful");
                    GoogleTrendPresenter.this.trendArrayMap = trendArrayMap;
                    getView().showTrendResult(trendArrayMap.get(Constant.DEFAULT_COUNTRY_CODE));
                }, throwable -> {
                    Log.d(TAG,"Get trend result failure");
                    getView().showErrorScreen();
                }));
    }

    @Override
    public void retrieveSingleTrend(String countryCode, int position) {
        //Use init data to find countryCode and show
        getView().changeTrend(trendArrayMap.get(countryCode), position);
    }
}
