package com.example.xingxiaogang.animationdemo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

/**
 * Created by xingxiaogang on 2016/8/1.
 * 加载动画drawable 三个点 颜色交替变化
 */
public class ColorDotLoadingDrawable extends Drawable implements Handler.Callback {

    private Paint mCirclePaint;
    private Rect mDrawRect;
    private int mMainDotColor;
    private int mHighLightDotColor;
    private int mHighLightPosition;
    private Handler mHandler;
    private boolean isStart = false;
    private int mDuration;
    private int mDotRadius;

    public ColorDotLoadingDrawable() {
        init(8, Color.GREEN, Color.YELLOW);
    }

    public ColorDotLoadingDrawable(int dotRadus, int mainColor, int highLightColor) {
        init(dotRadus, mainColor, highLightColor);
    }

    private void init(int dotRadius, int mainColor, int highLightColor) {
        this.mDotRadius = dotRadius;
        this.mMainDotColor = mainColor;
        this.mHighLightDotColor = highLightColor;
        Paint paint = new Paint();
        paint.setColor(mMainDotColor);
        paint.setStyle(Paint.Style.FILL);
        this.mCirclePaint = paint;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mDrawRect = getBounds();
    }

    @Override
    public void draw(Canvas canvas) {
        final Rect rect = mDrawRect;
        final int radius = 8;
        final int oneW = (rect.right - rect.left) / 3;
        final int currentPosition = mHighLightPosition;
        for (int i = 0; i < 3; i++) {
            int x = oneW * i + oneW / 2 - radius / 2;
            int y = rect.centerY();
            if (currentPosition == i) {
                mCirclePaint.setColor(mHighLightDotColor);
            } else {
                mCirclePaint.setColor(mMainDotColor);
            }
            canvas.drawCircle(x, y, radius, mCirclePaint);
        }
        mHighLightPosition++;
        mHighLightPosition = mHighLightPosition % 3;
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public boolean handleMessage(Message msg) {
        invalidateSelf();
        if (isStart && mHandler != null) {
            mHandler.sendEmptyMessageDelayed(0, mDuration);
        }
        return true;
    }

    public void start(int duration) {
        if (mHandler == null) {
            isStart = true;
            mHandler = new Handler(this);
            this.mDuration = duration;
            mHandler.sendEmptyMessageDelayed(0, mDuration);
        }
    }

    public void stop() {
        isStart = false;
        if (mHandler != null) {
            mHandler.removeMessages(0);
            mHandler = null;
        }
    }
}
