package com.example.xingxiaogang.animationdemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.xingxiaogang.animationdemo.view.TagFlowView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinggang on 2018/11/20 下午4:24.
 * <p>
 * email: xxg841076938@gmail.com
 * use: 流式布局测试
 **/
public class FlowLayoutActivity extends Activity {

    private TagFlowView mFlowLabelView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_layout);

        mFlowLabelView = (TagFlowView) findViewById(R.id.flow_view);

        initLabels();
    }

    private void initLabels() {
        List<TagFlowView.LabelItem> labelItems = new ArrayList<>();
        int textColor = Color.parseColor("#666666");
        labelItems.add(new TagFlowView.LabelItem("情话", textColor, getLabelColor("电影")));
//        labelItems.add(new TagFlowView.LabelItem("乡愁", textColor, getLabelColor("书籍")));
//        labelItems.add(new TagFlowView.LabelItem("这个杀手不太冷", textColor, getLabelColor("电影")));
//        labelItems.add(new TagFlowView.LabelItem("城府", textColor, getLabelColor("音乐")));
//        labelItems.add(new TagFlowView.LabelItem("北京", textColor, getLabelColor("地方")));
//        labelItems.add(new TagFlowView.LabelItem("三明治", textColor, getLabelColor("美食")));
//        labelItems.add(new TagFlowView.LabelItem("情话", textColor, getLabelColor("电影")));
//        labelItems.add(new TagFlowView.LabelItem("乡愁啊", textColor, getLabelColor("书籍")));
//        labelItems.add(new TagFlowView.LabelItem("这个杀手不太冷啊啊", textColor, getLabelColor("电影")));
//        labelItems.add(new TagFlowView.LabelItem("城城府城府城府城府城府府", textColor, getLabelColor("音乐")));
//        labelItems.add(new TagFlowView.LabelItem("北京", textColor, getLabelColor("地方")));
//        labelItems.add(new TagFlowView.LabelItem("三明治", textColor, getLabelColor("美食")));
        mFlowLabelView.setLabels(labelItems);
        mFlowLabelView.setMaxRows(2);
    }


    private int getLabelColor(String type) {
        int color = Color.parseColor("#DDF7FF");
        switch (type) {
            case "运动":
                color = Color.parseColor("#DDF7FF");
                break;
            case "音乐":
                color = Color.parseColor("#FFF5E4");
                break;
            case "电影":
                color = Color.parseColor("#DDFAF5");
                break;
            case "美食":
                color = Color.parseColor("#FFFFF0EF");
                break;
            case "书籍":
                color = Color.parseColor("#F8F2FF");
                break;
            case "地方":
                color = Color.parseColor("#E7F1FF");
                break;
        }
        return color;
    }
}
