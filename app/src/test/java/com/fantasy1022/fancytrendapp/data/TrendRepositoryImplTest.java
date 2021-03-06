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

package com.fantasy1022.fancytrendapp.data;

import android.support.v4.util.ArrayMap;

import com.fantasy1022.fancytrendapp.common.Constant;
import com.fantasy1022.fancytrendapp.data.TrendRepository;
import com.fantasy1022.fancytrendapp.data.TrendRepositoryImpl;
import com.fantasy1022.fancytrendapp.data.remote.FancyTrendRestService;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by fantasy1022 on 2017/2/23.
 */

public class TrendRepositoryImplTest {

    @Mock
    FancyTrendRestService fancyTrendRestService;

    private TrendRepository trendRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        trendRepository = new TrendRepositoryImpl(fancyTrendRestService);
    }

    @Test
    public void getGoogleTrend_200Response() {
        //Given
        when(fancyTrendRestService.getGoogleTrend()).thenReturn(Single.just(Constant.INSTANCE.generateTrendMap()));

        //When
        TestObserver<ArrayMap<String, List<String>>> subscriber = new TestObserver<>();
        trendRepository.getAllTrend().subscribe(subscriber);

        //Then
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();

        verify(fancyTrendRestService).getGoogleTrend();
    }

    @Test
    public void getGoogleTrend_200ResponseDelay5Secs() {
        //Given
        when(fancyTrendRestService.getGoogleTrend()).thenReturn(Single.just(Constant.INSTANCE.generateTrendMap()).delay(5, TimeUnit.SECONDS));

        //When
        TestObserver<ArrayMap<String, List<String>>> subscriber = new TestObserver<>();
        trendRepository.getAllTrend().subscribe(subscriber);

        //Then
        subscriber.awaitTerminalEvent();
        subscriber.assertError(TimeoutException.class);

        verify(fancyTrendRestService).getGoogleTrend();
    }


    @Test
    public void getGoogleTrend_IOException() {
        when(fancyTrendRestService.getGoogleTrend()).thenReturn(getIOExceptionError());

        TestObserver<ArrayMap<String, List<String>>> subscriber = new TestObserver<>();
        trendRepository.getAllTrend().subscribe(subscriber);

        //Then
        subscriber.awaitTerminalEvent();
        subscriber.assertError(IOException.class);


        verify(fancyTrendRestService, times(2)).getGoogleTrend();
    }

    @Test
    public void getGoogleTrend_OtherHttpError() {
        when(fancyTrendRestService.getGoogleTrend()).thenReturn(get403ForbiddenError());

        TestObserver<ArrayMap<String, List<String>>> subscriber = new TestObserver<>();
        trendRepository.getAllTrend().subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        subscriber.assertError(HttpException.class);

        verify(fancyTrendRestService, times(2)).getGoogleTrend();
    }

    @Test//Test retry logic
    public void getGoogleTrend_IOExceptionThan200Response() {
        //Given
        when(fancyTrendRestService.getGoogleTrend()).thenReturn(getIOExceptionError(), Single.just(Constant.INSTANCE.generateTrendMap()));

        //When
        TestObserver<ArrayMap<String, List<String>>> subscriber = new TestObserver<>();
        trendRepository.getAllTrend().subscribe(subscriber);

        //Then
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();

        verify(fancyTrendRestService, times(2)).getGoogleTrend();
    }


    private Single getIOExceptionError() {
        return Single.error(new IOException());
    }

    private Single<ArrayMap<String, List<String>>> get403ForbiddenError() {
        return Single.error(new HttpException(
                Response.error(403, ResponseBody.create(MediaType.parse("application/json"), "Forbidden"))));
    }


}
