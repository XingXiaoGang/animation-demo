package com.example.xingxiaogang.animationdemo;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewTreeObserver;

import com.example.xingxiaogang.animationdemo.view.snow.SnowPanel;

/**
 * Created by xingxiaogang on 2017/11/28.
 */

public class SnowActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_snow);
        final SnowPanel snowPanel = ((SnowPanel) findViewById(R.id.snow_panel));
        snowPanel.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                snowPanel.startSnow(20, 15, 180, 0, 4, BitmapFactory.decodeResource(getResources(), R.mipmap.snow_big));
                snowPanel.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        ((SnowPanel) findViewById(R.id.snow_panel)).stopSnow();
        super.onDestroy();
    }
}
