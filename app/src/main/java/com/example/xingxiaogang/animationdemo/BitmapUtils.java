package com.example.xingxiaogang.animationdemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by xingxiaogang on 2017/9/28.
 */

public class BitmapUtils {

    private static final String TAG = "BitmapUtils";


    public static Drawable normalDrawable(Drawable drawable) {
        if (drawable != null) {
            if (!(drawable instanceof BitmapDrawable)) {
                try {
                    Bitmap bitmap = Bitmap.createBitmap(56, 56, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    drawable.setBounds(0, 0, 56, 56);
                    drawable.draw(canvas);
                    return new BitmapDrawable(bitmap);
                } catch (Exception e) {
                }
            }
        }
        return drawable;
    }

    public static Drawable darkBitmapIfNeed(Drawable drawable, float percent) {
        if (drawable != null && drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            bitmap = darkBitmapIfNeed(bitmap, percent);
            if (bitmap != null) {
                return new BitmapDrawable(bitmap);
            }
        }
        return drawable;
    }

    public static Bitmap darkBitmapIfNeed(Bitmap bitmap, float percent) {
        //处理图片资源
        if (bitmap != null && !bitmap.isRecycled() && isAlphaBitmap(bitmap, percent)) {
            return renderBitmapWithColor(bitmap, Color.parseColor("#b8444444"));
        }
        return bitmap;
    }

    /**
     * 给位图叠加一层颜色
     **/
    public static Bitmap renderBitmapWithColor(Bitmap bitmap, int color) {
        Bitmap res = bitmap;
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(color, PorterDuff.Mode.SRC_IN);
        } catch (Exception e) {
            try {
                Bitmap newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                Canvas canvas = new Canvas(newBitmap);
                canvas.drawColor(color, PorterDuff.Mode.SRC_IN);
                res = newBitmap;
            } catch (Exception e2) {
                Log.w(TAG, "renderBitmapWithColor 叠加错误 2:" + e.getMessage());
            }
        }
        return res;
    }

    /**
     * 判断图片是否透明
     *
     * @param percent 透明信像素的比例
     **/
    public static boolean isAlphaBitmap(Bitmap bitmap, float percent) {
        int[][] points = calcPoints(bitmap.getWidth(), bitmap.getHeight());
        if (points.length > 0) {
            int count = points.length * points.length;
            int alphaCount = 0;
            int whiteCount = 0;
            for (int[] point : points) {
                int x = 0, y = 0;
                boolean newPointFlag = false;
                for (int i = 0; i < point.length; i++) {
                    if (i % 2 == 0) {
                        if (newPointFlag) {
                            newPointFlag = false;
                            int color = bitmap.getPixel(x, y);

                            if (Color.alpha(color) < 255) {
                                alphaCount++;
                            } else {
                                int r = Color.red(color);
                                int g = Color.green(color);
                                int b = Color.blue(color);
                                if (r > 75 && g > 75 && b > 75 && Math.abs(r - g) < 20 && Math.abs(r - b) < 20) {
                                    whiteCount++;
                                }
                            }
                        }
                        x = point[i];
                    } else {
                        y = point[i];
                        newPointFlag = true;
                    }
                }
            }
            boolean isAlpha = ((alphaCount + whiteCount) * 1.0f / count) >= percent;
            Log.d(TAG, "isAlphaBitmap: isAlphaBitmap：" + isAlpha + " ,percent=" + ((alphaCount + whiteCount) * 1.0f / count) + " alpha=" + alphaCount + " , white=" + whiteCount);
            return isAlpha;
        }
        return false;
    }

    /**
     * 在指定宽高内，形成一个平均分部的矩阵
     **/
    private static int[][] calcPoints(int width, int height) {
        if (width <= 0 || height <= 0) {
            return new int[0][0];
        }
        final int size = 10;// 一共取这些点
        int oneW = width / (size - 1);
        int oneH = height / (size - 1);
        int[][] res = new int[size - 2][(size - 2) * 2];//去掉边缘
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res[i].length; j++) {
                if (j % 2 == 0) {
                    res[i][j] = ((j) / 2 + 1) * oneW;//x
                } else {
                    res[i][j] = (i + 1) * oneH;//y
                }
            }
        }

      /*  StringBuilder sb = new StringBuilder();
        for (int[] re : res) {
            sb.append(Arrays.toString(re));
            sb.append("\n");
        }
        System.out.println("calcPoints: 要取的像素点：\n" + sb.toString());*/
        return res;
    }
}
