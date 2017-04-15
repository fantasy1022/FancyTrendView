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

import android.util.Log;

import com.fantasy1022.fancytrendapp.common.Constant;
import com.fantasy1022.fancytrendapp.common.SPUtils;
import com.fantasy1022.fancytrendapp.data.TrendRepository;
import com.fantasy1022.fancytrendapp.presentation.base.BasePresenter;
import com.fantasy1022.fancytrendapp.presentation.trend.FancyTrendContract;
import com.fantasy1022.fancytrendapp.presentation.trend.FancyTrendPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by fantasy1022 on 2017/3/2.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class FancyTrendPresenterTest {

    @Mock
    SPUtils spUtils;
    @Mock
    TrendRepository trendRepository;
    @Mock
    FancyTrendContract.View view;

    FancyTrendPresenter fancyTrendPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(Log.class);//api-mockito in dependency
        fancyTrendPresenter = new FancyTrendPresenter(spUtils,trendRepository, Schedulers.trampoline(), Schedulers.trampoline());
        fancyTrendPresenter.attachView(view);
    }

    @Test
    public void getGoogleTrend_ReturnResult() {
        //Given
        when(trendRepository.getAllTrend()).thenReturn(Single.just(Constant.generateTrendMap()));
        when(spUtils.getString(Constant.SP_DEFAULT_COUNTRY_KEY,"")).thenReturn(Constant.DEFAULT_COUNTRY_CODE);
        //When
        fancyTrendPresenter.retrieveAllTrend();
        //Then
        verify(view).showLoading();
        verify(view).hideLoading();
        verify(view).showTrendResult(Constant.generateTrendMap().get(Constant.DEFAULT_COUNTRY_CODE));//TODO:Change to getDefaultcode
        verify(view, never()).showErrorScreen();
    }

    @Test
    public void getGoogleTrend_ReturnError() {
        //Given 
        when(trendRepository.getAllTrend()).thenReturn(Single.error(new IOException()));
        when(spUtils.getString(Constant.SP_DEFAULT_COUNTRY_KEY,"")).thenReturn(Constant.DEFAULT_COUNTRY_CODE);
        //When 
        fancyTrendPresenter.retrieveAllTrend();
        //Then 
        verify(view).showLoading();
        verify(view).hideLoading();
        verify(view, never()).showTrendResult(anyList());
        verify(view).showErrorScreen();
    }

    @Test
    public void changeCountryTrend() { //Get all trend firstly
        //Given 
        when(trendRepository.getAllTrend()).thenReturn(Single.just(Constant.generateTrendMap()));
        when(spUtils.getString(Constant.SP_DEFAULT_COUNTRY_KEY,"")).thenReturn(Constant.DEFAULT_COUNTRY_CODE);
        //When 
        fancyTrendPresenter.retrieveAllTrend();
        fancyTrendPresenter.retrieveSingleTrend(anyString(), anyInt());
        //Then 
        verify(view).changeTrend(anyList(), anyInt());
        verify(view, never()).showErrorScreen();
    }


    @Test(expected = BasePresenter.MvpViewNotAttachedException.class)
    public void search_NotAttached_ThrowsMvpException() {
        when(spUtils.getString(Constant.SP_DEFAULT_COUNTRY_KEY,"")).thenReturn(Constant.DEFAULT_COUNTRY_CODE);

        fancyTrendPresenter.detachView();
        fancyTrendPresenter.retrieveAllTrend();
        verify(view, never()).showLoading();
        verify(view, never()).showTrendResult(anyList());
    }


}
