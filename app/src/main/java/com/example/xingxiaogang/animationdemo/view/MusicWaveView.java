package com.example.xingxiaogang.animationdemo.view;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.xingxiaogang.animationdemo.view.wave.SoundFile;

/**
 * Created by xinggang on 2018/8/29 下午12:10.
 * <p>
 * email: xxg841076938@gmail.com
 * use: 显示音乐的波形图
 **/
public class MusicWaveView extends View {

    private SoundFile soundFile;
    private Paint mPaint;

    public MusicWaveView(Context context) {
        super(context);
        initView(context, null);
    }

    public MusicWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public MusicWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {

    }

    public void prepareWave(final String mp3Path, long duration) {

    }

    private void drawWaves() {

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void updateIndicator(int mills) {

    }
}