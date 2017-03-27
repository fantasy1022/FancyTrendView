package com.fantasy1022.googletrendapp.presentation.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * Created by fantasy1022 on 2017/2/7.
 */

public class BasePresenter <T extends MvpView> implements MvpPresenter<T> {

    private T view;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void attachView(T mvpView) {
        view = mvpView;
    }

    @Override
    public void detachView() {
        compositeDisposable.clear();
        view = null;
    }

    public T getView() {
        return view;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) {
            throw new MvpViewNotAttachedException();
        }
    }

    private boolean isViewAttached() {
        return view != null;
    }


    protected void addSubscription(Disposable subscription) {
        this.compositeDisposable.add(subscription);
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before" + " requesting data to the Presenter");
        }
    }
}

