package com.fantasy1022.googletrendapp.presentation.trend;

import android.content.Intent;
import android.os.SystemClock;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.fantasy1022.googletrendapp.R;
import com.fantasy1022.googletrendapp.data.remote.MockGoogleTrendRestServiceImpl;
import com.fantasy1022.googletrendapp.presentation.utils.OrientationChangeAction;
import com.jraska.falcon.FalconSpoon;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import io.reactivex.Single;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by fantasy1022 on 2017/3/3.
 */

@RunWith(AndroidJUnit4.class)
public class GoogleTrendActivityTest {

    @Rule  //Target which Activity to execute and do open
    public ActivityTestRule<GoogleTrendActivity> testRule = new ActivityTestRule<>(GoogleTrendActivity.class);


    @Test
    public void mainActivity_onLaunch_RecycleViewDisplayed() {
        //Given activity automatically launched
        FalconSpoon.screenshot(testRule.getActivity(), "Initial_state");
        onView(withId(R.id.trendRecycleView)).check(matches(isDisplayed()));
    }


    @Test
    public void mainActivity_RecycleViewClickItem() {
        //Show toolbar
        onView(withId(R.id.trendRecycleView)).perform(click());
        //Show country menu
        onView(withId(R.id.trendRecycleView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withText(R.string.choose_country)).inRoot(isDialog()).check(matches(isDisplayed()));

        // Spoon.screenshot(testRule.getActivity(), "Country_menu");
        FalconSpoon.screenshot(testRule.getActivity(), "Country_menu");
    }


    @Test
    public void mainActivity_RecycleViewChangeCountry() {
        //Show toolbar
        if (!testRule.getActivity().isVisible()) {
            onView(withId(R.id.trendRecycleView)).perform(click());
            SystemClock.sleep(500);//Workaround for toolbar showing
        }
        //Show country menu
        onView(withId(R.id.trendRecycleView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withText("Brazil")).perform(click());
        FalconSpoon.screenshot(testRule.getActivity(), "Country_Brazil");

        SystemClock.sleep(500);//Workaround for
        //Show toolbar
        if (!testRule.getActivity().isVisible()) {
            onView(withId(R.id.trendRecycleView)).perform(click());
            SystemClock.sleep(500);//Workaround for toolbar showing
        }
        //Show country menu
        onView(withId(R.id.trendRecycleView)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        onView(withText("Australia")).perform(click());
        SystemClock.sleep(1000);//Workaround for
        FalconSpoon.screenshot(testRule.getActivity(), "Country_Australia");

//        onView(withId(R.id.trendRecycleView)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
//        onView(withId(R.id.trendRecycleView)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
//        onView(withText("Australia")).perform(click());
//        FalconSpoon.screenshot(testRule.getActivity(), "Country_Australia");
//
//        onView(withId(R.id.trendRecycleView)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
//        onView(withId(R.id.trendRecycleView)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
//        onView(withText("Chile")).perform(click());
//        FalconSpoon.screenshot(testRule.getActivity(), "Country_Chile");

        //TODO:Scroll list to find another Country recycleview automatically scroll
    }


    @Test
    public void mainActivity_RecycleViewChangeGridSize() {
        //Show toolbar
        if (!testRule.getActivity().isVisible()) {
            onView(withId(R.id.trendRecycleView)).perform(click());
            SystemClock.sleep(500);//Workaround for toolbar showing
        }
        //Click toolbar to change
        onView(withId(R.id.gridNum)).perform(click());
        onView(withText(R.string.choose_grid)).inRoot(isDialog()).check(matches(isDisplayed()));
        FalconSpoon.screenshot(testRule.getActivity(), "Choose_grid");
        onView(withText("1*1")).perform(click());
        FalconSpoon.screenshot(testRule.getActivity(), "11_grid");

        //Show toolbar
        if (!testRule.getActivity().isVisible()) {
            onView(withId(R.id.trendRecycleView)).perform(click());
            SystemClock.sleep(500);//Workaround for toolbar showing
        }
        onView(withId(R.id.gridNum)).perform(click());
        onView(withText("2*2")).perform(click());
        FalconSpoon.screenshot(testRule.getActivity(), "22_grid");
    }

    @Test
    public void mainActivity_Display_Get_Trend_error() throws Exception {
        MockGoogleTrendRestServiceImpl.setDummyGoogleTrendResult(Single.error(new IOException()));
        Intent intent = new Intent("FancyTrendActivity");
        testRule.launchActivity(intent);
        onView(withText(R.string.trend_error)).check(matches(isDisplayed()));
        FalconSpoon.screenshot(testRule.getActivity(), "Error_screen");
    }

    @Test
    public void mainActivity_Orientation_To_Landscape() throws Exception {
        onView(isRoot()).perform(OrientationChangeAction.orientationLandscape());
        SystemClock.sleep(3000);
        FalconSpoon.screenshot(testRule.getActivity(), "Landscape_screen_33");
    }

    @Test
    public void mainActivity_Orientation_To_Landscape_TO31() throws Exception {
        onView(isRoot()).perform(OrientationChangeAction.orientationLandscape());
        //Show toolbar
        onView(withId(R.id.trendRecycleView)).perform(click());
        SystemClock.sleep(500);//Workaround for toolbar showing
        //Click toolbar to change
        onView(withId(R.id.gridNum)).perform(click());
        onView(withText(R.string.choose_grid)).inRoot(isDialog()).check(matches(isDisplayed()));
        FalconSpoon.screenshot(testRule.getActivity(), "Choose_grid");
        onView(withText("3*1")).perform(click());
        FalconSpoon.screenshot(testRule.getActivity(), "Landscape_screen_31");
    }


    @After
    public void tearDown() {
        MockGoogleTrendRestServiceImpl.setDummyGoogleTrendResult(null);//Init trend result to get mock result
        //Espresso.unregisterIdlingResources();
    }


}
