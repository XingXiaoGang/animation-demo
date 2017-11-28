package com.example.xingxiaogang.animationdemo.view.snow;

import android.graphics.Canvas;

/**
 * Created by xingxiaogang on 2017/11/28.
 * 精灵
 */

public interface ISprite {
    //逻辑
    void logic();

    //action
    void draw(Canvas canvas);

    //是否死亡
    boolean isDead();
}
