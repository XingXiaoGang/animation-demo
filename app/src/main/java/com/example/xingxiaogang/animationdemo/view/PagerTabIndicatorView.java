package com.example.xingxiaogang.animationdemo.view;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.example.xingxiaogang.animationdemo.BuildConfig;

import java.util.ArrayList;
import java.util.List;
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
    private int mLastIndex = -1; //默认选中的位置

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
        setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        setClickable(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("TEST_", "onConfigurationChanged: onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!isChangedByUser) {
            selectItem(mLastIndex, false, false, true);
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

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
            Log.i("TEST_", "onConfigurationChanged: PagerTabIndicatorView");
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

    public void selectItem(final int viewIndex, final boolean anim, final boolean vibrate, final boolean notify) {
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
            if (getMeasuredWidth() > 0) {
                notifySelect(anim, viewIndex, vibrate);
            } else {
                getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                        notifySelect(anim, viewIndex, vibrate);
                    }
                });
            }
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
                    selectLive(position);
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

    final List<Pair<Integer, Integer>> childCenterAera = new ArrayList<>();

    private void selectLive(int position) {
        Log.d("GANG_", "onTouchEvent:  position=" + position);
        int childWidth = getChildAt(0).getMeasuredWidth();
        boolean evenChild = getChildCount() % 2 == 0;
        int centerChildIndex = getChildCount() / 2;
        //找出每个child的中心(child要等宽)
        if (childCenterAera.isEmpty()) {
            int start = evenChild ? childWidth / 2 : 0;
            for (int i = 0; i < centerChildIndex; i++) {
                int center = -(centerChildIndex - i) * childWidth + start;
                childCenterAera.add(new Pair<Integer, Integer>(center, i));
            }
            for (int i = centerChildIndex; i < getChildCount(); i++) {
                int center = (i - centerChildIndex) * childWidth + start;
                childCenterAera.add(new Pair<Integer, Integer>(center, i));
            }

            StringBuilder sb = new StringBuilder();
            for (Pair<Integer, Integer> pair : childCenterAera) {
                sb.append("index: ").append(pair.second).append(",center: ").append(pair.first).append(" | ");
            }
            Log.i("GANG_", "childWidth " + childWidth + " ,centerChildIndex " + centerChildIndex + " , positions " + sb);
        }

        int left = childCenterAera.get(0).first;
        if (position < left) {
            position = left;
        }
        int right = childCenterAera.get(childCenterAera.size() - 1).first;
        if (position > right) {
            position = right;
        }

        //遍历
        int hitSize = Math.max(childWidth / 6, 2);
        for (Pair<Integer, Integer> pair : childCenterAera) {
            if (position >= pair.first - hitSize && position <= pair.first + hitSize) {
                selectItem(pair.second, true, true);
                break;
            }
        }
        scrollTo(position, 0);
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