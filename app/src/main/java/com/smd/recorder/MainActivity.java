package com.smd.recorder;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.smd.recorder.adapter.CalendarPagerAdapter;
import com.smd.recorder.bean.RecorderInfo;
import com.smd.recorder.util.DateUtil;

import java.util.Calendar;

public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";
    private ViewPager vp_calendar; // 声明一个翻页视图对象
    private ImageButton button_plus;
    private int mSelectedYear = 2000; // 当前选中的年份

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 从布局文件中获取名叫vp_calendar的翻页视图
        vp_calendar = findViewById(R.id.vp_calendar);
        button_plus = findViewById(R.id.plusIcon);
        button_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, InfoActivity.class);
                RecorderInfo recorderInfo = new RecorderInfo(Calendar.getInstance());
                recorderInfo.setMoodNum(1);
                intent.putExtra("date",recorderInfo);
                startActivity(intent);
            }
        });
        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.defaultBac);
        this.getWindow().setBackgroundDrawable(drawable);
        // 万年历默认显示当前年月的月历
        showCalendar(DateUtil.getNowYear(), DateUtil.getNowMonth());
    }

    // 显示指定年月的万年历
    private void showCalendar(int year, int month) {
        // 如果指定年份不是上次选中的年份，则需重新构建该年份的年历
        if (year != mSelectedYear) {
            // 构建一个指定年份的年历翻页适配器
            CalendarPagerAdapter adapter = new CalendarPagerAdapter(getSupportFragmentManager(), year);
            // 给vp_calendar设置年历翻页适配器
            vp_calendar.setAdapter(adapter);
            mSelectedYear = year;
        }
        // 设置vp_calendar默认显示指定月份的月历页
        vp_calendar.setCurrentItem(month - 1);
    }


}