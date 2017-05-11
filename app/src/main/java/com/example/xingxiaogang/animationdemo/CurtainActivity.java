package com.example.xingxiaogang.animationdemo;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import com.example.xingxiaogang.animationdemo.drawable.CurtainDrawable;

/**
 * Created by xingxiaogang on 2017/1/24.
 */

public class CurtainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_curtain);

        View target = findViewById(R.id.curtain_view);
        CurtainDrawable drawable = new CurtainDrawable();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            target.setBackground(drawable);
        } else {
            target.setBackgroundDrawable(drawable);
        }
    }
}
