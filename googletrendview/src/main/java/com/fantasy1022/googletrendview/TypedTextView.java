package com.fantasy1022.googletrendview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatTextView;


import com.fantasy1022.googletrendview.common.Constant;

import java.util.ArrayList;

/**
 * Created by fantasy1022 on 2017/2/8.
 */

public class TypedTextView extends AppCompatTextView {

    public final String TAG = getClass().getSimpleName();
    private AttributeSet attrs;
    private int defStyle, currentLength, currentLine, textStartIndex, intervalBlinkTimes, cursorBlinkTimesAfterTypeDone;
    private int typedSpeed, cursorBlinkInterval;
    protected Paint cursorPaint, cursorTransparentPaint;
    protected TextPaint textPaint;
    protected CharSequence text;
    protected float cursorWidth, startX, startY, cursorStartX, cursorStartY, textSize;
    private boolean cursorBilnk;
    private ArrayList<Integer> indexList;
    private OnBlinkCompleteListener blinkCompleteListener;


    public TypedTextView(Context context) {
        super(context);
        init(null, 0);
    }

    public TypedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    private void init(AttributeSet attrs, int defStyle) {
        this.attrs = attrs;//TODO: check
        this.defStyle = defStyle;

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);//this.getCurrentTextColor()
        textPaint.setStyle(Paint.Style.FILL);

        cursorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cursorPaint.setColor(Color.WHITE);

        cursorPaint.setStyle(Paint.Style.FILL);

        cursorTransparentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cursorTransparentPaint.setColor(Color.TRANSPARENT);
        cursorPaint.setStyle(Paint.Style.FILL);
        text = this.getText();

        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                prepareAnimate();
            }
        }, 50);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Log.d(TAG, "getSuggestedMinimumWidth():" + getSuggestedMinimumWidth() + " widthMeasureSpec" + widthMeasureSpec);
        // Log.d(TAG, "getSuggestedMinimumHeight():" + getSuggestedMinimumHeight() + " widthMeasureSpec" + heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        //Log.d(TAG, "width:" + width + " height:" + height + " line count:" + getlineCount());
        //TODO:Handle outer setting, ex:wrap_content, match_parrent, 10dp
        // setMeasuredDimension(width, 300);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Draw text
        if (indexList != null) {
            float heightOffset = textPaint.descent() - textPaint.ascent();
            float multiLineOffset = textPaint.ascent() * currentLine;//Make multi line in center. Negative value
            if (currentLength > indexList.get(currentLine) && currentLine < indexList.size()) {//Update line info
                textStartIndex = indexList.get(currentLine);
                currentLine++;
                //Log.d(TAG, "check currentLine:" + textStartIndex + " currentLine:" + currentLine);
            }
            //1.draw previous line
            for (int line = 0; line < currentLine; line++) {
                int x = (line == 0 ? 0 : indexList.get(line - 1));
                canvas.drawText(text, x, indexList.get(line), startX, startY + heightOffset * line + multiLineOffset, textPaint);
            }

            //2.draw current typed text
            // Log.d(TAG, "textStartIndex:" + textStartIndex + " currentLength:" + currentLength);
            cursorStartX = startX + textPaint.measureText(text.toString(), textStartIndex, currentLength);
            cursorStartY = startY - textSize + heightOffset * currentLine + multiLineOffset;
            canvas.drawText(text, textStartIndex, currentLength, startX, startY + heightOffset * currentLine + multiLineOffset, textPaint);
        }

        //Draw cursor
        if (currentLength < text.length()) {
            // Log.d(TAG, "drawLine cursor after text");
            canvas.drawLine(cursorStartX, cursorStartY, cursorStartX, cursorStartY + textSize * (1 + Constant.CURSOR_LENGTH_SCALE), cursorPaint);
            currentLength++;
            this.postInvalidateDelayed(typedSpeed);
        } else if (text.length() > 0) {//After text input finish to blink cursor (blink function)
            if (intervalBlinkTimes < cursorBlinkTimesAfterTypeDone) {
                if (cursorBilnk) {
                    // Log.d(TAG, "drawLine blink cursor");
                    cursorBilnk = false;
                    canvas.drawLine(cursorStartX, cursorStartY, cursorStartX, cursorStartY + textSize * (1 + Constant.CURSOR_LENGTH_SCALE), cursorPaint);
                } else {
                    // Log.d(TAG, "drawLine blink cursor transparent");
                    cursorBilnk = true;
                    canvas.drawLine(cursorStartX, cursorStartY, cursorStartX, cursorStartY + textSize * (1 + Constant.CURSOR_LENGTH_SCALE), cursorTransparentPaint);
                }
                intervalBlinkTimes++;
                this.postInvalidateDelayed(cursorBlinkInterval);
            } else {
                // Log.d(TAG, "onBlinkComplete:");
                intervalBlinkTimes = 0;
                if (blinkCompleteListener != null) {
                    blinkCompleteListener.onBlinkComplete();
                }
            }
        }
    }

    public void animateText(CharSequence text) {//Entry point
        this.setText("");
        cursorBilnk = true;
        this.text = text;
        prepareAnimate();
        animateStart();
    }

    protected void animateStart() {
        currentLength = 0;
        currentLine = 0;
        textStartIndex = 0;
        this.invalidate();
    }

    private void prepareAnimate() {
        textSize = this.getTextSize();
        textPaint.setTextSize(textSize);
        startX = (getMeasuredWidth() - getCompoundPaddingLeft() - getPaddingLeft() - textPaint.measureText(text.toString())) / 2f;
        startX = (startX > 0 ? startX : getCompoundPaddingLeft());
        //Log.d(TAG, "startX:" + startX);
        //Log.d(TAG, String.format("measureWidth:%s, CompoundpaddingLeft:%s, paddingLeft:", this.getMeasuredWidth(), this.getCompoundPaddingLeft(), this.getPaddingLeft()));
        startY = this.getBaseline();
        cursorStartX = startX;
        indexList = getLineIndexList();

    }

    private int getlineCount() {
        int index = 0;
        int linecount = 0;
        while (index < text.length()) {
            index += textPaint.breakText(text, index, text.length(), true, getWidth() - getPaddingLeft() - getPaddingRight(), null);
            linecount++;
        }
        return linecount;
    }

    private ArrayList<Integer> getLineIndexList() {
        ArrayList<Integer> list = new ArrayList<>();
        int index = 0;
        while (index < text.length()) {
            index += textPaint.breakText(text, index, text.length(), true, getWidth() - getPaddingLeft() - getPaddingRight(), null);
            list.add(index);
        }
        return list;
    }


    protected void setTypedSpeed(int typedSpeed) {
        this.typedSpeed = typedSpeed;
    }

    protected void setCursorBlinkInterval(int cursorBlinkInterval) {
        this.cursorBlinkInterval = cursorBlinkInterval;
    }

    protected void setCursorWidth(float cursorWidth) {
        this.cursorWidth = cursorWidth;
        cursorPaint.setStrokeWidth(cursorWidth);
    }

    protected void setCursorBlinkTimesAfterTypeDone(int cursorBlinkTimesAfterTypeDone) {
        this.cursorBlinkTimesAfterTypeDone = cursorBlinkTimesAfterTypeDone;
    }

    @Override
    public void setTextSize(float textSize) {
        super.setTextSize(textSize);
    }

    protected void setOnBlinkCompleteListener(OnBlinkCompleteListener blinkCompleteListener) {
        this.blinkCompleteListener = blinkCompleteListener;
    }

    interface OnBlinkCompleteListener {
        void onBlinkComplete();
    }

}
