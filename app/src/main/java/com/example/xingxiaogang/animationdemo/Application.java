package com.example.xingxiaogang.animationdemo;

import android.util.Log;

import com.example.xingxiaogang.animationdemo.utils.LinuxUtils;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by xingxiaogang on 2018/4/25.
 */

public class Application extends android.app.Application {
    public static Application sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        Fresco.initialize(this);

        String process = LinuxUtils.getCurrentProcessName();
        Log.d("Application", "onCreate: process=" + process);
    }
}
