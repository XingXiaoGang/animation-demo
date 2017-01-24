package com.apusapps.tools.highlight.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by zhy on 15/10/8.
 */
public class ViewUtils {
    private static final String FRAGMENT_CON = "NoSaveStateFrameLayout";

    public static Rect getLocationInView(View parent, View child) {
        if (child == null || parent == null) {
            throw new IllegalArgumentException("parent and child can not be null .");
        }

        View decorView = null;
        Context context = child.getContext();
        if (context instanceof Activity) {
            decorView = ((Activity) context).getWindow().getDecorView();
        }

        Rect result = new Rect();
        View tmp = child;
        if (child == parent) {
            child.getHitRect(result);
            return result;
        }
        int[] location = new int[2];
        child.getLocationInWindow(location);
        int[] locations = new int[2];
        child.getLocationOnScreen(locations);
        while (tmp != decorView && tmp != parent) {
            if (!tmp.getClass().equals(FRAGMENT_CON)) {
                result.left += tmp.getLeft();
                result.top += tmp.getTop();
            }
            tmp = (View) tmp.getParent();
        }
        result.right = result.left + child.getMeasuredWidth();
        result.bottom = result.top + child.getMeasuredHeight();
        return result;
    }
}
