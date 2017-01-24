package com.apusapps.tools.highlight.shape;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.apusapps.tools.highlight.HighLight;


/**
 * Created by caizepeng on 16/8/20.
 */
public class RectLightShape implements HighLight.LightShape {
    @Override
    public void shape(Canvas canvas, Paint p, HighLight.ViewPosInfo viewPosInfo) {
        p.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.SOLID));
        canvas.drawRoundRect(viewPosInfo.rectF, 6, 6, p);
    }
}
