# FancyTrendView
 [ ![Download](https://api.bintray.com/packages/fantasy1022/fantasy1022/GoogleTrendView/images/download.svg?version=v0.8.0) ](https://bintray.com/fantasy1022/fantasy1022/GoogleTrendView/v0.8.0/link)
[![Build Status](https://travis-ci.org/fantasy1022/FancyTrendView.svg?branch=master)](https://travis-ci.org/fantasy1022/FancyTrendView)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/1cbed3bc8a6f416a9ddb9da965d567a6)](https://www.codacy.com/app/fantasy1022/FancyTrendView?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=fantasy1022/FancyTrendView&amp;utm_campaign=Badge_Grade)
[![GitHub license](https://img.shields.io/hexpm/l/plug.svg)](https://github.com/fantasy1022/FancyTrendView/blob/master/LICENSE)


The custom UI view including animation and typing text.

The behavior of Android APP is like Google trend web. https://trends.google.com/trends/hottrends/visualize

![Screenshots gif](https://raw.githubusercontent.com/fantasy1022/FancyTrendView/master/art/showcase.gif)


# Sample project 

It's also on Google Play:

<a href="https://play.google.com/store/apps/details?id=com.fantasy1022.fancytrendapp" target="_blank">
  <img alt="Get it on Google Play"
       src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" height="60"/>
</a>

# Gradle Dependency

### Repository


### Library

```gradle
dependencies {
	// ... other dependencies here
    compile 'com.fantasy1022.fancytrendview:0.8.0'
}
```

# How to use
```xml
    <com.fantasy1022.fancytrendview.FancyTrendView
        android:id="@+id/googleTrendView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:colorArray="@array/trendcolors"
        app:cursorBlinkInterval="300"
        app:cursorBlinkTimesAfterTypeDone="10"
        app:cursorWidth="3dp"
        app:flipDirection="random"
        app:flipSpeed="1000"
        app:textArray="@array/demoTrendArray"
        app:textChangeType="random"
        app:textSize="10sp"
        app:typedSpeed="200" />
```

### Custom color array
```xml
  <item name="material_blue" type="color">#FF4285F4</item>
    <item name="material_red" type="color">#FFea4335</item>
    <item name="material_green" type="color">#FF34a852</item>
    <item name="material_yellow" type="color">#FFfabb05</item>

    <integer-array name="trendcolors">
        <item>@color/material_blue</item>
        <item>@color/material_red</item>
        <item>@color/material_green</item>
        <item>@color/material_yellow</item>
    </integer-array>
```

### Custom text array
```xml
    <string-array name="demoTrendArray" translatable="false">
        <item>Google</item>
        <item>Apple</item>
        <item>IBM</item>
        <item>Microsoft</item>
        <item>HTC</item>
        <item>Amazon</item>
        <item>OPPO</item>
        <item>Asus</item>
    </string-array>
```
