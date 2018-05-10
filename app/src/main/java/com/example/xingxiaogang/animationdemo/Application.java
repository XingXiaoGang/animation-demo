package com.example.xingxiaogang.animationdemo;

import android.util.Log;

import com.example.xingxiaogang.animationdemo.utils.LinuxUtils;

/**
 * Created by xingxiaogang on 2018/4/25.
 */

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        String process = LinuxUtils.getCurrentProcessName();
        Log.d("Application", "onCreate: process=" + process);
    }
}
