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

import android.support.annotation.NonNull;

import com.fantasy1022.fancytrendapp.presentation.base.MvpView;
import com.fantasy1022.fancytrendapp.presentation.base.MvpPresenter;

import java.util.List;

/**
 * Created by fantasy1022 on 2017/2/7.
 */

public interface FancyTrendContract {

    interface View extends MvpView {
        void showTrendResult(@NonNull List<String> trendList);

        void changeTrend(@NonNull List<String> trendList, int position);

        void showErrorScreen();

        void showLoading();

        void hideLoading();


    }

    interface Presenter extends MvpPresenter<View> {
        void generateCountryCodeMapping();

        String getDefaultCountryCode();

        void setDefaultCountryCode(String code);

        int getDefaultCountryIndex();

        void setDefaultCountryIndex(int index);

        int getClickBehavior();

        void setClickBehavior(int index);

        void retrieveAllTrend();

        void retrieveSingleTrend(String countryCode, int position);

    }

}
