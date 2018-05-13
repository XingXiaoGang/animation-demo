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

    //多长时间后，叫
    private long mWaitForBarkTime;
    private static long mBarTime = 600;
    private static long mBarkDelayTime = 1000;


    private static long mRunDuration = 1000;
    //开始跑的时间
    private long mStartRunTime;
    private int mRunX;
    private int mRunY;

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

        //位移
        long runPast = System.currentTimeMillis() - mStartRunTime;
        long eachTime = mRunDuration / 4;
        if (runPast >= eachTime && runPast <= mRunDuration) {
            switch ((int) (runPast / eachTime)) {
                case 1: {
                    mRunY = -(int) ((runPast - eachTime) * 1.0f / eachTime * mHeight * 0.6f);
                    break;
                }
                case 2: {
                    mRunX = (int) ((runPast - 2 * eachTime) * 1.0f / eachTime * mWidth * 0.7f);
                    break;
                }
                case 3: {
                    mRunY = -(int) ((mRunDuration - runPast) * 1.0f / eachTime * mHeight * 0.6f);
                    break;
                }
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        float transX = 0;
        float transY = 0;

        int chickenPositionX = mBodyPositionX + mRunX;
        int chickenPositionY = mBodyPositionY + mRunY;

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
        long waitForBark = System.currentTimeMillis() - mWaitForBarkTime;
        if (waitForBark > mBarkDelayTime && waitForBark < mBarkDelayTime + mBarTime) {
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

        canvas.restore();
    }

    @Override
    public boolean isDead() {
        return false;
    }

    void toPositionAndBark(int positionX, int positionY) {
        //位移到某个位置
        mBodyPositionX = positionX - mWidth / 2;
        mBodyPositionY = positionY - mHeight / 2;
        mRunX = 0;
        mRunY = 0;
        //跑到旁边
        mStartRunTime = System.currentTimeMillis();
        //bark
        mWaitForBarkTime = System.currentTimeMillis();
    }
}
