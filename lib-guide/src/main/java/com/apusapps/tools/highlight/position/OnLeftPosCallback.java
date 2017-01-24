package com.apusapps.tools.highlight.position;

import android.graphics.Rect;

import com.apusapps.tools.highlight.HighLight;


/**
 * Created by caizepeng on 16/8/20.
 */
public class OnLeftPosCallback extends OnBaseCallback {
    public OnLeftPosCallback() {
    }

    public OnLeftPosCallback(float offset) {
        super(offset);
    }

    @Override
    public void getPosition(float rightMargin, float bottomMargin, Rect rect, HighLight.MarginInfo marginInfo) {
        marginInfo.rightMargin = rightMargin + rect.width() + offset;
        marginInfo.topMargin = rect.top;
    }
}
