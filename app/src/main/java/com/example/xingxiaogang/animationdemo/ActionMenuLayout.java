package com.example.xingxiaogang.animationdemo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingxiaogang on 2017/7/18.
 */

public class ActionMenuLayout extends LinearLayout implements IMenu, Animator.AnimatorListener {

    private static final boolean DEBUG = true;
    private static final String TAG = "ActionMenuLayout";
    private int mState = STATE_CLOSED;
    private ValueAnimator.AnimatorListener mExtenalListener;

    private static final long TIME_BETWEEN = 80;
    private static final long DURATION = 200;

    private final List<ValueAnimator> mOpenAnimators = new ArrayList<>();
    private final List<View> mElements = new ArrayList<>();
    private boolean isAnimInited;

    public ActionMenuLayout(Context context) {
        super(context);
        initView();
        setClipChildren(false);
    }

    public ActionMenuLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {

    }

    private void initAnims() {
        isAnimInited = true;
        for (int i = 0; i < mElements.size(); i++) {
            View child = mElements.get(i);
            PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("alpha", 0.6f, 1);
            PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("translationY", child.getHeight(), 0);
            ObjectAnimator openAnimator = ObjectAnimator.ofPropertyValuesHolder(child, holder1, holder2);
            openAnimator.setStartDelay(TIME_BETWEEN * i);
            openAnimator.setInterpolator(new OvershootInterpolator());
            openAnimator.setDuration(DURATION);
            openAnimator.addListener(this);
            mOpenAnimators.add(openAnimator);
        }
    }

    private void updateAnim(boolean open) {
        int size = mOpenAnimators.size();
        for (int i = 0; i < size; i++) {
            Animator animator = mOpenAnimators.get(i);
            if (open) {
                animator.setStartDelay(TIME_BETWEEN * i);
                animator.setInterpolator(new OvershootInterpolator(1));
                animator.setDuration(DURATION);
            } else {
                animator.setStartDelay((size - 1 - i) * TIME_BETWEEN);
                animator.setInterpolator(new LinearInterpolator());
                animator.setDuration(100);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mElements.clear();
        for (int i = getChildCount() - 1; i >= 0; i--) {
            mElements.add(getChildAt(i));
            getChildAt(i).setVisibility(INVISIBLE);
        }
    }

    @Override
    public boolean open() {
        if (mState == STATE_CLOSED) {
            mState = STATE_OPENING;
            if (!isAnimInited) {
                initAnims();
            }
            updateAnim(true);
            for (ValueAnimator animator : mOpenAnimators) {
                animator.start();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean close() {
        if (mState == STATE_OPENED) {
            mState = STATE_CLOSEING;
            if (!isAnimInited) {
                initAnims();
            }
            updateAnim(false);
            for (ValueAnimator animator : mOpenAnimators) {
                animator.reverse();
            }
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
        return DURATION + (mElements.size() - 1) * TIME_BETWEEN;
    }

    @Override
    public void onAnimationStart(Animator animation) {
        if (mExtenalListener != null) {
            mExtenalListener.onAnimationStart(animation);
        }
        //显示item
        mElements.get(mOpenAnimators.indexOf(animation)).setVisibility(VISIBLE);
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (mState == STATE_OPENING && mOpenAnimators.indexOf(animation) == mOpenAnimators.size() - 1) {
            mState = STATE_OPENED;
            if (mExtenalListener != null) {
                mExtenalListener.onAnimationEnd(animation);
            }
        } else if (mState == STATE_CLOSEING && mOpenAnimators.indexOf(animation) == 0) {
            mElements.get(mOpenAnimators.indexOf(animation)).setVisibility(INVISIBLE);

            mState = STATE_CLOSED;
            if (mExtenalListener != null) {
                mExtenalListener.onAnimationEnd(animation);
            }
        }

        if (mState == STATE_CLOSEING) {
            //隐藏item
            mElements.get(mOpenAnimators.indexOf(animation)).setVisibility(INVISIBLE);
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
