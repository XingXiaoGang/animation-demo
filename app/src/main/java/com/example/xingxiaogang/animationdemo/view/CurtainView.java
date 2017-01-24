package com.example.xingxiaogang.animationdemo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

/**
 * Created by xingxiaogang on 2017/1/18.
 */

public class CurtainView extends FrameLayout implements ValueAnimator.AnimatorUpdateListener {

    private static final String TAG = "CurtainView";
    private Path mPath;
    private Rect mVisibleRect;
    private ValueAnimator mAnimator;

    private int mCurrentValue = 400;
    private int mMaxValue;

    private boolean isAnimating;
    private Paint mPaint;

    public CurtainView(Context context) {
        super(context);
        initView(context, null);
    }

    public CurtainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CurtainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attr) {
        mPath = new Path();
        mVisibleRect = new Rect();
        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.FILL);
        setWillNotDraw(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mVisibleRect.set(0, right - left, 0, bottom - top);
    }

    public void startOpenAnim() {
        isAnimating = true;
        mAnimator = ValueAnimator.ofInt(0, mMaxValue);
        mAnimator.setDuration(500);
        mAnimator.addUpdateListener(this);
        mAnimator.start();
    }

    public void cancleAnim() {
        isAnimating = true;
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
//        mCurrentValue = (int) mAnimator.getAnimatedValue();
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
//        super.draw(canvas);

        Log.d(TAG, "onDraw: ");
        canvas.drawColor(Color.WHITE);
        if (isAnimating) {
            final Path path = mPath;
            path.reset();
            final int height = mVisibleRect.height();
            final int width = mVisibleRect.width();
            float persent = mCurrentValue * 1.0f / mMaxValue;
            path.moveTo(0, 0);
            path.lineTo( width / 2, 0);
//            path.quadTo(0, 0, width / 2, 0);
//            path.quadTo(width, 0, 0, height);
            path.lineTo(width, height);
            path.lineTo(0, 0);
            path.close();
            canvas.drawPath(path, mPaint);
//            canvas.clipPath(path);
        }
//        super.onDraw(canvas);
    }
}
