package com.example.xingxiaogang.animationdemo;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by xingxiaogang on 2016/6/2.
 */
public class SizeUtils {

    public static int dp2px(@NonNull Context context, float dp) {
        final float density = context.getResources().getDisplayMetrics().density;
        return (int) (density * dp + 0.5F);
    }

}
