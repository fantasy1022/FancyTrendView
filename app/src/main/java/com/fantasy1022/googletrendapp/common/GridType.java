package com.fantasy1022.googletrendapp.common;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by fantasy1022 on 2017/2/20.
 */

public class GridType {

    public static final int GRID_1_PLUS_1 = 0;
    public static final int GRID_2_PLUS_1 = 1;
    public static final int GRID_3_PLUS_1 = 2;
    public static final int GRID_1_PLUS_2 = 3;
    public static final int GRID_2_PLUS_2 = 4;
    public static final int GRID_3_PLUS_2 = 5;
    public static final int GRID_1_PLUS_3 = 6;
    public static final int GRID_2_PLUS_3 = 7;
    public static final int GRID_3_PLUS_3 = 8;


    @IntDef(value = {GRID_1_PLUS_1, GRID_2_PLUS_1, GRID_3_PLUS_1, GRID_1_PLUS_2, GRID_2_PLUS_2, GRID_3_PLUS_2,
            GRID_1_PLUS_3, GRID_2_PLUS_3, GRID_3_PLUS_3})
    @Retention(RetentionPolicy.SOURCE)
    @interface type {
    }
}
