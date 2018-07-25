package com.example.xingxiaogang.animationdemo.view;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xingxiaogang.animationdemo.R;
import com.example.xingxiaogang.animationdemo.SizeUtils;
import com.example.xingxiaogang.animationdemo.drawable.VoiceStrengthDrawable;
import com.example.xingxiaogang.animationdemo.drawable.VoiceWaveDrawable;

import static com.example.xingxiaogang.animationdemo.BuildConfig.DEBUG;


public class VoiceGuideLayer extends LinearLayout implements Handler.Callback {

    private static final String TAG = "VoiceGuideLayer";
    private static final float sDp1 = SizeUtils.dp2px(1);

    private static final int mNormalColor = Color.parseColor("#04C7B7");
    private static final int mCacellingColor = Color.parseColor("#FF506D");

    private ViewGroup mVoiceContainer;
    private ImageView mVoiceWave;
    private TextView mTimeText;
    private boolean isCanceling;
    private Handler mHandler;
    private long mDownTime;
    private VoiceWaveDrawable mVoiceDrawable;
    private VoiceStrengthDrawable mStrengthDrawable;

    private ViewGroup mParentView;
    private ViewPropertyAnimator mUpAnim;
    private ValueAnimator colorAnimator;

    public VoiceGuideLayer(Context context) {
        super(context);
        initView(context, null);
    }

    public VoiceGuideLayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public VoiceGuideLayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    private void initView(Context context, AttributeSet attr) {
        View.inflate(context, R.layout.voice_layout, this);
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(Color.BLUE);

        mHandler = new Handler(this);
        mVoiceWave = (ImageView) findViewById(R.id.voice_wave);
        mVoiceContainer = (ViewGroup) findViewById(R.id.voice_container);
        mTimeText = (TextView) findViewById(R.id.time_tips);

        mVoiceDrawable = new VoiceWaveDrawable(mNormalColor, mCacellingColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mVoiceWave.setBackground(mVoiceDrawable);
        } else {
            mVoiceWave.setBackgroundDrawable(mVoiceDrawable);
        }

        mStrengthDrawable = new VoiceStrengthDrawable(mNormalColor, mCacellingColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mTimeText.setBackground(mStrengthDrawable);
        } else {
            mTimeText.setBackgroundDrawable(mStrengthDrawable);
        }
        mTimeText.setTextColor(mNormalColor);
    }

    public void startAnim(ViewGroup parent, ViewGroup.LayoutParams lp) {
        if (DEBUG) {
            Log.d(TAG, "startAnim: ");
        }
        //重置动画
        resetAnim();
        attatch(parent, lp);
        //上升
        mUpAnim = mVoiceContainer.animate().translationY(-sDp1 * 8).setDuration(500).setInterpolator(new DecelerateInterpolator());
        mUpAnim.start();
        //背影渐变
        colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor("#00ffffff"), Color.WHITE).setDuration(800);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setBackgroundColor((Integer) valueAnimator.getAnimatedValue());
            }
        });
        colorAnimator.start();

        //语音时间
        mDownTime = System.currentTimeMillis();
        mHandler.sendEmptyMessage(0);
    }

    public void stopAnim() {
        if (DEBUG) {
            Log.d(TAG, "stopAnim: ");
        }
        if (mUpAnim != null) {
            mUpAnim.cancel();
        }
        if (colorAnimator != null) {
            colorAnimator.cancel();
        }
        mHandler.removeMessages(0);
        dettatch();
    }

    private void attatch(ViewGroup viewGroup, ViewGroup.LayoutParams lp) {
        this.mParentView = viewGroup;
        try {
            viewGroup.addView(this, lp);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "attatch: ", e);
            }
        }
    }

    private void dettatch() {
        if (mParentView == null) {
            return;
        }
        try {
            mParentView.removeView(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetAnim() {
        mVoiceDrawable.reset();
        mStrengthDrawable.reset();
        mVoiceContainer.setTranslationY(0);
        setBackgroundColor(Color.TRANSPARENT);
        mHandler.removeMessages(0);
    }

    public void setIsCanceling(boolean isCanceling) {
        this.isCanceling = isCanceling;
        this.mVoiceDrawable.setIsCancelling(isCanceling);
        this.mStrengthDrawable.setIsCancelling(isCanceling);
        this.mTimeText.setTextColor(isCanceling ? mCacellingColor : mNormalColor);
    }

    @Override
    public boolean handleMessage(Message message) {
        mTimeText.setText(getContext().getString(R.string.voice_time_tips, (System.currentTimeMillis() - mDownTime) / 1000));
        mHandler.sendEmptyMessageDelayed(0, 1000);
        return true;
    }
}
