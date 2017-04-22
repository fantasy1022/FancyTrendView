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

package com.fantasy1022.fancytrendview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.fantasy1022.fancytrendview.common.ChangeType;
import com.fantasy1022.fancytrendview.common.Constant;
import com.fantasy1022.fancytrendview.common.FlipDirectionType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * Created by fantasy_apple on 2017/2/12.
 */

public class FancyTrendView extends RelativeLayout implements TypedTextView.OnBlinkCompleteListener {
    public final String TAG = getClass().getSimpleName();
    private int colorTotalNum;
    private AttributeSet attrs;
    private int defStyle;
    private int flipSpeed, flipDirectionType, groupChangeType, typedSpeed, cursorBlinkInterval, cursorBlinkTimesAfterTypeDone;
    private float cursorWidth, textSize;
    private RelativeLayout clientFlipLay, masterFlipLay;
    private TypedTextView masterTypedText, clientTypedText;
    private ArrayList<Integer> trendColorList = new ArrayList<>();
    private ArrayList<String> trendTextList = new ArrayList<>();
    private Handler handler = new Handler();
    private Runnable flipRunnable, typedRunnable;
    private int previousColorIndex = 0;
    private List<String> trendList = new ArrayList<>();
    private List<String> tempTrendList;
    private boolean isRunning;
    private int count;


    public FancyTrendView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public FancyTrendView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        this.attrs = attrs;//TODO: check
        this.defStyle = defStyle;
        if (attrs != null) {
            final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FancyTrendView);
            flipSpeed = a.getInteger(R.styleable.FancyTrendView_flipSpeed, Constant.DEFALUT_FLIP_SPEED);
            flipDirectionType = a.getInteger(R.styleable.FancyTrendView_flipDirection, Constant.DEFALUT_FLIP_DIRECTION_TYPE);
            groupChangeType = a.getInteger(R.styleable.FancyTrendView_textChangeType, Constant.DEFALUT_CHANGE_TYPE);
            typedSpeed = a.getInteger(R.styleable.FancyTrendView_typedSpeed, Constant.DEFALUT_TYPE_SPEED);
            cursorBlinkInterval = a.getInteger(R.styleable.FancyTrendView_cursorBlinkInterval, Constant.DEFALUT_CURSOR_BLINK_INTERVAL);
            cursorWidth = a.getDimension(R.styleable.FancyTrendView_cursorWidth, context.getResources().getDimension(R.dimen.cursorWidth));
            cursorBlinkTimesAfterTypeDone = a.getInteger(R.styleable.FancyTrendView_cursorBlinkTimesAfterTypeDone, Constant.DEFALUT_CURSOR_BLINK_TIMES_AFTER_TYPED_DONE);
            textSize = a.getDimension(R.styleable.FancyTrendView_textSize, context.getResources().getDimension(R.dimen.textSize));

            int colorsId = a.getResourceId(R.styleable.FancyTrendView_colorArray, 0);
            if (colorsId != 0) {
                final int[] colors = getResources().getIntArray(colorsId);
                colorTotalNum = colors.length;
                for (int i = 0; i < colorTotalNum; i++) {
                    trendColorList.add(colors[i]);
                }
            } else {//Use default Google trend color
                TypedArray colors = context.getResources().obtainTypedArray(R.array.trendcolors);
                colorTotalNum = colors.length();
                for (int i = 0; i < colorTotalNum; i++) {
                    trendColorList.add(colors.getColor(i, 0xffffff));
                }
                colors.recycle();
            }

            int textsId = a.getResourceId(R.styleable.FancyTrendView_textArray, 0);
            if (textsId != 0) {
                final String[] texts = getResources().getStringArray(textsId);
                for (int i = 0; i < texts.length; i++) {
                    trendTextList.add(texts[i]);
                }
            }


