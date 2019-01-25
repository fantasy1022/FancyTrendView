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

package com.fantasy1022.fancytrendapp.presentation.trend

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.fantasy1022.fancytrendapp.FancyTrendApplication
import com.fantasy1022.fancytrendapp.R
import com.fantasy1022.fancytrendapp.common.Constant
import com.fantasy1022.fancytrendapp.common.GridType
import com.fantasy1022.fancytrendapp.common.UiUtils
import kotlinx.android.synthetic.main.activity_google_trend.*
import javax.inject.Inject


class FancyTrendActivity : AppCompatActivity(), FancyTrendContract.View {
    val TAG = javaClass.simpleName

    lateinit var decorView: View
    @Inject
    lateinit var fancyTrendPresenter: FancyTrendContract.Presenter
    private lateinit var googleTrendAdapter: FancyTrendAdapter
    private var trendItemList: MutableList<List<String>> = mutableListOf()
    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private val mShowPart2Runnable = Runnable {
        // Delayed display of UI elements
        val actionBar = supportActionBar
        actionBar?.show()
    }
    var isVisibleContent = false

    private val mHideRunnable = Runnable { hide() }

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_trend)
        decorView = window.decorView
        (application as FancyTrendApplication).fancyTrendComponent.inject(this)
        fancyTrendPresenter.attachView(this)
        isVisibleContent = true
        setUpRecyclerView()
        //TODO:Show loading
        fancyTrendPresenter.retrieveAllTrend()
    }

    private fun setUpRecyclerView() {
        googleTrendAdapter = FancyTrendAdapter(this, trendItemList, Constant.DEFAULT_ROW_NUMBER,
                object : FancyTrendAdapter.OnItemClickListener {
                    override fun onItemClick(v: View, trend: String, position: Int) {
                        if (!isVisibleContent) {
                            this@FancyTrendActivity.toggle()
                            this@FancyTrendActivity.delayedHide(AUTO_HIDE_DELAY_MILLIS)
                        } else {
                            //TODO:Use chrome tab to implement
                            val intent = Intent(Intent.ACTION_WEB_SEARCH)
                            intent.putExtra(SearchManager.QUERY, trend)
                            startActivity(intent)
                        }
                    }
                })
        trendRecycleView.layoutManager = CustomGridLayoutManager(this, Constant.DEFAULT_COLUMN_NUMBER)
        trendRecycleView.adapter = googleTrendAdapter
        trendRecycleView.setHasFixedSize(true)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    override fun showTrendResult(trendList: List<String>) {
        Log.d(TAG, "showTrendResult")
        for (i in 0 until Constant.DEFAULT_TREND_ITEM_NUMBER) {
            trendItemList.add(trendList)
        }
        googleTrendAdapter.updateAllList(trendItemList)
        //  googleTrendAdapter.updateAllList(Injection.getMockTrendList());
    }

    override fun changeTrend(trendList: List<String>, position: Int) {
        if (trendList != null) {
            googleTrendAdapter.updateSingleList(trendList, position)
        }
    }

    override fun showErrorScreen() {
        noTrendInfoTxt.visibility = View.VISIBLE
    }

    override fun showLoading() {
        Log.d(TAG, "showLoading")
    }

    override fun hideLoading() {
        Log.d(TAG, "hideLoading")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.gridNum -> {
                MaterialDialog.Builder(this@FancyTrendActivity)
                        .title(R.string.choose_grid)
                        .items(R.array.grid_item_name)
                        .itemsCallback { _, _, which, _ ->
                            when (which) {
                                GridType.GRID_1_PLUS_1//1*1 1 item
                                -> {
                                    trendRecycleView.layoutManager = CustomGridLayoutManager(this@FancyTrendActivity, 1)//1 column
                                    googleTrendAdapter.changeRowNumber(1)
                                    UiUtils.animateForViewGroupTransition(trendRecycleView)
                                }
                                GridType.GRID_2_PLUS_1 -> {
                                    trendRecycleView.layoutManager = CustomGridLayoutManager(this@FancyTrendActivity, 1)//1 column
                                    googleTrendAdapter.changeRowNumber(2)
                                    UiUtils.animateForViewGroupTransition(trendRecycleView)
                                }
                                GridType.GRID_3_PLUS_1 -> {
                                    trendRecycleView.layoutManager = CustomGridLayoutManager(this@FancyTrendActivity, 1)//1 column
                                    googleTrendAdapter.changeRowNumber(3)
                                    UiUtils.animateForViewGroupTransition(trendRecycleView)
                                }
                                GridType.GRID_1_PLUS_2 -> {
                                    trendRecycleView.layoutManager = CustomGridLayoutManager(this@FancyTrendActivity, 2)
                                    googleTrendAdapter.changeRowNumber(1)
                                    UiUtils.animateForViewGroupTransition(trendRecycleView)
                                }
                                GridType.GRID_2_PLUS_2 -> {
                                    trendRecycleView.layoutManager = CustomGridLayoutManager(this@FancyTrendActivity, 2)
                                    googleTrendAdapter.changeRowNumber(2)
                                    UiUtils.animateForViewGroupTransition(trendRecycleView)
                                }
                                GridType.GRID_3_PLUS_2 -> {
                                    trendRecycleView.layoutManager = CustomGridLayoutManager(this@FancyTrendActivity, 2)
                                    googleTrendAdapter.changeRowNumber(3)
                                    UiUtils.animateForViewGroupTransition(trendRecycleView)
                                }
                                GridType.GRID_1_PLUS_3 -> {
                                    trendRecycleView.layoutManager = CustomGridLayoutManager(this@FancyTrendActivity, 3)
                                    googleTrendAdapter.changeRowNumber(1)
                                    UiUtils.animateForViewGroupTransition(trendRecycleView)
                                }
                                GridType.GRID_2_PLUS_3 -> {
                                    trendRecycleView.layoutManager = CustomGridLayoutManager(this@FancyTrendActivity, 3)
                                    googleTrendAdapter.changeRowNumber(2)
                                    UiUtils.animateForViewGroupTransition(trendRecycleView)
                                }
                                GridType.GRID_3_PLUS_3 -> {
                                    trendRecycleView.layoutManager = CustomGridLayoutManager(this@FancyTrendActivity, 3)
                                    googleTrendAdapter.changeRowNumber(3)
                                    UiUtils.animateForViewGroupTransition(trendRecycleView)
                                }
                            }
                        }
                        .show()
                return true
            }
            R.id.defaultCountry -> MaterialDialog.Builder(this@FancyTrendActivity)
                    .title(R.string.choose_default_country)
                    .items(fancyTrendPresenter.getAllCountryNames())
                    .itemsCallbackSingleChoice(fancyTrendPresenter.defaultCountryIndex) { _, _, which, text ->
                        for (i in 0 until Constant.DEFAULT_TREND_ITEM_NUMBER) {
                            fancyTrendPresenter.retrieveSingleTrend(fancyTrendPresenter.getAllCountryNames()[which], i)
                        }
                        fancyTrendPresenter.defaultCountryName = text.toString()
                        fancyTrendPresenter.defaultCountryIndex = which
                        true
                    }
                    .positiveText(android.R.string.ok)
                    .show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toggle() {
        if (isVisibleContent) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        val actionBar = supportActionBar
        actionBar?.hide()
        isVisibleContent = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    @SuppressLint("InlinedApi")
    private fun show() {
        // Show the system bar
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        isVisibleContent = true

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable)
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        //Screen changes orientation, it needs to update item height.
        googleTrendAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        fancyTrendPresenter.cancelJob()
        super.onDestroy()
    }

    companion object {
        private val AUTO_HIDE = true
        /**
         * If [.AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private val UI_ANIMATION_DELAY = 300
    }

}
