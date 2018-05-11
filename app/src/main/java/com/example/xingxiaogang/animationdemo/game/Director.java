package com.example.xingxiaogang.animationdemo.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

import com.example.xingxiaogang.animationdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingxiaogang on 2018/5/11.
 */

public class Director implements Handler.Callback {


    private static final String TAG = "GameActivity";

    private boolean isAssetsReady;
    private MainScene mScene;
    private BigChicken mBigChicken;
    private final List<SmallChicken> mSmallChickens = new ArrayList<>();

    private boolean isStart;
    private boolean isPause;
    private SurfaceHolder mHolder;

    private Handler handler;

    public Director(SurfaceHolder holder) {
        this.mHolder = holder;
        this.handler = new Handler(Looper.getMainLooper(), this);
    }

    /**
     * 后台加载
     **/
    public void initAssets(Context context) {
        mScene = new MainScene(Color.parseColor("#2DB5FD"));
        mBigChicken = new BigChicken(context);
        isAssetsReady = true;
    }

    /**
     * 主循环
     **/
    public void loop() {
        if (!isAssetsReady) {
            return;
        }

        Log.d(TAG, " ---game loop--- ");
        logic();
        draw();
        if (isStart && !isPause) {
            handler.sendEmptyMessageDelayed(R.id.game_loop, 30);
        }
    }

    public void start() {
        if (!isAssetsReady) {
            return;
        }
        isStart = true;
        loop();
    }

    public void onPause() {
        isPause = true;
    }

    public void resume() {
        isPause = false;
        if (isStart) {
            loop();
        }
    }

    public void destory() {
        isPause = true;
        isStart = false;
    }

    private void draw() {
        Canvas canvas = mHolder.lockCanvas();
        //背景
        if (mScene != null) {
            mScene.draw(canvas);
        }
        //精灵
        if (mBigChicken != null) {
            mBigChicken.draw(canvas);
        }
        for (SmallChicken chicken : mSmallChickens) {
            chicken.draw(canvas);
        }
        mHolder.unlockCanvasAndPost(canvas);
    }

    private void logic() {
        if (mBigChicken != null) {
            mBigChicken.logic();
        }
        synchronized (mSmallChickens) {
            for (SmallChicken chicken : mSmallChickens) {
                chicken.logic();
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.game_loop: {
                loop();
                break;
            }
        }
        return false;
    }
}
