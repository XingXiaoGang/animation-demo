package com.example.xingxiaogang.animationdemo.view.menu;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.xingxiaogang.animationdemo.SizeUtils;

/**
 * Created by xingxiaogang on 2017/7/18.
 */

public class ActionMeterialView extends View implements IMenu, ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

    private static final boolean DEBUG = true;
    private static final String TAG = "ActionMeterialView";

    private int mIconColor = Color.WHITE;
    private int mIconWidth = SizeUtils.dp2px(2.8f);
    private int mState = STATE_CLOSED;
    private Paint mPaint;
    private ValueAnimator mValueAnimator;
    private ValueAnimator.AnimatorListener mExtenalListener;
    private static final int[] mCurrentState = new int[]{0, 90};

    private static final int[] mCloseState = new int[]{0, 90};
    private static final int[] mOpenState = new int[]{135, 45};

    public ActionMeterialView(Context context) {
        super(context);
        initView(context, null);
    }

    public ActionMeterialView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attributeSet) {
        mValueAnimator = ValueAnimator.ofInt(0, 100);
        mValueAnimator.addUpdateListener(this);
        mValueAnimator.setDuration(300);
        mValueAnimator.addListener(this);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mIconColor);
        mPaint.setStrokeWidth(mIconWidth);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //todo 可以拓展一些xml属性  如：线的宽度，颜色 ，打开的时间
    }

    private void logic(int currentValue) {
        //计算位置
        mCurrentState[0] = mCloseState[0] + (int) ((mOpenState[0] - mCloseState[0]) * 1.0f * currentValue * 1.0f / 100);
        mCurrentState[1] = mCloseState[1] + (int) ((mOpenState[1] - mCloseState[1]) * 1.0f * currentValue * 1.0f / 100);
        //刷新
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getWidth() - getPaddingRight();
        int bottom = getHeight() - getPaddingBottom();
        int centerX = left + (right - left) / 2;
        int centerY = top + (bottom - top) / 2;

        int[] current = mCurrentState;
        //第一条
        canvas.save();
        canvas.rotate(current[0], centerX, centerY);
        canvas.drawLine(left, centerY, right, centerY, mPaint);
        canvas.restore();

        //第二条
        canvas.save();
        canvas.rotate(current[1], centerX, centerY);
        canvas.drawLine(left, centerY, right, centerY, mPaint);
        canvas.restore();
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
        logic((int) animation.getAnimatedValue());
    }

    @Override
    public void onAnimationStart(Animator animation) {
        if (mExtenalListener != null) {
            mExtenalListener.onAnimationStart(animation);
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (mState == STATE_OPENING) {
            mState = STATE_OPENED;
            if (mExtenalListener != null) {
                mExtenalListener.onAnimationEnd(animation);
            }
        } else if (mState == STATE_CLOSEING) {
            mState = STATE_CLOSED;
            if (mExtenalListener != null) {
                mExtenalListener.onAnimationEnd(animation);
            }
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
