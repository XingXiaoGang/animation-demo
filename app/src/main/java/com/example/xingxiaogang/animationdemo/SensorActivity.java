package com.example.xingxiaogang.animationdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.xingxiaogang.animationdemo.view.SensorView;

/**
 * Created by xingxiaogang on 2017/6/13.
 */

public class SensorActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new SensorView(this));
    }
}
