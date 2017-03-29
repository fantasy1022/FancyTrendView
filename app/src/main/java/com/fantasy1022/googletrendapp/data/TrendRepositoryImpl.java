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
