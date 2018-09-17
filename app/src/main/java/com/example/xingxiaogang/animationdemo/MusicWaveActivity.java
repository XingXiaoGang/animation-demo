package com.example.xingxiaogang.animationdemo;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.xingxiaogang.animationdemo.view.MusicWaveView;
import com.example.xingxiaogang.animationdemo.view.wave.SoundFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by xinggang on 2018/8/29 下午4:24.
 * <p>
 * email: xxg841076938@gmail.com
 * use:
 **/
public class MusicWaveActivity extends Activity implements SoundFile.ProgressListener {


    private MusicWaveView musicWaveView;
    private Subscription subscription;
    private MediaPlayer mediaPlayer;
    private String mPath;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_wave);

        musicWaveView = (MusicWaveView) findViewById(R.id.music_wave);
    }

    private boolean copyResToSdCard() {
        String sdCardPath = Environment.getExternalStorageDirectory().getPath();
        final File musicFile = new File(sdCardPath, "music_one_millon_maybe.mp3");
        if (musicFile.exists()) {
            mPath = musicFile.getPath();
            return true;
        }

        dialog = new ProgressDialog(this);
        dialog.setTitle("正在准备音乐");
        dialog.show();

//        subscription = Observable.just(getResources().openRawResource(R.raw.music)).doOnNext(new Action1<InputStream>() {
//            @Override
//            public void call(InputStream inputStream) {
//            }
//        }).map(new Func1<InputStream, String>() {
//            @Override
//            public String call(InputStream inputStream) {
//                try {
//                    return copyRawToSdCard(inputStream, musicFile);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//                dialog.dismiss();
//                Toast.makeText(getApplicationContext(), "导出音乐完成", Toast.LENGTH_LONG).show();
//                mPath = s;
//                findViewById(R.id.start).performClick();
//            }
//        });
        return false;
    }

    private String copyRawToSdCard(InputStream inputStream, File outFile) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        FileOutputStream fileOutputStream = new FileOutputStream(outFile);
        while ((len = inputStream.read(buffer)) > 0) {
            fileOutputStream.write(buffer, 0, len);
        }
        inputStream.close();
        fileOutputStream.flush();
        fileOutputStream.close();
        return outFile.getPath();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) {
            subscription.unsubscribe();
        }
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start: {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MusicWaveActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
                    return;
                }
                if (copyResToSdCard()) {
                    playMusic(mPath);
                    showWave();
                }
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 200: {
                findViewById(R.id.start).performClick();
                break;
            }
        }
    }

    private void showWave() {
        subscription = Schedulers.io().createWorker().schedule(new Action0() {
            @Override
            public void call() {
                try {
                    SoundFile soundFile = SoundFile.create(mPath, MusicWaveActivity.this);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SoundFile.InvalidInputException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void playMusic(String path) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean reportProgress(double fractionComplete) {
        Log.d("GANG+", "reportProgress: " + fractionComplete);

        return true;
    }

    @Override
    public boolean complete() {
        Log.d("GANG+", "complete: 解析完成了");

        return false;
    }
}
