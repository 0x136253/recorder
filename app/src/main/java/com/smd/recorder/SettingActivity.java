package com.smd.recorder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.smd.recorder.adapter.SettingListAdapter;
import com.smd.recorder.util.BackupTask;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends Activity {

    private ImageButton backToHomeButton;
    private ListView settingListView;
    private List<String> settingList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        backToHomeButton = findViewById(R.id.backToHomeButton);
        backToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        settingList = new ArrayList<>();
        settingList.add("数据备份");
        settingList.add("数据恢复");
        SettingListAdapter settingListAdapter = new SettingListAdapter(SettingActivity.this,R.layout.item_setting,settingList);
        settingListView = findViewById(R.id.settingListView);
        settingListView.setAdapter(settingListAdapter);
        // 为ListView注册一个监听器，当用户点击了ListView中的任何一个子项时，就会回调onItemClick()方法
        // 在这个方法中可以通过position参数判断出用户点击的是那一个子项
        settingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String settingName=settingList.get(position);
                if (settingName.equals("数据备份")){
                    new BackupTask(SettingActivity.this).execute("backupDatabase");
                }
                else if (settingName.equals("数据恢复")){
                    new BackupTask(SettingActivity.this).execute("restroeDatabase");
                }
                Toast.makeText(SettingActivity.this,settingName, Toast.LENGTH_SHORT).show();
                //TODO wait for 数据同步,导出，备份具体逻辑
            }
        });
    }
}