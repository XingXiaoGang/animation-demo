package com.example.xingxiaogang.animationdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by xingxiaogang on 2017/6/19.
 */

public class WelcomeActivity extends Activity {
    private static final boolean DEBUG = true;
    private static final String TAG = "WelcomeActivity";
    private List<View> mViewObjects = new ArrayList<>();
    private View mBuket = null;

    private View mLogoImage = null;
    private View mNameText = null;
    private static final int ICON_VIEW_ANIM_DURATION = 400;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.notification_license_activity);

        initView();

        mBuket.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                startAnimate();
                mBuket.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    private void startAnimate() {

        final int startTime = 400;
        //盒子放大
        PropertyValuesHolder buketX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
        PropertyValuesHolder buketY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);
        ObjectAnimator bukketAnim = ObjectAnimator.ofPropertyValuesHolder(mBuket, buketX, buketY).setDuration(600);
        bukketAnim.setInterpolator(new OvershootInterpolator(3));
        bukketAnim.setStartDelay(startTime);
        bukketAnim.start();

        //聚合动画
        final View buket = mBuket;
        Random random = new Random();
        for (int i = 0; i < mViewObjects.size(); i++) {
            final View view = mViewObjects.get(i);
            int finalX = (int) (buket.getX() + buket.getWidth() / 2 - view.getWidth() / 2);
            int finalY = (int) (buket.getY());

            //位移
            finalX = (int) (finalX + (random.nextFloat() * view.getWidth() / 2) * (i % 2 == 0 ? -1 : 1));

            int startX = (int) mViewObjects.get(i).getX();
            int startY = (int) mViewObjects.get(i).getY();

            ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(view, "translationY", 0f, (float) (finalY - startY));
            objectAnimatorX.setInterpolator(new AccelerateInterpolator());
            objectAnimatorX.setDuration(ICON_VIEW_ANIM_DURATION);
            ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(view, "translationX", 0f, (float) (finalX - startX));
            objectAnimatorY.setInterpolator(new LinearInterpolator());
            objectAnimatorY.setDuration(ICON_VIEW_ANIM_DURATION);
            AnimatorSet set = new AnimatorSet();
            set.playTogether(objectAnimatorX, objectAnimatorY);
            set.setStartDelay(i * (ICON_VIEW_ANIM_DURATION - 100) + startTime + 500 - 200);
            final int finalI = i;
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.animate().translationYBy(-view.getHeight() / 2).setDuration(ICON_VIEW_ANIM_DURATION / 2).setInterpolator(new DecelerateInterpolator()).start();

                    //缩小动画
                    if (finalI == mViewObjects.size() - 1) {
                        PropertyValuesHolder buketX = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.2f, 0f);
                        PropertyValuesHolder buketY = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.2f, 0f);
                        ObjectAnimator bukketAnim = ObjectAnimator.ofPropertyValuesHolder(mBuket, buketX, buketY).setDuration(800);
                        bukketAnim.setInterpolator(new LinearInterpolator());
                        bukketAnim.setStartDelay(startTime);
                        bukketAnim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                //Logo放大动画
                                PropertyValuesHolder buketX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f, 1.1f, 1);
                                PropertyValuesHolder buketY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f, 1.1f, 1);
                                ObjectAnimator bukketAnim = ObjectAnimator.ofPropertyValuesHolder(mLogoImage, buketX, buketY).setDuration(800);
                                bukketAnim.setInterpolator(new DecelerateInterpolator());
                                bukketAnim.start();

                                //Logo上升动画
                                PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("translationY", 1f, SizeUtils.dp2px(120) - mLogoImage.getTop());
                                ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(mLogoImage, pvhX).setDuration(900);
                                objectAnimator.setStartDelay(1000);
                                objectAnimator.setInterpolator(new DecelerateInterpolator());
                                objectAnimator.start();

                                //显示App Name
                                PropertyValuesHolder titleAnimAlpa = PropertyValuesHolder.ofFloat("alpha", 0, 1f);
                                ObjectAnimator titleAnimator = ObjectAnimator.ofPropertyValuesHolder(mNameText, titleAnimAlpa).setDuration(1000);
                                titleAnimator.setStartDelay(2000);
                                titleAnimator.start();
                            }
                        });
                        bukketAnim.start();

                        //icon缩小
                        for (View mViewObject : mViewObjects) {
                            ObjectAnimator itemAnim = ObjectAnimator.ofPropertyValuesHolder(mViewObject, buketX, buketY).setDuration(500);
                            itemAnim.setInterpolator(new LinearInterpolator());
                            itemAnim.setStartDelay(startTime);
                            itemAnim.start();
                        }
                    }
                }
            });
            set.start();
        }
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
}
