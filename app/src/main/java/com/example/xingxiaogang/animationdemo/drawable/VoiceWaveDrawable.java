package com.example.xingxiaogang.animationdemo.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.example.xingxiaogang.animationdemo.BuildConfig.DEBUG;

public class VoiceWaveDrawable extends Drawable {
    private static final String TAG = "VoiceWaveDrawable";

    private static final int mWaveNum = 2;
    private static final float mSpeed = 0.15f;

    private final List<Oval> mWaves = new ArrayList<>();
    private RectF mDrawRect = new RectF();
    private RectF mCenterRect = new RectF();
    private Paint mPaint;
    private int mWaveNormalColor;
    private int mWaveCancelColor;

    //时间相关
    private long lastDrawTime = 0;
    //每个环的开始时间有一定的时间间隔
    private static final long mDuration = 700;
    private boolean needReset = true;

    //录制状态是否是上滑取消
    private boolean isCancelling;

    public VoiceWaveDrawable(@ColorInt int normalColor, @ColorInt int cancelColor) {
        this.mPaint = new Paint();
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mWaveNormalColor = normalColor;
        this.mWaveCancelColor = cancelColor;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        if (DEBUG) {
            Log.d(TAG, "onBoundsChange: " + bounds.width());
        }
        synchronized (mWaves) {
            mWaves.clear();
            for (int i = 0; i < mWaveNum; i++) {
                mWaves.add(new Oval());
            }
        }
        mDrawRect.set(bounds);
        //最初圆形的状态
        mCenterRect.set(mDrawRect.centerX() - 1, mDrawRect.centerY() - 1, mDrawRect.centerX() + 1, mDrawRect.centerY() + 1);
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

        final List<Oval> waves = mWaves;
        for (Oval oval : waves) {
            if (oval.rectF.width() > 0 && oval.isStarted()) {
                canvas.drawOval(oval.rectF, getPaint(oval.rectF));
            }
        }

        invalidateSelf();
    }

    private void logic() {
        final List<Oval> waves = mWaves;
        if (needReset) {
            synchronized (mWaves) {
                for (Oval oval : waves) {
                    oval.rectF.set(mCenterRect);
                    oval.mStartTime = 0;
                }
            }
            needReset = false;
        }
        long timePast = lastDrawTime == 0 ? 8 : System.currentTimeMillis() - lastDrawTime;
        for (int i = 0; i < waves.size(); i++) {
            Oval oval = waves.get(i);
            //更新开始绘制的时间点
            if (i == 0 && oval.mStartTime == 0) {
                oval.mStartTime = System.currentTimeMillis();
                for (int i1 = 0; i1 < waves.size(); i1++) {
                    waves.get(i1).mStartTime = oval.mStartTime + i1 * mDuration;
                }
            }
            if (oval.isStarted()) {
                float distance = timePast * mSpeed;
                oval.rectF.left -= distance;
                oval.rectF.right += distance;
                oval.rectF.top -= distance;
                oval.rectF.bottom += distance;
                //张满了以后，再从0开始
                if (oval.rectF.width() >= mDrawRect.width()) {
                    oval.rectF.set(mCenterRect);
                }
            }
        }
        lastDrawTime = System.currentTimeMillis();
    }

    private Paint getPaint(RectF rect) {
        float size = rect.width();
        float percent = Math.max(0, 1 - size / mDrawRect.width());
        int color = isCancelling ? mWaveCancelColor : mWaveNormalColor;
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = Color.alpha(color);
        mPaint.setColor(Color.argb((int) (alpha * percent), red, green, blue));
        return mPaint;
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
        this.isCancelling = isCancelling;
    }

    private static class Oval {
        RectF rectF = new RectF();
        long mStartTime;

        boolean isStarted() {
            return System.currentTimeMillis() >= mStartTime;
        }
    }
}
