package com.example.xingxiaogang.animationdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.example.xingxiaogang.animationdemo.R;


/**
 * Created by xingxiaogang on 2016/11/14.
 * 动态可配置的圆角
 */
public class CornerDrawFrameLayout extends FrameLayout {

    private Path mVisiblePath = new Path();

    private RectF mRoundRect = new RectF();
    private float mLeftTopRadius;
    private float mLeftBottomRadius;
    private float mRightTopRadius;
    private float mRightBottomRadius;

    private final Paint mClipPaint = new Paint();
    private final Paint mVisibleAreaPaint = new Paint();

    public CornerDrawFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CornerDrawFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    private void initView(Context context, AttributeSet attributeSet) {
        setWillNotDraw(false);
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.CornerFrameLayout, 0, 0);
        float radius = (int) a.getDimension(R.styleable.CornerFrameLayout_radius, 0);
        if (radius > 0) {
            mLeftTopRadius = radius;
            mLeftBottomRadius = radius;
            mRightTopRadius = radius;
            mRightBottomRadius = radius;
        }

        mClipPaint.setAntiAlias(true);
        mClipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        mVisibleAreaPaint.setAntiAlias(true);
        mVisibleAreaPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mRoundRect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        mVisiblePath.reset();
        mVisiblePath.addRoundRect(mRoundRect, mLeftTopRadius, mRightTopRadius, Path.Direction.CW);
        mVisiblePath.close();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.saveLayer(mRoundRect, mVisibleAreaPaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawPath(mVisiblePath, mVisibleAreaPaint);
        canvas.saveLayer(mRoundRect, mClipPaint, Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);
        canvas.restore();
    }
}
