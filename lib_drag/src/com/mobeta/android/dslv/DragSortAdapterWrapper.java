package com.mobeta.android.dslv;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

/**
 * Created by xingxiaogang on 2017/7/4.
 */
class DragSortAdapterWrapper extends BaseAdapter {
    private DragSortListView dragSortListView;
    private ListAdapter mAdapter;

    DragSortAdapterWrapper(DragSortListView dragSortListView, ListAdapter adapter) {
        super();
        this.dragSortListView = dragSortListView;
        mAdapter = adapter;

        mAdapter.registerDataSetObserver(new DataSetObserver() {
            public void onChanged() {
                notifyDataSetChanged();
            }

            public void onInvalidated() {
                notifyDataSetInvalidated();
            }
        });
    }

    ListAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public long getItemId(int position) {
        return mAdapter.getItemId(position);
    }

    @Override
    public Object getItem(int position) {
        return mAdapter.getItem(position);
    }

    @Override
    public int getCount() {
        return mAdapter.getCount();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return mAdapter.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(int position) {
        return mAdapter.isEnabled(position);
    }

    @Override
    public int getItemViewType(int position) {
        return mAdapter.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return mAdapter.getViewTypeCount();
    }

    @Override
    public boolean hasStableIds() {
        return mAdapter.hasStableIds();
    }

    @Override
    public boolean isEmpty() {
        return mAdapter.isEmpty();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DragSortItemWrapperView wrapper;
        View child;
        if (convertView != null) {
            wrapper = (DragSortItemWrapperView) convertView;
            View oldChild = wrapper.getChildAt(0);
            child = mAdapter.getView(position, oldChild, dragSortListView);
            if (child != oldChild) {
                if (oldChild != null) {
                    wrapper.removeViewAt(0);
                }
                wrapper.addView(child);
            }
        } else {
            child = mAdapter.getView(position, null, dragSortListView);
            wrapper = new DragSortItemWrapperView(dragSortListView.getContext());
            wrapper.setLayoutParams(new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            wrapper.addView(child);
        }

        dragSortListView.adjustItem(position + dragSortListView.getHeaderViewsCount(), wrapper, true);

        return wrapper;
    }
}
