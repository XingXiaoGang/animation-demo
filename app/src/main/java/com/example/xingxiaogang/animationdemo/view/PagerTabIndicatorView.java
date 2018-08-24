package com.example.xingxiaogang.animationdemo.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class PagerTabIndicatorView extends LinearLayout {


    private Scroller mScroller;
    private int mDownX;
    private int mDownY;
    private int mDownScrollX;
    private int mTouchSlop;
    private boolean isSliding;


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
        mScroller = new Scroller(context, new OvershootInterpolator());
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
        selectItem(0, true);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            invalidate();
        }
    }

    private void selectItem(int viewIndex, boolean anim) {
        if (true) {
            Log.d("GANG-", "selectItem: 选中item :" + viewIndex + "  anim=" + anim);
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
        Log.d("GANG-", "selectItem: scrollX=" + getScrollX() + " distance=" + distance);

        if (distance != 0) {
            if (anim) {
                mScroller.startScroll(getScrollX(), 0, -distance, 0, 400);
                invalidate();
            } else {
                scrollBy(-distance, 0);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int active = MotionEventCompat.getActionIndex(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
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
                    scrollTo((int) (mDownX + mDownScrollX - event.getX(active)), 0);
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                isSliding = false;
                ensureChildCenter(true);
                break;
            }
        }
        return super.onTouchEvent(event);
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
                selectItem(i, anim);
                break;
            }
        }
    }


}
