package com.example.xingxiaogang.animationdemo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingxiaogang on 2017/7/18.
 */

public class ActionMenuLayout extends LinearLayout implements IMenu, ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

    private static final boolean DEBUG = true;
    private static final String TAG = "ActionMenuLayout";
    private int mState = STATE_CLOSED;
    private ValueAnimator mValueAnimator;
    private ValueAnimator.AnimatorListener mExtenalListener;
    private final List<View> mElements = new ArrayList<>();

    private static final long TIME_BETWEEN = 40;
    private static final long DRATION = 300;

    private int[] mStartPositions;
    private int[] mEndPositions;
    private int[] mCurrentPositions;

    public ActionMenuLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public ActionMenuLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attributeSet) {
        mValueAnimator = ValueAnimator.ofInt(0, 100);
        mValueAnimator.addUpdateListener(this);
        mValueAnimator.setDuration(DRATION);
        mValueAnimator.addListener(this);
        mValueAnimator.setInterpolator(new OvershootInterpolator(1));

        findElements();
    }

    private void findElements() {
        mElements.clear();
        for (int i = 0; i < getChildCount(); i++) {
            mElements.add(getChildAt(i));
        }
    }

    private void logic(int curentValue) {
        if (DEBUG) {
            Log.d(TAG, "logic: " + curentValue);
        }
        //计算当前位置
        int count = getChildCount();
        int allBetween = (int) ((count - 1) * TIME_BETWEEN);
        if (allBetween > DRATION) {
            return;
        }

        long oneTime = (DRATION - allBetween);

        long currentTime = (long) (curentValue * 1.0f / 100 * DRATION);

        if (DEBUG) {
            Log.d(TAG, "logic: currentTime=" + currentTime);
        }

        for (int i = 0; i < count; i++) {
            int start = mStartPositions[i];
            int end = mEndPositions[i];

//            mCurrentPositions[i] = (int) ((start - end) * Math.max(1, Math.max(0, (currentTime - TIME_BETWEEN * i) * 1.0f / oneTime)));
            mCurrentPositions[i] = start + (int) ((end - start) * (curentValue * 1.0f / 100));
            if (DEBUG) {
                Log.d(TAG, "logic: translate percent:  " + i + " = " + Math.max(0, (currentTime - TIME_BETWEEN * i) * 1.0f / oneTime));
            }
        }
        invalidate();
    }

    @Override
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
        findElements();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //计算位置
        int count = getChildCount();
        mEndPositions = new int[count];
        mStartPositions = new int[count];
        mCurrentPositions = new int[count];
        for (int i = 0; i < count; i++) {
            View v = getChildAt(i);
            mStartPositions[i] = getBottom();
            mEndPositions[i] = v.getTop();
            mCurrentPositions[i] = getBottom();
        }
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        findElements();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        List<View> elements = mElements;
        for (int i = 0; i < elements.size(); i++) {
            canvas.save();
            canvas.translate(0, mCurrentPositions[i]);
            elements.get(i).draw(canvas);
            canvas.restore();
        }
    }

    @Override
    public boolean open() {
        if (mState == STATE_CLOSED) {
            mState = STATE_OPENING;
            mValueAnimator.start();
            return true;
        }
        return false;
    }

    @Override
    public boolean close() {
        if (mState == STATE_OPENED) {
            mState = STATE_CLOSEING;
            mValueAnimator.reverse();
            return true;
        }
        return false;
    }

    @Override
    public int getState() {
        return mState;
    }

    @Override
    public boolean toggle() {
        switch (mState) {
            case STATE_OPENED: {
                close();
                return true;
            }
            case STATE_CLOSED: {
                open();
                return true;
            }
        }
        return false;
    }

    @Override
    public void setListener(Animator.AnimatorListener listener) {
        mExtenalListener = listener;
    }

    @Override
    public long getDuration() {
        return mValueAnimator.getDuration();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        logic((Integer) animation.getAnimatedValue());
    }

    @Override
    public void onAnimationStart(Animator animation) {
        if (mExtenalListener != null) {
            mExtenalListener.onAnimationStart(animation);
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (((int) ((ValueAnimator) animation).getAnimatedValue()) == 100) {
            mState = STATE_OPENED;
        } else {
            mState = STATE_CLOSED;
        }
        if (mExtenalListener != null) {
            mExtenalListener.onAnimationEnd(animation);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        if (mExtenalListener != null) {
            mExtenalListener.onAnimationCancel(animation);
        }
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        if (mExtenalListener != null) {
            mExtenalListener.onAnimationRepeat(animation);
        }
    }
}
