package com.example.xingxiaogang.animationdemo.particle;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by xingxiaogang on 2017/6/19.
 */

public abstract class Particle {
    float cx;
    float cy;
    int color;


    /**
     * @param color 颜色
     * @param x
     * @param y
     */
    public Particle(int color, float x, float y) {
        this.color = color;
        cx = x;
        cy = y;
    }

    protected abstract void draw(Canvas canvas, Paint paint);

    protected abstract void logic(float factor);

    public void advance(Canvas canvas, Paint paint, float factor) {
        logic(factor);
        draw(canvas, paint);
    }
}
