package com.smd.recorder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.smd.recorder.bean.RecorderInfo;
import com.smd.recorder.util.DateUtil;

import java.util.Calendar;

public class RecorderActivity extends Activity implements View.OnClickListener {

    private ImageButton backToInfoButton;
    private RecorderInfo recorderInfo;
    private ImageButton playButton;
    private ImageButton replayButton;
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
        replayButton  = findViewById(R.id.replayButton);
        replayButton.setOnClickListener(this);
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
        if (v.getId() == R.id.playButton){
            playButton.setVisibility(View.GONE);
            replayButton.setVisibility(View.VISIBLE);
        }
        if (v.getId() == R.id.replayButton){
            replayButton.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
        }
    }
}
