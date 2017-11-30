package com.example.xingxiaogang.animationdemo;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.xingxiaogang.animationdemo.view.ScanBcDrawable;

/**
 * Created by xingxiaogang on 2017/9/29.
 */

public class ProgressActivity extends Activity {

    ScanBcDrawable scanBcDrawable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_progress);

        View view = findViewById(R.id.progress_banner);


        int[] colorArray = getResources().getIntArray(R.array.progress_colors);
        int[] valueArray = getResources().getIntArray(R.array.progress_values);
        int[] lights = getResources().getIntArray(R.array.light_colors);
        scanBcDrawable = new ScanBcDrawable(valueArray, colorArray, lights);
        view.setBackgroundDrawable(scanBcDrawable);
        scanBcDrawable.startScan();

        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
        valueAnimator.setDuration(5000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scanBcDrawable.onLevelUpdate((Integer) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onDestroy() {
        scanBcDrawable.stopScan();
        super.onDestroy();
    }
}
