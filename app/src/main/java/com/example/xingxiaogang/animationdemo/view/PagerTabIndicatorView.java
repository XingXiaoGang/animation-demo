package com.example.xingxiaogang.animationdemo.view;

import android.content.Context;
import android.os.Vibrator;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.example.xingxiaogang.animationdemo.BuildConfig;

import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

public class PagerTabIndicatorView extends LinearLayout {
    private Scroller mScroller;
    private int mDownX;
    private int mDownY;
    private int mDownScrollX;
    private int mTouchSlop;
    private boolean isSliding;
    private onPagerTabSelectListener onPagerTabSelectListener;
    private Subscription mSubscription;

    private boolean isChangedByUser = false;
    private int mLastIndex = -1;

    public PagerTabIndicatorView(Context context) {
        super(context);
        initView(context, null);
    }

    public PagerTabIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public PagerTabIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet atts) {
        mScroller = new Scroller(context, new LinearInterpolator());
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setClickable(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!isChangedByUser) {
            selectItem(0, false, false, true);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            invalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    public void selectItem(int viewIndex, boolean vibrate, boolean notify) {
        if (BuildConfig.DEBUG) {
            Log.d("GANG", "selectItem: 选中item :" + viewIndex + "notify=" + notify + " final notify=" + (notify && (mLastIndex != viewIndex)));
        }
        if (viewIndex == mLastIndex) {
            return;
        }
        if (vibrate) {
            viberate();
        }
        //更新状态
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view != null) {
                view.setActivated(i == viewIndex);
                view.setSelected(i == viewIndex);
            }
        }

        if (notify && mLastIndex != viewIndex) {
            notifySelect(false, viewIndex, false);
        }
        mLastIndex = viewIndex;
    }

    public void selectItem(int viewIndex, boolean anim, boolean vibrate, boolean notify) {
        if (BuildConfig.DEBUG) {
            Log.d("GANG", "selectItem: 选中item :" + viewIndex + "  anim=" + anim + "notify=" + notify + " final notify=" + (notify && (mLastIndex != viewIndex)));
        }
        if (getChildCount() < viewIndex) {
            return;
        }
        View child = getChildAt(viewIndex);
        if (child == null) {
            return;
        }
        int center = getMeasuredWidth() / 2;
        int distance = center + child.getMeasuredWidth() / 2 - (child.getLeft() + child.getMeasuredWidth()) + getScrollX();

        if (distance != 0) {
            if (anim) {
                mScroller.startScroll(getScrollX(), 0, -distance, 0, 100);
                invalidate();
            } else {
                scrollBy(-distance, 0);
            }
        }

        //更新状态
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view != null) {
                view.setActivated(view == child);
                view.setSelected(view == child);
            }
        }

        if (notify && mLastIndex != viewIndex) {
            notifySelect(anim, viewIndex, vibrate);
        }
        mLastIndex = viewIndex;
    }

    private void notifySelect(boolean anim, final int tabIndex, final boolean vibrate) {
        if (!anim) {
            if (onPagerTabSelectListener != null) {
                onPagerTabSelectListener.onPageSelect(tabIndex);
            }
        } else {
            if (vibrate) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viberate();
                    }
                }, 100);
            }
            mSubscription = AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                @Override
                public void call() {
                    if (onPagerTabSelectListener != null) {
                        onPagerTabSelectListener.onPageSelect(tabIndex);
                    }
                }
            }, 300, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int active = MotionEventCompat.getActionIndex(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                isChangedByUser = true;
                mDownX = (int) event.getX(active);
                mDownY = (int) event.getY(active);
                mDownScrollX = getScrollX();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int disX = (int) event.getX(active) - mDownX;
                int disY = (int) (event.getY(active) - mDownY);
                if (Math.abs(disX) > Math.abs(disY) && Math.abs(disX) > mTouchSlop) {
                    isSliding = true;
                }
                if (isSliding) {
                    int position = (int) (mDownX + mDownScrollX - event.getX(active));
                    if (position > getChildAt(0).getMeasuredWidth() / 2) {
                        position = getChildAt(0).getMeasuredWidth() / 2;
                        //滑动中也要切换
                        selectItem(1, true, true);
                    } else if (position < -getChildAt(0).getMeasuredWidth() / 2) {
                        position = -getChildAt(0).getMeasuredWidth() / 2;
                        selectItem(0, true, true);
                    }
                    scrollTo(position, 0);
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                if (isSliding) {
                    isSliding = false;
                    ensureChildCenter(true);
                } else {
                    performChildClick((int) event.getX(active));
                }
                break;
            }
        }
        return super.onTouchEvent(event);
    }

    private void performChildClick(int x) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.getLeft() - getScrollX() < x && view.getRight() - getScrollX() > x) {
                if (onPagerTabSelectListener != null && i != mLastIndex) {
                    viberate();
                    selectItem(i, true, false, true);
                    break;
                }
            }
        }
    }

    private void viberate() {
        Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(40);
        }
    }

    //保证有一个child在正中(选中)
    private void ensureChildCenter(boolean anim) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child == null) {
                continue;
            }
            int parentCenter = getMeasuredWidth() / 2;
            //找到正在下方的view
            int childRight = child.getLeft() + child.getMeasuredWidth() - getScrollX();
            if (childRight >= parentCenter && child.getLeft() - getScrollX() <= parentCenter) {
                selectItem(i, anim, true, true);
                break;
            } else if (i == 0 && child.getLeft() - getScrollX() > parentCenter) {
                selectItem(i, anim, true, true);
                break;
            } else if (i == count - 1 && childRight < parentCenter) {
                selectItem(i, anim, true, true);
                break;
            }
        }
    }

    public void setOnPagerTabSelectListener(PagerTabIndicatorView.onPagerTabSelectListener onPagerTabSelectListener) {
        this.onPagerTabSelectListener = onPagerTabSelectListener;
    }

    public static interface onPagerTabSelectListener {
        void onPageSelect(int tabIndex);
    }
}