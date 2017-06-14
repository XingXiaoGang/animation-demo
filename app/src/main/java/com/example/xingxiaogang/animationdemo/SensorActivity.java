package com.example.xingxiaogang.animationdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by xingxiaogang on 2017/6/13.
 */

public class SensorActivity extends Activity implements SensorListener {

    SensorView mSensorView = null;
    SensorManager sensorManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(mSensorView = new SensorView(this));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, Sensor.TYPE_ACCELEROMETER, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onSensorChanged(int sensor, float[] values) {
        switch (sensor) {
            case Sensor.TYPE_ACCELEROMETER: {
                mSensorView.updateSensor(values);
                break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(int sensor, int accuracy) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    public static class SensorView extends View {

        private Paint paint;
        private Paint mLinePaint;
        private Paint mDotLinePaint;
        private Paint mTextPaint;
        private Rect rect = new Rect();
        private float[] sensorValues;
        private Path path = new Path();

        public SensorView(Context context) {
            super(context);
            setBackgroundColor(Color.GRAY);
            paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(SizeUtils.dp2px(8));
            paint.setAntiAlias(true);

            mLinePaint = new Paint();
            mLinePaint.setAntiAlias(true);
            mLinePaint.setStyle(Paint.Style.STROKE);
            mLinePaint.setColor(Color.BLACK);
            mLinePaint.setStrokeWidth(SizeUtils.dp2px(2));
            mLinePaint.setTextAlign(Paint.Align.CENTER);

            mDotLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mDotLinePaint.setStyle(Paint.Style.STROKE);
            mDotLinePaint.setColor(Color.BLUE);
            mDotLinePaint.setStrokeWidth(SizeUtils.dp2px(1.5f));
            mDotLinePaint.setPathEffect(new DashPathEffect(new float[]{5, 10, 5, 10}, 10));

            mTextPaint = new Paint();
            mTextPaint.setAntiAlias(true);
            mTextPaint.setStrokeWidth(SizeUtils.dp2px(0.7f));
            mTextPaint.setTextSize(SizeUtils.dp2px(15));
            mTextPaint.setColor(Color.BLACK);
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            rect.set(left, top, right, bottom);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int radius = rect.width() / 2 / 5 * 4;
            //大圆
            canvas.drawCircle(rect.centerX(), rect.centerY(), radius, paint);
            //小圆
            canvas.drawCircle(rect.centerX(), rect.centerY(), SizeUtils.dp2px(5), paint);
            if (sensorValues != null) {
                int x = (int) sensorValues[2];
                int y = (int) sensorValues[1];
                //是否相反
                final boolean REVERT = Build.VERSION.SDK_INT <= 22;
                int degress = (int) accelerometer2Degrees(x, y, REVERT);

                y = 90 - y;//(0-180,上到下)
                if (REVERT) {
                    x += 90;
                } else {
                    x = 90 - x;//(0-180,左到右)
                }
                String showText = "角度：" + degress;
                int textLenth = (int) mTextPaint.measureText(showText);
                canvas.drawText(showText, rect.centerX() - textLenth / 2, rect.centerY() + radius + SizeUtils.dp2px(28), mTextPaint);

                int finalX = (int) (x / 180f * (radius * 2));
                int finalY = (int) (y / 180f * (radius * 2));

                final int lineLenth = radius * 2 + SizeUtils.dp2px(20);

                //画x 竖线  中心(finalX,centerY)
                int line1X = finalX + rect.centerX() - radius;
                canvas.drawLine(line1X, rect.centerY() - lineLenth / 2, line1X, rect.centerY() + lineLenth / 2, mLinePaint);

                //画y 横线  中心(centerX,finalY)
                canvas.drawLine(rect.centerX() - lineLenth / 2, finalY + rect.centerY() - radius, rect.centerX() + lineLenth / 2, finalY + rect.centerY() - radius, mLinePaint);

                path.reset();
                path.moveTo(rect.centerX(), rect.centerY());
                path.lineTo(finalX + rect.centerX() - radius, finalY + rect.centerY() - radius);
                path.close();
                canvas.drawPath(path, mDotLinePaint);
            }
        }

        //装重力转换为[0-360)弧度
        public float accelerometer2Degrees(int x, int y, boolean reversX) {
            int degree = 0;
            int base = 0;
            if (reversX) {
                if (x > 0) {
                    if (y > 0) {
                        base = 180;
                    }
                } else if (x == 0) {
                    degree = y > 0 ? 270 : y == 0 ? -1 : 90;
                } else {
                    base = 180;
                }
            } else {
                if (x > 0) {
                    if (y > 0) {
                        x = -x;
                        base = 360;
                    } else {
                        x = -x;
                    }
                } else if (x == 0) {
                    degree = y > 0 ? 270 : y == 0 ? -1 : 90;
                } else {
                    x = -x;
                    base = 180;
                }
            }
            return degree == 270 || degree == 90 ? degree : (int) Math.toDegrees(Math.atan(y * 1.0f / x)) + base;
        }

        public void updateSensor(float[] values) {
            sensorValues = values;
            postInvalidate();
        }
    }
}
