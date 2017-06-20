package com.example.xingxiaogang.animationdemo.particle;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.Random;

/**
 * 展示动画的载体
 */
public class ParticleView extends View {
    private static final String TAG = "ExplosionField";
    private ParticleAnimator explosionAnimator;
    private ParticleFactory mParticleFactory;

    public ParticleView(Context context, ParticleFactory particleFactory) {
        super(context);
        init(particleFactory);
    }

    private void init(ParticleFactory particleFactory) {
        mParticleFactory = particleFactory;
        attach2Activity((Activity) getContext());
    }

    private void attach2Activity(Activity activity) {
        ViewGroup rootView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rootView.addView(this, lp);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (explosionAnimator != null) {
            explosionAnimator.draw(canvas);
        }
    }

    /**
     * 爆破
     *
     * @param view 使得该view爆破
     */
    public void runParticle(final View view) {
        //防止重复点击
        if (explosionAnimator != null && explosionAnimator.isStarted()) {
            return;
        }
        if (view.getVisibility() != View.VISIBLE || view.getAlpha() == 0) {
            return;
        }

        final Rect rect = new Rect();
        view.getGlobalVisibleRect(rect); //得到view相对于整个屏幕的坐标
        int contentTop = ((ViewGroup) getParent()).getTop();
        Rect frame = new Rect();
        ((Activity) getContext()).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        rect.offset(0, -contentTop - statusBarHeight);//去掉状态栏高度和标题栏高度
        if (rect.width() == 0 || rect.height() == 0) {
            return;
        }

        //震动动画
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(150);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            Random random = new Random();

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setTranslationX((random.nextFloat() - 0.5f) * view.getWidth() * 0.05f);
                view.setTranslationY((random.nextFloat() - 0.5f) * view.getHeight() * 0.05f);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                runParticle(view, rect);
            }
        });
        animator.start();
    }

    private void runParticle(final View view, Rect rect) {
        final ParticleAnimator animator = new ParticleAnimator(this, Utils.createBitmapFromView(view), rect, mParticleFactory);
        explosionAnimator = animator;
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                //缩小,透明动画
                view.animate().setDuration(150).scaleX(0f).scaleY(0f).alpha(0f).start();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(150).start();

                //动画结束时从动画集中移除
                explosionAnimator = null;
            }
        });
        animator.start();
    }

}
