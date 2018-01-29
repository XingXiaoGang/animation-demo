package com.example.xingxiaogang.animationdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

/**
 * Created by xingxiaogang on 2016/12/8.
 */

public class RecylerActivity extends FragmentActivity {
    public static Activity mInstance;

    private static final boolean DEBUG = true;
    public static final String TAG = "test.ListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recycler);
        if (DEBUG) {
            Log.d(TAG, "onCreate: ");
        }
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
