package com.example.xingxiaogang.animationdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.xingxiaogang.animationdemo.view.MultiSwitcher;

/**
 * Created by xingxiaogang on 2017/9/5.
 */

public class SwitchActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = new FrameLayout(SwitchActivity.this);
        MultiSwitcher multiSwitcher = new MultiSwitcher(SwitchActivity.this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(240, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        frameLayout.setForegroundGravity(Gravity.CENTER);
        frameLayout.addView(multiSwitcher, lp);
        setContentView(frameLayout, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }
}
