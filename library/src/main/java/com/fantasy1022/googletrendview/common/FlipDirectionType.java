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
