package com.example.xingxiaogang.animationdemo;

import android.app.Activity;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;

import com.example.xingxiaogang.animationdemo.view.BeginnerGuideView;
import com.example.xingxiaogang.animationdemo.view.FocusingDrawable;
import com.example.xingxiaogang.animationdemo.view.GuideTarget;

public class FocusActivity extends Activity {

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
            BeginnerGuideView mGuideView = BeginnerGuideView.fromStub(viewStub, R.layout.beginners_guide_small_view, 1);
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

                    final int statusBarHeight = SizeUtils.getStatusBarHeight(getApplicationContext());
                    int actionBarHeight = 0;
                    if (getActionBar() != null) {
                        actionBarHeight = getActionBar().getHeight();
                    }

                    int yOff = statusBarHeight + actionBarHeight;

                    //todo 适当扩散

                    return new RectF(location[0], location[1] - yOff, location[0] + (float) (targetView.getMeasuredWidth()), location[1] + targetView.getMeasuredHeight() - yOff);
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
