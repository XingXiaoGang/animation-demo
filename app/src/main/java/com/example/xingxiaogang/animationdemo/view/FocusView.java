package com.example.xingxiaogang.animationdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.OverScroller;

/**
 * Created by xingxiaogang on 2017/1/24.
 */

public class FocusView extends FrameLayout {

    private final static boolean DEBUG = true;
    private final static String TAG = "FocusView";

    private Rect mVisibleRect = new Rect();
    private Paint mFocusPaint = new Paint();
    private OverScroller mScroller;
    private int mCoverColor = Color.GRAY;
    private PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);


    public FocusView(Context context) {
        super(context);
        init(context, null);
    }

    public FocusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FocusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            //todo 取颜色
        }
        mFocusPaint.setColor(Color.parseColor("#44444444"));
        mCoverColor = Color.parseColor("#22000000");
        mFocusPaint.setStyle(Paint.Style.FILL);
        mFocusPaint.setStrokeWidth(20);
        mScroller = new OverScroller(context, new OvershootInterpolator(1f));
        setWillNotDraw(false);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (getMeasuredHeight() > 0) {
            getGlobalVisibleRect(mVisibleRect);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mScroller.isFinished()) {
                        mScroller.startScroll(0, 0, getMeasuredHeight(), 0, 500);
                        postInvalidate();
                    }
                }
            }, 500);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            invalidate();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        int r = getHeight() - mScroller.getCurrX();
        if (r > 0) {
            int endx = getWidth() / 2;
            int endy = getHeight();

            int count = canvas.saveLayerAlpha(getLeft(), getTop(), getRight(), getBottom(), 255, Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);
            mFocusPaint.setXfermode(null);
            canvas.drawColor(mCoverColor);
            mFocusPaint.setXfermode(porterDuffXfermode);
            canvas.drawCircle(endx, endy, r, mFocusPaint);
            canvas.restoreToCount(count);
        }
    }
}
