package com.example.xingxiaogang.animationdemo.view.text;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class RollingTextView extends FrameLayout implements Handler.Callback {

    private TextView mTextTop;
    private TextView mBottomDown;

    private final Queue<String> mQueueDatas = new ArrayDeque<>();
    private final List<String> mArrayDatas = new ArrayList<>();
    private int mCurrentIndex;
    private static final int ID_LOOP = 1000;
    private static final int DURATION = 600;
    private boolean isLooping = false;
    private boolean autoRemove = true;
    private Handler mHandler;

    public RollingTextView(Context context) {
        super(context);
        initView(context, null);
    }

    public RollingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, null);
    }

    public RollingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, null);
    }

    private void initView(Context context, AttributeSet attrs) {
        mHandler = new Handler(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        initChildren();
        resetChildrenPosition();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case ID_LOOP: {
                String text = getNextData();
                mTextTop.setText(mBottomDown.getText());
                mBottomDown.setText(text);
                performRollAnim();
                break;
            }
        }
        return false;
    }

    public RollingTextView setAutoRemove(boolean autoRemove) {
        this.autoRemove = autoRemove;
        if (autoRemove) {
            mQueueDatas.addAll(mArrayDatas);
            mArrayDatas.clear();
        } else {
            mArrayDatas.addAll(mQueueDatas);
            mQueueDatas.clear();
        }
        return this;
    }

    public RollingTextView addData(List<String> texts) {
        if (autoRemove) {
            mQueueDatas.addAll(texts);
        } else {
            mArrayDatas.addAll(texts);
        }
        if (!isLooping) {
            resetChildrenPosition();
            loop();
            isLooping = true;
        }
        return this;
    }

    /**
     * 把textView在布局中声名是为了方便定义样式
     **/
    private void initChildren() {
        if (getChildCount() != 2) {
            throw new IllegalArgumentException("RollingTextView must contains two TextView");
        }
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (!(child instanceof TextView)) {
                throw new IllegalArgumentException("RollingTextView's child must implement of TextView");
            }
        }

        mTextTop = (TextView) getChildAt(0);
        mBottomDown = (TextView) getChildAt(1);
    }

    private void resetChildrenPosition() {
        if (mTextTop == null || mBottomDown == null) {
            return;
        }
        mTextTop.setTranslationY(0);
        mTextTop.setPivotY(mTextTop.getMeasuredHeight());
        mBottomDown.setTranslationY(mBottomDown.getMeasuredHeight());
        mBottomDown.setAlpha(0);
        mBottomDown.setPivotY(0);

        String txt = getNextData();
        mTextTop.setText(txt);
        mBottomDown.setText(txt);
    }

    private void performRollAnim() {
        {
            int height = mTextTop.getMeasuredHeight();
            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1, 0.9f, 0.7f, 0);
            PropertyValuesHolder translateY = PropertyValuesHolder.ofFloat("translationY", 0, -height);
            PropertyValuesHolder rotate = PropertyValuesHolder.ofFloat("rotationX", 0, 65);
            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mTextTop, alpha, translateY, rotate);
            animator.setDuration(DURATION);
            animator.start();
        }
        {
            int height = mBottomDown.getMeasuredHeight();
            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 0.9f, 1);
            PropertyValuesHolder translateY = PropertyValuesHolder.ofFloat("translationY", height, 0);
            PropertyValuesHolder rotate = PropertyValuesHolder.ofFloat("rotationX", -90, 0);
            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mBottomDown, alpha, translateY, rotate);
            animator.setDuration(DURATION);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    //循环
                    loop();
                }
            });
            animator.start();
        }
    }

    private void loop() {
        if (autoRemove && mQueueDatas.size() > 0 || !autoRemove && mArrayDatas.size() > 0) {
            mHandler.removeMessages(ID_LOOP);
            mHandler.sendEmptyMessageDelayed(ID_LOOP, 1000);
        } else {
            isLooping = false;
        }
    }

    private String getNextData() {
        if (autoRemove) {
            return mQueueDatas.poll();
        } else {
            String obj = mArrayDatas.size() > 0 ? mArrayDatas.get(mCurrentIndex % mArrayDatas.size()) : null;
            mCurrentIndex++;
            return obj;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
    }
}
