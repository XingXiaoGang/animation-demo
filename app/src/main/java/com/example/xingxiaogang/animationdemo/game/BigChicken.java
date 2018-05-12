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

    //左脚位置
    private final float mLeftLegPositinY = 0.78f;
    private final float mLeftLegPositinX = 0.29f;
    //右脚位置
    private final float mRightLegPositinY = 0.74f;
    private final float mRightLegPositinX = 0.51f;
    //嘴位置
    private final float mMouthPositionX = 0.03f;
    private final float mMouthPositionY = 0.271f;
    //眼位置
    private final float mEyePositionX = 0.33f;
    private final float mEyePositionY = 0.30f;


    private int mBodyPositionX;
    private int mBodyPositionY;

    private Drawable mBody;
    private Drawable mMouthNormal;
    private Drawable mMouthOpen;
    private Drawable mLeftLeg;
    private Drawable mRightLeg;
    private Drawable mEye;

    //动作 脚的上下动作时间周期是1秒
    private static final int mLegDuration = 1000;
    private int mLeftLegTransY;
    private int mRightLegTransY;
    private int mLegMaxTranslateY;

    private long mWaitForBarkTime;
    private static long mBarTime = 600;
    private static long mBarkDelayTime = 1000;

    BigChicken(Context context) {
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

        mLegMaxTranslateY = (int) (mWidth * 1.0f / 14);

        mBodyPositionX = 300;
        mBodyPositionY = 600;
    }

    @Override
    public void logic() {
        long time = System.currentTimeMillis() % mLegDuration;
        long half = mLegDuration / 2;
        if (time < half) {
            mLeftLegTransY = (int) -(time * 1.0f / half * mLegMaxTranslateY);
            mRightLegTransY = -mLegMaxTranslateY - mLeftLegTransY;
        } else {
            mLeftLegTransY = (int) -((mLegDuration - time) * 1.0f / half * mLegMaxTranslateY);
            mRightLegTransY = -mLegMaxTranslateY - mLeftLegTransY;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        float transX = 0;
        float transY = 0;
        //脚
        transX = mLeftLegPositinX * mWidth + mBodyPositionX;
        transY = mLeftLegPositinY * mHeight + mBodyPositionY + mLeftLegTransY;
        canvas.translate(transX, transY);
        mLeftLeg.draw(canvas);
        canvas.translate(-transX, -transY);

        transX = mRightLegPositinX * mWidth + mBodyPositionX;
        transY = mRightLegPositinY * mHeight + mBodyPositionY + mRightLegTransY;
        canvas.translate(transX, transY);
        mRightLeg.draw(canvas);
        canvas.translate(-transX, -transY);

        //嘴
        transX = mMouthPositionX * mWidth + mBodyPositionX;
        transY = mMouthPositionY * mHeight + mBodyPositionY;
        canvas.translate(transX, transY);
        long waitForBark = System.currentTimeMillis() - mWaitForBarkTime;
        if (waitForBark > mBarkDelayTime && waitForBark < mBarkDelayTime + mBarTime) {
            mMouthOpen.draw(canvas);
        } else {
            mMouthNormal.draw(canvas);
        }
        canvas.translate(-transX, -transY);

        //身体
        canvas.translate(mBodyPositionX, mBodyPositionY);
        mBody.draw(canvas);
        canvas.translate(-mBodyPositionX, -mBodyPositionY);

        //眼
        transX = mEyePositionX * mWidth + mBodyPositionX;
        transY = mEyePositionY * mHeight + mBodyPositionY;
        canvas.translate(transX, transY);
        mEye.draw(canvas);
        canvas.translate(-transX, -transY);

        canvas.restore();
    }

    @Override
    public boolean isDead() {
        return false;
    }

    void toPositionAndBark(int positionX, int positionY) {
        //todo 限制区域
        mBodyPositionX = positionX - mWidth / 2;
        mBodyPositionY = positionY - mHeight / 2;
        //自己刷新

        //bark
        mWaitForBarkTime = System.currentTimeMillis();
    }
}
