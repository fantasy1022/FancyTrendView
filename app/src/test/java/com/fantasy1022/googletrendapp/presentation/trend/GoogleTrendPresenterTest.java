package com.fantasy1022.googletrendapp.presentation.trend;

import android.util.Log;

import com.fantasy1022.googletrendapp.common.Constant;
import com.fantasy1022.googletrendapp.data.TrendRepository;
import com.fantasy1022.googletrendapp.presentation.trend.GoogleTrendContract;
import com.fantasy1022.googletrendapp.presentation.trend.GoogleTrendPresenter;
import com.fantasy1022.googletrendapp.injection.Injection;
import com.fantasy1022.googletrendapp.presentation.base.BasePresenter;

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
public class GoogleTrendPresenterTest {

    @Mock
    TrendRepository trendRepository;
    @Mock
    GoogleTrendContract.View view;

    GoogleTrendPresenter googleTrendPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(Log.class);//api-mockito in dependency
        googleTrendPresenter = new GoogleTrendPresenter(trendRepository, Schedulers.trampoline(), Schedulers.trampoline());
        googleTrendPresenter.attachView(view);
    }

    @Test
    public void getGoogleTrend_ReturnResult() {
        //Given
        when(trendRepository.getAllTrend()).thenReturn(Single.just(Injection.generateTrendMap()));
        //When
        googleTrendPresenter.retrieveAllTrend();
        //Then
        verify(view).showLoading();
        verify(view).hideLoading();
        verify(view).showTrendResult(Injection.generateTrendMap().get(Constant.DEFAULT_COUNTRY_CODE));
        verify(view, never()).showErrorScreen();
    }

    @Test
    public void getGoogleTrend_ReturnError() {
        //Given 
        when(trendRepository.getAllTrend()).thenReturn(Single.error(new IOException()));
        //When 
        googleTrendPresenter.retrieveAllTrend();
        //Then 
        verify(view).showLoading();
        verify(view).hideLoading();
        verify(view, never()).showTrendResult(anyList());
        verify(view).showErrorScreen();
    }

    @Test
    public void changeCountryTrend() { //Get all trend firstly
        //Given 
        when(trendRepository.getAllTrend()).thenReturn(Single.just(Injection.generateTrendMap()));
        //When 
        googleTrendPresenter.retrieveAllTrend();
        googleTrendPresenter.retrieveSingleTrend(anyString(), anyInt());
        //Then 
        verify(view).changeTrend(anyList(), anyInt());
        verify(view, never()).showErrorScreen();
    }


    @Test(expected = BasePresenter.MvpViewNotAttachedException.class)
    public void search_NotAttached_ThrowsMvpException() {
        googleTrendPresenter.detachView();
        googleTrendPresenter.retrieveAllTrend();
        verify(view, never()).showLoading();
        verify(view, never()).showTrendResult(anyList());
    }


}
