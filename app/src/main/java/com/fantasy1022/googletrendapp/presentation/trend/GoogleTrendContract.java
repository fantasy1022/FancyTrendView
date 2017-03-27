package com.fantasy1022.googletrendapp.presentation.trend;

import android.support.annotation.NonNull;

import com.fantasy1022.googletrendapp.presentation.base.MvpPresenter;
import com.fantasy1022.googletrendapp.presentation.base.MvpView;

import java.util.List;

/**
 * Created by fantasy1022 on 2017/2/7.
 */

public interface GoogleTrendContract {

    interface View extends MvpView {
        void showTrendResult(@NonNull List<String> trendList);

        void changeTrend(@NonNull List<String> trendList, int position);

        void showErrorScreen();

        void showLoading();

        void hideLoading();


    }

    interface Presenter extends MvpPresenter<View> {
        void retrieveAllTrend();

        void retrieveSingleTrend(String countryCode, int position);

    }

}
