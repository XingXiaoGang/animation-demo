package com.example.xingxiaogang.animationdemo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * Created by xingxiaogang on 2016/6/2.
 * 雷达搜索效果view
 */
public class RadarView extends ImageView implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

    private Paint mPaintLine;
    private Paint mPaintSector;

    private int progress = 0;
    //线条的宽度
    private int lineWidth;
    private Shader mShader;
    private Matrix matrix = new Matrix();
    //    private ViewAnimator mViewAnimator;
    private ValueAnimator mValueAnimator;

    private int mWidth;
    private int mHeight;
    private long mStopPoint;

    //一圈的时间
    private int ROUND_SPEED = 800;
    private int STOP_DURATION = 2000;

    public RadarView(Context context) {
        super(context);
        init(context);
    }

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(final Context context) {
        lineWidth = dp2px(getContext(), 2.5f);
        mPaintLine = new Paint();
        mPaintLine.setStrokeWidth(lineWidth);
        mPaintLine.setAntiAlias(true);
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setColor(Color.WHITE);

        mPaintSector = new Paint();
        mPaintSector.setColor(getResources().getColor(R.color.radar_color_light));
        mPaintSector.setAntiAlias(true);
        mPaintSector.setDither(true);
//硬件加速
//        if (!isHardwareAccelerated()) {
//            setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        }
        setBackgroundColor(Color.TRANSPARENT);
        setWillNotDraw(false);

        mValueAnimator = ValueAnimator.ofInt(0, 360);
        mValueAnimator.setRepeatCount(-1);
        mValueAnimator.setDuration(ROUND_SPEED);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (((int) animation.getAnimatedValue()) > 1500) {
                    animation.setInterpolator(new DecelerateInterpolator());
                }
                if (centerX == 0 || centerY == 0) {
                    centerX = getWidth() / 2;
                    centerY = getHeight() / 2;
                }
                progress = (int) animation.getAnimatedValue();
                matrix.reset();
                matrix.setRotate(progress, centerX, centerY);
                invalidate();
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = right - left;
        mHeight = bottom - top;
        if (mShader == null) {
            mShader = new SweepGradient(mWidth / 2, mHeight / 2, Color.TRANSPARENT, mPaintSector.getColor());
        }
    }

    //一圈的速度
    public void setSpeed(int roundDuration) {
        ROUND_SPEED = roundDuration;
        mValueAnimator.setDuration(roundDuration);
    }

    //一圈的速度
    public int getSpeed() {
        return ROUND_SPEED;
    }

    public int getStopDuration() {
        return STOP_DURATION;
    }

    public void setStopDuraion(int duration) {
        STOP_DURATION = duration;
    }

    //开始
    public void start() {
        mStopPoint = 0;
        mShader = new SweepGradient(mWidth / 2, mHeight / 2, Color.TRANSPARENT, mPaintSector.getColor());
        mValueAnimator = ValueAnimator.ofInt(0, 360);
        mValueAnimator.setRepeatCount(-1);
        mValueAnimator.setDuration(ROUND_SPEED);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.addUpdateListener(this);
        mValueAnimator.removeListener(this);
        mValueAnimator.start();
        onScanStart();
    }

    //停止
    public void stop() {
        mValueAnimator.cancel();
        mStopPoint = (int) mValueAnimator.getAnimatedValue();
    }

    //平滑停止
    public void stopFlat() {
        mValueAnimator.cancel();
        int progress = (int) mValueAnimator.getAnimatedValue();
        mStopPoint = progress;
        mValueAnimator.setDuration(STOP_DURATION);
        mValueAnimator.setInterpolator(new DecelerateInterpolator());
        mValueAnimator.setFloatValues(progress, progress + 180);
        mValueAnimator.setRepeatCount(0);
        mValueAnimator.start();
        mValueAnimator.addListener(this);
    }

    protected ValueAnimator getValueAnimator() {
        return mValueAnimator;
    }

    public static int dp2px(@NonNull Context context, float dp) {
        final float density = context.getResources().getDisplayMetrics().density;
        return (int) (density * dp + 0.5F);
    }

    protected void onScanStart() {

    }

    //所有动画结束
    protected void onScanStop() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int centerX = mWidth / 2;
        final int centerY = mHeight / 2;
//        canvas.drawCircle(centerX, centerY, centerX >> 1, mPaintLine);
//        canvas.drawCircle(centerX, centerY, centerX - (lineWidth >> 1), mPaintLine);
//        canvas.drawLine(centerX, 0, centerX, mHeight, mPaintLine);
//        canvas.drawLine(0, centerY, centerX << 1, centerY, mPaintLine);
        mPaintSector.setShader(mShader);
        canvas.concat(matrix);
        canvas.drawCircle(centerX, centerY, centerX, mPaintSector);

        mPaintLine.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(centerX, centerY, centerX / 3, mPaintLine);
        mPaintLine.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerX, centerY, centerX / 6, mPaintLine);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mValueAnimator.cancel();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        progress = (int) animation.getAnimatedValue();
        progress += mStopPoint;
        matrix.reset();
        matrix.setRotate(progress, mWidth >> 1, mHeight >> 1);
        invalidate();
    }

    @Override
    public void onAnimationStart(Animator animation) {
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        //执行缩小动画
//        mValueAnimator.cancel();
//        mValueAnimator.setIntValues(100, 30);
//        mValueAnimator.setInterpolator(new DecelerateInterpolator());
//        mValueAnimator.setDuration(800);
//        mValueAnimator.setRepeatCount(0);
//        mValueAnimator.start();
        onScanStop();
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
