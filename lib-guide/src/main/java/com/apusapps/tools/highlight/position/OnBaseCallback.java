package com.apusapps.tools.highlight.position;

import android.graphics.Rect;

import com.apusapps.tools.highlight.HighLight;


/**
 * Created by caizepeng on 16/8/20.
 */
public abstract class OnBaseCallback implements HighLight.OnPosCallback {
    protected float offset;

    public OnBaseCallback() {
    }

    public OnBaseCallback(float offset) {
        this.offset = offset;
    }

    /**
     * 如果需要调整位置,重写该方法
     *
     * @param rightMargin
     * @param bottomMargin
     * @param rectF
     * @param marginInfo
     */
    public void posOffset(float rightMargin, float bottomMargin, Rect rectF, HighLight.MarginInfo marginInfo) {
    }

    @Override
    public void getPos(float rightMargin, float bottomMargin, Rect rect, HighLight.MarginInfo marginInfo) {
        getPosition(rightMargin, bottomMargin, rect, marginInfo);
        posOffset(rightMargin, bottomMargin, rect, marginInfo);
    }

    public abstract void getPosition(float rightMargin, float bottomMargin, Rect rect, HighLight.MarginInfo marginInfo);
}
