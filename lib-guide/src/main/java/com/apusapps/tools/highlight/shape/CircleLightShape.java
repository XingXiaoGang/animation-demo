package com.apusapps.tools.highlight.shape;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.apusapps.tools.highlight.HighLight;


/**
 * Created by caizepeng on 16/8/20.
 */
public class CircleLightShape implements HighLight.LightShape {

    private float mScale = 1;

    public CircleLightShape(float scale) {
        this.mScale = scale;
    }

    @Override
    public void shape(Canvas canvas, Paint p, HighLight.ViewPosInfo viewPosInfo) {
        p.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.SOLID));
        RectF rectF = viewPosInfo.rectF;
        canvas.drawCircle(rectF.left + (rectF.width() / 2), rectF.top + (rectF.height() / 2), Math.max(rectF.width(), rectF.height()) / 2 * mScale, p);
    }
}
