package com.example.xingxiaogang.animationdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

/**
 * Created by xingxiaogang on 2016/12/8.
 */

public class RecylerActivity extends FragmentActivity {
    public static Activity mInstance;

    private static final boolean DEBUG = true;
    public static final String TAG = "test.ListActivity";

    private View mBottom;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recycler);
        if (DEBUG) {
            Log.d(TAG, "onCreate: ");
        }
        viewPager = (ViewPager) findViewById(R.id.nest_scroll_scroller);
        mBottom = findViewById(R.id.bottom);
        ViewPager viewPager = (ViewPager) findViewById(R.id.nest_scroll_scroller);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new RecyclerFragment();
            }

            @Override
            public int getCount() {
                return 1;
            }
        });
        mInstance = this;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            mBottom.setVisibility(View.VISIBLE);
            viewPager.setPadding(0, 0, 0, SizeUtils.dp2px(60));//bottom要与布局一致
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            mBottom.setVisibility(View.GONE);
            viewPager.setPadding(0, 0, 0, 0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DEBUG) {
            Log.d(TAG, "onResume: ");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (DEBUG) {
            Log.d(TAG, "onDestroy: ");
        }
    }
}
