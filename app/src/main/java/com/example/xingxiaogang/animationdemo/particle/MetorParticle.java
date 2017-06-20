package com.example.xingxiaogang.animationdemo.particle;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by xingxiaogang on 2017/6/19.
 * 流星效果
 */

public class MetorParticle extends Particle {

    /**
     * @param color 颜色
     * @param x
     * @param y
     */
    public MetorParticle(int color, float x, float y) {
        super(color, x, y);
    }

    @Override
    protected void draw(Canvas canvas, Paint paint) {

    }

    @Override
    protected void logic(float factor) {

    }
}
