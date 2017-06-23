package com.example.xingxiaogang.animationdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.xingxiaogang.animationdemo.R;

/**
 * Created by xingxiaogang on 2017/6/19.
 * 可以添加子view到任意百分比位置
 */

public class PercentLayout extends ViewGroup {
    private static final boolean DEBUG = true;
    private static final String TAG = "PercentLayout";

    public PercentLayout(@NonNull Context context) {
        super(context);
    }

    public PercentLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PercentLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initVeiw(Context context, AttributeSet attrs) {

    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int width = r - l;
        int height = b - t;
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            LayoutParams lp = (LayoutParams) view.getLayoutParams();
            int childWidth = view.getMeasuredWidth();
            int childHeight = view.getMeasuredHeight();
            int left = (int) (l + width * lp.percentX) - childWidth / 2;
            int top = (int) (t + height * lp.percentY) - childHeight / 2;
            int right = left + view.getMeasuredWidth();
            int bottom = top + view.getMeasuredHeight();
            //修正
            if (left < l) {
                left = l;
                right = left + childWidth;
            }
            if (right > r) {
                right = r;
                left = right - childWidth;
            }
            if (top < t) {
                top = t;
                bottom = top + childHeight;
            }
            if (bottom >= b) {
                bottom = b;
                top = bottom - childHeight;
            }
            view.layout(left, top, right, bottom);
            if (DEBUG) {
                Log.d(TAG, "onLayout: left=" + left + " ,top=" + top + " right=" + right + " bottom=" + bottom + " selfW:" + getWidth() + " selfH:" + getHeight());
            }
        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        return super.drawChild(canvas, child, drawingTime);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {

        public float percentX;
        public float percentY;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.PercentLayout);
            percentX = a.getFloat(R.styleable.PercentLayout_percentInX, 0);
            percentY = a.getFloat(R.styleable.PercentLayout_percentInY, 0);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
