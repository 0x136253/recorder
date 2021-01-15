package com.smd.recorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.smd.recorder.adapter.MoodPagerAdapter;
import com.smd.recorder.bean.RecorderInfo;

import java.util.Calendar;

public class InfoActivity extends FragmentActivity implements ViewPager.OnPageChangeListener,View.OnClickListener{
    private static final String TAG = "InfoActivity";
    private TextView textDay;
    private TextView textMonth;
    private TextView textWeek;
    private TextView textDesc;
    private EditText editTextTitle;
    private Integer moodNum;
    private ImageButton backButton;
    private ImageButton forwardButton;
    private RecorderInfo recorderInfo;
    private ViewPager vp_info;
    private String[] weekDay = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        textDay = findViewById(R.id.textDay);
        textMonth = findViewById(R.id.textMonth);
        textWeek = findViewById(R.id.textWeek);
        textDesc = findViewById(R.id.textDesc);
        editTextTitle = findViewById(R.id.editTextTitle);
        vp_info = findViewById(R.id.vp_info);
        backButton = findViewById(R.id.backIcon);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(InfoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        forwardButton = findViewById(R.id.forwardIcon);
        forwardButton.setOnClickListener(this);
        Intent intent = getIntent();
        recorderInfo = (RecorderInfo) intent.getSerializableExtra("date");
        if (recorderInfo!=null){
            recorderInfo.setMoodTitle((String) this.getResources().getText(R.string.emotionTitle1));
            textDay.setText(recorderInfo.getDay().toString());
            textMonth.setText((recorderInfo.getMonth()+1)+"月");
            textWeek.setText(weekDay[recorderInfo.getWeek()-1]);
            setBacAndMoodName(recorderInfo.getMoodNum());
            MoodPagerAdapter moodPagerAdapter = new MoodPagerAdapter(getSupportFragmentManager());
            vp_info.setAdapter(moodPagerAdapter);
            vp_info.setCurrentItem(recorderInfo.getMoodNum()-1);
            vp_info.addOnPageChangeListener(this);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d("InfoActivity","postiton is "+position);
        recorderInfo.setMoodNum(position+1);
        setBacAndMoodName(recorderInfo.getMoodNum());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId()!=R.id.forwardIcon){
            return;
        }
        try {
            recorderInfo.setTitle(String.valueOf(editTextTitle.getText()));
            Log.d(TAG,recorderInfo.toString());
            Intent intent=new Intent(InfoActivity.this, RecorderActivity.class);
            intent.putExtra("date",recorderInfo);
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    private void setBacAndMoodName(int position){
        Drawable drawable;
        String moodName;
        switch (position){
            case 1:
                drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.emotion1);
                moodName = (String) this.getResources().getText(R.string.emotionTitle1);
                break;
            case 2:
                drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.emotion2);
                moodName = (String) this.getResources().getText(R.string.emotionTitle2);
                break;
            case 3:
                drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.emotion3);
                moodName = (String) this.getResources().getText(R.string.emotionTitle3);
                break;
            case 4:
                drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.emotion4);
                moodName = (String) this.getResources().getText(R.string.emotionTitle4);
                break;
            case 5:
                drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.emotion5);
                moodName = (String) this.getResources().getText(R.string.emotionTitle5);
                break;
            default:
                drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.emotion1);
                moodName = (String) this.getResources().getText(R.string.emotionTitle1);
        }
        this.getWindow().setBackgroundDrawable(drawable);
        textDesc.setText(moodName);
        recorderInfo.setMoodTitle(moodName);
    }
}