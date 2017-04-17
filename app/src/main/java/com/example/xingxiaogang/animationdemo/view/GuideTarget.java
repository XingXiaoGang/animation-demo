package com.example.xingxiaogang.animationdemo.view;

import android.graphics.RectF;

/**
 * 要引导显示的目标
 * Created by wangwei on 2016/8/4.
 */
public interface GuideTarget {

    /**
     * 聚焦的区域
     */
    RectF getTargetRegionOnWindow();

    /**
     * 聚焦的类型
     */
    FocusingDrawable getFocusingType();
}