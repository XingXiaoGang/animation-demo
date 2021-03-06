package com.example.xingxiaogang.animationdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.xingxiaogang.animationdemo.game.Director;

/**
 * Created by xingxiaogang on 2018/5/11.
 */

public class GameActivity extends Activity implements SurfaceHolder.Callback {

    private static final String TAG = "GameActivity";

    private SurfaceHolder mHolder;
    private SurfaceView mSurfaceView;
    private Director mGame;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mSurfaceView = (SurfaceView) findViewById(R.id.surface);
        mSurfaceView.getHolder().addCallback(this);
        mSurfaceView.setClickable(true);
        mSurfaceView.setFocusableInTouchMode(true);
        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        Log.d(TAG, "onTouch: " + event.getX() + "," + event.getY());
                        if (mGame != null) {
                            mGame.onTouch((int) event.getX(), (int) event.getY());
                        }
                        break;
                    }
                }
                return true;
            }
        });
        mHolder = mSurfaceView.getHolder();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated: ");
        mGame = new Director(mHolder);
        //放后台
        mGame.initAssets(GameActivity.this);
        mGame.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged: ");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGame != null) {
            mGame.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGame != null) {
            mGame.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGame != null) {
            mGame.destory();
        }
    }
}
