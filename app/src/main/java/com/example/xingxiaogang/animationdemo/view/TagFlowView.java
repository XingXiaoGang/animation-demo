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
import android.view.View;

import com.example.xingxiaogang.animationdemo.SizeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinggang on 2018/11/20 下午4:26.
 * <p>
 * email: xxg841076938@gmail.com
 * use:标签控件,流式布局
 **/
public class TagFlowView extends View {

    private final List<LabelItem> mItems = new ArrayList<>();
    private Paint mPaint = new Paint();
    private int mMaxRows;

    public TagFlowView(Context context) {
        super(context);
    }

    public TagFlowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TagFlowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setLabels(@NonNull List<LabelItem> drawables) {
        this.mItems.clear();
        this.mItems.addAll(drawables);
        if (getMeasuredHeight() > 0) {
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (width > 0 && mItems.size() > 0) {
            int tempWidth = 0;
            int tempHeight = 0;
            int tmpRows = 0;
            int lineHeight = 0;
            boolean isMaxRows = false;
            for (int i = 0; i < mItems.size(); i++) {
                LabelItem item = mItems.get(i);
                mPaint.setTextSize(item.textSize);
                int w = (int) (mPaint.measureText(item.text) + item.leftRight * 2);
                if (lineHeight == 0) {
                    lineHeight = (int) (Math.abs(mPaint.descent() + mPaint.ascent()) + item.topBottom * 2);
                }
                if (tempWidth + w > width) {
                    tempWidth = 0;
                    tempHeight += lineHeight;
                    tmpRows++;
                    if (mMaxRows > 0 && tmpRows == mMaxRows) {
                        isMaxRows = true;
                        break;
                    }
                    tempHeight += item.itemVerticalMargin;
                }
                item.set(tempWidth, tempHeight, lineHeight, w);
                tempWidth += w + item.itemHorizontalMargin;
            }
            tempHeight = isMaxRows ? tempHeight : tempHeight + lineHeight;
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#aabb44"));
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        for (LabelItem item : mItems) {
            if (item.isLayout) {
                item.draw(canvas, mPaint);
            }
        }
    }

    public void setMaxRows(int mMaxRows) {
        this.mMaxRows = mMaxRows;
    }

    public static class LabelItem {
        private final float density = Resources.getSystem().getDisplayMetrics().density;
        private final RectF displayRect = new RectF();
        private boolean isLayout;

        public String text;
        public int textColor;
        public int bgColor;
        public float textSize = SizeUtils.sp2px(12);
        public int bgRadius = (int) (density * 6);

        public int topBottom = (int) (density * 6);
        public int leftRight = (int) (density * 8);
        public int itemVerticalMargin = (int) (density * 8);
        public int itemHorizontalMargin = (int) (density * 8);

        public LabelItem(String text, int textColor, int bgColor) {
            this.text = text;
            this.textColor = textColor;
            this.bgColor = bgColor;
        }

        private void set(int left, int top, int height, int with) {
            displayRect.set(left, top, left + with, top + height);
            isLayout = true;
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

