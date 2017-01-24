package com.apusapps.tools.highlight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.apusapps.tools.highlight.shape.RectLightShape;
import com.apusapps.tools.highlight.util.ViewUtils;
import com.apusapps.tools.highlight.view.HightLightView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhy on 15/10/8.
 */
public class HighLight {

    public static class ViewPosInfo {
        public int layoutId = -1;
        public RectF rectF;
        public MarginInfo marginInfo;
        public OnPosCallback onPosCallback;
        public LightShape lightShape;
    }

    public interface LightShape {
        public void shape(Canvas canvas, Paint paint, ViewPosInfo viewPosInfo);
    }

    public static class MarginInfo {
        public float topMargin;
        public float leftMargin;
        public float rightMargin;
        public float bottomMargin;

    }

    public static interface OnPosCallback {
        void getPos(float rightMargin, float bottomMargin, Rect rect, MarginInfo marginInfo);
    }

    public static interface OnClickCallback {
        void onClick();
    }


    private View mAnchor;
    private List<ViewPosInfo> mViewRects;
    private Context mContext;
    private HightLightView mHightLightView;
    private OnClickCallback clickCallback;

    private boolean intercept = true;
    //    private boolean shadow = true;
    private int maskColor = 0xCC000000;
    private boolean alphaAnim = true;

    //added by isanwenyu@163.com
    private boolean autoRemove = true;//点击是否自动移除 默认为true
    private boolean next = false;//next模式标志 默认为false

    public HighLight(Context context) {
        mContext = context;
        mViewRects = new ArrayList<ViewPosInfo>();
    }

    public HighLight anchor(View anchor) {
        mAnchor = anchor;
        return this;
    }

    public HighLight intercept(boolean intercept) {
        this.intercept = intercept;
        return this;
    }

    public HighLight maskColor(int maskColor) {
        this.maskColor = maskColor;
        return this;
    }

    public HighLight addHighLight(int viewId, int decorLayoutId, OnPosCallback onPosCallback, LightShape lightShape) {
        ViewGroup parent = (ViewGroup) mAnchor;
        View view = parent.findViewById(viewId);
        Rect rect = ViewUtils.getLocationInView(parent, view);
        addHighLight(rect, decorLayoutId, onPosCallback, lightShape);
        return this;
    }

    public HighLight addHighLight(Rect target, int decorLayoutId, OnPosCallback onPosCallback, LightShape lightShape) {
        ViewGroup parent = (ViewGroup) mAnchor;
        if (ModuleConfig.DEBUG) {
            Log.d(ModuleConfig.TAG, "addHighLight: 添加高亮目标:" + target);
        }
        ViewPosInfo viewPosInfo = new ViewPosInfo();
        viewPosInfo.layoutId = decorLayoutId;
        viewPosInfo.rectF = new RectF(target);
        if (onPosCallback == null && decorLayoutId != -1) {
            throw new IllegalArgumentException("onPosCallback can not be null.");
        }
        MarginInfo marginInfo = new MarginInfo();
        onPosCallback.getPos(parent.getWidth() - target.right, parent.getHeight() - target.bottom, target, marginInfo);
        viewPosInfo.marginInfo = marginInfo;
        viewPosInfo.onPosCallback = onPosCallback;
        viewPosInfo.lightShape = lightShape == null ? new RectLightShape() : lightShape;
        mViewRects.add(viewPosInfo);

        return this;
    }

    // 一个场景可能有多个步骤的高亮。一个步骤完成之后再进行下一个步骤的高亮
    // 添加点击事件，将每次点击传给应用逻辑
    public HighLight setClickCallback(OnClickCallback clickCallback) {
        this.clickCallback = clickCallback;
        return this;
    }

    /**
     * 点击后是否自动移除
     *
     * @return 链式接口 返回自身
     * @author isanwenyu@163.com
     * @see #show()
     * @see #remove()
     */
    public HighLight autoRemove(boolean autoRemove) {
        this.autoRemove = autoRemove;
        return this;
    }

    /**
     * 开启next模式
     *
     * @return 链式接口 返回自身
     * @author isanwenyu@163.com
     * @see #show()
     */
    public HighLight enableNext() {
        this.next = true;
        return this;
    }

    /**
     * 返回是否是next模式
     *
     * @return
     * @author isanwenyu@163.com
     */
    public boolean isNext() {
        return next;
    }

    /**
     * 切换到下个提示布局
     *
     * @return HighLight自身对象
     * @author isanwenyu@163.com
     */
    public HighLight next() {
        if (mHightLightView != null) mHightLightView.next();
        else
            remove();
        return this;
    }

    public void show() {
        if (mHightLightView == null) {
            HightLightView hightLightView = new HightLightView(mContext, this, maskColor, mViewRects, next);
            //add high light view unique id by isanwenyu@163.com  on 2016/9/28.
            hightLightView.setId(R.id.high_light_view);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.flags = WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
            lp.format = PixelFormat.RGBA_8888;
            lp.windowAnimations = R.style.guide_anim_style;
            lp.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;

            try {
                ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).addView(hightLightView, lp);
            } catch (Exception e) {
                if (ModuleConfig.DEBUG) {
                    Log.e(ModuleConfig.TAG, "show: " + mContext.getClass(), e);
                }
            }
            if (alphaAnim) {
                Animation anim = new AlphaAnimation(0, 1);
                anim.setDuration(500);
                hightLightView.startAnimation(anim);
            }

            if (intercept) {
                hightLightView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ModuleConfig.DEBUG) {
                            Log.d(ModuleConfig.TAG, "onClick inner: next" + next + ",autoRemove:" + autoRemove);
                        }
                        if (next) {
                            next();
                        } else if (autoRemove) {
                            remove();
                        }
                        if (clickCallback != null) {
                            clickCallback.onClick();
                        }
                    }
                });
            }
            mHightLightView = hightLightView;
        }
    }

    public void remove() {
        if (mHightLightView == null) return;
        try {
            ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).removeView(mHightLightView);
        } catch (Exception ignore) {
        }
        mHightLightView = null;
    }


}
