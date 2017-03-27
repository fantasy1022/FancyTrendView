package com.fantasy1022.googletrendview.common;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by fantasy_apple on 2017/2/12.
 */

public class FlipDirectionType {

    public static final int TYPE_RANDOM = 0;
    public static final int TYPE_UP = 1;
    public static final int TYPE_DOWN = 2;
    public static final int TYEP_LEFT = 3;
    public static final int TYEP_RIGHT = 4;

    @IntDef(value = {TYPE_RANDOM, TYPE_UP, TYPE_DOWN, TYEP_LEFT, TYEP_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    @interface type {
    }
}
