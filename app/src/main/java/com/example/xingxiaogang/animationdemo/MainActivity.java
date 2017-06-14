package com.example.xingxiaogang.animationdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.example.xingxiaogang.animationdemo.view.ColorDotLoadingDrawable;
import com.example.xingxiaogang.animationdemo.view.RadarView;
import com.plattysoft.leonids.ParticleSystem;

public class MainActivity extends Activity implements View.OnClickListener {

    private RadarView iconScanView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iconScanView = (RadarView) findViewById(R.id.icon);

        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);
        findViewById(R.id.cornerImage_window).setOnClickListener(this);
        findViewById(R.id.list).setOnClickListener(this);
        findViewById(R.id.sensor).setOnClickListener(this);
        findViewById(R.id.focus_open).setOnClickListener(this);

        ColorDotLoadingDrawable colorDotLoadingDrawable = new ColorDotLoadingDrawable(SizeUtils.dp2px(this, 2));
        ((ImageView) findViewById(R.id.loading_icon)).setImageDrawable(colorDotLoadingDrawable);

        iconScanView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //抛物线
                ParticleSystem ps2 = new ParticleSystem(MainActivity.this, 100, R.mipmap.star_pink, 4000);
                ps2.setScaleRange(0.5f, 1.0f);
                ps2.setSpeedModuleAndAngleRange(0.02f, 0.08f, 265, 360);
                ps2.setRotationSpeedRange(90, 360);
                ps2.setAcceleration(0.0001f, 80);
                ps2.setFadeOut(800);
                ps2.emit(iconScanView, 10);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    iconScanView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    iconScanView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iconScanView.stopFlat();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start: {
                iconScanView.start();
                break;
            }
            case R.id.stop: {
                iconScanView.stopFlat();
                break;
            }
            case R.id.cornerImage_window: {
                startActivity(new Intent(this, CornerImageActivity.class));
                break;
            }
            case R.id.list: {
                startActivity(new Intent(this, ListActivity.class));
                break;
            }
            case R.id.focus_open: {
                startActivity(new Intent(this, FocusActivity.class));
                break;
            }
            case R.id.sensor: {
                startActivity(new Intent(this, SensorActivity.class));
                break;
            }
        }
    }
}
