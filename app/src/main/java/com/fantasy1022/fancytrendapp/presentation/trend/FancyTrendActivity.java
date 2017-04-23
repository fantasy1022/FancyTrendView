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

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fantasy1022.fancytrendapp.R;
import com.fantasy1022.fancytrendapp.common.Constant;
import com.fantasy1022.fancytrendapp.common.GridType;
import com.fantasy1022.fancytrendapp.common.SPUtils;
import com.fantasy1022.fancytrendapp.common.UiUtils;
import com.fantasy1022.fancytrendapp.injection.Injection;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class FancyTrendActivity extends AppCompatActivity implements FancyTrendContract.View {

    public final String TAG = getClass().getSimpleName();

    @BindView(R.id.trendRecycleView)
    RecyclerView trendRecycleView;
    @BindView(R.id.noTrendInfoTxt)
    TextView noTrendInfoTxt;
    View decorView;

    private FancyTrendContract.Presenter fancyTrendPresenter;
    private FancyTrendAdapter googleTrendAdapter;
    private ArrayList<List<String>> trendItemList;
    private String[] countries;
    private FirebaseAnalytics firebaseAnalytics;
    private static final boolean AUTO_HIDE = true;
    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    //private TextView mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
//    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            if (AUTO_HIDE) {
//                delayedHide(AUTO_HIDE_DELAY_MILLIS);
//            }
//            return false;
//        }
//    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_trend);
        decorView = getWindow().getDecorView();
        ButterKnife.bind(this);
        fancyTrendPresenter = new FancyTrendPresenter(new SPUtils(this), Injection.provideTrendRepo(), Schedulers.io(), AndroidSchedulers.mainThread());
        fancyTrendPresenter.attachView(this);
        fancyTrendPresenter.generateCountryCodeMapping();
        mVisible = true;
        countries = getResources().getStringArray(R.array.trend_country_name);
        setUpRecyclerView();
        fancyTrendPresenter.retrieveAllTrend();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this); 
    }

    private void setUpRecyclerView() {

        googleTrendAdapter = new FancyTrendAdapter(this, trendItemList, Constant.DEFAULT_ROW_NUMBER, new FancyTrendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, String trend, int position) {
                if (!mVisible) {
                    FancyTrendActivity.this.toggle();
                    FancyTrendActivity.this.delayedHide(AUTO_HIDE_DELAY_MILLIS);
                } else {
                    Bundle bundle = new Bundle();
                    if (fancyTrendPresenter.getClickBehavior() == SPUtils.ClickBehaviorItem.googleSearch) {
                        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "search:" + trend);
                        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                        //TODO:Use chrome tab to implement
                        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                        intent.putExtra(SearchManager.QUERY, trend);
                        startActivity(intent);
                    } else if (fancyTrendPresenter.getClickBehavior() == SPUtils.ClickBehaviorItem.singlecountry) {
                        new MaterialDialog.Builder(FancyTrendActivity.this)
                                .title(R.string.choose_country)
                                .items(R.array.trend_country_name)
                                .itemsCallback((dialog, view, which, text) -> {
                                    bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "single country");
                                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                                    String code = Constant.getCountryCode(String.valueOf(text));
                                    fancyTrendPresenter.retrieveSingleTrend(code, position);
                                })
                                .show();
                    }
                }
            }
        });
        trendRecycleView.setLayoutManager(new CustomGridLayoutManager(this, Constant.DEFAULT_COLUMN_NUMBER));
        trendRecycleView.setAdapter(googleTrendAdapter);
        trendRecycleView.setHasFixedSize(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public void showTrendResult(@NonNull List<String> trendList) {
        Log.d(TAG, "showTrendResult");
        trendItemList = new ArrayList<>();//Init
        for (int i = 0; i < Constant.DEFAULT_TREND_ITEM_NUMBER; i++) {
            trendItemList.add(trendList);
        }
        googleTrendAdapter.updateAllList(trendItemList);
        //  googleTrendAdapter.updateAllList(Injection.getMockTrendList());

    }

    @Override
    public void changeTrend(@NonNull List<String> trendList, int position) {
        if (trendList != null) {
            googleTrendAdapter.updateSingleList(trendList, position);
        }
    }

    @Override
    public void showErrorScreen() {
        noTrendInfoTxt.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {
        Log.d(TAG, "showLoading");
    }

    @Override
    public void hideLoading() {
        Log.d(TAG, "hideLoading");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Bundle bundle = new Bundle();
        if (id == R.id.gridNum) {
            new MaterialDialog.Builder(FancyTrendActivity.this)
                    .title(R.string.choose_grid)
                    .items(R.array.grid_item_name)
                    .itemsCallback((dialog, view, which, text) -> {
                        switch (which) {
                            case GridType.GRID_1_PLUS_1://1*1 1 item
                                bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "gridNum");
                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "1*1");
                                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);

                                trendRecycleView.setLayoutManager(new CustomGridLayoutManager(FancyTrendActivity.this, 1));//1 column
                                googleTrendAdapter.changeRowNumber(1);
                                UiUtils.animateForViewGroupTransition(trendRecycleView);
                                break;
                            case GridType.GRID_2_PLUS_1:
                                bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "gridNum");
                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "2*1");
                                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);

                                trendRecycleView.setLayoutManager(new CustomGridLayoutManager(FancyTrendActivity.this, 1));//1 column
                                googleTrendAdapter.changeRowNumber(2);
                                UiUtils.animateForViewGroupTransition(trendRecycleView);
                                break;
                            case GridType.GRID_3_PLUS_1:
                                bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "gridNum");
                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "3*1");
                                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);

                                trendRecycleView.setLayoutManager(new CustomGridLayoutManager(FancyTrendActivity.this, 1));//1 column
                                googleTrendAdapter.changeRowNumber(3);
                                UiUtils.animateForViewGroupTransition(trendRecycleView);
                                break;
                            case GridType.GRID_1_PLUS_2:
                                bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "gridNum");
                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "1*2");
                                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);

                                trendRecycleView.setLayoutManager(new CustomGridLayoutManager(FancyTrendActivity.this, 2));
                                googleTrendAdapter.changeRowNumber(1);
                                UiUtils.animateForViewGroupTransition(trendRecycleView);
                                break;
                            case GridType.GRID_2_PLUS_2:
                                bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "gridNum");
                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "2*2");
                                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);

                                trendRecycleView.setLayoutManager(new CustomGridLayoutManager(FancyTrendActivity.this, 2));
                                googleTrendAdapter.changeRowNumber(2);
                                UiUtils.animateForViewGroupTransition(trendRecycleView);
                                break;
                            case GridType.GRID_3_PLUS_2:
                                bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "gridNum");
                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "3*2");
                                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);

                                trendRecycleView.setLayoutManager(new CustomGridLayoutManager(FancyTrendActivity.this, 2));
                                googleTrendAdapter.changeRowNumber(3);
                                UiUtils.animateForViewGroupTransition(trendRecycleView);
                                break;
                            case GridType.GRID_1_PLUS_3:
                                bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "gridNum");
                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "1*3");
                                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);

                                trendRecycleView.setLayoutManager(new CustomGridLayoutManager(FancyTrendActivity.this, 3));
                                googleTrendAdapter.changeRowNumber(1);
                                UiUtils.animateForViewGroupTransition(trendRecycleView);
                                break;
                            case GridType.GRID_2_PLUS_3:
                                bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "gridNum");
                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "2*3");
                                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);

                                trendRecycleView.setLayoutManager(new CustomGridLayoutManager(FancyTrendActivity.this, 3));
                                googleTrendAdapter.changeRowNumber(2);
                                UiUtils.animateForViewGroupTransition(trendRecycleView);
                                break;
                            case GridType.GRID_3_PLUS_3:
                                bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "gridNum");
                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "3*3");
                                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);

                                trendRecycleView.setLayoutManager(new CustomGridLayoutManager(FancyTrendActivity.this, 3));
                                googleTrendAdapter.changeRowNumber(3);
                                UiUtils.animateForViewGroupTransition(trendRecycleView);
                                break;
                        }
                    })
                    .show();
            return true;
        } else if (id == R.id.defaultCountry) {
            new MaterialDialog.Builder(FancyTrendActivity.this)
                    .title(R.string.choose_default_country)
                    .items(R.array.trend_country_name) //get
                    .itemsCallbackSingleChoice(fancyTrendPresenter.getDefaultCountryIndex(), (dialog, itemView, which, text) -> {
                        Log.d(TAG, "onSelection:" + which);
                        String code = Constant.getCountryCode(countries[which]);
                        fancyTrendPresenter.setDefaultCountryCode(code);
                        fancyTrendPresenter.setDefaultCountryIndex(which);

                        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "defaultCountry");
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, countries[which]);
                        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);

                        for (int i = 0; i < Constant.DEFAULT_TREND_ITEM_NUMBER; i++) {
                            fancyTrendPresenter.retrieveSingleTrend(code, i);
                        }
                        Log.d(TAG, "code:" + code);
                        return true;
                    })
                    .positiveText(android.R.string.ok)
                    .show();
        } else if (id == R.id.clickBehavior) {
            new MaterialDialog.Builder(FancyTrendActivity.this)
                    .title(R.string.choose_click_behavior)
                    .items(R.array.click_behavior_item_name)
                    .itemsCallbackSingleChoice(fancyTrendPresenter.getClickBehavior(), (dialog, itemView, which, text) -> {
                        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "clickBehavior");
                        bundle.putInt(FirebaseAnalytics.Param.ITEM_NAME, which);
                        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);

                        fancyTrendPresenter.setClickBehavior(which);
                        return true;
                    })
                    .positiveText(android.R.string.ok)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    public boolean isVisible() {
        return mVisible;
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //Screen changes orientation, it needs to update item height.
        googleTrendAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }


}
