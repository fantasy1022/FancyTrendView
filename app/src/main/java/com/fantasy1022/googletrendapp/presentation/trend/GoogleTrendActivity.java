package com.fantasy1022.googletrendapp.presentation.trend;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
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
import com.fantasy1022.googletrendapp.common.Constant;
import com.fantasy1022.googletrendapp.common.GridType;
import com.fantasy1022.googletrendapp.R;
import com.fantasy1022.googletrendapp.common.UiUtils;
import com.fantasy1022.googletrendapp.injection.Injection;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class GoogleTrendActivity extends AppCompatActivity implements GoogleTrendContract.View {

    public final String TAG = getClass().getSimpleName();

    @BindView(R.id.trendRecycleView)
    RecyclerView trendRecycleView;
    @BindView(R.id.noTrendInfoTxt)
    TextView noTrendInfoTxt;
//    @BindView(R.id.googleTrendView)
//    GoogleTrendView googleTrendView;
    View decorView;

    private GoogleTrendContract.Presenter googleTrendPresenter;
    private GoogleTrendAdapter googleTrendAdapter;
    private ArrayList<List<String>> trendItemList;

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
        googleTrendPresenter = new GoogleTrendPresenter(Injection.provideTrendRepo(), Schedulers.io(), AndroidSchedulers.mainThread());
        googleTrendPresenter.attachView(this);
        mVisible = true;
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {

        googleTrendAdapter = new GoogleTrendAdapter(this, trendItemList, Constant.DEFAULT_ROW_NUMBER, new GoogleTrendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (!mVisible) {
                    toggle();
                    delayedHide(AUTO_HIDE_DELAY_MILLIS);
                } else {
                    new MaterialDialog.Builder(GoogleTrendActivity.this)
                            .title(R.string.choose_country)
                            .items(R.array.trendCountryName)
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    String code = Injection.getCountryCode(String.valueOf(text));
                                    googleTrendPresenter.retrieveSingleTrend(code, position);
                                }
                            })
                            .show();
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
        googleTrendPresenter.retrieveAllTrend();
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
        if (id == R.id.gridNum) {
            Log.d(TAG, "gridNum");
            //TODO:Use custom view or MaterialDialog
            new MaterialDialog.Builder(GoogleTrendActivity.this)
                    .title(R.string.choose_grid)
                    .items(R.array.gridItemName)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            switch (which) {
                                case GridType.GRID_1_PLUS_1://1*1 1 item
                                    trendRecycleView.setLayoutManager(new CustomGridLayoutManager(GoogleTrendActivity.this, 1));//1 column
                                    googleTrendAdapter.changeRowNumber(1);
                                    UiUtils.animateForViewGroupTransition(trendRecycleView);
                                    break;
                                case GridType.GRID_2_PLUS_1:
                                    trendRecycleView.setLayoutManager(new CustomGridLayoutManager(GoogleTrendActivity.this, 1));//1 column
                                    googleTrendAdapter.changeRowNumber(2);
                                    UiUtils.animateForViewGroupTransition(trendRecycleView);
                                    break;
                                case GridType.GRID_3_PLUS_1:
                                    trendRecycleView.setLayoutManager(new CustomGridLayoutManager(GoogleTrendActivity.this, 1));//1 column
                                    googleTrendAdapter.changeRowNumber(3);
                                    UiUtils.animateForViewGroupTransition(trendRecycleView);
                                    break;
                                case GridType.GRID_1_PLUS_2:
                                    trendRecycleView.setLayoutManager(new CustomGridLayoutManager(GoogleTrendActivity.this, 2));
                                    googleTrendAdapter.changeRowNumber(1);
                                    UiUtils.animateForViewGroupTransition(trendRecycleView);
                                    break;
                                case GridType.GRID_2_PLUS_2:
                                    trendRecycleView.setLayoutManager(new CustomGridLayoutManager(GoogleTrendActivity.this, 2));
                                    googleTrendAdapter.changeRowNumber(2);
                                    UiUtils.animateForViewGroupTransition(trendRecycleView);
                                    break;
                                case GridType.GRID_3_PLUS_2:
                                    trendRecycleView.setLayoutManager(new CustomGridLayoutManager(GoogleTrendActivity.this, 2));
                                    googleTrendAdapter.changeRowNumber(3);
                                    UiUtils.animateForViewGroupTransition(trendRecycleView);
                                    break;
                                case GridType.GRID_1_PLUS_3:
                                    trendRecycleView.setLayoutManager(new CustomGridLayoutManager(GoogleTrendActivity.this, 3));
                                    googleTrendAdapter.changeRowNumber(1);
                                    UiUtils.animateForViewGroupTransition(trendRecycleView);
                                    break;
                                case GridType.GRID_2_PLUS_3:
                                    trendRecycleView.setLayoutManager(new CustomGridLayoutManager(GoogleTrendActivity.this, 3));
                                    googleTrendAdapter.changeRowNumber(2);
                                    UiUtils.animateForViewGroupTransition(trendRecycleView);
                                    break;
                                case GridType.GRID_3_PLUS_3:
                                    trendRecycleView.setLayoutManager(new CustomGridLayoutManager(GoogleTrendActivity.this, 3));
                                    googleTrendAdapter.changeRowNumber(3);
                                    UiUtils.animateForViewGroupTransition(trendRecycleView);
                                    break;
                            }
                        }
                    })
                    .show();


            return true;
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
