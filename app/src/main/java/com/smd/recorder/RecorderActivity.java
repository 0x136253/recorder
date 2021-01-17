package com.smd.recorder;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.smd.recorder.bean.RecorderInfo;
import com.smd.recorder.util.DateUtil;

import java.util.Calendar;

public class RecorderActivity extends Activity implements View.OnClickListener {
    private ImageButton backToInfoButton;
    private RecorderInfo recorderInfo;
    private ImageButton playButton;
    private ImageButton recordButton;
    private ImageButton reRecordButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);
        Intent intent = getIntent();
        recorderInfo = (RecorderInfo) intent.getSerializableExtra("date");
        backToInfoButton  = findViewById(R.id.backToInfoIcon);
        backToInfoButton.setOnClickListener(this);
        playButton  = findViewById(R.id.playButton);
        playButton.setOnClickListener(this);
        recordButton  = findViewById(R.id.recordButton);
        recordButton.setOnClickListener(this);
        reRecordButton  = findViewById(R.id.reRecordButton);
        reRecordButton.setOnClickListener(this);
        playButton.setEnabled(false);
        recordButton.setVisibility(View.VISIBLE);
        recordButton.setEnabled(true);
        reRecordButton.setVisibility(View.GONE);
        reRecordButton.setEnabled(false);
        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.recorderBac);
        this.getWindow().setBackgroundDrawable(drawable);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() ==R.id.backToInfoIcon){
            Intent intent=new Intent(RecorderActivity.this, InfoActivity.class);
            intent.putExtra("date",recorderInfo);
            startActivity(intent);
        }

        //TODO wait for complete
        if (v.getId() == R.id.recordButton){
            recordButton.setVisibility(View.GONE);
            recordButton.setEnabled(false);
            reRecordButton.setVisibility(View.VISIBLE);
            reRecordButton.setEnabled(true);
            //record
        }
        if (v.getId() == R.id.reRecordButton){
            reRecordButton.setVisibility(View.GONE);
            reRecordButton.setEnabled(false);
            recordButton.setVisibility(View.VISIBLE);
            recordButton.setEnabled(true);
            //record
        }
        if (v.getId() == R.id.playButton){
            //play()
        }
    }


}
