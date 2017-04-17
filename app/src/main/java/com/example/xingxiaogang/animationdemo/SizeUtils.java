package com.example.xingxiaogang.animationdemo;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

/**
 * Created by xingxiaogang on 2016/6/2.
 */
public class SizeUtils {

    public static int dp2px(@NonNull Context context, float dp) {
        final float density = context.getResources().getDisplayMetrics().density;
        return (int) (density * dp + 0.5F);
    }

    public static int dp2px(int dp) {
        final float density = Resources.getSystem().getDisplayMetrics().density;

        return (int) (density * dp + 0.5F);
    }

    public static boolean hasNavBar (Resources resources)
    {
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && resources.getBoolean(id);
    }

    public static int getScreenHeight() {
        final DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return metrics.heightPixels;
    }

    public static int getScreenWidth() {
        final DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return metrics.widthPixels;
    }

}
