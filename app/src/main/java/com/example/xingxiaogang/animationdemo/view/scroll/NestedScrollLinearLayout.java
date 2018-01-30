package com.example.xingxiaogang.animationdemo.view.scroll;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.example.xingxiaogang.animationdemo.R;

/**
 * Created by xingxiaogang on 2018/1/29.
 * 一个类似于CoordinatorLayout的layout，为了灵活实现需求
 * <p>
 * 使用方法：
 * 对于内部的viewPager 打上 id=R.id.nest_scroll_scroller
 * 对于内部的header 打上 id=R.id.nest_scroll_header
 */
@SuppressLint("LongLogTag")
public class NestedScrollLinearLayout extends LinearLayout implements NestedScrollingParent {

    private static final boolean DEBUG = true;
    private static final String TAG = "NestedScrollLinearLayout";

    private View mTop;
    private ViewPager mViewPager;
    private int mTopViewHeight;
    private OverScroller mScroller;
    private NestedScrollingParentHelper mParentHelper;

    //自动显示、隐藏
    boolean isMoving;
    private int mDownY;
    private boolean isSlideUpOrDown;

    //是否默认隐藏head
    private boolean isDefaultHideHead = true;
    private boolean isFirstLayout = true;

    public NestedScrollLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NestedScrollLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        mScroller = new OverScroller(context);
        mParentHelper = new NestedScrollingParentHelper(this);
    }

    public void setDefaultHideHead(boolean defaultHideHead) {
        isDefaultHideHead = defaultHideHead;
    }

    public void setHeadViewVisibility(int visibility) {
        if (DEBUG) {
            Log.d(TAG, "setHeadViewVisibility: ");
        }
        mTop.setVisibility(visibility);
        if (visibility == View.GONE) {
            scrollTo(0, 0);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTop = findViewById(R.id.nest_scroll_header);
        mViewPager = (ViewPager) findViewById(R.id.nest_scroll_scroller);
        //FIXME 要与程序里面的id一致
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (DEBUG) {
            Log.i(TAG, "onMeasure: height= " + getMeasuredHeight() + " pagerHeight=" + mViewPager.getMeasuredHeight() + " topHeight=" + mTop.getMeasuredHeight());
        }
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        params.height = getMeasuredHeight();
        mViewPager.setLayoutParams(params);
        //todo 问题 布局变化时，measure异常
        mTopViewHeight = mTop.getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (DEBUG) {
            Log.i(TAG, "onLayout: ");
        }
        if (isDefaultHideHead && isFirstLayout && mTop.getVisibility() == VISIBLE) {
            scrollTo(0, mTopViewHeight);
            isFirstLayout = false;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (DEBUG) {
            Log.i(TAG, "onSizeChanged: ");
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void computeScroll() {
        Log.i(TAG, "computeScroll: " + mScroller.getCurrY());
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if (DEBUG) {
            Log.i(TAG, "onStartNestedScroll nestedScrollAxes=" + nestedScrollAxes);
        }
        //true if this ViewParent accepts the nested scroll operation
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        if (DEBUG) {
            Log.i(TAG, "onNestedScrollAccepted");
        }
        mParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        if (DEBUG) {
            Log.i(TAG, "onStopNestedScroll getScrolly=" + getScrollY());
        }
        mParentHelper.onStopNestedScroll(target);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (DEBUG) {
            Log.v(TAG, "onNestedPreScroll ");
        }
        if (mTop.getVisibility() != VISIBLE) {
            return;
        }
        boolean hiddenTop = dy > 0 && getScrollY() <= mTopViewHeight;
        boolean showTop = dy < 0 && getScrollY() >= 0 && !ViewCompat.canScrollVertically(target, -1);

        if (hiddenTop || showTop) {
            //防止下滑超出
            if (getScrollY() >= 0 && getScrollY() + dy < 0) {
                dy = -getScrollY();
            }
            //防止上滑超出
            if (getScrollY() <= mTopViewHeight && getScrollY() + dy > mTopViewHeight) {
                dy = mTopViewHeight - getScrollY();
            }
            //整体滚动
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mDownY = (int) event.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                isMoving = true;
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (event.getY() - mDownY > 0) {
                    isSlideUpOrDown = true;
                } else {
                    isSlideUpOrDown = false;
                }
                if (DEBUG) {
                    Log.i(TAG, "dispatchTouchEvent: ACTION_UP " + isMoving + " ,up=" + isSlideUpOrDown);
                }
                if (isMoving) {
                    smoothToPosition();
                }
                isMoving = false;
                break;
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        //不做拦截 可以传递给子View
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return mParentHelper.getNestedScrollAxes();
    }

    private void smoothToPosition() {
        if (getScrollY() > 0 && getScrollY() < mTopViewHeight) {
            if (isSlideUpOrDown) {
                if (DEBUG) {
                    Log.i(TAG, "smoothToPosition: " + getScrollY() + " to " + -getScrollY());
                }
                mScroller.startScroll(0, getScrollY(), 0, -getScrollY());
                invalidate();
            } else {
                if (DEBUG) {
                    Log.i(TAG, "smoothToPosition: " + getScrollY() + " to " + (150 - getScrollY()));
                }
                mScroller.startScroll(0, getScrollY(), 0, 150 - getScrollY());
                invalidate();
            }
        }
    }
}