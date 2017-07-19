package com.example.xingxiaogang.animationdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by xingxiaogang on 2017/7/18.
 */

public class ActionMenuActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_menu_layout);
    }


    @Override
    public void onClick(View v) {
        ((IMenu) findViewById(R.id.action_menu)).toggle();
    }
}
