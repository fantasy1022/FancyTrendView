package com.fantasy1022.googletrendapp.presentation.base;

/**
 * Created by fantasy1022 on 2017/2/7.
 */

public interface MvpPresenter<V extends MvpView> {

    void attachView(V mvpView);

    void detachView();
}
