package com.apusapps.tools.highlight.position;

import android.graphics.Rect;

import com.apusapps.tools.highlight.HighLight;


/**
 * Created by caizepeng on 16/8/20.
 */
public class OnBottomPosCallback extends OnBaseCallback {
    public OnBottomPosCallback() {
    }

    public OnBottomPosCallback(float offset) {
        super(offset);
    }

    @Override
    public void getPosition(float rightMargin, float bottomMargin, Rect rect, HighLight.MarginInfo marginInfo) {
        marginInfo.rightMargin = rightMargin;
        marginInfo.topMargin = rect.top + rect.height() + offset;
    }

}
