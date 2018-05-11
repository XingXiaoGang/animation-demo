package com.example.xingxiaogang.animationdemo.game;

import android.graphics.Canvas;

/**
 * Created by xingxiaogang on 2018/5/11.
 */

public class MainScene implements IScenen {

    private int mBGColor;

    public MainScene(int mBGColor) {
        this.mBGColor = mBGColor;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(mBGColor);
    }
}
