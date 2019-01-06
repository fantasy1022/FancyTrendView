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

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.util.AttributeSet

/**
 * Created by fantasy_apple on 2017/2/19.
 *
 *
 * Purpose:Disable scroll function
 */


class CustomGridLayoutManager : GridLayoutManager {
    private var isScrollEnabled = true

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    constructor(context: Context, spanCount: Int) : super(context, spanCount) {
        this.isScrollEnabled = false
    }

    constructor(context: Context, spanCount: Int, orientation: Int, reverseLayout: Boolean) : super(context, spanCount, orientation, reverseLayout) {}

    fun setScrollEnabled(flag: Boolean) {
        this.isScrollEnabled = flag
    }

    override fun canScrollVertically(): Boolean {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically()
    }
}

