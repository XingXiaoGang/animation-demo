package com.example.xingxiaogang.animationdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.example.xingxiaogang.animationdemo.view.VoiceButton;

public class VoiceGuideActivity extends Activity {


    private VoiceButton mBtnRecord;
    private LinearLayout mInputView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_guide);

        mInputView = (LinearLayout) findViewById(R.id.input);
        mBtnRecord = (VoiceButton) findViewById(R.id.btn_record);
    }


}
