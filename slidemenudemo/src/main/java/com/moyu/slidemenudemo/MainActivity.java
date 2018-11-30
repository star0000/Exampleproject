package com.moyu.slidemenudemo;

import android.animation.FloatEvaluator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FloatEvaluator mFloatEvaluator;


    private ImageView mIvHeadMenuPic;
    private ListView mMenuListview;
    private LinearLayout mMenuLinearlayout;
    private ImageView mIvHeadMainPic;
    private ListView mMainListview;
    private LinearLayout mMainLinearlayout;
    private SlideMenu mSlideMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        mFloatEvaluator = new FloatEvaluator();
        mIvHeadMenuPic = (ImageView) findViewById(R.id.iv_headMenuPic);
        mMenuListview = (ListView) findViewById(R.id.menu_listview);
        mMenuLinearlayout = (LinearLayout) findViewById(R.id.menu_linearlayout);
        mIvHeadMainPic = (ImageView) findViewById(R.id.iv_headMainPic);
        mMainListview = (ListView) findViewById(R.id.main_listview);
        mMainLinearlayout = (LinearLayout) findViewById(R.id.main_linearlayout);
        mSlideMenu = (SlideMenu) findViewById(R.id.slideMenu);
    }

    private void initData() {
        mMenuListview.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, Constant.SETTINGS) {
            @NonNull
            @Override
            // 修改布局属性
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(Color.parseColor("#FF3E96"));
                textView.setTextSize(18);
                textView.setSingleLine();
                return textView;
            }
        });
        mMenuListview.setDivider(null);
        mMenuListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, Constant.SETTINGS[i], Toast.LENGTH_SHORT).show();
            }
        });

        List<Map<String, Object>> data = new ArrayList<>();
        String[] from = {"pic", "contact"};
        int[] to = {R.id.iv_pic, R.id.tv_contact};
        mMainListview.setAdapter(new SimpleAdapter(MainActivity.this, data, R.layout.mainitem, from, to));

        for (int i = 0; i < Constant.CONSTACTS.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("pic", R.mipmap.ic_qzone);
            map.put("contact", Constant.CONSTACTS[i]);
            data.add(map);
        }

        mMainListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, Constant.CONSTACTS[i], Toast.LENGTH_SHORT).show();
            }
        });

        mSlideMenu.setOnSlideListener(new SlideMenu.OnSlideListener() {
            @Override
            public void onOpen() {
                Toast.makeText(MainActivity.this, "onOpen", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClose() {
                Toast.makeText(MainActivity.this, "onClose", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDraging(float fraction) {
                Log.i(TAG, "fraction:" + fraction);
                // 沿着y轴旋转
                mIvHeadMainPic.setRotationY(mFloatEvaluator.evaluate(fraction, 0, 720));
            }
        });

        mIvHeadMenuPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "我是menuView头像", Toast.LENGTH_SHORT).show();
            }
        });

        mIvHeadMainPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "我是mainView头像", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

