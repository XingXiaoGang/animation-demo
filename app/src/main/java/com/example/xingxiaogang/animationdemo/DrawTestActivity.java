package com.example.xingxiaogang.animationdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.xingxiaogang.animationdemo.view.PercentLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingxiaogang on 2017/7/13.
 */

public class DrawTestActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new DrawView(this), new PercentLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    }

    static class DrawView extends View {

        private static List<PorterDuffXfermode> mode = new ArrayList<>();

        static {
            mode.add(new PorterDuffXfermode(PorterDuff.Mode.ADD));
            mode.add(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            mode.add(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
            mode.add(new PorterDuffXfermode(PorterDuff.Mode.DST));
            mode.add(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
            mode.add(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            mode.add(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            mode.add(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
            mode.add(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
            mode.add(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
            mode.add(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
            mode.add(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            mode.add(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
            mode.add(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            mode.add(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
            mode.add(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
            mode.add(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        }


        public DrawView(Context context) {
            super(context);
            init(context);
        }

        public DrawView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        private Paint mPaint;

        private RectF mRound;
        private Rect mRect;

        void init(Context context) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(Color.YELLOW);
            mPaint.setStyle(Paint.Style.FILL);

            mRound = new RectF();
            mRect = new Rect();
            setLayerType(View.LAYER_TYPE_SOFTWARE, mPaint);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int width = getWidth();
            final int rowCount = 4;

            int oneW = width / rowCount;

            int startX = 0;
            int startY = 0;
            int r = oneW / 3;

            for (int i = 0; i < mode.size(); i++) {
                startX = i % rowCount * oneW;
                startY = i / rowCount * oneW;
                //画圆
                int count = canvas.saveLayer(startX, startY, startX + oneW, startY + oneW, mPaint, Canvas.CLIP_SAVE_FLAG);
                mPaint.setColor(Color.CYAN);
                mRound.set(startX, startY, startX + r * 2, startY + r * 2);
                canvas.drawOval(mRound, mPaint);

                //矩形
                mPaint.setColor(Color.BLUE);
                mPaint.setXfermode(mode.get(i));
                mRect.set(startX + oneW / 3, startY + oneW / 3, startX + oneW, startY + oneW);
                canvas.drawRect(mRect, mPaint);
                mPaint.setXfermode(null);
                canvas.restoreToCount(count);
            }


        }
    }

}
