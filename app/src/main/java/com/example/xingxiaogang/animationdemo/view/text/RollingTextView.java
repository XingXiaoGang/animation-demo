package com.example.xingxiaogang.animationdemo.view.text;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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

    private final Queue<RollingTextData> mQueueDatas = new ArrayDeque<>();
    private final List<RollingTextData> mArrayDatas = new ArrayList<>();
    private int mCurrentIndex;
    private static final int ID_LOOP = 1000;
    private static final int ANIM_DURATION = 600;
    private static final int LOOP_DURATION = 1000;
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
        if (!isLooping) {
            resetChildrenPositionAndLoop();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case ID_LOOP: {
                RollingTextData text = getNextData();
                if (text != null) {
                    mTextTop.setText(mBottomDown.getText());
                    mBottomDown.setText(text.text);
                    performRollAnim(text.duration);
                } else {
                    //没有更多数据了
                    mTextTop.setText(mBottomDown.getText());
                    mBottomDown.setText("");
                    performRollAnim(-1);
                }
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

    public RollingTextView addData(List<RollingTextData> texts) {
        if (autoRemove) {
            mQueueDatas.addAll(texts);
        } else {
            mArrayDatas.addAll(texts);
        }
        if (!isLooping) {
            resetChildrenPositionAndLoop();
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

    private synchronized void resetChildrenPositionAndLoop() {
        if (mTextTop == null || mBottomDown == null) {
            return;
        }
        mTextTop.setPivotY(mTextTop.getMeasuredHeight());
        mBottomDown.setPivotY(0);
        isLooping = true;
        loop(0);
    }

    private void performRollAnim(final long duration) {
        {
            int height = mTextTop.getMeasuredHeight();
            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1, 0.9f, 0.7f, 0);
            PropertyValuesHolder translateY = PropertyValuesHolder.ofFloat("translationY", 0, -height);
            PropertyValuesHolder rotate = PropertyValuesHolder.ofFloat("rotationX", 0, 90);
            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mTextTop, alpha, translateY, rotate);
            animator.setDuration(ANIM_DURATION);
            animator.start();
        }
        {
            int height = mBottomDown.getMeasuredHeight();
            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 0.8f, 1);
            PropertyValuesHolder translateY = PropertyValuesHolder.ofFloat("translationY", height, 0);
            PropertyValuesHolder rotate = PropertyValuesHolder.ofFloat("rotationX", -90, 0);
            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mBottomDown, alpha, translateY, rotate);
            animator.setDuration(ANIM_DURATION);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    //循环
                    loop(duration);
                }
            });
            animator.start();
        }
    }

    private void loop(long duration) {
        if (duration < 0) {
            isLooping = false;
            return;
        }
        boolean hasData = autoRemove && mQueueDatas.size() > 0 || !autoRemove && mArrayDatas.size() > 0;
        boolean hasLastData = !TextUtils.isEmpty(mTextTop.getText()) || !TextUtils.isEmpty(mBottomDown.getText());
        if (hasData || hasLastData) {
            mHandler.removeMessages(ID_LOOP);
            //如果没数据了，最后一条一秒后消失
            mHandler.sendEmptyMessageDelayed(ID_LOOP, hasData ? duration : LOOP_DURATION);
        }
    }

    private RollingTextData getNextData() {
        if (autoRemove) {
            return mQueueDatas.poll();
        } else {
            RollingTextData obj = mArrayDatas.size() > 0 ? mArrayDatas.get(mCurrentIndex % mArrayDatas.size()) : null;
            mCurrentIndex++;
            return obj;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
    }

    public static class RollingTextData {
        //要显示的文本
        public String text;
        //显示多长时间
        public int duration = LOOP_DURATION;

        public RollingTextData(String text) {
            this.text = text;
        }

        public RollingTextData(String text, int duration) {
            this.text = text;
            this.duration = duration;
        }
    }
}
