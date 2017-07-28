package com.example.xingxiaogang.animationdemo.view.menu;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
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

    private static final boolean DEBUG = true;
    private static final String TAG = "ActionMenu";
    private List<IMenu> mSubComponent = new ArrayList<>();
    private int mState = STATE_CLOSED;


    public ActionMenu(Context context) {
        super(context);
        initView(context, null);
    }

    public ActionMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
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
                for (IMenu iMenu : mSubComponent) {
                    if (DEBUG) {
                        Log.d(TAG, "findComponent: " + iMenu.getDuration());
                    }
                }

                //时间最长的那个
                ((IMenu) mSubComponent.get(mSubComponent.size() - 1)).setListener(this);
            }
        }
    }

    @Override
    public boolean open() {
        if (mState == STATE_CLOSED) {
            mState = STATE_OPENING;
            for (IMenu iMenu : mSubComponent) {
                iMenu.open();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean close() {
        if (mState == STATE_OPENED) {
            mState = STATE_CLOSEING;
            for (IMenu iMenu : mSubComponent) {
                iMenu.close();
            }
            return true;
        }
        return false;
    }

    @Override
    public int getState() {
        return mSubComponent.size() == 0 ? STATE_CLOSED : mSubComponent.get(mSubComponent.size() - 1).getState();
    }

    @Override
    public boolean toggle() {
        switch (mState) {
            case STATE_OPENED: {
                close();
                return true;
            }
            case STATE_CLOSED: {
                open();
                return true;
            }
        }
        return false;
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
        if (mState == STATE_OPENING) {
            mState = STATE_OPENED;
        } else if (mState == STATE_CLOSEING) {
            mState = STATE_CLOSED;
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
