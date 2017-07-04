package com.example.xingxiaogang.animationdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.example.xingxiaogang.animationdemo.SizeUtils;

/**
 * Created by xingxiaogang on 2017/1/4.
 */

public class DragView extends TextView {

    private static final boolean DEBUG = true;
    private static final String TAG = "DragView";

    private int downX;
    private int downY;
    private int currentX;
    private int currentY;
    private int distanceX;
    private int distanceY;
    private boolean isDragging;
    private int maxDistance;
    private Paint mBgPaint;
    private Paint mLinePaint;
    private Path mPath;


    private float maxOffset;

    public DragView(Context context) {
        super(context);
        init(context);
    }

    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        setFocusable(true);
        float dp1 = SizeUtils.dp2px(context, 1);
        maxDistance = (int) (dp1 * 100);
        maxOffset = dp1 * 10;

        mBgPaint = new Paint();
        mBgPaint.setColor(Color.RED);
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setAntiAlias(true);

        mLinePaint = new Paint();
        mLinePaint.setColor(Color.RED);
        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setPathEffect(new CornerPathEffect(5));

        mPath = new Path();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int active = MotionEventCompat.findPointerIndex(event, 0);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = (int) event.getX(active);
                downY = (int) event.getY(active);
                isDragging = true;
                currentX = downX;
                currentY = downY;
                invalidate();
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                int cx = (int) event.getX(active);
                int cy = (int) event.getY(active);
                distanceX = (int) Math.abs(Math.sqrt((cx - downX) * (cx - downX)));
                distanceY = (int) Math.abs(Math.sqrt((cy - downY) * (cy - downY)));
                currentX = cx;
                currentY = cy;
                invalidate();
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                isDragging = false;
                invalidate();
                break;
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isDragging) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - getPaddingTop(), mBgPaint);
            super.onDraw(canvas);
        } else {
            float width = getWidth() - getPaddingTop();
            float distance = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);
            float scale = (maxDistance - distance) / maxDistance;
            if (scale <= 0.6f) {
                scale = 0.6f;
            }
            if (DEBUG) {
                Log.d(TAG, "onDraw: scale :" + scale);
            }
            mLinePaint.setStrokeWidth(width / 2 * scale);

            canvas.drawLine(getWidth() / 2, getHeight() / 2, currentX, currentY, mLinePaint);

            canvas.drawCircle(getWidth() / 2, getHeight() / 2, width / 2 * scale, mBgPaint);

            canvas.drawCircle(currentX, currentY, width / 2 * (1 - scale), mBgPaint);


            mPath.reset();
            

        }
    }


}
