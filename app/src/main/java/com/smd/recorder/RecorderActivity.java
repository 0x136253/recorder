package com.smd.recorder;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;

import com.smd.recorder.bean.RecorderInfo;
import com.smd.recorder.database.RoomDemoDatabase;
import com.smd.recorder.util.DateUtil;
import com.smd.recorder.util.RecordUtil;

import java.util.Calendar;
import java.util.List;

public class RecorderActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "RecorderActivity";
    private TextView mRecordTimeTv;
    private ImageButton backToInfoButton;
    private ImageButton completeButton;
    private RecorderInfo recorderInfo;
    private ImageButton playButton;
    private ImageButton recordButton;
//    private ImageButton reRecordButton;
    private RecordUtil recordUtil;
    private static final int PLAY_END = 1;
    private static final int RECORD_END = 2;
    private RoomDemoDatabase roomDemoDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);
        Intent intent = getIntent();
        recorderInfo = (RecorderInfo) intent.getSerializableExtra("date");
        mRecordTimeTv = findViewById(R.id.recordTimeTv);
        backToInfoButton  = findViewById(R.id.backToInfoIcon);
        backToInfoButton.setOnClickListener(this);
        playButton  = findViewById(R.id.playButton);
        playButton.setOnClickListener(this);
        recordButton  = findViewById(R.id.recordButton);
        recordButton.setOnClickListener(this);
        playButton.setEnabled(true);
        recordButton.setVisibility(View.VISIBLE);
        recordButton.setEnabled(true);
        completeButton = findViewById(R.id.completeIcon);
        completeButton.setOnClickListener(this);
        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.recorderBac);
        this.getWindow().setBackgroundDrawable(drawable);
        initDatabase();
    }


    public void initDatabase(){
        roomDemoDatabase = Room.databaseBuilder(this, RoomDemoDatabase.class, RoomDemoDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        recordUtil = new RecordUtil();
        try {
            RecorderActivity.RecorderHandler recorderHandler = new RecorderActivity.RecorderHandler();
            recordUtil.setmHandler(recorderHandler);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d(TAG, "start --");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (recordUtil.getmAudioRecord() == null) {
            recordUtil.setBtnAudioRecord();
        }
        playButton.setEnabled(false);
//        setListener();
        Log.d(TAG, "onResume --");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "stop --");
        if (mTimer != null) {
            mTimer.cancel();
        }
        if (recordUtil.getmAudioRecord() != null && recordUtil.getmAudioRecord().getState() == AudioRecord.STATE_INITIALIZED) {
            recordUtil.getmAudioRecord().stop();
            recordUtil.getmAudioRecord().release();
            recordUtil.setmAudioRecord(null);
        }
//        recordButton.setText("开始录音");
        playButton.setEnabled(true);
        recordUtil.setmIsRecording(false);
        mRecordTimeTv.setText("");
        if (recordUtil.getmAudio() != null) {
            if (recordUtil.getmAudio().getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
                recordUtil.getmAudio().stop();
            }
            recordUtil.getmAudio().release();
            recordUtil.setmAudio(null);
        }
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
            startRecord();
        }
        if (v.getId() == R.id.playButton){
            startPlay();
        }
        if (v.getId()==R.id.completeIcon){
            insertIntoDataBase();
        }
    }


    public void insertIntoDataBase(){
        List<RecorderInfo> recorderInfoList = roomDemoDatabase.recorderInfoDao().selectByDate(recorderInfo.getYear(),recorderInfo.getMonth(),recorderInfo.getDay());
        if (recorderInfoList.size()!=0){
            for (int i = 0;i<recorderInfoList.size();i++){
                Log.d(TAG,"重复:"+recorderInfoList.get(i).toString());
                roomDemoDatabase.recorderInfoDao().deleteByFaceId(recorderInfoList.get(i).getId());
            }
        }
        recorderInfo.setPath(recordUtil.getmFileName());
        roomDemoDatabase.recorderInfoDao().insertUser(recorderInfo);
        List<RecorderInfo> testList = roomDemoDatabase.recorderInfoDao().selectAll();
        for (int i=0;i<testList.size();i++){
            Log.d(TAG,"Database have "+testList.get(i).toString());
        }
    }

    public void startRecord(){
        if (recordUtil.getmAudioRecord() == null || recordUtil.getmAudioRecord().getState() == AudioRecord.STATE_UNINITIALIZED) {
            return;
        }
        //mIsRecording表示是否正在录音
        if (!recordUtil.ismIsRecording()) {
            recordUtil.setmIsRecording(true);
            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_stop);
            recordButton.setBackground(drawable);
//            recordButton.setText("结束录音");
            playButton.setEnabled(false);
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    mTimer.start();
                    recordUtil.writeAudioDataToFile(getExternalCacheDir()+"");
                }
            }.start();
        } else {
            recordUtil.getmHandler().sendEmptyMessage(RECORD_END);
        }
    }

    public void startPlay(){
        //mIsPlay表示是否正在播放
        //正在播放
        if (recordUtil.ismIsPlay()) {
            recordUtil.stopPlayBack();
//            mPlayBtn.setText("播放录音");
            //TODO 播放停止;
            mRecordTimeTv.setText("播放停止");
            recordButton.setEnabled(true);
            recordUtil.setmIsPlay(false);
        } else {
            recordUtil.setmIsPlay(true);
            //TODO 开始播放
            mRecordTimeTv.setText("正在播放");
            playButton.setEnabled(false);
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    recordUtil.playBackNow(recordUtil.getmFileName());
                }
            }.start();
        }

    }


    //响应message,根据message做相应的操作
    public class RecorderHandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PLAY_END:
                    recordUtil.stopPlayBack();
                    mRecordTimeTv.setText("播放结束");
                    Log.d(TAG,"播放结束--------------");
//                    mPlayBtn.setText("播放录音");
                    recordButton.setEnabled(true);
                    recordUtil.setmIsPlay(false);
                    break;
                case RECORD_END:
                    mRecordTimeTv.setText("");
                    recordUtil.getmAudioRecord().stop();
//                    mStartRecordBtn.setText("开始录音");
                    Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_record);
                    recordButton.setBackground(drawable);
                    playButton.setEnabled(true);
                    recordUtil.setmIsRecording(false);
                    mTimer.cancel();
                    break;
                default:
                    break;
            }
        }
    }

    //millisInFuture:倒计时的总时长,countDownInterval：每次的间隔时间  单位都是毫秒
    private CountDownTimer mTimer = new CountDownTimer(600 * 1000, 1000) {

        //这个是每次间隔指定时间的回调，millisUntilFinished：剩余的时间，单位毫秒
        //显示时间到界面
        @Override
        public void onTick(long millisUntilFinished) {
            long time = (600 * 1000 - millisUntilFinished) / 1000;
            long min = time / 60;
            time = time%60;
            mRecordTimeTv.setText(String.format("%s:%s", min < 10 ? "0" + min : min,time < 10 ? "0" + time : time));
        }

        //这个是倒计时结束的回调
        @Override
        public void onFinish() {
            recordUtil.getmHandler().sendEmptyMessage(RECORD_END);
        }
    };

}
