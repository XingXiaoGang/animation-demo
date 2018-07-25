package com.example.xingxiaogang.animationdemo.view;

import android.content.Context;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.xingxiaogang.animationdemo.SizeUtils;

import static com.example.xingxiaogang.animationdemo.BuildConfig.DEBUG;

public class VoiceButton extends ImageView implements View.OnTouchListener {

    private static final String TAG = "VoiceButton";
    private static final int sCancelDistance = SizeUtils.dp2px(60);

    private VoiceGuideLayer mVoiceLayer;

    public VoiceButton(Context context) {
        super(context);
        init(context, null);
    }

    public VoiceButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public VoiceButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setFocusable(true);
        mVoiceLayer = new VoiceGuideLayer(context);
        setOnTouchListener(this);
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction();
        if (DEBUG) {
            Log.d(TAG, "onTouch: action=" + action);
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                actionDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                actionMove(event);
                break;
            case MotionEvent.ACTION_UP://cancel的时候
                actionUp();
                break;
        }
        return true;
    }

    private void actionUp() {
        mVoiceLayer.stopAnim();
        if (isCanceling) {
            //取消发送
        }
    }

    private int mDownY;
    private boolean isCanceling;

    private void actionMove(MotionEvent event) {
        //上滑取消
        if (mDownY - event.getY() > sCancelDistance) {
            isCanceling = true;
        } else {
            isCanceling = false;
        }
        mVoiceLayer.setIsCanceling(isCanceling);
    }

    private void actionDown(MotionEvent event) {
        Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                vibrator.vibrate(100, new AudioAttributes.Builder().build());
            } else {
                vibrator.vibrate(new long[]{100, 100}, -1);
            }
        }

        mDownY = (int) event.getY();

        //语音动画
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, PercentLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM;
        mVoiceLayer.startAnim(((ViewGroup) getParent().getParent()), lp);
    }


}
