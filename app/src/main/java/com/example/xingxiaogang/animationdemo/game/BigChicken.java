package com.example.xingxiaogang.animationdemo.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.example.xingxiaogang.animationdemo.R;
import com.example.xingxiaogang.animationdemo.SizeUtils;
import com.example.xingxiaogang.animationdemo.view.snow.ISprite;

/**
 * Created by xingxiaogang on 2018/5/11.
 */

public class BigChicken implements ISprite {

    private final int mWidth;
    private final int mHeight;

    private int mPositionX;
    private int mPositionY;

    private Drawable mBody;
    private Drawable mMouthNormal;
    private Drawable mMouthOpen;
    private Drawable mLeftLeg;
    private Drawable mRightLeg;
    private Drawable mEye;

    public BigChicken(Context context) {
        mWidth = SizeUtils.dp2px(120);
        mHeight = SizeUtils.dp2px(100);

        mBody = ContextCompat.getDrawable(context, R.mipmap.main);
        mMouthNormal = ContextCompat.getDrawable(context, R.mipmap.mouth);
        mMouthOpen = ContextCompat.getDrawable(context, R.mipmap.mouth_open);
        mLeftLeg = ContextCompat.getDrawable(context, R.mipmap.left);
        mRightLeg = ContextCompat.getDrawable(context, R.mipmap.right);
        mEye = ContextCompat.getDrawable(context, R.mipmap.eye);

        mBody.setBounds(new Rect(0, 0, mWidth, mHeight));
        mLeftLeg.setBounds(new Rect(0, 0, (int) (mWidth * 1.0f / 4), (int) (mWidth * 1.0f / 4)));
        mRightLeg.setBounds(new Rect(0, 0, (int) (mWidth * 1.0f / 4), (int) (mWidth * 1.0f / 4)));
        mEye.setBounds(new Rect(0, 0, (int) (mWidth * 1.0f / 5), (int) (mWidth * 1.0f / 5)));
        mMouthNormal.setBounds(new Rect(0, 0, (int) (mWidth * 1.0f / 4), (int) (mWidth * 1.0f / 4)));
        mMouthOpen.setBounds(new Rect(0, 0, (int) (mWidth * 1.0f / 4), (int) (mWidth * 1.0f / 4)));

        mPositionX = 300;
        mPositionY = 600;
    }

    @Override
    public void logic() {

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.translate(mPositionX, mPositionY);
        mBody.draw(canvas);
        canvas.translate(mPositionX / 3, mPositionY / 2);
        mLeftLeg.draw(canvas);
        canvas.translate(mPositionX * 1.0f / 2, 0);
        mRightLeg.draw(canvas);
        mEye.draw(canvas);
        canvas.restore();
    }

    @Override
    public boolean isDead() {
        return false;
    }
}
