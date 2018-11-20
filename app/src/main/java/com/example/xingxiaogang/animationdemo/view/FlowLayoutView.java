package com.example.xingxiaogang.animationdemo.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.xingxiaogang.animationdemo.SizeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinggang on 2018/11/20 下午4:26.
 * <p>
 * email: xxg841076938@gmail.com
 * use:
 **/
public class FlowLayoutView extends View {
    private static final String TAG = "FlowLayoutView";

    public FlowLayoutView(Context context) {
        super(context);
    }

    public FlowLayoutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayoutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private final List<LabelItem> mItems = new ArrayList<>();
    private Paint mPaint = new Paint();
    private int mMaxRows;

    public void setLabels(@NonNull List<LabelItem> drawables) {
        this.mItems.clear();
        this.mItems.addAll(drawables);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (width > 0 && mItems.size() > 0) {
            int tempWidth = 0;
            int tempHeight = 0;
            int tmpRows = 0;
            int lineHeight = 0;
            for (int i = 0; i < mItems.size(); i++) {
                LabelItem item = mItems.get(i);
                mPaint.setTextSize(item.textSize);
                int w = (int) (mPaint.measureText(item.text) + item.leftRight * 2);
                if (lineHeight == 0) {
                    lineHeight = (int) (Math.abs(mPaint.descent() + mPaint.ascent()) + item.topBottom * 2);
                }
                if (tempWidth + w > width) {
                    Log.d(TAG, "onMeasure: 换行 tempWidth=" + tempWidth + " w=" + w);
                    tempWidth = 0;
                    tempHeight += lineHeight;
                    tmpRows++;
                    if (mMaxRows > 0 && tmpRows == mMaxRows + 1) {
                        break;
                    }
                    tempHeight += item.itemVerticalMargin;
                }
                item.set(tempWidth, tempHeight, lineHeight, w);
                tempWidth += w + item.itemHorizantalmargin;
                Log.d(TAG, "onMeasure: item=" + item.displayRect + " index=" + i);
            }
            tempHeight = tempHeight + lineHeight;
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.rgb(200, 200, 150));
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        for (LabelItem item : mItems) {
            item.draw(canvas, mPaint);
        }
    }

    public void setmMaxRows(int mMaxRows) {
        this.mMaxRows = mMaxRows;
    }

    public static class LabelItem {
        final float density = Resources.getSystem().getDisplayMetrics().density;

        private final RectF displayRect = new RectF();
        public String text;
        public int textColor = Color.WHITE;
        public float textSize = SizeUtils.sp2px(12);
        public int bgColor = Color.GRAY;
        public int bgRadius = (int) (density * 6);
        public int topBottom = (int) (density * 6);
        public int leftRight = (int) (density * 8);
        public int itemVerticalMargin = (int) (density * 8);
        public int itemHorizantalmargin = (int) (density * 8);

        private int textWidth;

        public LabelItem() {
        }

        public LabelItem(String text, int bgColor) {
            this.text = text;
            this.bgColor = bgColor;
        }

        private void set(int left, int top, int height, int with) {
            this.textWidth = with;
            displayRect.set(left, top, left + with, top + height);
        }

        public void draw(Canvas canvas, Paint paint) {
            //背景
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(bgColor);
            canvas.drawRoundRect(displayRect, bgRadius, bgRadius, paint);
            //文字
            paint.setStyle(Paint.Style.STROKE);
            paint.setTextSize(textSize);
            paint.setColor(textColor);
            canvas.save();
            canvas.translate(displayRect.left, displayRect.top);
            canvas.drawText(text, leftRight, (displayRect.height() / 2) - ((paint.descent() + paint.ascent()) / 2), paint);
            canvas.restore();
        }
    }
}

