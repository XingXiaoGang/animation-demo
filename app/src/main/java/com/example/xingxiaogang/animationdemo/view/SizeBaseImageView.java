package com.example.xingxiaogang.animationdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.xingxiaogang.animationdemo.R;

/**
 * Created by xingxiaogang on 2016/8/4.
 */
public class SizeBaseImageView extends View {

    protected static final boolean DEBUG = true;
    protected static final String TAG = "test.SizeBaseImageView";

    private static final int BASE_WIDTH = 1;
    private static final int BASE_HEIGHT = 0;
    private Bitmap mBitmap;
    private Bitmap mOriginBitmap;
    //图像大小
    private Rect mBitmapRect;
    //实际可见大小
    private Rect mDisplayRect;
    private float mScale = 1;
    private int mBase;

    public SizeBaseImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public SizeBaseImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attributeSet) {
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.SizeBaseImageView, 0, 0);

        Drawable drawable = a.getDrawable(R.styleable.SizeBaseImageView_src);
        mBase = a.getInt(R.styleable.SizeBaseImageView_baseWith, BASE_WIDTH);
        mScale = a.getFloat(R.styleable.SizeBaseImageView_imageScale, 1f);
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            setImageBitMap(bitmap);
        }

        if (DEBUG) {
            Log.d(TAG, "SizeBaseImageView.initView: mBase:" + mBase + " mScale:" + mScale);
        }
        a.recycle();
        mBitmapRect = new Rect();
        mDisplayRect = new Rect();
    }

    public void setImageBitMap(Bitmap bitmap) {
        mOriginBitmap = bitmap;
        requestLayout();
    }

    //转换成比较优化的大小
    private void convertBitmap(int baseSize) {
        if (mOriginBitmap != null || mBitmap == null) {
            if (mOriginBitmap != null) {
                switch (mBase) {
                    case BASE_HEIGHT: {
                        int height = (int) (baseSize * mScale);
                        int bitmapWidth = mOriginBitmap.getWidth();
                        int bitmapHeight = mOriginBitmap.getHeight();
                        int width = (int) (bitmapWidth * 1.0f / bitmapHeight * height);
                        if (DEBUG) {
                            Log.d(TAG, "SizeBaseImageView.convertBitmap: BASE_HEIGHT :" + (mBitmap == null));
                        }
                        mBitmap = createBitmap(mOriginBitmap, width, height);
                        break;
                    }
                    case BASE_WIDTH: {
                        int width = (int) (baseSize * mScale);
                        int bitmapWidth = mOriginBitmap.getWidth();
                        int bitmapHeight = mOriginBitmap.getHeight();
                        int height = (int) (bitmapHeight * 1.0f / bitmapWidth * width);
                        if (DEBUG) {
                            Log.d(TAG, "SizeBaseImageView.convertBitmap: BASE_WIDTH :" + mOriginBitmap.getHeight() + ",," + mOriginBitmap.getWidth() + (mBitmap == null));
                        }
                        mBitmap = createBitmap(mOriginBitmap, width, height);
                        break;
                    }
                }
//                mOriginBitmap.recycle();
                mOriginBitmap = null;
            }
        }
    }

    //生成特定大小的bitmap
    private Bitmap createBitmap(Bitmap origin, int width, int height) {
        if (DEBUG) {
            Log.d(TAG, "SizeBaseImageView.createBitmap: width:" + width + " height:" + height);
        }
        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createScaledBitmap(origin, width, height, false);
        } catch (Exception e) {
            System.gc();
            try {
                bitmap = Bitmap.createScaledBitmap(origin, width, height, false);
            } catch (Exception ignore) {
            }
        }
        return bitmap;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public Rect getDrawRect() {
        return mBitmapRect;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        //计算高度 或宽度

        switch (mBase) {
            case BASE_HEIGHT: {
                if (height <= 0) {
                    setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
                    return;
                } else {
                    convertBitmap(height);
                    if (mBitmap == null) {
                        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
                        return;
                    }

                    int widthSpec = MeasureSpec.makeMeasureSpec((int) (mBitmap.getWidth() / mScale), MeasureSpec.getMode(widthMeasureSpec));
                    setMeasuredDimension(widthSpec, heightMeasureSpec);
                }
                break;
            }
            case BASE_WIDTH: {
                if (width <= 0) {
                    setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
                    return;
                } else {
                    convertBitmap(width);
                    if (mBitmap == null) {
                        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
                        return;
                    }

                    int heightSpec = MeasureSpec.makeMeasureSpec((int) (mBitmap.getHeight() / mScale), MeasureSpec.getMode(heightMeasureSpec));
                    setMeasuredDimension(widthMeasureSpec, heightSpec);

                }
                break;
            }
        }

        getGlobalVisibleRect(mDisplayRect);
        if (mBitmap != null) {
            mBitmapRect.set(getLeft(), getTop(), getLeft() + width, (int) (getTop() + mBitmap.getHeight() / mScale));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mBitmap != null) {
            canvas.drawColor(Color.TRANSPARENT);
            canvas.drawBitmap(mBitmap, mBitmapRect, mBitmapRect, null);
        }

        if (DEBUG) {
            Log.d(TAG, "SizeBaseImageView.onDraw: mBitmap:" + (mBitmap != null ? " width:" + mBitmap.getWidth() + ",height:" + mBitmap.getHeight() : "null"));
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mBitmap = null;
    }
}
