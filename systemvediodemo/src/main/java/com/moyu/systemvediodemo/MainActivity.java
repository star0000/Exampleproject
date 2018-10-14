package com.moyu.systemvediodemo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * 调用系统播放器播放视频，
 * 其实就是调用系统自带的应用，使用intent隐式跳转，打开系统播放视频的界面
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void play(View v){
        Intent intent = new Intent();
        //指定一个跳转的动作
        intent.setAction(Intent.ACTION_VIEW);
        //指定要展示的数据...uri统一资源定位...type 指定数据的mime类型..video/*所有视频的格式
        //如果播放的是本地sdcard的视频的话，
        // intent.setDataAndType(Uri.parse("Environment.getExternalStorageDirectory()"+"/miniony.mp4"),"video/*");
        intent.setDataAndType(Uri.parse("http://play.g3proxy.lecloud.com/vod/v2/MjUxLzE2LzgvbGV0di11dHMvMTQvdmVyXzAwXzIyLTExMDc2NDEzODctYXZjLTE5OTgxOS1hYWMtNDgwMDAtNTI2MTEwLTE3MDg3NjEzLWY1OGY2YzM1NjkwZTA2ZGFmYjg2MTVlYzc5MjEyZjU4LTE0OTg1NTc2ODY4MjMubXA0?b=259&mmsid=65565355&tm=1499247143&key=f0eadb4f30c404d49ff8ebad673d3742&platid=3&splatid=345&playid=0&tss=no&vtype=21&cvid=2026135183914&payff=0&pip=08cc52f8b09acd3eff8bf31688ddeced&format=0&sign=mb&dname=mobile&expect=1&tag=mobile&xformat=super"),"video/*");
        startActivity(intent);
    }

}
