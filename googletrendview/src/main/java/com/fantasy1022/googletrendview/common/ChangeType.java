package com.fantasy1022.googletrendview.common;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by fantasy1022 on 2017/2/9.
 */

public class ChangeType {
    public static final int TYPE_RANDOM = 0;
    public static final int TYPE_INCREASE = 1;
    public static final int TYEP_DECREASE = 2;

    @IntDef(value = {TYPE_RANDOM,TYPE_INCREASE,TYEP_DECREASE})
    @Retention(RetentionPolicy.SOURCE)
    @interface type{}
}
