package com.example.xingxiaogang.animationdemo.avi.indicator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingxiaogang on 2016/9/7.
 */
public class CircleLineIndicator extends BaseIndicatorController {


    private static final int ENTER_SPEED = 500;
    private static final int SWIPE_SPEED = 3500;
    private static final int ROUNT_SPEED = 2000;
    private static final int START_ANGLE_1 = 135;

    private int mCircleSpacingOffset;
    private float mDensity;
    private int mArcWidth;
    private int mCircleSpacing;
    private int mBaseOffset;

    private RectF mArcRectFs1 = new RectF();

    public CircleLineIndicator() {

    }

    @Override
    public void setTarget(View target) {
        super.setTarget(target);
        if (target != null) {
            mDensity = target.getResources().getDisplayMetrics().density;
            mArcWidth = (int) (mDensity * 2);
            mCircleSpacing = (int) (mDensity * 8);
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        paint.setStrokeWidth(mArcWidth);
        paint.setStyle(Paint.Style.STROKE);

        final RectF rect = mArcRectFs1;
        final int circleSpacing = mCircleSpacing + mCircleSpacingOffset;

        rect.set(circleSpacing, circleSpacing, getWidth() - circleSpacing, getHeight() - circleSpacing);

        canvas.drawArc(rect, START_ANGLE_1 + mBaseOffset, 20, false, paint);
    }

    @Override
    public List<Animator> createAnimation() {

        List<Animator> animators = new ArrayList<>();

        ValueAnimator startOffAnim = ValueAnimator.ofInt(0, 360);
        startOffAnim.setDuration(ROUNT_SPEED);
        startOffAnim.setStartDelay(ENTER_SPEED);
        startOffAnim.setInterpolator(new LinearInterpolator());
        startOffAnim.setRepeatCount(-1);
        startOffAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mBaseOffset = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        startOffAnim.start();
        animators.add(startOffAnim);

        return animators;
    }

    @Override
    public void setAnimationStatus(AnimStatus animStatus) {
        super.setAnimationStatus(animStatus);
    }
}
