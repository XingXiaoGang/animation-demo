package com.example.xingxiaogang.animationdemo;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by xingxiaogang on 2017/7/18.
 */

public class ActionMenu extends LinearLayout implements IMenu, Animator.AnimatorListener {

    private List<IMenu> mSubComponent = new ArrayList<>();

    public ActionMenu(Context context) {
        super(context);
        initView(context, null);
    }

    public ActionMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        findComponent(this);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        findComponent(this);
    }

    @Override
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
        findComponent(this);
    }

    //查找menu单元
    private void findComponent(ViewGroup group) {
        mSubComponent.clear();
        if (group.getChildCount() > 0) {
            //只找一层
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                if (child instanceof IMenu) {
                    mSubComponent.add((IMenu) child);
                }
            }
            //排序
            if (mSubComponent.size() > 0) {
                Collections.sort(mSubComponent, new Comparator<IMenu>() {
                    @Override
                    public int compare(IMenu lhs, IMenu rhs) {
                        return (int) (lhs.getDuration() - rhs.getDuration());
                    }
                });
                //时间最长的那个
                ((IMenu) mSubComponent.get(mSubComponent.size() - 1)).setListener(this);
            }
        }
    }

    @Override
    public boolean open() {
        for (IMenu iMenu : mSubComponent) {
            iMenu.open();
        }
        return true;
    }

    @Override
    public boolean close() {
        for (IMenu iMenu : mSubComponent) {
            iMenu.close();
        }
        return true;
    }

    @Override
    public int getState() {
        return mSubComponent.size() == 0 ? STATE_CLOSED : mSubComponent.get(mSubComponent.size() - 1).getState();
    }

    @Override
    public boolean toggle() {
        for (IMenu iMenu : mSubComponent) {
            iMenu.toggle();
        }
        return true;
    }

    @Override
    public void setListener(Animator.AnimatorListener listener) {

    }

    @Override
    public long getDuration() {
        return mSubComponent.size() == 0 ? 0 : mSubComponent.get(mSubComponent.size() - 1).getDuration();
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
