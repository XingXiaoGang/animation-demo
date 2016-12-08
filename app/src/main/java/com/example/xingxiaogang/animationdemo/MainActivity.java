package com.example.xingxiaogang.animationdemo;

import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.xingxiaogang.animationdemo.view.ColorDotLoadingDrawable;
import com.example.xingxiaogang.animationdemo.view.RadarView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RadarView iconScanView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iconScanView = (RadarView) findViewById(R.id.icon);

        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);
        findViewById(R.id.loading_window).setOnClickListener(this);

        ColorDotLoadingDrawable colorDotLoadingDrawable = new ColorDotLoadingDrawable(SizeUtils.dp2px(this, 2));
        ((ImageView) findViewById(R.id.loading_icon)).setImageDrawable(colorDotLoadingDrawable);

        Drawable drawable = ((ImageView) findViewById(R.id.vector_image)).getDrawable();
        if (drawable != null && drawable instanceof AnimatedVectorDrawable) {
            ((AnimatedVectorDrawable) drawable).start();
        }
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
            case R.id.loading_window: {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setView(R.layout.dialog_layout).create();
                dialog.show();
                break;
            }
        }
    }
}
