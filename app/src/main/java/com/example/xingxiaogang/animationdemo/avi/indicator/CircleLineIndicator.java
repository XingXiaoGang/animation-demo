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

    private float mDensity;
    private int mArcWidth;
    private int mCircleSpacing;

    private static final int ENTER_SPEED = 500;
    private static final int SWIPE_SPEED = 3500;
    private static final int ROUNT_SPEED = 2000;

    private int mSwipeAngle = 5;
    private int mStartOffset;
    private int mCircleSpacingOffset;

    private final int[] bStartAngles = new int[]{135, -45};
    private RectF mArcRectFs1 = new RectF();

    public CircleLineIndicator() {

    }

    @Override
    public void setTarget(View target) {
        super.setTarget(target);
        if (target != null) {
            mDensity = target.getResources().getDisplayMetrics().density;
            mArcWidth = (int) (mDensity * 4);
            mCircleSpacing = (int) (mDensity * 8);
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        paint.setStrokeWidth(mArcWidth);
        paint.setStyle(Paint.Style.STROKE);

        final RectF rect = mArcRectFs1;
        final int[] startAngles = bStartAngles;
        final int circleSpacing = mCircleSpacing + mCircleSpacingOffset;

        rect.set(circleSpacing, circleSpacing, getWidth() - circleSpacing, getHeight() - circleSpacing);

        int swipValue = mSwipeAngle > 100 ? 200 - mSwipeAngle : mSwipeAngle;
        int startOff2 = mSwipeAngle > 100 ? mSwipeAngle - 100 : 100 - mSwipeAngle;
        canvas.drawArc(rect, startAngles[0] + mStartOffset + startOff2, swipValue - startOff2, false, paint);
        canvas.drawArc(rect, startAngles[1] + mStartOffset + startOff2, swipValue - startOff2, false, paint);
    }

    @Override
    public List<Animator> createAnimation() {

        List<Animator> animators = new ArrayList<>();

        //开始动画
        ValueAnimator scaleAnim = ValueAnimator.ofInt(mCircleSpacing * 5, 0);
        scaleAnim.setDuration(ENTER_SPEED);
        scaleAnim.setInterpolator(new LinearInterpolator());
        scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCircleSpacingOffset = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        scaleAnim.start();
        animators.add(scaleAnim);

        ValueAnimator swipeAnim = ValueAnimator.ofInt(0, 195);
        swipeAnim.setDuration(SWIPE_SPEED);
        swipeAnim.setInterpolator(new LinearInterpolator());
        swipeAnim.setRepeatCount(-1);
        swipeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSwipeAngle = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        swipeAnim.start();
        animators.add(swipeAnim);

        ValueAnimator startOffAnim = ValueAnimator.ofInt(0, 360);
        startOffAnim.setDuration(ROUNT_SPEED);
        startOffAnim.setStartDelay(ENTER_SPEED);
        startOffAnim.setInterpolator(new LinearInterpolator());
        startOffAnim.setRepeatCount(-1);
        startOffAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mStartOffset = (int) animation.getAnimatedValue();
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
