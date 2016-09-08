package com.example.xingxiaogang.animationdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import com.example.xingxiaogang.animationdemo.R;

/**
 * Created by xingxiaogang on 2016/8/5.
 * 气泡样式的imageview
 */
public class BubbleImageView extends SizeBaseImageView {

    private Path mRightTopCorner;
    private Path mLeftShapePath;
    private RectF mRightTopArc;
    private int mAngleWidth;
    private int mRadius;

    public BubbleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public BubbleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        mLeftShapePath = new Path();
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final Rect displayRect = getDrawRect();

        //1.切掉左边的
        final Path left = mLeftShapePath;
        left.reset();
        left.moveTo(displayRect.left, displayRect.top);
        left.lineTo(displayRect.left + mAngleWidth, (float) (displayRect.top + Math.sqrt(mAngleWidth * mAngleWidth * 2)));
        left.lineTo(displayRect.left + mAngleWidth, displayRect.bottom);
        left.lineTo(displayRect.left, displayRect.bottom);
        left.lineTo(displayRect.left, displayRect.top);
        left.close();
        try {
            canvas.clipPath(left, Region.Op.DIFFERENCE);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "onDraw: clipPath error :", e);
            }
        }

        //2.切右上角的圆弧
        final Path path = mRightTopCorner;
        path.reset();
        path.moveTo(displayRect.right - mRadius, displayRect.top);
        //右上圆角
        path.arcTo(mRightTopArc, 270, 90);
        path.lineTo(displayRect.right, displayRect.top + mRadius);
        path.lineTo(displayRect.right, displayRect.top);
        path.close();
        if (DEBUG) {
            Log.d(TAG, "onDraw: ========displayRect:" + displayRect + " path:" + mRightTopArc);
        }
        try {
            canvas.clipPath(path, Region.Op.DIFFERENCE);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "onDraw: clipPath error :", e);
            }
        }

        super.onDraw(canvas);

    }
}
