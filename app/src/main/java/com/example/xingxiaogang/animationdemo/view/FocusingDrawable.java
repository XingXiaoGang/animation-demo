package com.example.xingxiaogang.animationdemo.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;

import com.example.xingxiaogang.animationdemo.SizeUtils;


/**
 * 聚焦显示的Drawable
 * Created by wangwei on 2016/8/4.
 */
public abstract class FocusingDrawable extends ShapeDrawable {

    private final Paint mPaint;

    public static FocusingDrawable oval() {
        return new OvalFocusingDrawable();
    }

    public static FocusingDrawable corneredRect() {
        return new CorneredRectFocusingDrawable(SizeUtils.dp2px(8));
    }

    public static FocusingDrawable corneredRect(float radius) {
        return new CorneredRectFocusingDrawable(radius);
    }

    public static FocusingDrawable semiRoundRect() {
        return new SemiRoundRectFocusingDrawable();
    }

    public FocusingDrawable() {
        mPaint = new Paint();
        mPaint.setColor(0x19ffffff);
        getPaint().setColor(0x99000000);
    }

    private Path mPath = new Path();

    private PointF mCenterPoint = new PointF();
    private PointF mSize = new PointF();

    private float mRadius = 40;
    private RectF mRectF = new RectF();

    @Override
    protected void onBoundsChange(Rect bounds) {

        mPath.reset();
        mPath.moveTo(bounds.left, bounds.top);
        mPath.lineTo(bounds.right, bounds.top);
        mPath.lineTo(bounds.right, bounds.bottom);
        mPath.lineTo(bounds.left, bounds.bottom);
        mPath.close();

        applyHollowShape(mPath);

        mPath.setFillType(Path.FillType.EVEN_ODD);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(mPath, getPaint());
        if (mPaint != null && mCenterPoint != null && mRadius > 0) {
            canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mRadius, mPaint);
        }
    }

    FocusingDrawable setCenterPoint(float x, float y) {
        mCenterPoint.set(x, y);
        onBoundsChange(getBounds());

        return this;
    }

    public void setRadius(float radius) {
        mRadius = radius;
        onBoundsChange(getBounds());

    }

    public FocusingDrawable setSize(float w, float h) {
        mSize.set(w, h);
        onBoundsChange(getBounds());

        return this;
    }

    private void applyHollowShape(Path mPath) {

        final float cx = mCenterPoint.x;
        final float cy = mCenterPoint.y;
        final float radius = mRadius;
        final float w = mSize.x;
        final float h = mSize.y;

        mRectF.set(cx - w / 2, cy - h / 2, cx + w / 2, cy + h / 2);
        mPath.addRoundRect(mRectF, radius, radius, Path.Direction.CCW);
    }

    private static class OvalFocusingDrawable extends FocusingDrawable {

        @Override
        public FocusingDrawable setSize(float w, float h) {
            final float size = Math.max(w, h);
            super.setSize(size, size);
            setRadius(size / 2);
            return this;
        }

    }

    private static class CorneredRectFocusingDrawable extends FocusingDrawable {

        CorneredRectFocusingDrawable(float radius) {
            setRadius(radius);
        }
    }

    private static class SemiRoundRectFocusingDrawable extends FocusingDrawable {

        @Override
        public FocusingDrawable setSize(float w, float h) {
            super.setSize(w, h);
            setRadius(Math.min(w, h) / 2);

            return this;
        }
    }
}
