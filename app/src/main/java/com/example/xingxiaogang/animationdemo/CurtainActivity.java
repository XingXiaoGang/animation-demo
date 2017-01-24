package com.example.xingxiaogang.animationdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.example.xingxiaogang.animationdemo.view.CurtainView;

/**
 * Created by xingxiaogang on 2017/1/24.
 */

public class CurtainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_curtain);

        ((CurtainView) findViewById(R.id.curtain_view)).startOpenAnim();
    }
}
