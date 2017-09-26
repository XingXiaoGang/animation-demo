package com.example.xingxiaogang.animationdemo.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.example.xingxiaogang.animationdemo.R;


/**
 * Created by xingxiaogang on 2017/9/26.
 */

public class SwipeItemLayout extends FrameLayout implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

    private static final String TAG = "SwipeItemLayout";
    private static final boolean DEBUG = true;

    public static final int DRAG_LEFT = 0;
    public static final int DRAG_RIGHT = 1;
    public static final int DRAG_BOTH = 2;


    private static final int mDismissTime = 250;
    private static final int mResetTime = 250;
    private static final int mDismissDistance = 8;

    private int mDragMode = DRAG_BOTH;
    private boolean isDragging;
    private boolean isSliding;
    private boolean isRemoving;
    private boolean isReseting;
    private int mDownX;
    private int mDistanceX;
    private int mTouchSlop;
    private int mDragTargetResID;
    private ValueAnimator mValueAnimator;
    private ISwipeListener mSwipeListener;

    public SwipeItemLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public SwipeItemLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SwipeItemLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attr) {
        setFocusable(true);
        setFocusableInTouchMode(true);
        setWillNotDraw(false);
        setClickable(true);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mValueAnimator = new ValueAnimator();
        mValueAnimator.addUpdateListener(this);
        mValueAnimator.addListener(this);

        if (attr != null) {
            TypedArray a = getContext().obtainStyledAttributes(attr, R.styleable.SwipeItemLayout);
            mDragMode = a.getInt(R.styleable.SwipeItemLayout_swipeMode, DRAG_LEFT);
            mDragTargetResID = a.getResourceId(R.styleable.SwipeItemLayout_dragContentId, 0);
            a.recycle();
        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        if (child.getId() == mDragTargetResID) {
            canvas.save();
            canvas.translate(mDistanceX, 0);
            child.draw(canvas);
            canvas.restore();
        } else {
            child.draw(canvas);
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isDragging || super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP: {
                onActionUp();
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                performRestore();
                break;
            }
            case MotionEvent.ACTION_DOWN: {
                if (DEBUG) {
                    Log.d(TAG, "dispatchTouchEvent: DOWN ");
                }
                mDownX = (int) event.getX();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (DEBUG) {
                    Log.d(TAG, "dispatchTouchEvent: move " + (event.getX() - mDownX));
                }
                mDistanceX = (int) event.getX() - mDownX;
                if (!isDragging && Math.abs(mDistanceX) > mTouchSlop) {
                    isDragging = true;
                }
                //判断方向
                if (mDragMode == DRAG_LEFT && mDistanceX > 0
                        || mDragMode == DRAG_RIGHT && mDistanceX < 0) {
                    mDistanceX = 0;
                }
                if (isDragging) {
                    invalidate();
                }
                break;
            }
        }
        if (isDragging) {
            requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(event);
    }

    //todo 应该还有一个速度
    private void onActionUp() {
        if (Math.abs(mDistanceX) > mDismissDistance * mTouchSlop) {
            //移除
            performRemove();
        } else {
            //还原
            performRestore();
        }
    }

    private void performRestore() {
        mValueAnimator.setIntValues(mDistanceX, 0);
        mValueAnimator.setDuration(mResetTime);
        mValueAnimator.start();
        isReseting = true;
    }

    private void performRemove() {
        int endpoint = (int) (mDistanceX > 0 ? getWidth() - getTranslationX() : getTranslationX() - getWidth());
        mValueAnimator.setIntValues(mDistanceX, endpoint);
        mValueAnimator.setDuration(mDismissTime);
        mValueAnimator.start();
        isRemoving = true;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSwipeListener != null) {
                    mSwipeListener.onSwipeDismiss(SwipeItemLayout.this);
                }
            }
        }, mDismissTime - 100);
    }

    public void setListener(ISwipeListener listener) {
        mSwipeListener = listener;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        isSliding = true;
        mDistanceX = (int) animation.getAnimatedValue();
        postInvalidate();
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        isDragging = false;
        isSliding = false;
        isReseting = false;
        isRemoving = false;
        mDistanceX = 0;
        postInvalidate();
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    public static interface ISwipeListener {
        void onSwipeDismiss(SwipeItemLayout rootView);
    }
}
