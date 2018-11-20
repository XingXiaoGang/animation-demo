package com.example.xingxiaogang.animationdemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.xingxiaogang.animationdemo.view.FlowLayoutView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinggang on 2018/11/20 下午4:24.
 * <p>
 * email: xxg841076938@gmail.com
 * use: 流式布局测试
 **/
public class FlowLayoutActivity extends Activity {

    private FlowLayoutView mFlowLabelView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_layout);

        mFlowLabelView = (FlowLayoutView) findViewById(R.id.flow_view);

        initLabels();
    }

    private void initLabels() {
        List<FlowLayoutView.LabelItem> labelItems = new ArrayList<>();
        labelItems.add(new FlowLayoutView.LabelItem("情话", Color.parseColor("#dddd77")));
        labelItems.add(new FlowLayoutView.LabelItem("乡愁", Color.GREEN));
        labelItems.add(new FlowLayoutView.LabelItem("这个杀手不太冷", Color.argb(100, 150, 100, 90)));
        labelItems.add(new FlowLayoutView.LabelItem("冰激凌", Color.CYAN));
        labelItems.add(new FlowLayoutView.LabelItem("三明治", Color.BLUE));
        mFlowLabelView.setLabels(labelItems);
    }
}
