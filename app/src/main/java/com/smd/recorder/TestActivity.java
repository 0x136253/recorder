package com.smd.recorder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.smd.recorder.util.RecordUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestActivity extends Activity {
    private static final String TAG = "TestActivity";
    private static final int PLAY_END = 1;
    private static final int RECORD_END = 2;
    private TextView mRecordTimeTv;
    private Button mStartRecordBtn;
    private Button mPlayBtn;
    private RecordUtil recordUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mRecordTimeTv = findViewById(R.id.mRecordTimeTv);
        mStartRecordBtn = findViewById(R.id.mStartRecordBtn);
        mPlayBtn = findViewById(R.id.mPlayBtn);
    }

    @Override
    public void onStart() {
        super.onStart();
        recordUtil = new RecordUtil();
        try {
            RecorderHandler recorderHandler = new RecorderHandler();
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
        mPlayBtn.setEnabled(false);
        setListener();
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
        mStartRecordBtn.setText("开始录音");
        mPlayBtn.setEnabled(true);
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

    //响应message,根据message做相应的操作
    public class RecorderHandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PLAY_END:
                    recordUtil.stopPlayBack();
                    mPlayBtn.setText("播放录音");
                    mStartRecordBtn.setEnabled(true);
                    recordUtil.setmIsPlay(false);
                    break;
                case RECORD_END:
                    mRecordTimeTv.setText("");
                    recordUtil.getmAudioRecord().stop();
                    mStartRecordBtn.setText("开始录音");
                    mPlayBtn.setEnabled(true);
                    recordUtil.setmIsRecording(false);
                    mTimer.cancel();
                    break;
                default:
                    break;
            }
        }
    }

    //millisInFuture:倒计时的总时长,countDownInterval：每次的间隔时间  单位都是毫秒
    private CountDownTimer mTimer = new CountDownTimer(30 * 1000, 1000) {

        //这个是每次间隔指定时间的回调，millisUntilFinished：剩余的时间，单位毫秒
        //显示时间到界面
        @Override
        public void onTick(long millisUntilFinished) {
            long time = (30 * 1000 - millisUntilFinished) / 1000;
            mRecordTimeTv.setText(String.format("00:%s", time < 10 ? "0" + time : time));
        }

        //这个是倒计时结束的回调
        @Override
        public void onFinish() {
            recordUtil.getmHandler().sendEmptyMessage(RECORD_END);
        }
    };


    private void setListener() {
        // 开始录音
        mStartRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recordUtil.getmAudioRecord() == null || recordUtil.getmAudioRecord().getState() == AudioRecord.STATE_UNINITIALIZED) {
                    return;
                }
                //mIsRecording表示是否正在录音
                if (!recordUtil.ismIsRecording()) {
                    recordUtil.setmIsRecording(true);
                    mStartRecordBtn.setText("结束录音");
                    mPlayBtn.setEnabled(false);
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

        });

        // 播放录音
        mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mIsPlay表示是否正在播放
                //正在播放
                if (recordUtil.ismIsPlay()) {
                    recordUtil.stopPlayBack();
                    mPlayBtn.setText("播放录音");
                    mStartRecordBtn.setEnabled(true);
                    recordUtil.setmIsPlay(false);
                } else {
                    recordUtil.setmIsPlay(true);
                    mPlayBtn.setText("停止播放");
                    mStartRecordBtn.setEnabled(false);
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            recordUtil.playBackNow(recordUtil.getmFileName());
                        }
                    }.start();
                }

            }
        });

    }
}