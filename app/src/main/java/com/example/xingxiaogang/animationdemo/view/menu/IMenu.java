package com.example.xingxiaogang.animationdemo.view.menu;

import android.animation.Animator;

/**
 * Created by xingxiaogang on 2017/7/18.
 */

public interface IMenu {

    static int STATE_CLOSED = 0;
    static int STATE_OPENED = 1;
    static int STATE_OPENING = 2;
    static int STATE_CLOSEING = 3;

    boolean open();

    boolean close();

    int getState();

    boolean toggle();

    void setListener(Animator.AnimatorListener listener);

    long getDuration();
}