            //Log.d(TAG, "flipSpeed:" + flipSpeed + "cursorWidth:" + textSize);
            a.recycle();
        }

        View view = View.inflate(context, R.layout.view_flip_page, this);
        clientFlipLay = (RelativeLayout) view.findViewById(R.id.clientFlipLay);
        masterFlipLay = (RelativeLayout) view.findViewById(R.id.masterFlipLay);
        masterTypedText = (TypedTextView) view.findViewById(R.id.masterTypedText);
        masterTypedText.setCursorWidth(cursorWidth);
        masterTypedText.setTypedSpeed(typedSpeed);
        masterTypedText.setCursorBlinkInterval(cursorBlinkInterval);
        masterTypedText.setCursorBlinkTimesAfterTypeDone(cursorBlinkTimesAfterTypeDone);
        masterTypedText.setTextSize(textSize);
        masterTypedText.setOnBlinkCompleteListener(this);

        clientTypedText = (TypedTextView) view.findViewById(R.id.clientTypedText);
        clientTypedText.setCursorWidth(cursorWidth);
        clientTypedText.setTypedSpeed(typedSpeed);
        clientTypedText.setCursorBlinkInterval(cursorBlinkInterval);
        clientTypedText.setCursorBlinkTimesAfterTypeDone(cursorBlinkTimesAfterTypeDone);
        clientTypedText.setTextSize(textSize);
        clientTypedText.setOnBlinkCompleteListener(this);
        flipRunnable = new FlipAnimationRunnable();
        masterFlipLay.setBackgroundColor(trendColorList.get(getNextColorIndex()));//Init background color

        if (trendTextList.size() != 0) {//If
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    startAllAnimation(trendTextList);
                }
            }, 1);
        }
    }

    @Override
    public void onBlinkComplete() {
        handler.postDelayed(flipRunnable, 1);
        if (tempTrendList != null) {//If user choose another country, APP waits typing text finishing to show another trend
            //Log.d(TAG, "change trend list");
            trendList = tempTrendList;
            tempTrendList = null;
        }
    }

    public void startAllAnimation(List<String> list) {//Include flip page and typed view
        if (list == null || list.size() == 0) {
            return;
        }
        //Log.d(TAG, "startAllAnimation");
        if (isRunning) {//Already run animation, so it just updates list
            tempTrendList = new ArrayList<>();
            for (String item : list) {
                tempTrendList.add(item);
            }
        } else {
            for (String item : list) {//Copy list to trendList for every list shuffle.
                trendList.add(item);
            }
            animateTextList();//Entry point to trigger
            isRunning = true;
        }
    }

    public String getNowText() {
        return trendList.get(count);
    }

    private void doFlipAnimation(final View beginView, final View endView, final int flipType) {//
        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());//TODO:Use another Interpolator
        animator.setDuration(flipSpeed);
        final int beginColorIndex = getNextColorIndex();
        beginView.setBackgroundColor(trendColorList.get(beginColorIndex));

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float height = beginView.getHeight();
                final float width = beginView.getWidth();
                final float translationX = width * progress;
                final float translationY = height * progress;


                switch (flipType) {//Random be controlled from outer method, and converted to four direction.
                    case FlipDirectionType.TYPE_UP://↑
                        beginView.setTranslationY(height - translationY);
                        endView.setTranslationY(-translationY);
                        break;
                    case FlipDirectionType.TYPE_DOWN://↓
                        beginView.setTranslationY(translationY - height);
                        endView.setTranslationY(translationY);
                        break;
                    case FlipDirectionType.TYEP_LEFT://←
                        beginView.setTranslationX(width - translationX);
                        endView.setTranslationX(-translationX);
                        break;
                    case FlipDirectionType.TYEP_RIGHT://→
                        beginView.setTranslationX(translationX - width);
                        endView.setTranslationX(translationX);
                        break;
                }
                if (progress == 1.0f) {//set endView to begin view state for next animation
                    //Log.d(TAG, "Animation complete");
                    endView.setX(0);
                    endView.setY(0);
                    endView.setBackgroundColor(trendColorList.get(beginColorIndex));//Set color of begin view
                    beginView.bringToFront();//Avoid endview covering beginView. Let beginView to top
                    handler.postDelayed(typedRunnable, 1);
                }
            }
        });
        animator.start();
    }


    private int getNextColorIndex() {//Random color index and differentiate previous color index.
        int colorIndex;
        do {
            colorIndex = (int) (Math.random() * colorTotalNum);
        } while (colorIndex == previousColorIndex);
        previousColorIndex = colorIndex;
        return colorIndex;
    }

    private void animateTextList() {
        switch (groupChangeType) {
            case ChangeType.TYPE_RANDOM:
                Collections.shuffle(trendList, new Random(System.nanoTime()));
                break;
            default:
                //Do nothing
                break;
        }
        typedRunnable = new TypedRunnable(0);
        handler.postDelayed(typedRunnable, 1);//First text
    }


    private class FlipAnimationRunnable implements Runnable {
        private boolean isClientShow;

        public FlipAnimationRunnable() {

        }

        @Override
        public void run() {
            if (flipDirectionType == FlipDirectionType.TYPE_RANDOM) {//Generate direction.
                int num = ((int) (Math.random() * FlipDirectionType.TYEP_RIGHT)) + 1;//1~4
                //Log.d(TAG, "direction number:" + num);
                if (isClientShow) {
                    doFlipAnimation(masterFlipLay, clientFlipLay, num);
                    isClientShow = false;
                } else {
                    doFlipAnimation(clientFlipLay, masterFlipLay, num);
                    isClientShow = true;
                }

            } else {
                doFlipAnimation(clientFlipLay, masterFlipLay, flipDirectionType);
            }
        }
    }

    private class TypedRunnable implements Runnable {//TODO: change to animation logic function
        private boolean isClientShow;


        public TypedRunnable(int c) {
            count = c;
        }

        @Override
        public void run() {
            switch (groupChangeType) {
                case ChangeType.TYPE_RANDOM:
                case ChangeType.TYPE_INCREASE:
                    if (++count > trendList.size() - 1) {
                        count = 0;
                    }
                    break;
                case ChangeType.TYEP_DECREASE:
                    count -= 1;
                    if (--count < 0) {
                        count = trendList.size();
                    }
                    break;
            }
            //Determine which text view to show text
            if (isClientShow) {
                clientTypedText.setVisibility(VISIBLE);
                masterTypedText.setVisibility(INVISIBLE);
                clientTypedText.animateText(trendList.get(count));
                isClientShow = false;
            } else {
                masterTypedText.setVisibility(VISIBLE);
                clientTypedText.setVisibility(INVISIBLE);
                masterTypedText.animateText(trendList.get(count));
                isClientShow = true;
            }
        }
    }

}
