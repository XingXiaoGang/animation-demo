package com.example.xingxiaogang.animationdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import com.example.xingxiaogang.animationdemo.R;


/**
 * Created by xingxiaogang on 2016/11/14.
 * 动态可配置的圆角
 * 时间问题 直接去掉配置逻辑，直接右上角圆角
 */
public class RightCornerImageView extends SizeBaseImageView {

    private Path mRightTopCorner;
    private RectF mRightTopArc;
    private int mAngleWidth;
    private int mRadius;

    public RightCornerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public RightCornerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attributeSet) {
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.SizeBaseImageView, 0, 0);

        Drawable drawable = a.getDrawable(R.styleable.SizeBaseImageView_src);
        mAngleWidth = (int) a.getDimension(R.styleable.SizeBaseImageView_angleWidth, 20);
        mRadius = (int) a.getDimension(R.styleable.SizeBaseImageView_radius, 10);
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            setImageBitMap(bitmap);
        }

        if (DEBUG) {
            Log.d(TAG, "SizeBaseImageView.initView: mAngleWidth:" + mAngleWidth + " mRadius:" + mRadius);
        }
        a.recycle();

        mRightTopCorner = new Path();
        mRightTopArc = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //计算弧度
        final Rect drawRect = getDrawRect();
        if (drawRect != null) {
            mRightTopArc.set(drawRect.right - mRadius * 2, drawRect.top, drawRect.right, drawRect.top + mRadius * 2);
        }

        //有些手机绘制不正常 (切了半边) 再刷新一下就好了
        postInvalidateDelayed(100);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
