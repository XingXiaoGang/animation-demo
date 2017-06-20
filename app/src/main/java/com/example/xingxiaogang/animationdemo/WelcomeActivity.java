package com.example.xingxiaogang.animationdemo;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingxiaogang on 2017/6/19.
 */

public class WelcomeActivity extends Activity {
    private static final boolean DEBUG = true;
    private static final String TAG = "WelcomeActivity";
    private List<View> mViewObjects = new ArrayList<>();
    private List<TogetherAnim> mAnims = new ArrayList<>();
    private View mBuket = null;

    private View mLogoImage = null;
    private View mNameText = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.notification_license_activity);

        initView();
        initAnimations();

        mBuket.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                startAnimate();
                mBuket.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    private void startAnimate() {
        //聚合动画
        final View buket = mBuket;
        final int startTime = 400;
        for (int i = 0; i < mAnims.size(); i++) {
            TogetherAnim anim = mAnims.get(i);
            anim.start(buket.getX(), buket.getY(), startTime + i * TogetherAnim.DURATION);
            final int finalI = i;
            mBuket.post(new Runnable() {
                @Override
                public void run() {
                    PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.8f, 1.2f, 1f);
                    PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.8f, 1.2f, 1f);
                    ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(mBuket, pvhY, pvhZ).setDuration(400);
                    objectAnimator.setStartDelay(finalI * TogetherAnim.DURATION + startTime - 10);
                    objectAnimator.start();
                }
            });
        }


        //上升动画
     /*   PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.6f, 1.3f, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.6f, 1.3f, 1f);
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("translationY", 1f, SizeUtils.dp2px(100) - mBuket.getTop());
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(mBuket, pvhY, pvhZ, pvhZ).setDuration(400);
        objectAnimator.setStartDelay(1000);
        objectAnimator.start();*/

        //抖动
    /*    int delta = SizeUtils.dp2px(4);
        PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 0),
                Keyframe.ofFloat(.10f, -delta),
                Keyframe.ofFloat(.26f, delta),
                Keyframe.ofFloat(.42f, -delta),
                Keyframe.ofFloat(.58f, delta),
                Keyframe.ofFloat(.74f, -delta),
                Keyframe.ofFloat(.90f, delta),
                Keyframe.ofFloat(1f, delta)
        );
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(mBuket, pvhTranslateX).setDuration(1000);
        objectAnimator.setStartDelay(1000);
        objectAnimator.setRepeatCount(10);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.start();*/

        //显示logo
    /*    ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1f);
        valueAnimator.setStartDelay(2000);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                findViewById(R.id.anim_panel).setVisibility(View.GONE);
                mLogoImage.setVisibility(View.VISIBLE);
                mNameText.setVisibility(View.VISIBLE);
            }
        });
        valueAnimator.start();*/
    }

    private void initView() {
        mViewObjects.add(findViewById(R.id.icon_1));
        mViewObjects.add(findViewById(R.id.icon_2));
        mViewObjects.add(findViewById(R.id.icon_3));
        mViewObjects.add(findViewById(R.id.icon_4));
        mViewObjects.add(findViewById(R.id.icon_5));
        mBuket = findViewById(R.id.target);
        mLogoImage = findViewById(R.id.img_logo);
        mNameText = findViewById(R.id.txt_name);

        TextView tv_summary = (TextView) findViewById(R.id.notify_license_desc);

        final String btn_start = getString(R.string.start_text);
        final String privacyPolicy = getString(R.string.notification_license_link);
        String summary = getString(R.string.notification_license_desc, btn_start, privacyPolicy);
        SpannableStringBuilder spanStr = new SpannableStringBuilder(summary);

        try {
            int privacyPolicyIndex = summary.indexOf(privacyPolicy);
            spanStr.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.text_alpha_60));
                    ds.setUnderlineText(true);
                }

                @Override
                public void onClick(View widget) {
                }
            }, privacyPolicyIndex, privacyPolicyIndex + privacyPolicy.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
        }

        tv_summary.setText(spanStr);
        tv_summary.setHighlightColor(Color.TRANSPARENT);
        tv_summary.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void initAnimations() {
        final List<View> viewObjects = mViewObjects;
        for (View view : viewObjects) {
            view.setAlpha(0);
            mAnims.add(new TogetherAnim(view));
        }
    }

    static class TogetherAnim extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener {
        private int finalX;
        private int finalY;
        private View targetView;

        private int startX;
        private int startY;

        private int mCurrentX;
        private int mCurrentY;

        private float mApha;
        private static final float mVelocity = 0.2f;//加速度
        private static float mTotalTime;

        public static final long DURATION = 500;

        public TogetherAnim(View targetView) {
            this.targetView = targetView;
            setFloatValues(0, 1f);
            setDuration(DURATION);
            setInterpolator(new OvershootInterpolator(1));
            addUpdateListener(this);
        }

        public void start(float finalX, float finalY, long delay) {
            super.start();
            setStartDelay(delay);
            this.finalX = (int) finalX;
            this.finalY = (int) finalY;
            startX = (int) targetView.getX();
            startY = (int) targetView.getY();
            mCurrentX = startX;
            mCurrentY = startY;
        }

        private void logic(float value, long time) {
            final int XDis = finalX - startX;
            final int YDis = finalY - startY;

            if (mTotalTime == 0) {
                mTotalTime = (float) (2 * Math.sqrt(YDis));
            }

            mCurrentX = (int) (value * (XDis + targetView.getWidth() / 3));

            //转换系数
            float t = time / 1000f * mTotalTime;
//            mCurrentY = (int) (mVelocity * (t * t));
            mCurrentY = (int) (value * (YDis + targetView.getHeight() / 3));
            mApha = 1f - value;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            logic(Float.parseFloat(getAnimatedValue().toString()), animation.getCurrentPlayTime());
            targetView.setTranslationX(mCurrentX);
            targetView.setTranslationY(mCurrentY);
            targetView.setAlpha(mApha);
        }
    }
}
