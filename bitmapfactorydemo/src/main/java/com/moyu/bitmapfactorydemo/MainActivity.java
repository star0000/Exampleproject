package com.moyu.bitmapfactorydemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;


/**
 * 想要解决图片OOM的问题图片的二次采样
 * 1.得到图片的宽高信息
 * 2.得到屏幕的宽高信息
 * 3.两两相除得到采样率
 * 4.采样率设置给BitmapFactory,的新特图片Bitmap对象,设置给控件显示
 * 好处:1.避免了加载大图出现的OOM问题
 * 2.可以使图片根据用户手机的性能,进行不同效果的展示,好的手机通过这种方法加载的图片就变得好看;不好的手机加载的图片,不会出现OOM,同时显示出效果
 * 也就是每一次通过这种方法加载的图片都和用户手机性能刚好匹配.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        //点击事件
        btn.setOnClickListener(this);
    }

    private void init() {
        btn = (Button) findViewById(R.id.btn);
        img = (ImageView) findViewById(R.id.img);
    }


    /**
     * 点击加载一张大图片,采用二次采样的方案解决
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn:
                try {
                    //1.得到图片的宽高信息
                    // 建立一个图片信息类对象,传入要获取图片信息的文件,filename:String类型
                    ExifInterface exifInterface = new ExifInterface("mnt/sdcard/big.jpg");
                    /**
                     * 通过ExifInterface对象,调用getAttributeInt,获取图片属性,返回值int
                     * ExifInterface.TAG_IMAGE_WIDTH你要获取的信息,defaultValue:当没有获取图片的信息,默认指定的数据.
                     */
                    int width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0);
                    int height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0);

                    //2.获取屏幕的宽高信息
                    // 建立一个窗口管理类对象,通过静态方法getSystemService,调用系统底层获取窗口对象
                    WindowManager windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
                    int screenWidth = windowManager.getDefaultDisplay().getWidth();
                    int screenHeight = windowManager.getDefaultDisplay().getHeight();

                    //3.通过图片的宽和高以及屏幕的宽和高去得到一个采样率
                    int resultWidth = width / screenWidth;
                    int resultHeight = height / screenHeight;
                    //设置一个采样率的参数对象
                    int scale = 0;

                    if(resultWidth >= resultHeight && resultWidth > 1){
                        scale = resultWidth;
                    }else if(resultHeight >= resultWidth && resultHeight > 1){
                        scale = resultHeight;
                    }

                    //4.加载一个图片到内存中,显示到ImageView上
                    BitmapFactory.Options options = new BitmapFactory.Options();//创建一个参数对象
                    options.inSampleSize = scale;//设置采样率,这个值直接影响图片的大小和显示的效果
                    Bitmap bitmap = BitmapFactory.decodeFile("mnt/sdcard/big.jpg", options);
                    img.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
