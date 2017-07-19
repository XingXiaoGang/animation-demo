package com.example.xingxiaogang.animationdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

/**
 * Created by xingxiaogang on 2017/7/18.
 */

public class ActionMenuActivity extends Activity implements View.OnClickListener {

    IMenu mActionMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_menu_layout);
        mActionMenu = (IMenu) findViewById(R.id.action_menu);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_handle: {
                mActionMenu.toggle();
                break;
            }
            default: {
                mActionMenu.close();
                Toast.makeText(this, v.getContentDescription().toString(), Toast.LENGTH_LONG).show();
                break;
            }
        }
    }
}
