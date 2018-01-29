package com.example.xingxiaogang.animationdemo.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.example.xingxiaogang.animationdemo.RecylerActivity;
import com.example.xingxiaogang.animationdemo.SizeUtils;

/**
 * Created by xingxiaogang on 2016/12/8.
 * 使用drawable直接画 不要用bitmap
 */

public class AvatorDrawable extends Drawable {

    private String mAvatorName = "";
    private Rect mDrawaRect = new Rect();
    private Paint mBgPaint;
    private Paint mTextPaint;

    public AvatorDrawable(String title) {
        this.mAvatorName = title;
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setColor(Color.parseColor("#B477AF"));
        if (title != null && title.length() > 1) {
            mAvatorName = title.substring(0, 1);
        }

        mTextPaint = new Paint();
        mTextPaint.setTextSize(SizeUtils.dp2px(RecylerActivity.mInstance, 20));
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setDither(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setAntiAlias(true);
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null) {
            canvas.drawColor(Color.TRANSPARENT);
            canvas.drawCircle(mDrawaRect.centerX(), mDrawaRect.centerY(), mDrawaRect.width() / 9 * 4, mBgPaint);
            int height = (int) (mTextPaint.ascent() + mTextPaint.descent());
            canvas.drawText(mAvatorName, mDrawaRect.centerX(), mDrawaRect.centerY() - height / 2, mTextPaint);
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mDrawaRect.set(bounds);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
