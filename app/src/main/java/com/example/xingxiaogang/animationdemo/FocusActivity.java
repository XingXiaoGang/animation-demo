package com.example.xingxiaogang.animationdemo;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;

import com.example.xingxiaogang.animationdemo.view.BeginnerGuideView;
import com.example.xingxiaogang.animationdemo.view.FocusingDrawable;
import com.example.xingxiaogang.animationdemo.view.GuideTarget;

import java.util.Arrays;

public class FocusActivity extends AppCompatActivity {

    private static final String TAG = "FocusActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);

        View view = findViewById(R.id.target_view);

        if (view != null) {
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showGuide((ViewStub) findViewById(R.id.viewStub), findViewById(R.id.target_view));
                }
            }, 500);
        }
    }

    private void showGuide(ViewStub viewStub, final View targetView) {
        if (viewStub != null) {
            BeginnerGuideView mGuideView = BeginnerGuideView.fromStub(viewStub, R.layout.beginners_guide_small_view, (2.0f / 3.0f));
            final BeginnerGuideView.GuideArguments guideArguments = new BeginnerGuideView.GuideArguments();
            guideArguments.titleId = R.string.guide_title;
            guideArguments.contentId = R.string.guide_content;
            guideArguments.accentColorId = R.color.colorPrimary;
            guideArguments.gotItBtnNameId = R.string.guide_action_content;

            mGuideView.setListener(new BeginnerGuideView.OnGotItClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            GuideTarget guideTarget = new GuideTarget() {
                @Override
                public RectF getTargetRegionOnWindow() {
                    final int[] location = new int[2];
                    targetView.getLocationInWindow(location);
                    Log.d(TAG, "getTargetRegionOnWindow: " + Arrays.toString(location));
                    /**
                     * 因为Dock栏中图标间隙较小，在分辨率低的手机中，会圈住其他手机，因此AllApps以宽为直径进行聚焦
                     */
                    int verticalOffset = SizeUtils.dp2px(8);
                    final RectF rectF = new RectF(0, 0, (float) (targetView.getMeasuredWidth() * 1.5), (float) (targetView.getMeasuredWidth() * 1.5));
                    int margin = (targetView.getHeight() - targetView.getWidth()) / 2;
                    rectF.offset(location[0] - (targetView.getMeasuredWidth() / 4), margin + verticalOffset - (targetView.getMeasuredWidth() / 4));

                    return rectF;
                }

                @Override
                public FocusingDrawable getFocusingType() {
                    return FocusingDrawable.oval();
                }
            };

            mGuideView.reset();
            mGuideView
                    .setGuideTarget(guideTarget)
                    .setTitle(guideArguments.titleId)
                    .setContent(guideArguments.contentId)
                    .setAccentColor(guideArguments.accentColorId)
                    .setGotItBtnName(guideArguments.gotItBtnNameId)
                    .setOnTargetRegionHitListener(new BeginnerGuideView.OnTargetRegionHitListener() {
                        @Override
                        public void onTargetRegionHit(RectF rect) {
                            Log.d(TAG, "onTargetRegionHit: ");
                        }
                    });
            mGuideView.show();
        }

    }
}
