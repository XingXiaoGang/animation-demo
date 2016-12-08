package com.example.xingxiaogang.animationdemo.view;

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
public class ColorDotLoadingDrawable extends Drawable {

    private Paint mCirclePaint;
    private Rect mDrawRect;
    private int mMainDotColor;
    private int mHighLightDotColor;
    private int mHighLightPosition;
    private int mDotRadius;
    private final static int MSG_RESFRESH = 2;
    private Handler mRefreshHandler;

    public ColorDotLoadingDrawable(int size) {
        init(size, Color.GREEN, Color.BLUE);
    }

    public ColorDotLoadingDrawable(int dotRadus, int mainColor, int highLightColor) {
        init(dotRadus, mainColor, highLightColor);
    }

    private void init(int dotRadius, int mainColor, int highLightColor) {
        this.mDotRadius = dotRadius;
        this.mMainDotColor = mainColor;
        this.mHighLightDotColor = highLightColor;
        final Paint paint = new Paint();
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
        final int radius = mDotRadius;
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
    public boolean setVisible(boolean visible, boolean restart) {
        super.setVisible(visible, restart);
        if (visible) {
            start(400);
        } else {
            stop();
        }
        return true;
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void start(final int duration) {
        if (mRefreshHandler == null) {
            mRefreshHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == MSG_RESFRESH) {
                        invalidateSelf();
                        mRefreshHandler.sendEmptyMessageDelayed(MSG_RESFRESH, duration);
                    }
                    return true;
                }
            });
            mRefreshHandler.sendEmptyMessage(MSG_RESFRESH);
        }
    }

    public void stop() {
        if (mRefreshHandler != null) {
            mRefreshHandler.removeMessages(MSG_RESFRESH);
        }
    }
}
