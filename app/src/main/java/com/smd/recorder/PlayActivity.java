package com.smd.recorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.smd.recorder.bean.RecorderInfo;
import com.smd.recorder.util.RecordUtil;

public class PlayActivity extends Activity {
    private static final String TAG = "InfoActivity";
    private TextView textDay;
    private TextView textMonth;
    private TextView textWeek;
    private TextView textDesc;
    private TextView titleText;
    private TextView remindText;
    private ImageButton backButton;
    private ImageButton playButton;
    private RecorderInfo recorderInfo;
    private ImageView moodImage;
    private String[] weekDay = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
    private RecordUtil recordUtil;

    private static final int PLAY_END = 1;
    private static final int RECORD_END = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Intent intent = getIntent();
        recorderInfo = (RecorderInfo) intent.getSerializableExtra("date");
        textDay = findViewById(R.id.textDayInPlay);
        textMonth = findViewById(R.id.textMonthInPlay);
        textWeek = findViewById(R.id.textWeekInPlay);
        textDesc = findViewById(R.id.textDescInPlay);
        moodImage = findViewById(R.id.moodImageInPlay);
        titleText = findViewById(R.id.textTitleInPlay);
        remindText = findViewById(R.id.remindText);
        backButton = findViewById(R.id.backToSearchButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PlayActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        playButton = findViewById(R.id.playButtonInPlay);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recordUtil.ismIsPlay()) {
                    recordUtil.stopPlayBack();
                    //TODO 播放停止;
                    remindText.setText("播放停止");
                    recordUtil.setmIsPlay(false);
                } else {
                    recordUtil.setmIsPlay(true);
                    //TODO 开始播放
                    remindText.setText("正在播放");
                    playButton.setEnabled(false);
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            recordUtil.playBackNow(recorderInfo.getPath());
                        }
                    }.start();
                }
            }
        });
        if (recorderInfo!=null) {
            recordUtil = new RecordUtil();
            try {
                PlayActivity.RecorderHandler recorderHandler = new PlayActivity.RecorderHandler();
                recordUtil.setmHandler(recorderHandler);
            }catch (Exception e){
                e.printStackTrace();
            }
            initUI();
        }
    }

    private void initUI() {
        textDay.setText(recorderInfo.getDay().toString());
        textMonth.setText(recorderInfo.getMonth()+1+"月");
        textWeek.setText(weekDay[recorderInfo.getWeek()-1]);
        titleText.setText("标题:"+recorderInfo.getMoodTitle());
        Drawable drawable;
        Drawable face;
        String moodName;
        switch (recorderInfo.getMoodNum()){
            case 1:
                drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.emotion1);
                face= ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_face1);
                moodName = (String) this.getResources().getText(R.string.emotionTitle1);
                break;
            case 2:
                drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.emotion2);
                face= ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_face2);
                moodName = (String) this.getResources().getText(R.string.emotionTitle2);
                break;
            case 3:
                drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.emotion3);
                face= ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_face3);
                moodName = (String) this.getResources().getText(R.string.emotionTitle3);
                break;
            case 4:
                drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.emotion4);
                face= ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_face4);
                moodName = (String) this.getResources().getText(R.string.emotionTitle4);
                break;
            case 5:
                drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.emotion5);
                face= ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_face5);
                moodName = (String) this.getResources().getText(R.string.emotionTitle5);
                break;
            default:
                drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.emotion1);
                face= ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_face1);
                moodName = (String) this.getResources().getText(R.string.emotionTitle1);
        }
        this.getWindow().setBackgroundDrawable(drawable);
        moodImage.setImageDrawable(face);
        textDesc.setText(moodName);
    }

    //响应message,根据message做相应的操作
    public class RecorderHandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PLAY_END:
                    recordUtil.stopPlayBack();
                    remindText.setText("播放结束");
                    Log.d(TAG,"播放结束--------------");
                    playButton.setEnabled(true);
                    recordUtil.setmIsPlay(false);
                    break;
                default:
                    break;
            }
        }
    }
}