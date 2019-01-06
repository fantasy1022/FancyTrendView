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

package com.fantasy1022.fancytrendapp.common

import android.content.Context
import android.content.SharedPreferences
import android.support.annotation.IntDef

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Created by fantasy_apple on 2017/4/3.
 */

class SPUtils(context: Context) {
    private val sp: SharedPreferences
    private val editor: SharedPreferences.Editor
//
//    @IntDef(ClickBehaviorItem.singlecountry, ClickBehaviorItem.googleSearch)
//    @Retention(RetentionPolicy.SOURCE)
//    annotation class ClickBehaviorItem {
//        companion object {
//            val googleSearch = 0
//            val singlecountry = 1
//        }
//    }

    enum class ClickBehavior(val value:Int){
        GoogleSearch(0),SingleCountry(1)
    }


    init {
        sp = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        editor = sp.edit()
        editor.apply()
    }

    fun putString(key: String, value: String?) {
        editor.putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String): String {
        return sp.getString(key, defaultValue)
    }

    fun putInt(key: String, value: Int) {
        editor.putInt(key, value).apply()
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return sp.getInt(key, defaultValue)
    }


}
