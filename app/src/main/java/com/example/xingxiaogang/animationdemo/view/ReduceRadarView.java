package com.example.xingxiaogang.animationdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

/**
 * Created by xingxiaogang on 2016/6/3.
 * 停止后缩小的
 */
public class ReduceRadarView extends RadarView {


    private Animation mScaleAnim;
    private int DURATION = 600;
    private boolean isScale = true;

    public ReduceRadarView(Context context) {
        super(context);
        init(context);
    }

    public ReduceRadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mScaleAnim = new ScaleAnimation(1, 0.4f, 1, 0.4f, RotateAnimation.RELATIVE_TO_SELF, 1f, RotateAnimation.RELATIVE_TO_SELF, 1f);
        mScaleAnim.setDuration(DURATION);
        mScaleAnim.setFillAfter(true);
    }

    @Override
    protected void onScanStart() {
        super.onScanStart();
        mScaleAnim.cancel();
    }

    @Override
    protected void onScanStop() {
        super.onScanStop();
        if (isScale) {
            startAnimation(mScaleAnim);
        }
    }

    public int getScaleDuration() {
        return DURATION;
    }

    public void setStopScale(boolean isScale) {
        this.isScale = isScale;
    }

    public void setScaleDuration(int duration) {
        this.DURATION = duration;
        mScaleAnim.setDuration(duration);
    }
}
