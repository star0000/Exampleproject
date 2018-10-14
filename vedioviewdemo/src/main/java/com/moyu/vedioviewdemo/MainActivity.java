package com.moyu.vedioviewdemo;


import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import android.view.View.OnClickListener;


public class MainActivity extends AppCompatActivity {

    private Button btnPlay;
    private VideoView videoView;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //设置视频的路径
        videoView.setVideoURI(Uri.parse("http://play.g3proxy.lecloud.com/vod/v2/MjUxLzE2LzgvbGV0di11dHMvMTQvdmVyXzAwXzIyLTExMDc2NDEzODctYXZjLTE5OTgxOS1hYWMtNDgwMDAtNTI2MTEwLTE3MDg3NjEzLWY1OGY2YzM1NjkwZTA2ZGFmYjg2MTVlYzc5MjEyZjU4LTE0OTg1NTc2ODY4MjMubXA0?b=259&mmsid=65565355&tm=1499247143&key=f0eadb4f30c404d49ff8ebad673d3742&platid=3&splatid=345&playid=0&tss=no&vtype=21&cvid=2026135183914&payff=0&pip=08cc52f8b09acd3eff8bf31688ddeced&format=0&sign=mb&dname=mobile&expect=1&tag=mobile&xformat=super"));
        //创建控制器对象
        mediaController = new MediaController(this);
        //相互绑定，给videoview设置控制器
        videoView.setMediaController(mediaController);
        //mediaController也要绑定上videoView
        mediaController.setMediaPlayer(videoView);//参数是控制器要控制的对象
        //设置下面的监听会显示上一个，下一个的按键，默认不显示
        mediaController.setPrevNextListeners(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"下一个",Toast.LENGTH_SHORT).show();
            }
        }, new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"上一个",Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initView() {
        btnPlay = (Button) findViewById(R.id.btn_play);
        videoView = (VideoView) findViewById(R.id.videoview);
        btnPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.start();
            }
        });
    }


}
