package com.example.xingxiaogang.animationdemo.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.xingxiaogang.animationdemo.R;

/**
 * Created by xingxiaogang on 2017/9/5.
 * 多个档位的开关. 目前是3个
 */

public class MultiSwitcher extends View implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

    //不同档位的seekBar颜色
    private static float dp_1;
    private static int states = 3;

    //圆点
    private int mDrawableSize;
    private Drawable mProgressDrawable;
    private final Rect mSeekbarRect = new Rect();
    //背景
    private int mSeekBgHeight;
    private int mSeekBgRadius;
    private int[] colors;
    private int mSeekBgColor;
    private final RectF mSeekBgRect = new RectF();
    //是否在滑动中,此时点击无效
    private boolean isSeeking;
    private int mCurrentState = -1;
    private ValueAnimator mAnimator;
    private int mPendingStates = -1;
    private int mPendingStateOnLayout = -1;

    public MultiSwitcher(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public MultiSwitcher(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MultiSwitcher(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        dp_1 = (int) Resources.getSystem().getDisplayMetrics().density;
        //todo 取属性
        colors = new int[]{Color.RED, Color.GRAY, Color.GREEN};
        mSeekBgColor = colors[0];
        mDrawableSize = (int) (dp_1 * 28);
        mSeekBgHeight = (int) (dp_1 * 8);
        mSeekBgRadius = (int) (dp_1 * 5);
        mProgressDrawable = ContextCompat.getDrawable(context, R.mipmap.abc_btn_switch_to_on);

        mAnimator = ValueAnimator.ofInt(0, 100);
        mAnimator.addUpdateListener(this);
        mAnimator.addListener(this);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.setDuration(300);

        setWillNotDraw(false);
        setState(1);
    }

    private void setState(int state) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (width <= 0 || height <= 0) {
            mPendingStateOnLayout = state;
            return;
        }
        int positionX = (int) (width * (state * 1.0f / states)) + getPaddingLeft();
        int positionY = height / 2 - mDrawableSize / 2;
        mSeekbarRect.set(positionX, positionY, positionX + mDrawableSize, positionY + mDrawableSize);
        invalidate();
        mCurrentState = state;
        mPendingStateOnLayout = -1;
    }

    private void startSwitch(int distance) {

    }

    private void setSateWithAnim(int state) {
        mPendingStates = state;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int aciton = event.getAction();
        boolean res = false;
        switch (aciton) {
            case MotionEvent.ACTION_DOWN: {
                int y = (int) event.getY();

                break;
            }
            case MotionEvent.ACTION_UP: {

                break;
            }
            case MotionEvent.ACTION_MOVE: {

                break;
            }
        }
        return res;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setState(mCurrentState);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //measure
        int height = (int) (Math.max(mDrawableSize, mSeekBgHeight) + getPaddingTop() + getBottom());
        //注：太小不利于操作
        height = (int) Math.max(height, dp_1 * 18);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);

        //逻辑
        if (mPendingStateOnLayout != -1) {
            setState(mPendingStateOnLayout);
        }
        mSeekBgRect.set(mDrawableSize / 2, getMeasuredHeight() / 2 - mSeekBgHeight / 2,
                getMeasuredWidth() - mDrawableSize / 2, getMeasuredHeight() / 2 + mSeekBgHeight / 2);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {

    }

    @Override
    public void onAnimationStart(Animator animation) {
        isSeeking = true;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        //结束seek动画
        isSeeking = false;
        if (mPendingStates != -1) {
            setState(mPendingStates);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        throw new RuntimeException("unsupport");
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    Paint p = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //背景
        p.setStyle(Paint.Style.FILL);
        p.setColor(mSeekBgColor);
        canvas.drawRoundRect(mSeekBgRect, mSeekBgRadius, mSeekBgRadius, p);

        //圆点
        mProgressDrawable.setBounds(mSeekbarRect);
        mProgressDrawable.draw(canvas);


    }
}
