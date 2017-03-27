package com.fantasy1022.googletrendapp.presentation.trend;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.fantasy1022.googletrendapp.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class GoogleTrendActivityRecoderTest {

    @Rule
    public ActivityTestRule<GoogleTrendActivity> mActivityTestRule = new ActivityTestRule<>(GoogleTrendActivity.class);

    @Test
    public void googleTrendActivityTest2() {
        ViewInteraction flipPageView = onView(
                allOf(withId(R.id.flipPageView), withContentDescription("Choose grid"), isDisplayed()));
        flipPageView.perform(click());

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.gridNum), withContentDescription("Choose grid"), isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.md_contentRecyclerView),
                        withParent(withId(R.id.md_contentListViewFrame)),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction flipPageView2 = onView(
                allOf(withId(R.id.flipPageView), withContentDescription("Choose grid"), isDisplayed()));
        flipPageView2.perform(click());

        ViewInteraction flipPageView3 = onView(
                allOf(withId(R.id.flipPageView), withContentDescription("Choose grid"), isDisplayed()));
        flipPageView3.perform(click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.md_contentRecyclerView),
                        withParent(withId(R.id.md_contentListViewFrame)),
                        isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(4, click()));

    }

}
