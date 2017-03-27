package com.fantasy1022.googletrendapp.presentation.trend;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.support.v7.widget.RecyclerView;

import com.fantasy1022.googletrendapp.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by fantasy1022 on 2017/3/3.
 * Use UiAutomator to test
 */

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class GoogleTrendActivityUiAutomatorTest {
    public final static String TAG = GoogleTrendActivityUiAutomatorTest.class.getSimpleName();

    private static final String BASIC_PACKAGE = "com.fantasyfang.googletrendapp";//Package name

    private static final String SETTING_PACKAGE = "com.android.settings";
    private static final int LAUNCH_TIMEOUT = 5000;

    private UiDevice mDevice;

    public static class SettingsHelper {
        public static final UiSelector LIST_VIEW =
                new UiSelector().className(android.widget.ListView.class.getName());
        public static final UiSelector RECYCLE_VIEW =
                new UiSelector().className(android.support.v7.widget.RecyclerView.class.getName());

        public static final UiSelector SCROLL_VIEW =
                new UiSelector().className(android.widget.ScrollView.class.getName());

        public static final UiSelector FRAMELAYOUT_VIEW =
                new UiSelector().className(android.widget.FrameLayout.class.getName());

        public static final UiSelector LIST_VIEW_ITEM =
                new UiSelector().className(android.widget.TextView.class.getName());
    }

    @Before
    public void startMainActivityFromHomeScreen() throws Exception {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);

        // Launch the blueprint app
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(BASIC_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context.startActivity(intent);
        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(BASIC_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void checkPreconditions() {
        takeScreenShot("Main_page.jpg");
        assertThat(mDevice, notNullValue());
    }

    @Test
    public void mainActivity_RecycleViewChangeCountry() throws Exception {
        UiScrollable recycleView = new UiScrollable(new UiSelector().resourceId("com.fantasyfang.googletrendapp:id/trendRecycleView")
                .className(RecyclerView.class));//recycleView.click();
        UiObject item = recycleView.getChild(new UiSelector().resourceId("com.fantasyfang.googletrendapp:id/googleTrendView"));
        item.click();
        item.click();
        mDevice.waitForIdle();
        takeScreenShot("Country_menu_page.jpg");
    }

    @Test
    public void mainActivity_ChangeOrientation() throws Exception {
        mDevice.setOrientationRight();
        mDevice.waitForIdle();
        takeScreenShot("Change_orientation.jpg");
    }

    @Test
    public void mainActivity_changeLanguageToEnglish() throws Exception {
        UiScrollable recycleView = new UiScrollable(new UiSelector().resourceId("com.fantasyfang.googletrendapp:id/trendRecycleView")
                .className(RecyclerView.class));//recycleView.click();
        UiObject item = recycleView.getChild(new UiSelector().resourceId("com.fantasyfang.googletrendapp:id/googleTrendView"));
        item.click();
        item.click();
        mDevice.waitForIdle();
        takeScreenShot("Country_chinese.jpg");
        mDevice.pressHome();
        // Validate that the package name is the expected one

        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(SETTING_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context.startActivity(intent);

        mDevice.wait(Until.hasObject(By.pkg(SETTING_PACKAGE).depth(0)), LAUNCH_TIMEOUT);

        if(selectSettingsFor("語言與輸入設定")){
            UiObject language = mDevice.findObject(new UiSelector().text("語言"));
            language.click();

            takeScreenShot("language_setting.jpg");

            //Click english language item
            UiScrollable languageFrame = new UiScrollable(SettingsHelper.FRAMELAYOUT_VIEW);
            UiObject englishLanguageItem = languageFrame.getChildByText(SettingsHelper.LIST_VIEW_ITEM, "English (United States)");
            englishLanguageItem.click();
            mDevice.pressHome();

            // Wait for launcher
            final String launcherPackage = mDevice.getLauncherPackageName();
            assertThat(launcherPackage, notNullValue());
            mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                    LAUNCH_TIMEOUT);

            // Launch the blueprint app

            final Intent intentAPP = context.getPackageManager().getLaunchIntentForPackage(BASIC_PACKAGE);
            intentAPP.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
            context.startActivity(intentAPP);
            // Wait for the app to appear
            mDevice.wait(Until.hasObject(By.pkg(BASIC_PACKAGE).depth(0)), LAUNCH_TIMEOUT);

//            UiScrollable recycleView = new UiScrollable(new UiSelector().resourceId("com.fantasyfang.googletrendapp:id/trendRecycleView")
//                    .className(RecyclerView.class));//recycleView.click();
//            UiObject item = recycleView.getChild(new UiSelector().resourceId("com.fantasyfang.googletrendapp:id/googleTrendView"));
            item.click();
            item.click();
            mDevice.waitForIdle();
            takeScreenShot("Country_english.jpg");
        }


    }

    private boolean selectSettingsFor(String name)  {
        try {
            UiScrollable appsSettingsList = new UiScrollable(SettingsHelper.SCROLL_VIEW);
            UiObject obj = appsSettingsList.getChildByText(SettingsHelper.LIST_VIEW_ITEM, name);
            obj.click();
        } catch (UiObjectNotFoundException e) {
            return false;
        }
        return true;
    }

    @Test
    public void mainActivity_changeGridSize() throws Exception {
        UiScrollable recycleView = new UiScrollable(new UiSelector().resourceId("com.fantasyfang.googletrendapp:id/trendRecycleView")
                .className(RecyclerView.class));//recycleView.click();
        UiObject item = recycleView.getChild(new UiSelector().resourceId("com.fantasyfang.googletrendapp:id/googleTrendView"));
        item.click();

        String gridDescription = InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.choose_grid);
        UiObject grid = mDevice.findObject(new UiSelector().descriptionContains(gridDescription));
        grid.click();

        UiObject gridList = mDevice.findObject(new UiSelector().text("2*2"));
        gridList.click();
        mDevice.waitForIdle();
        takeScreenShot("Change_grid_to_2_2.jpg");
        mDevice.pressHome();
        takeScreenShot("Home.jpg");
    }

    private void takeScreenShot(String name) {
        String dir = String.format("%s/%s", Environment.getExternalStorageDirectory().getPath(), "test-screenshots");
        File theDir = new File(dir);
        if (!theDir.exists()) {
            theDir.mkdir();
        }
        mDevice.takeScreenshot(new File(String.format("%s/%s", dir, name)));
    }

    @After
    public void tearDown() throws Exception {
        mDevice.setOrientationNatural();
    }
}
