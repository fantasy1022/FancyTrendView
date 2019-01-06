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
import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import com.fantasy1022.fancytrendapp.R
import com.fantasy1022.fancytrendapp.presentation.trend.FancyTrendAdapter.ItemViewHolder
import com.fantasy1022.fancytrendview.FancyTrendView

import java.util.ArrayList


/**
 * Created by fantasy1022 on 2017/2/17.
 */

class FancyTrendAdapter(private val context: Context, private var trendItemList: MutableList<List<String>>, private var rows: Int,
                        private val onItemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<FancyTrendAdapter.ItemViewHolder>() {

    val TAG = FancyTrendAdapter::class.java.simpleName

    interface OnItemClickListener {
        fun onItemClick(v: View, trend: String, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FancyTrendAdapter.ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trend, parent, false)
        return ItemViewHolder(view, onItemClickListener)
    }

    override fun onBindViewHolder(holder: FancyTrendAdapter.ItemViewHolder, position: Int) {
        val displayMetrics = context.resources.displayMetrics
        val heightPixels: Int
        if (hasNavigationBar(context)) {
            heightPixels = displayMetrics.heightPixels + getNavigationBarHeight(context)
        } else {
            heightPixels = displayMetrics.heightPixels
        }
        //Log.d(TAG, "heightPixels:" + heightPixels);
        when (rows) {
            1 -> {
                val params = holder.googleTrendView.layoutParams
                params.height = heightPixels
                holder.googleTrendView.layoutParams = params
            }
            2 -> {
                val params = holder.googleTrendView.layoutParams
                params.height = heightPixels / 2
                holder.googleTrendView.layoutParams = params
            }
            3 -> {
                val params = holder.googleTrendView.layoutParams
                params.height = heightPixels / 3
                holder.googleTrendView.layoutParams = params
            }
        }
        holder.googleTrendView.startAllAnimation(trendItemList[position])
    }

    override fun getItemCount(): Int = trendItemList.size

    class ItemViewHolder(itemView: View, private val listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        // @BindView(R.id.googleTrendView)
        internal var googleTrendView: FancyTrendView

        init {
            googleTrendView = itemView.findViewById<View>(R.id.googleTrendView) as FancyTrendView
            googleTrendView.setOnClickListener(this)
        }//ButterKnife.bind(this, itemView);

        override fun onClick(v: View) {
            listener.onItemClick(v, googleTrendView.nowText, layoutPosition)
        }
    }

    fun updateAllList(trendItemList: MutableList<List<String>>) {
        this.trendItemList = trendItemList
        notifyDataSetChanged()
    }

    fun updateSingleList(trendList: List<String>, position: Int) {
        Log.d(TAG, "updateSingleList position:$position trendList:$trendList")
        trendItemList.add(position, trendList)
        notifyItemChanged(position)
    }

    fun changeRowNumber(rows: Int) {
        this.rows = rows
        notifyDataSetChanged()
    }

    //TODO:Add to extension
    private fun getNavigationBarHeight(context: Context): Int {
        val resources = context.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
    }

    private fun hasNavigationBar(context: Context): Boolean {
        val resources = context.resources
        val id = resources.getIdentifier("config_showNavigationBar", "bool", "android")
        return id > 0 && resources.getBoolean(id)
    }

}
