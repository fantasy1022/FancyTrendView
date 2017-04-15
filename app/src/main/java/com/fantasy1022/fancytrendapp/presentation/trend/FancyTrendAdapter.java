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

package com.fantasy1022.fancytrendapp.presentation.trend;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.fantasy1022.fancytrendapp.R;
import com.fantasy1022.fancytrendview.FancyTrendView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by fantasy1022 on 2017/2/17.
 */

public class FancyTrendAdapter extends RecyclerView.Adapter<FancyTrendAdapter.ItemViewHolder> {

    public final String TAG = getClass().getSimpleName();
    private Context context;
    private ArrayList<List<String>> trendItemList;
    private OnItemClickListener onItemClickListener;
    private int rows;


    public FancyTrendAdapter(Context context, ArrayList<List<String>> trendItemList, int rows, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.trendItemList = trendItemList;
        this.onItemClickListener = onItemClickListener;
        this.rows = rows;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, String trend, int position);
    }

    @Override
    public FancyTrendAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trend, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view, onItemClickListener);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(FancyTrendAdapter.ItemViewHolder holder, int position) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int heightPixels;
        if (hasNavigationBar(context)) {
            heightPixels = displayMetrics.heightPixels + getNavigationBarHeight(context);
        } else {
            heightPixels = displayMetrics.heightPixels;
        }
        //Log.d(TAG, "heightPixels:" + heightPixels);
        if (rows == 1) {
            ViewGroup.LayoutParams params = holder.googleTrendView.getLayoutParams();
            params.height = heightPixels;
            holder.googleTrendView.setLayoutParams(params);
        } else if (rows == 2) {
            ViewGroup.LayoutParams params = holder.googleTrendView.getLayoutParams();
            params.height = heightPixels / 2;
            holder.googleTrendView.setLayoutParams(params);
        } else if (rows == 3) {
            ViewGroup.LayoutParams params = holder.googleTrendView.getLayoutParams();
            params.height = heightPixels / 3;
            holder.googleTrendView.setLayoutParams(params);
        }
        holder.googleTrendView.startAllAnimation(trendItemList.get(position));
    }

    @Override
    public int getItemCount() {
        int size = (trendItemList != null ? trendItemList.size() : 0);
        return size;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // @BindView(R.id.googleTrendView)
        FancyTrendView googleTrendView;

        private OnItemClickListener listener;

        public ItemViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            googleTrendView = (FancyTrendView) itemView.findViewById(R.id.googleTrendView);
            //ButterKnife.bind(this, itemView);
            this.listener = listener;
            googleTrendView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v, googleTrendView.getNowText(), getLayoutPosition());
        }
    }

    public void updateAllList(ArrayList<List<String>> trendItemList) {
        this.trendItemList = trendItemList;
        notifyDataSetChanged();
    }

    public void updateSingleList(List<String> trendList, int position) {
        Log.d(TAG, "updateSingleList position:" + position + " trendList:" + trendList);
        trendItemList.set(position, trendList);
        notifyItemChanged(position);
    }

    public void changeRowNumber(int rows) {
        this.rows = rows;
        notifyDataSetChanged();
    }

    private int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return (resourceId > 0) ? resources.getDimensionPixelSize(resourceId) : 0;
    }

    private boolean hasNavigationBar(Context context) {
        Resources resources = context.getResources();
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && resources.getBoolean(id);
    }

}
