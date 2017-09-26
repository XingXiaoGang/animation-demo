package com.example.xingxiaogang.animationdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xingxiaogang.animationdemo.drawable.AvatorDrawable;
import com.mobeta.android.dslv.DragSortListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingxiaogang on 2016/12/8.
 */

public class ListActivity extends Activity {
    public static Activity mInstance;

    private static final boolean DEBUG = true;
    private static final String TAG = "test.ListActivity";
    private Adapter mListAdapter;
    private DragSortListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);
        if (DEBUG) {
            Log.d(TAG, "onCreate: ");
        }
        mListView = (DragSortListView) findViewById(R.id.drag_list);

        mListAdapter = new Adapter();
        mListAdapter.setData(creatFakeData(100));
        mListView.setAdapter(mListAdapter);
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

    private class Adapter extends BaseAdapter implements DragSortListView.DragListener, DragSortListView.DropListener, View.OnClickListener {

        private SparseIntArray mListMapping = new SparseIntArray();
        private List<Bean> mData = new ArrayList<>();

        public void setData(List<Bean> data) {
            this.mData.clear();
            this.mData.addAll(data);
            resetMappings();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Bean getItem(int position) {
            return mData.get(mListMapping.get(position, position));
        }

        @Override
        public long getItemId(int position) {
            return mListMapping.get(position);
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
            convertView.setOnClickListener(this);
            holder.title.setTag("title");
            holder.title.setOnClickListener(this);
            return convertView;
        }

        @Override
        public void drag(int from, int to) {
            if (DEBUG) {
                Log.d(TAG, "drag: from=" + from + " to=" + to);
            }
        }

        @Override
        public void drop(int from, int to) {
            if (DEBUG) {
                Log.d(TAG, "drop: form=" + from + " to=" + to);
            }
            if (from != to) {
                int cursorFrom = mListMapping.get(from, from);

                if (from > to) {
                    for (int i = from; i > to; --i) {
                        mListMapping.put(i, mListMapping.get(i - 1, i - 1));
                    }
                } else {
                    for (int i = from; i < to; ++i) {
                        mListMapping.put(i, mListMapping.get(i + 1, i + 1));
                    }
                }
                mListMapping.put(to, cursorFrom);

                cleanMapping();
                notifyDataSetChanged();
            }
        }

        private void resetMappings() {
            mListMapping.clear();
        }


        private void cleanMapping() {
            ArrayList<Integer> toRemove = new ArrayList<Integer>();

            int size = mListMapping.size();
            for (int i = 0; i < size; ++i) {
                if (mListMapping.keyAt(i) == mListMapping.valueAt(i)) {
                    toRemove.add(mListMapping.keyAt(i));
                }
            }

            size = toRemove.size();
            for (int i = 0; i < size; ++i) {
                mListMapping.delete(toRemove.get(i));
            }
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Click:" + v.getTag(), Toast.LENGTH_LONG).show();
        }
    }

    private class Holder {
        private ImageView imageView;
        private TextView title;
        private TextView summery;
    }

}
