package com.fantasy1022.googletrendapp;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by fantasy1022 on 2017/3/15.
 */

public class GoogleTrendApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }
}
