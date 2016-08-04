package com.example.xingxiaogang.animationdemo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RadarView iconScanView;
    private RadarView bigIconScanView;
    private ImageView smallImageView;
    private Animation mRotateAnimation;
    private Animation mScaleAnimation;
    private Animation mFadeInAnimation;

    private TextView mTitle;
    private TextView mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iconScanView = (RadarView) findViewById(R.id.icon);
        smallImageView = (ImageView) findViewById(R.id.small_icon);
        mTitle = (TextView) findViewById(R.id.title);
        mDescription = (TextView) findViewById(R.id.description);
        bigIconScanView = (RadarView) findViewById(R.id.icon_big);
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);
        findViewById(R.id.loading_window).setOnClickListener(this);

        ColorDotLoadingDrawable colorDotLoadingDrawable = new ColorDotLoadingDrawable();
        ((ImageView) findViewById(R.id.loading_icon)).setImageDrawable(colorDotLoadingDrawable);
        colorDotLoadingDrawable.start(400);

        initAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iconScanView.stopFlat();
        bigIconScanView.stopFlat();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start: {
//                iconScanView.start();
                bigIconScanView.start();
//                bigIconScanView.startAnimation(mRotateAnimation);
                break;
            }
            case R.id.stop: {
//                iconScanView.stopFlat();
                bigIconScanView.stopFlat();
//                bigIconScanView.clearAnimation();
                break;
            }
            case R.id.loading_window: {
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("请稍后");
                progressDialog.setMessage("正在扫描..");
//                Drawable loadingDrawable = new FoldingCirclesDrawable.Builder(this).build();
                ColorDotLoadingDrawable drawable = new ColorDotLoadingDrawable();
                progressDialog.setIndeterminateDrawable(drawable);
                progressDialog.show();
                break;
            }
        }
    }

    private void initAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(2000);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setFillAfter(true);
        mRotateAnimation = rotateAnimation;
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0.4f, 1, 0.4f, RotateAnimation.RELATIVE_TO_SELF, 1f, RotateAnimation.RELATIVE_TO_SELF, 1f);
        scaleAnimation.setDuration(600);
        mScaleAnimation = scaleAnimation;
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1f);
        alphaAnimation.setDuration(1000);
        mFadeInAnimation = alphaAnimation;
    }

}
