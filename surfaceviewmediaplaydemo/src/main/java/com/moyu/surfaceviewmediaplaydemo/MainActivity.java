package com.moyu.surfaceviewmediaplaydemo;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnPlay;
    private SurfaceView surfaceView;
    private SeekBar seekbar;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    private int duration;
    private int currentPosition;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                int c = (Integer)msg.obj;
                seekbar.setProgress(c*100/duration);
            }

        }
    };
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //创建MediaPlayer
        mediaPlayer = new MediaPlayer();
        //获取surfaceView的holder
        surfaceHolder = surfaceView.getHolder();

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        //设置尺寸
        surfaceHolder.setFixedSize(width,height-100);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // surfaceView创建完成的时候
                //将视频设置展示到surfaceHolder....这个需要在surfaceView完全创建完成的时候设置
                mediaPlayer.setDisplay(surfaceHolder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                // surfaceView的时改变的时候，例如屏幕发生变化
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // surfaceView销毁的时候
            }
        });
        btnPlay.setOnClickListener(this);


    }

    private void initView() {
        btnPlay = (Button) findViewById(R.id.btn_play);
        //在界面完全加载完成的时候才去创建
        surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
    }

    @Override
    public void onClick(View v) {
        try {
            mediaPlayer.reset();
            //设置播放的资源
            mediaPlayer.setDataSource(MainActivity.this, Uri.parse("http://play.g3proxy.lecloud.com/vod/v2/MjUxLzE2LzgvbGV0di11dHMvMTQvdmVyXzAwXzIyLTExMDc2NDEzODctYXZjLTE5OTgxOS1hYWMtNDgwMDAtNTI2MTEwLTE3MDg3NjEzLWY1OGY2YzM1NjkwZTA2ZGFmYjg2MTVlYzc5MjEyZjU4LTE0OTg1NTc2ODY4MjMubXA0?b=259&mmsid=65565355&tm=1499247143&key=f0eadb4f30c404d49ff8ebad673d3742&platid=3&splatid=345&playid=0&tss=no&vtype=21&cvid=2026135183914&payff=0&pip=08cc52f8b09acd3eff8bf31688ddeced&format=0&sign=mb&dname=mobile&expect=1&tag=mobile&xformat=super"));
           //准备
            mediaPlayer.prepareAsync();
            //准备完成
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    //播放
                    mediaPlayer.start();
                    //获取视频的总长度
                    duration = mediaPlayer.getDuration();
                    timer = new Timer();
                    TimerTask task = new TimerTask() {

                        @Override
                        public void run() {
                            //获取视频当前进度
                            currentPosition = mediaPlayer.getCurrentPosition();
                            //发送出去
                            Message msg = Message.obtain();
                            msg.what = 0;
                            msg.obj = currentPosition;
                            handler.sendMessage(msg);
                        }
                    };
                    timer.schedule(task,0,1000);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            timer.cancel();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
