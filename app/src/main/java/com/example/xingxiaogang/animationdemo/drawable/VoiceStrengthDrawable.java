package com.example.xingxiaogang.animationdemo.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.Log;

import com.example.xingxiaogang.animationdemo.SizeUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.xingxiaogang.animationdemo.BuildConfig.DEBUG;

public class VoiceStrengthDrawable extends Drawable {
    private static final String TAG = "VoiceStrengthDrawable";

    private static final int mLineNum = 4;
    private static final float mSpeed = 0.12f;
    private static final int mDistanceHorizantal = SizeUtils.dp2px(10);
    private static final int mMaxHeight = SizeUtils.dp2px(12);
    private static final int mLineWidth = SizeUtils.dp2px(3.5f);

    private final List<Line> mLines = new ArrayList<>();
    private RectF mDrawRect = new RectF();
    private Paint mPaint;
    private int mWaveNormalColor;
    private int mWaveCancelColor;

    private boolean needReset = true;
    //时间相关
    private long lastDrawTime = 0;

    public VoiceStrengthDrawable(@ColorInt int normalColor, @ColorInt int cancelColor) {
        this.mPaint = new Paint();
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setColor(normalColor);
        this.mPaint.setStrokeWidth(mLineWidth);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mWaveNormalColor = normalColor;
        this.mWaveCancelColor = cancelColor;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        if (DEBUG) {
            Log.d(TAG, "onBoundsChange: " + bounds.width());
        }
        mDrawRect.set(bounds);
        initLine();
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        if (mDrawRect.width() == 0) {
            return;
        }

        logic();

        final List<Line> waves = mLines;
        for (Line oval : waves) {
            oval.draw(canvas, mPaint);
        }
        invalidateSelf();
    }

    private void initLine() {
        int parentCenterY = (int) mDrawRect.centerY();
        synchronized (mLines) {
            mLines.clear();
            //左边的
            for (int i = 0; i < mLineNum; i++) {
                mLines.add(new Line((int) (mDrawRect.left + (i + 1) * mDistanceHorizantal), parentCenterY, i * mMaxHeight / 4));
            }
            //右边的
            for (int i = 0; i < mLineNum; i++) {
                mLines.add(new Line((int) (mDrawRect.right - (i + 1) * mDistanceHorizantal), parentCenterY, i * mMaxHeight / 4));
            }
        }
    }

    private void logic() {
        if (needReset) {
            initLine();
            needReset = false;
        }

        final List<Line> lines = mLines;
        long timePast = lastDrawTime == 0 ? 8 : System.currentTimeMillis() - lastDrawTime;
        for (int i = 0; i < lines.size(); i++) {
            lines.get(i).logic(timePast * mSpeed);
        }
        lastDrawTime = System.currentTimeMillis();
    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }

    public void reset() {
        if (DEBUG) {
            Log.d(TAG, "reset: ");
        }
        lastDrawTime = 0;
        needReset = true;
    }

    public void setIsCancelling(boolean isCancelling) {
        mPaint.setColor(isCancelling ? mWaveCancelColor : mWaveNormalColor);
    }

    private static class Line {
        int height;
        int centerY;
        int centerX;

        boolean isAdding;

        Line(int centerX, int centerY, int height) {
            this.centerY = centerY;
            this.centerX = centerX;
            this.height = height;
        }

        void setHeight(int height) {
            if (height < 0) {
                height = 0;
            }
            this.height = height;
        }

        int getHeight() {
            return height;
        }

        void logic(float add) {
            if (isAdding) {
                setHeight((int) (getHeight() + add));
            } else {
                setHeight((int) (getHeight() - add));
            }

            if (getHeight() >= mMaxHeight) {
                isAdding = false;
            } else if (getHeight() <= 0) {
                isAdding = true;
            }
        }

        void draw(Canvas canvas, Paint paint) {
            int top = centerY - Math.max(1, height / 2);
            int bottom = centerY + height / 2;
            canvas.drawLine(centerX, top < 0 ? 0 : top, centerX, bottom > mMaxHeight * 2 ? mMaxHeight * 2 : bottom, paint);
        }
    }
}
