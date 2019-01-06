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

package com.fantasy1022.fancytrendapp.injection;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.fantasy1022.fancytrendapp.common.Constant;
import com.fantasy1022.fancytrendapp.data.TrendRepository;
import com.fantasy1022.fancytrendapp.data.TrendRepositoryImpl;
import com.fantasy1022.fancytrendapp.data.remote.FancyTrendRestService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fantasy1022 on 2017/2/8.
 */

public class Injection {
    private static final String BASE_URL = Constant.INSTANCE.GOOGLE_TREND_BASE_URL;
    private static OkHttpClient okHttpClient;
    private static FancyTrendRestService fancyTrendRestService;
    private static Retrofit retrofitInstance;


    public static TrendRepository provideTrendRepo() {
        return new TrendRepositoryImpl(provideFancyTrendRestService());
    }

    private static FancyTrendRestService provideFancyTrendRestService() {
        if (fancyTrendRestService == null) {
            fancyTrendRestService = getRetrofitInstance().create(FancyTrendRestService.class);
        }
        return fancyTrendRestService;
    }


    private static Retrofit getRetrofitInstance() {
        if (retrofitInstance == null) {
            Retrofit.Builder retrofit = new Retrofit.Builder().client(Injection.getOkHttpClient()).baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
            retrofitInstance = retrofit.build();

        }
        return retrofitInstance;
    }


    private static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient = new OkHttpClient.Builder().addInterceptor(logging).build();
        }
        return okHttpClient;
    }

    //For test using gson to transform response
    public static void getTrendUsingOkhttp() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.INSTANCE.GOOGLE_TREND_BASE_URL + "/api/terms/")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Test", "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                Type trendMapType = new TypeToken<ArrayMap<String, ArrayList<String>>>() {
                }.getType();
                ArrayMap<String, ArrayList<String>> map = gson.fromJson(response.body().string(), trendMapType);
            }
        });
    }
}



