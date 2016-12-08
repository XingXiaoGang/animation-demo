package com.example.xingxiaogang.animationdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xingxiaogang.animationdemo.drawable.AvatorDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingxiaogang on 2016/12/8.
 */

public class ListActivity extends android.app.ListActivity {
    public static Activity mInstance;

    private static final boolean DEBUG = true;
    private static final String TAG = "test.ListActivity";
    private Adapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (DEBUG) {
            Log.d(TAG, "onCreate: ");
        }
        mListAdapter = new Adapter();
        mListAdapter.setData(creatFakeData(100));
        getListView().setAdapter(mListAdapter);
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

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        if (DEBUG) {
            Log.d(TAG, "onContentChanged: ");
        }
    }

    private List<Bean> creatFakeData(int num) {
        List<Bean> list = new ArrayList<>();
        String[] source = new String[]{"Jack", "Aaron", "Bunny", "Emily", "Charlotte", "Elaine", "Julie", "邢刚", "王", "颜"};
        for (int i = 0; i < num; i++) {
            Bean bean = new Bean();
            bean.title = source[i % source.length];
            bean.content = "南北极海冰面积降至历史最\n 消融掉一个‘印度’ " + i;
            list.add(bean);
        }
        return list;
    }

    private class Bean {
        String title;
        String content;
    }

    private class Adapter extends BaseAdapter {

        private List<Bean> mData = new ArrayList<>();

        public void setData(List<Bean> data) {
            this.mData.clear();
            this.mData.addAll(data);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Bean getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = convertView != null ? (Holder) convertView.getTag() : null;
            if (holder == null) {
                holder = new Holder();
                View item = View.inflate(ListActivity.this, R.layout.list_item_layout, null);
                holder.imageView = (ImageView) item.findViewById(R.id.item_img);
                holder.title = (TextView) item.findViewById(R.id.item_title);
                holder.summery = (TextView) item.findViewById(R.id.item_content);
                item.setTag(holder);
                convertView = item;
            }
            Bean bean = getItem(position);
            holder.imageView.setImageDrawable(new AvatorDrawable(bean.title));
            holder.summery.setText(bean.content);
            holder.title.setText(bean.title);
            return convertView;
        }
    }

    private class Holder {
        private ImageView imageView;
        private TextView title;
        private TextView summery;
    }

}
