package com.example.xingxiaogang.animationdemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.xingxiaogang.animationdemo.view.ColorDotLoadingDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingxiaogang on 2017/1/24.
 */

public class ImageActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corner);


        ColorDotLoadingDrawable colorDotLoadingDrawable = new ColorDotLoadingDrawable(SizeUtils.dp2px(this, 2));
        ((ImageView) findViewById(R.id.loading_icon)).setImageDrawable(colorDotLoadingDrawable);

        List<Drawable> drawables = new ArrayList<>();
        drawables.add(getResources().getDrawable(R.mipmap.star_pink));
        drawables.add(getResources().getDrawable(R.mipmap.star_pink_aplha));
        drawables.add(getResources().getDrawable(R.mipmap.ic_launcher));
        drawables.add(getResources().getDrawable(R.drawable.icon_retry));

        long time = System.currentTimeMillis();
        for (Drawable drawable : drawables) {
            if (drawable instanceof BitmapDrawable) {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                BitmapUtils.isAlphaBitmap(bitmap, 0.5f);
            }
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - time));

        final ImageView imageView = ((ImageView) findViewById(R.id.gray_icon));
        imageView.setImageResource(R.drawable.icon_retry);
        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_retry).copy(Bitmap.Config.ARGB_8888, true);
                bitmap = BitmapUtils.darkBitmapIfNeed(bitmap, 0.5f);
                imageView.setImageBitmap(bitmap);
            }
        }, 2000);
    }
}
