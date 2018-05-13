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

public class SmallChicken implements ISprite {

    private final int mWidth;
    private final int mHeight;
    private final int mScreenWidth;

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

    private Drawable mEggNormal;
    private Drawable mEggBroke;

    //动作 脚的上下动作时间周期是1秒
    private static final int mLegDuration = 1000;
    private int mLeftLegTransY;
    private int mRightLegTransY;
    private int mLegMaxTranslateY;

    //时间线
    private static long mShakeTime = 3000;
    private static long mBrokeTime = mShakeTime + 1000;
    private static long mBirthTime = mBrokeTime + 600;
    private static long mRunTime = mBirthTime + 100;
    private static long mBarkTime = mRunTime + 100;

    private static long mBarkDuration = 600;

    private int mRunX;
    private long mStartTime;

    SmallChicken(Context context, int positionX, int positionY) {
        mStartTime = System.currentTimeMillis();
        mWidth = SizeUtils.dp2px(80);
        mHeight = SizeUtils.dp2px(60);
        mScreenWidth = SizeUtils.getScreenWidth();

        mEggNormal = ContextCompat.getDrawable(context, R.mipmap.egg);
        mEggBroke = ContextCompat.getDrawable(context, R.mipmap.egg_broke);
        mEggNormal.setBounds(new Rect(0, 0, (int) (mWidth * 0.6f), (int) (mWidth * 0.8f)));
        mEggBroke.setBounds(new Rect(0, 0, (int) (mWidth * 0.6f), (int) (mWidth * 0.8f)));

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

        mBodyPositionX = positionX;

        mBodyPositionY = positionY;
    }

    @Override
    public void logic() {
        //脚动画
        long time = System.currentTimeMillis() % mLegDuration;
        long half = mLegDuration / 2;
        if (time < half) {
            mLeftLegTransY = (int) -(time * 1.0f / half * mLegMaxTranslateY);
            mRightLegTransY = -mLegMaxTranslateY - mLeftLegTransY;
        } else {
            mLeftLegTransY = (int) -((mLegDuration - time) * 1.0f / half * mLegMaxTranslateY);
            mRightLegTransY = -mLegMaxTranslateY - mLeftLegTransY;
        }
        //跑动画
        long timeLine = System.currentTimeMillis() - mStartTime;
        if (timeLine > mRunTime) {
            mRunX = -(int) (((timeLine - mRunTime) * 1.0f / 6000) * mScreenWidth);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        float transX = 0;
        float transY = 0;

        long timeLine = System.currentTimeMillis() - mStartTime;
        int chickenPositionX = mBodyPositionX + mRunX;
        int chickenPositionY = mBodyPositionY;

        if (timeLine < mBirthTime) {
            //未孵化
            canvas.translate(chickenPositionX, chickenPositionY);
            if (timeLine < mBrokeTime) {
                mEggNormal.draw(canvas);
            } else {
                mEggBroke.draw(canvas);
            }
            canvas.translate(-chickenPositionX, -chickenPositionX);
        } else {
            //脚
            transX = mLeftLegPositinX * mWidth + chickenPositionX;
            transY = mLeftLegPositinY * mHeight + chickenPositionY + mLeftLegTransY;
            canvas.translate(transX, transY);
            mLeftLeg.draw(canvas);
            canvas.translate(-transX, -transY);

            transX = mRightLegPositinX * mWidth + chickenPositionX;
            transY = mRightLegPositinY * mHeight + chickenPositionY + mRightLegTransY;
            canvas.translate(transX, transY);
            mRightLeg.draw(canvas);
            canvas.translate(-transX, -transY);

            //嘴
            transX = mMouthPositionX * mWidth + chickenPositionX;
            transY = mMouthPositionY * mHeight + chickenPositionY;
            canvas.translate(transX, transY);
            if (timeLine > mBarkTime && timeLine < mBarkTime + mBarkDuration) {
                mMouthOpen.draw(canvas);
            } else {
                mMouthNormal.draw(canvas);
            }
            canvas.translate(-transX, -transY);

            //身体
            canvas.translate(chickenPositionX, chickenPositionY);
            mBody.draw(canvas);
            canvas.translate(-chickenPositionX, -chickenPositionY);

            //眼
            transX = mEyePositionX * mWidth + chickenPositionX;
            transY = mEyePositionY * mHeight + chickenPositionY;
            canvas.translate(transX, transY);
            mEye.draw(canvas);
            canvas.translate(-transX, -transY);
        }
        canvas.restore();
    }

    @Override
    public boolean isDead() {
        return mBodyPositionX > mScreenWidth || mBodyPositionX < -mWidth;
    }
}
