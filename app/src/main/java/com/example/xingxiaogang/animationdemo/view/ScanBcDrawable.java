package com.example.xingxiaogang.animationdemo.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gang on 17-11-29.
 */

public class ScanBcDrawable extends Drawable {

    private static final boolean DEBUG = true;
    private static final String TAG = "ScanBcDrawable";

    private final List<LevelColor> mColors = new ArrayList<>();

    private int mCurrentX;
    private Rect mRect = new Rect();
    private Rect mLightRect = new Rect();
    private Timer mTimer = new Timer();
    private int mLightWidth = 360;
    private Paint mLightPaint;
    private int[] mLights;
    private int mCurrentBcColor;
    private int mLastLevelBgColor;

    private static final int AIM_DURATION = 400;
    private long mAnimStartTime;
    private boolean isBgColorAnimating;
    private int mNextLevelColor;
    private boolean stopWhenColorEnd;


    private static final int INVALIDATE_SElF = 111;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INVALIDATE_SElF: {
                    invalidateSelf();
                    break;
                }
            }
        }
    };

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            //光效
            mCurrentX += 4;
            if (mCurrentX >= mRect.width() + mLightWidth) {
                mCurrentX = 0;
            }
            mLightRect.set(mCurrentX - mLightWidth, 0, mCurrentX, mRect.height());

            //背景色过度
            if (isBgColorAnimating) {
                long timePast = System.currentTimeMillis() - mAnimStartTime;
                float percent = timePast * 1.0f / AIM_DURATION;
                mCurrentBcColor = flatColor(percent);
                if (DEBUG) {
                    Log.v(TAG, "onLevelUpdate: animToFinalColor  time=" + percent + " color= " + mCurrentBcColor);
                }
                if (percent >= 1) {
                    mLastLevelBgColor = mNextLevelColor;
                    mCurrentBcColor = mNextLevelColor;
                    isBgColorAnimating = false;
                    if (stopWhenColorEnd) {
                        stopScan();
                    }
                    if (DEBUG) {
                        Log.i(TAG, "onLevelUpdate: animToFinalColor end  time=" + percent + " color= " + mCurrentBcColor);
                    }
                }
            }

            handler.sendEmptyMessage(INVALIDATE_SElF);
        }
    };


    public ScanBcDrawable() {
        init();
    }

    /**
     * 不同的级别使用不同的颜色
     * 注：colors.length=levals.length
     **/
    public ScanBcDrawable(int[] values, int[] colors, int[] lights) {
        mLights = lights;
        if (colors.length != values.length || colors.length < 1) {
            throw new RuntimeException("res 配置不正确 ");
        }
        for (int i = 0; i < colors.length; i++) {
            mColors.add(new LevelColor(values[i], colors[i]));
        }
        mCurrentBcColor = colors[0];
        mNextLevelColor = colors[0];
        mLastLevelBgColor = colors[0];
        init();
    }

    private int flatColor(float radio) {
        final int current = mLastLevelBgColor;
        int redStart = Color.red(current);
        int blueStart = Color.blue(current);
        int greenStart = Color.green(current);
        final int finalColor = mNextLevelColor;
        int redEnd = Color.red(finalColor);
        int blueEnd = Color.blue(finalColor);
        int greenEnd = Color.green(finalColor);

        int red = (int) (redStart + ((redEnd - redStart) * radio + 0.5));
        int greed = (int) (greenStart + ((greenEnd - greenStart) * radio + 0.5));
        int blue = (int) (blueStart + ((blueEnd - blueStart) * radio + 0.5));
        return Color.argb(255, red, greed, blue);
    }

    private void init() {
        if (mLights == null || mLights.length != 4) {
            throw new RuntimeException("progress Lights 配置不正确 ");
        }
        mLightPaint = new Paint();
        mLightPaint.setStyle(Paint.Style.FILL);
        mLightPaint.setShader(new LinearGradient(0, 0, mLightWidth, 0,
                mLights,
                new float[]{0f, 0.958f, 0.96f, 1f},
                Shader.TileMode.CLAMP));
    }

    /**
     * length=4
     **/
    public void setProgressLights(int[] light) {
        mLights = light;
    }

    public void setLevalColors(List<LevelColor> colors) {
        mColors.clear();
        mColors.addAll(colors);
    }

    /**
     * 开始扫描动画效果
     **/
    public void startScan() {
        mTimer.schedule(timerTask, 30, 10);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mRect.set(bounds);
    }

    /**
     * 结束扫描动画效果
     **/
    public void stopScan() {
        if (isBgColorAnimating) {
            stopWhenColorEnd = true;
            return;
        }
        mTimer.cancel();
        mTimer.purge();
    }

    /**
     * 进度变化,更新背景
     **/
    public void onLevelUpdate(int value) {
        int toColor = 0;
        int lastColor = mCurrentBcColor;
        for (LevelColor color : mColors) {
            if (value > color.mValue) {
                toColor = color.mColor;
            } else {
                toColor = lastColor;
                break;
            }
            lastColor = color.mColor;
        }
        if (mNextLevelColor != toColor) {
            mNextLevelColor = toColor;
            animToFinalColor(toColor);
            if (DEBUG) {
                Log.i(TAG, "onLevelUpdate: animToFinalColor " + mCurrentBcColor + " -> " + mNextLevelColor);
            }
        }
    }

    private void animToFinalColor(int to) {
        mAnimStartTime = System.currentTimeMillis();
        isBgColorAnimating = true;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawColor(mCurrentBcColor);

        canvas.save();
        canvas.translate(mCurrentX - mLightRect.width(), 0);
        canvas.drawPaint(mLightPaint);
        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }

    private static class LevelColor {
        int mValue;
        int mColor;

        public LevelColor(int mValue, int mColor) {
            this.mValue = mValue;
            this.mColor = mColor;
        }
    }
}
