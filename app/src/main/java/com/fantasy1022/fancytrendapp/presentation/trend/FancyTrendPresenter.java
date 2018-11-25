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

package com.fantasy1022.fancytrendapp.presentation.trend;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.fantasy1022.fancytrendapp.common.Constant;
import com.fantasy1022.fancytrendapp.common.SPUtils;
import com.fantasy1022.fancytrendapp.presentation.base.BasePresenter;
import com.fantasy1022.fancytrendapp.data.TrendRepository;

import java.util.List;
import java.util.Locale;

import io.reactivex.Scheduler;

/**
 * Created by fantasy1022 on 2017/2/7.
 */

public class FancyTrendPresenter extends BasePresenter<FancyTrendContract.View> implements FancyTrendContract.Presenter {
    private final String TAG = getClass().getSimpleName();
    private final Scheduler mainScheduler, ioScheduler;
    private SPUtils spUtils;
    private TrendRepository trendRepository;
    private ArrayMap<String, List<String>> trendArrayMap;

    public FancyTrendPresenter(SPUtils spUtils, TrendRepository trendRepository, Scheduler ioScheduler, Scheduler mainScheduler) {
        this.trendRepository = trendRepository;
        this.ioScheduler = ioScheduler;
        this.mainScheduler = mainScheduler;
        this.spUtils = spUtils;
    }

    @Override
    public String getDefaultCountryCode() {
        String result = spUtils.getString(Constant.SP_DEFAULT_COUNTRY_KEY, "");
        if (!result.isEmpty()) {
            return result;
        } else {//Use system language for first time
            String country = Locale.getDefault().getLanguage();
            Log.d(TAG, "country:" + country);
            switch (country) {
                case "zh":
                    return "12";
                case "en":
                    return "1";
                default://TODO:map other country
                    return "1";
            }
        }
    }

    @Override
    public void setDefaultCountryCode(String code) {//Store country code
        spUtils.putString(Constant.SP_DEFAULT_COUNTRY_KEY, code);
    }

    @Override
    public int getDefaultCountryIndex() {
        int result = spUtils.getInt(Constant.SP_DEFAULT_COUNTRY_INDEX_KEY, -1);
        if (result != -1) {
            return result;
        } else {//Use system language for first time
            String country = Locale.getDefault().getLanguage();
            Log.d(TAG, "country:" + country);
            switch (country) {
                case "zh":
                    return 40;
                case "en":
                    return 45;
                default://TODO:map other country
                    return 45;
            }
        }
    }

    @Override
    public void setDefaultCountryIndex(int index) {
        spUtils.putInt(Constant.SP_DEFAULT_COUNTRY_INDEX_KEY, index);
    }

    @Override
    public int getClickBehavior() {
        return spUtils.getInt(Constant.SP_DEFAULT_CLICK_BEHAVIOR_KEY, 0);
    }

    @Override
    public void setClickBehavior(@SPUtils.ClickBehaviorItem int index) {
        spUtils.putInt(Constant.SP_DEFAULT_CLICK_BEHAVIOR_KEY, index);
    }

    @Override
    public void generateCountryCodeMapping() {
        Constant.generateCountryCodeMapping();
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
                    Log.d(TAG, "Get trend result successful");
                    FancyTrendPresenter.this.trendArrayMap = trendArrayMap;
                    getView().showTrendResult(trendArrayMap.get("taiwan"));//Taiwan
                }, throwable -> {
                    Log.d(TAG, "Get trend result failure");
                    getView().showErrorScreen();
                }));
    }

    @Override
    public void retrieveSingleTrend(String countryCode, int position) {
        //Use init data to find countryCode and show
        getView().changeTrend(trendArrayMap.get(countryCode), position);
    }
}
