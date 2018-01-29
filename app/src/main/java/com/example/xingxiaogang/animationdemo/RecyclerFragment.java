package com.example.xingxiaogang.animationdemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xingxiaogang.animationdemo.drawable.AvatorDrawable;

import java.util.ArrayList;
import java.util.List;

import static com.example.xingxiaogang.animationdemo.RecylerActivity.TAG;

@SuppressLint("ValidFragment")
public class RecyclerFragment extends Fragment {
    Adapter mListAdapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(RecylerActivity.mInstance, R.layout.fragment_recycler, null);

        RecyclerView mRecycler = (RecyclerView) view.findViewById(R.id.recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(RecylerActivity.mInstance));

        mListAdapter = new Adapter();
        mListAdapter.setData(creatFakeData(100));
        mRecycler.setAdapter(mListAdapter);
        Log.i(TAG, "onCreateView: ");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private List<Bean> creatFakeData(int num) {
        Log.i(TAG, "creatFakeData: ");
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

    private class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Bean> mData = new ArrayList<>();


        public void setData(List<Bean> data) {
            Log.i(TAG, "setData: ");
            this.mData.clear();
            this.mData.addAll(data);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View item = View.inflate(parent.getContext(), R.layout.list_item_layout, null);
            Log.i(TAG, "onCreateViewHolder: ");
            return new ItemHolder(item);
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Log.i(TAG, "onBindViewHolder: ");
            ((ItemHolder) holder).bind(mData.get(position));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            Log.i(TAG, "getItemCount:" + mData.size());
            return mData.size();
        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView title;
        private TextView summery;

        public ItemHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_img);
            title = (TextView) itemView.findViewById(R.id.item_title);
            summery = (TextView) itemView.findViewById(R.id.item_content);
        }

        public void bind(Bean bean) {
            imageView.setImageDrawable(new AvatorDrawable(bean.title));
            summery.setText(bean.content);
            title.setText(bean.title);
        }
    }
}