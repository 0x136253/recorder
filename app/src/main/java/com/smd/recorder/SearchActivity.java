package com.smd.recorder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.smd.recorder.adapter.RecordListAdapter;
import com.smd.recorder.bean.RecorderInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SearchActivity extends Activity {

    private ImageButton searchBackToHomeButton;
    private ImageButton confirmToSearchButton;
    private EditText editSearchBar;
    private Button seeAllButton;
    private ListView recordListView;
    private List<RecorderInfo> recorderInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        editSearchBar = findViewById(R.id.editSearchBar);
        searchBackToHomeButton = findViewById(R.id.SearchBackToHomeButton);
        searchBackToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        seeAllButton = findViewById(R.id.seeAllButton);
        seeAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO wait for seeall
                Toast.makeText(SearchActivity.this,"see all", Toast.LENGTH_SHORT).show();
            }
        });
        confirmToSearchButton = findViewById(R.id.confirmToSearchButton);
        confirmToSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO wait for 具体搜索逻辑
                Toast.makeText(SearchActivity.this,"搜索成功",Toast.LENGTH_SHORT).show();
            }
        });
        recorderInfoList = new ArrayList<>();
        recorderInfoList.add(new RecorderInfo(Calendar.getInstance()));
        recorderInfoList.get(0).setMoodNum(3);
        RecordListAdapter recordListAdapter = new RecordListAdapter(SearchActivity.this,R.layout.item_record,recorderInfoList);
        recordListView = findViewById(R.id.recordList);
        recordListView.setAdapter(recordListAdapter);
        recordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecorderInfo recorderInfo=recorderInfoList.get(position);
                Toast.makeText(SearchActivity.this,recorderInfo.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}