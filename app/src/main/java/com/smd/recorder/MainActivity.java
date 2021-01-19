package com.smd.recorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageButton;

import com.smd.recorder.adapter.CalendarPagerAdapter;
import com.smd.recorder.bean.RecorderInfo;
import com.smd.recorder.util.DateUtil;

import java.util.Calendar;

public class MainActivity extends FragmentActivity {

    private static final int MY_PERMISSION_REQUEST_CODE = 10000;
    private static final String TAG = "MainActivity";
    private ViewPager vp_calendar; // 声明一个翻页视图对象
    private ImageButton button_plus;
    private ImageButton button_wave;
    private ImageButton settingButton;
    private ImageButton searchButton;
    private int mSelectedYear = 2000; // 当前选中的年份

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 从布局文件中获取名叫vp_calendar的翻页视图
        vp_calendar = findViewById(R.id.vp_calendar);
        searchButton = findViewById(R.id.SearchLogo);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        settingButton = findViewById(R.id.settingButton);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        button_plus = findViewById(R.id.plusIcon);
        button_wave = findViewById(R.id.waveButton);
        button_wave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, AnlysisActivity.class);
                startActivity(intent);
            }
        });
        button_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, InfoActivity.class);
                RecorderInfo recorderInfo = new RecorderInfo(Calendar.getInstance());
                recorderInfo.setMoodNum(3);
                intent.putExtra("date",recorderInfo);
                startActivity(intent);
            }
        });
        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.defaultBac);
        this.getWindow().setBackgroundDrawable(drawable);
        // 万年历默认显示当前年月的月历
        showCalendar(DateUtil.getNowYear(), DateUtil.getNowMonth());
        getPermission();
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


    public void getPermission(){
        boolean isAllGranted  = checkPermissionAllGranted( new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE

        });
        if (isAllGranted) {
            return;
        }
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE

        },MY_PERMISSION_REQUEST_CODE);
    }

    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;

            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                // 如果所有的权限都授予了, 则执行备份代码
//                doBackup();

            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                openAppDetails();
            }
        }
    }

    private void openAppDetails() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("需要访问 “录音” 和 “外部存储器”，请到 “应用信息 -> 权限” 中授予！");
        builder.setPositiveButton("去手动授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }
}