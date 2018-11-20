package com.example.xingxiaogang.animationdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.example.xingxiaogang.animationdemo.R;


/**
 * Created by xingxiaogang on 2016/11/14.
 * 动态可配置的圆角
 */
public class CornerClipFrameLayout extends FrameLayout {

    private Path mPath = new Path();

    private RectF mRoundRect = new RectF();
    private float mLeftTopRadius;
    private float mLeftBottomRadius;
    private float mRightTopRadius;
    private float mRightBottomRadius;

    private final PaintFlagsDrawFilter mPaintFlags = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    private Paint paint;

    public CornerClipFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CornerClipFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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

        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mRoundRect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        mPath.reset();
        mPath.addRoundRect(mRoundRect, mLeftTopRadius, mRightTopRadius, Path.Direction.CW);
        mPath.close();
    }

    @Override
    public void draw(Canvas canvas) {
        //DIFFERENCE是第一次不同于第二次的部分显示出来
        //REPLACE是显示第二次的
        //REVERSE_DIFFERENCE 是第二次不同于第一次的部分显示
        //INTERSECT交集显示
        //UNION全部显示
        //XOR补集 就是全集的减去交集生育部分显示
        //默认是  INTERSECT
        try {
            //抗锯齿
            canvas.setDrawFilter(mPaintFlags);
            canvas.clipPath(mPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.draw(canvas);
    }
}
