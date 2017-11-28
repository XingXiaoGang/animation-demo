package com.example.xingxiaogang.animationdemo.view.snow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xingxiaogang on 2017/11/28.
 */

public class SnowPanel extends View {

    public SnowPanel(Context context) {
        super(context);
        init(context, null);
    }

    public SnowPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SnowPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private TimerTask mTimerTask;
    private Timer mTimer;
    private Random mRandom;
    private final List<Snow> mSnows = new ArrayList<>();
    private Bitmap mSnowSrc;
    private long mLastTime;
    private int mMaxNewPerSecond;
    private int mSpeedXBase;
    private int mSpeedYBase;
    private int mMaxDistance;

    private void init(Context context, AttributeSet attrs) {
        mRandom = new Random();
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                logic();
                postInvalidate();
            }
        };
    }

    public void startSnow(int maxSizePerSecond, int timeInterval, int maxDistance, int speedXBase, int speedYBase, Bitmap snowsrc) {
        mMaxNewPerSecond = maxSizePerSecond;
        this.mSpeedXBase = speedXBase;
        this.mSpeedYBase = speedYBase;
        this.mSnowSrc = snowsrc;
        this.mMaxDistance = maxDistance;
        if (maxSizePerSecond <= 0) {
            throw new IllegalArgumentException();
        }

        fillSnow();
        mLastTime = System.currentTimeMillis();
        mTimer.schedule(mTimerTask, 30, timeInterval);
    }

    public void stopSnow() {
        mTimer.cancel();
        mTimer.purge();
    }

    private void logic() {
        synchronized (mSnows) {
            final List<Snow> snows = this.mSnows;
            //移除失效的
            List<Snow> toRemove = new ArrayList<>();
            for (Snow snow : snows) {
                if (snow.isDead()) {
                    toRemove.add(snow);
                }
            }
            snows.removeAll(toRemove);

            fillSnow();

            for (Snow snow : snows) {
                snow.logic();
            }
        }
    }

    private void fillSnow() {
        //诞生新的
        if (System.currentTimeMillis() - mLastTime > 1000) {
            mLastTime = System.currentTimeMillis();
            int sizeNow = mRandom.nextInt(mMaxNewPerSecond);
            Rect rect = new Rect(0, 0, getWidth(), getHeight());

            for (int i = 0; i <= sizeNow; i++) {
                mSnows.add(new Snow(rect, mSnowSrc, mSpeedXBase, mSpeedYBase, mMaxDistance));
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        synchronized (mSnows) {
            final List<Snow> snows = this.mSnows;
            for (Snow snow : snows) {
                snow.draw(canvas);
            }
        }
    }
}
