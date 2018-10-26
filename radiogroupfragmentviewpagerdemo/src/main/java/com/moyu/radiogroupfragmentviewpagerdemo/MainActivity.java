package com.moyu.radiogroupfragmentviewpagerdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private ViewPager view_pager;
    private RadioButton radio_01;
    private RadioButton radio_02;
    private RadioButton radio_03;
    private RadioButton radio_04;
    private RadioGroup radio_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        view_pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {//参数是fragment的管理员对象

            @Override
            public int getCount() {
                return 4;
            }
            //返回当前展示的fragment
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = null;

                switch (position) {
                    case 0://展示第一个fragment
                        fragment = new FragmentOne();
                        break;
                    case 1://展示第2个fragment
                        fragment = new FragmentTwo();
                        break;
                    case 2://展示第3个fragment
                        fragment = new FragmentThree();
                        break;
                    case 3://展示第4个fragment
                        fragment = new FragmentFour();
                        break;

                    default:
                        break;
                }
                return fragment;//返回
            }
        });

        //给group设置选中改变的监听事件
        radio_group.setOnCheckedChangeListener(this);

        //给viewPager设置监听
        view_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // 选中某一页的时候需要让group选中那一个button
                radio_group.check(radio_group.getChildAt(arg0).getId());

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private void initView() {
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        radio_01 = (RadioButton) findViewById(R.id.radio_01);
        radio_02 = (RadioButton) findViewById(R.id.radio_02);
        radio_03 = (RadioButton) findViewById(R.id.radio_03);
        radio_04 = (RadioButton) findViewById(R.id.radio_04);
        radio_group = (RadioGroup) findViewById(R.id.radio_group);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_01://需要viewpager去展示第一页
                //pager.setCurrentItem(0);
                //int item, boolean smoothScroll选中某一页的时候是否滑动过去,,,默认就是滑动
                view_pager.setCurrentItem(0, false);//就不会滑动了
                break;
            case R.id.radio_02:
                view_pager.setCurrentItem(1, false);
                break;
            case R.id.radio_03:
                view_pager.setCurrentItem(2, false);
                break;
            case R.id.radio_04:
                view_pager.setCurrentItem(3, false);
                break;

            default:
                break;
        }
    }
}
