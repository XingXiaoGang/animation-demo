package com.apusapps.tools.highlight.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.apusapps.tools.highlight.HighLight;

import java.util.List;


/**
 * Created by zhy on 15/10/8.
 */
public class HightLightView extends FrameLayout {
    private static final int DEFAULT_WIDTH_BLUR = 15;
    private static final int DEFAULT_RADIUS = 6;
    private static final PorterDuffXfermode MODE_DST_OUT = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);

    private List<HighLight.ViewPosInfo> mViewRects;
    private HighLight mHighLight;
    private LayoutInflater mInflater;
    private int maskColor = 0x77000000;
    private final boolean isNext;//next模式标志
    private int mPosition = -1;//当前显示的提示布局位置
    private HighLight.ViewPosInfo mViewPosInfo;//当前显示的高亮布局位置信息
    private Paint mHighLightPaint;
    private final Rect mDrawRect = new Rect();

    public HightLightView(Context context, HighLight highLight, int maskColor, List<HighLight.ViewPosInfo> viewRects, boolean isNext) {
        super(context);
        mHighLight = highLight;
        mInflater = LayoutInflater.from(context);
        mViewRects = viewRects;
        this.maskColor = maskColor;
        this.isNext = isNext;
        setWillNotDraw(false);
        mHighLightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHighLightPaint.setDither(true);
        mHighLightPaint.setAntiAlias(true);
        mHighLightPaint.setXfermode(MODE_DST_OUT);
        addViewForTip();
    }

    private void addViewForTip() {
        if (isNext) {
            //校验mPosition
            if (mPosition < -1 || mPosition > mViewRects.size() - 1) {
                //重置位置
                mPosition = 0;
            } else if (mPosition == mViewRects.size() - 1) {
                //移除当前布局
                mHighLight.remove();
                return;
            } else {
                //mPosition++
                mPosition++;
            }
            mViewPosInfo = mViewRects.get(mPosition);
            //移除所有tip再添加当前位置的tip布局
            removeAllTips();
            addViewForEveryTip(mViewPosInfo);
        } else {
            for (HighLight.ViewPosInfo viewPosInfo : mViewRects) {
                addViewForEveryTip(viewPosInfo);
            }
        }
    }

    /**
     * 移除当前高亮布局的所有提示布局
     */
    private void removeAllTips() {
        removeAllViews();
    }

    /**
     * 添加每个高亮布局
     *
     * @param viewPosInfo 高亮布局信息
     * @author isanwenyu@163.com
     */
    private void addViewForEveryTip(HighLight.ViewPosInfo viewPosInfo) {
        View view = mInflater.inflate(viewPosInfo.layoutId, this, false);
        LayoutParams lp = buildTipLayoutParams(view, viewPosInfo);

        if (lp == null) return;

        lp.leftMargin = (int) viewPosInfo.marginInfo.leftMargin;
        lp.topMargin = (int) viewPosInfo.marginInfo.topMargin;
        lp.rightMargin = (int) viewPosInfo.marginInfo.rightMargin;
        lp.bottomMargin = (int) viewPosInfo.marginInfo.bottomMargin;

        if (lp.rightMargin != 0) {
            lp.gravity = Gravity.RIGHT;
        } else {
            lp.gravity = Gravity.LEFT;
        }

        if (lp.bottomMargin != 0) {
            lp.gravity |= Gravity.BOTTOM;
        } else {
            lp.gravity |= Gravity.TOP;
        }
        addView(view, lp);
    }

    private void updateTipPos() {
        if (isNext)//如果是next模式 只有一个子控件 刷新当前位置tip
        {
            View view = getChildAt(0);

            LayoutParams lp = buildTipLayoutParams(view, mViewPosInfo);
            if (lp == null) return;
            view.setLayoutParams(lp);

        } else {
            for (int i = 0, n = getChildCount(); i < n; i++) {
                View view = getChildAt(i);
                HighLight.ViewPosInfo viewPosInfo = mViewRects.get(i);
                LayoutParams lp = buildTipLayoutParams(view, viewPosInfo);
                if (lp == null) continue;
                view.setLayoutParams(lp);
            }
        }
    }

    private LayoutParams buildTipLayoutParams(View view, HighLight.ViewPosInfo viewPosInfo) {
        LayoutParams lp = (LayoutParams) view.getLayoutParams();
        if (lp.leftMargin == (int) viewPosInfo.marginInfo.leftMargin &&
                lp.topMargin == (int) viewPosInfo.marginInfo.topMargin &&
                lp.rightMargin == (int) viewPosInfo.marginInfo.rightMargin &&
                lp.bottomMargin == (int) viewPosInfo.marginInfo.bottomMargin) return null;

        lp.leftMargin = (int) viewPosInfo.marginInfo.leftMargin;
        lp.topMargin = (int) viewPosInfo.marginInfo.topMargin;
        lp.rightMargin = (int) viewPosInfo.marginInfo.rightMargin;
        lp.bottomMargin = (int) viewPosInfo.marginInfo.bottomMargin;

        //fix the bug can't set gravity  LEFT|BOTTOM  or RIGHT|TOP
//        if (lp.leftMargin == 0 && lp.topMargin == 0)
//        {
//            lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
//        }
        if (lp.rightMargin != 0) {
            lp.gravity = Gravity.RIGHT;
        } else {
            lp.gravity = Gravity.LEFT;
        }

        if (lp.bottomMargin != 0) {
            lp.gravity |= Gravity.BOTTOM;
        } else {
            lp.gravity |= Gravity.TOP;
        }
        return lp;
    }

    /**
     * 切换下个提示布局
     *
     * @author isanwenyu@16.com
     */
    public void next() {
        if (isNext) addViewForTip();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        updateTipPos();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        getDrawingRect(mDrawRect);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        int count = canvas.saveLayerAlpha(mDrawRect.left, mDrawRect.top, mDrawRect.right, mDrawRect.bottom, 255, Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);
        //背景
        canvas.drawColor(maskColor);
        //挖出高亮
        if (isNext) {
            mViewPosInfo.lightShape.shape(canvas, mHighLightPaint, mViewPosInfo);
        } else {
            for (HighLight.ViewPosInfo viewPosInfo : mViewRects) {
                viewPosInfo.lightShape.shape(canvas, mHighLightPaint, viewPosInfo);
            }
        }
        canvas.restoreToCount(count);
        super.onDraw(canvas);
    }
}
