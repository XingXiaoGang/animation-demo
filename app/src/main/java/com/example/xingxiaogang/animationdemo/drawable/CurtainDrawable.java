package com.example.xingxiaogang.animationdemo.drawable;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.NonNull;

/**
 * Created by xingxiaogang on 2017/4/18.
 */

public class CurtainDrawable extends ShapeDrawable {

    private Path mPath = new Path();

    public CurtainDrawable() {
        getPaint().setColor(0x99000000);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

        mPath.reset();
        mPath.moveTo(bounds.left, bounds.top);
        mPath.lineTo(bounds.right, bounds.top);
        mPath.lineTo(bounds.right, bounds.bottom);
        mPath.lineTo(bounds.left, bounds.bottom);
        mPath.close();

        //减去高亮部分

        mPath.setFillType(Path.FillType.EVEN_ODD);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawPath(mPath, getPaint());
    }
}
