package com.example.xingxiaogang.animationdemo.view.snow;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by xingxiaogang on 2017/11/28.
 */

public class Snow implements ISprite {

    private Rect rect = null;
    private Bitmap src = null;

    private float mX;
    private float mY;
    private static Random mRandom = new Random();
    private float mXSpeed;
    private float mYSpeed;

    public Snow(Rect rect, Bitmap src, int speedX, int speedY, int maxDistance) {
        this.rect = rect;
        this.src = src;
        mX = mRandom.nextInt(rect.width());
        mY -= mRandom.nextInt(maxDistance);
        mXSpeed = speedX + mRandom.nextFloat() * (mRandom.nextBoolean() ? -1 : 1);
        mYSpeed = speedY + mRandom.nextInt(speedY / 3);
    }

    @Override
    public void logic() {
        mX += mXSpeed;
        mY += mYSpeed;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(src, mX, mY, null);
    }

    @Override
    public boolean isDead() {
        return mY > rect.height() || mX < 0 || mX > rect.width();
    }
}
