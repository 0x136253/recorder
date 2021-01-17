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
    //Mic audio record function
    private static final int PLAY_END = 1;
    private static final int RECORD_END = 2;
    private TextView mRecordTimeTv;
    private Button mStartRecordBtn;
    private Button mPlayBtn;
    private String mFileName = null;// 存储录音文件的路径

    private AudioRecord mAudioRecord;
    private volatile boolean mIsRecording = false;
    private AudioFormat.Builder mAudioFormat;
    private AudioAttributes mPlaybackAttributes;
    private boolean mIsPlay;
    private AudioTrack mAudio = null;
    private RecorderHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initAudioRecordBtn();
    }

    private void initAudioRecordBtn() {
        mRecordTimeTv = findViewById(R.id.mRecordTimeTv);
        mStartRecordBtn = findViewById(R.id.mStartRecordBtn);
        mPlayBtn = findViewById(R.id.mPlayBtn);
    }

    @Override
    public void onStart() {
        super.onStart();
        mHandler = new RecorderHandler();

        Log.d(TAG, "start --");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAudioRecord == null) {
            setBtnAudioRecord();
        }
        mPlayBtn.setEnabled(false);
        setListener();
        Log.d(TAG, "onResume --");
    }

    private void setBtnAudioRecord() {
        // 获得缓冲区字节大小 采样频率，声道，文件位数
        int bufferSize = AudioRecord.getMinBufferSize(64000, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
        // 设置音频格式
        final AudioFormat audioFormat = new AudioFormat.Builder()
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setSampleRate(64000)
                .setChannelMask(AudioFormat.CHANNEL_IN_STEREO)
                .build();
        // 初始化 AudioRecord 其中MediaRecorder.AudioSource.MIC为音频源
        mAudioRecord = new AudioRecord.Builder()
                .setAudioSource(MediaRecorder.AudioSource.MIC)
                .setBufferSizeInBytes(bufferSize)
                .setAudioFormat(audioFormat)
                .build();
        Log.d(TAG, "record bufferSize=(" + bufferSize + ")");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "stop --");
        if (mTimer != null) {
            mTimer.cancel();
        }
        if (mAudioRecord != null && mAudioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
            mAudioRecord.stop();
            mAudioRecord.release();
            mAudioRecord = null;
        }
        mStartRecordBtn.setText("开始录音");
        mPlayBtn.setEnabled(true);
        mIsRecording = false;
        mRecordTimeTv.setText("");
        if (mAudio != null) {
            if (mAudio.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
                mAudio.stop();
            }
            mAudio.release();
            mAudio = null;
        }
    }

    //响应message,根据message做相应的操作
    private class RecorderHandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PLAY_END:
                    stopPlayBack();
                    mPlayBtn.setText("播放录音");
                    mStartRecordBtn.setEnabled(true);
                    mIsPlay = false;
                    break;
                case RECORD_END:
                    mRecordTimeTv.setText("");
                    mAudioRecord.stop();
                    mStartRecordBtn.setText("开始录音");
                    mPlayBtn.setEnabled(true);
                    mIsRecording = false;
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
            mHandler.sendEmptyMessage(RECORD_END);
        }
    };



    public String getSDPath() {
        String path = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {

        }
        return path;
    }

    private void setListener() {
        // 开始录音
        mStartRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAudioRecord == null || mAudioRecord.getState() == AudioRecord.STATE_UNINITIALIZED) {
                    return;
                }
                //mIsRecording表示是否正在录音
                if (!mIsRecording) {
                    mIsRecording = true;
                    mStartRecordBtn.setText("结束录音");
                    mPlayBtn.setEnabled(false);
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            mTimer.start();
                            writeAudioDataToFile();
                        }
                    }.start();
                } else {
                    mHandler.sendEmptyMessage(RECORD_END);
                }
            }

        });

        // 播放录音
        mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //play();
                // return;
                //   String name = pcm2();

                //mIsPlay表示是否正在播放
                //正在播放
                if (mIsPlay) {
                    stopPlayBack();
                    mPlayBtn.setText("播放录音");
                    mStartRecordBtn.setEnabled(true);
                    mIsPlay = false;
                } else {
                    mIsPlay = true;
                    mPlayBtn.setText("停止播放");
                    mStartRecordBtn.setEnabled(false);
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            playBackNow();
                        }
                    }.start();
                }

            }
        });

    }


    public void stopPlayBack() {
        if (mAudio != null && mIsPlay && mAudio.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
            mAudio.stop();
        }
    }

    public void playBackNow() {
        int length = 0;
        Log.d(TAG, " before mAudio track");

        mPlaybackAttributes = new AudioAttributes.Builder()
                // .setUsage(AudioAttributes.USAGE_ASSISTANT)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                // .setUsage(AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE)
                .build();
        mAudioFormat = new AudioFormat.Builder();
//        mAudioFormat.setChannelMask(AudioFormat.CHANNEL_OUT_STEREO);
        mAudioFormat.setChannelMask(AudioFormat.CHANNEL_OUT_STEREO); //CHANNEL_IN_STEREO
        mAudioFormat.setSampleRate(32000);
        mAudioFormat.setEncoding(AudioFormat.ENCODING_PCM_16BIT);

        //  int bufferSize = AudioRecord.getMinBufferSize(64000, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);

        if (mAudio == null) {
            mAudio = new AudioTrack(mPlaybackAttributes, // attributes
                    // AudioManager.STREAM_TTS,
                    //48000, // sample rate
                    mAudioFormat.build(),
                    AudioTrack.getMinBufferSize(32000, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT) * 2,
                    AudioTrack.MODE_STREAM,
                    AudioManager.AUDIO_SESSION_ID_GENERATE
            );
        }
        Log.d(TAG, " after mAudio track");
        Log.d(TAG, "bufrer size:" + AudioTrack.getMinBufferSize(32000, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT));

        FileInputStream fis = null;
        try {
            Log.d(TAG, " get stream in");
//            fis = new FileInputStream(getSDPath()+"/20190525_035557voice.pcm");
            Log.d(TAG, "PALY - > " + mFileName);
            fis = new FileInputStream(mFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            length = fis.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "wav file readlen_1:" + length);
        byte buffer[] = new byte[length];

        try {
            while (fis.available() > 0) {
                length = fis.read(buffer);
                if (length == AudioTrack.ERROR_INVALID_OPERATION || length == AudioTrack.ERROR_BAD_VALUE) {
                    continue;
                }
                if (length != 0 && length != -1) {
                    mAudio.play();
                    mAudio.write(buffer, 0, length);
                }
                Log.d(TAG, "play length --> " + length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mHandler.sendEmptyMessage(PLAY_END);

        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * write sound file
     */
    private void writeAudioDataToFile() {
        int bytesRecord = 0;
        String filePath = getExternalCacheDir() + "/" + new SimpleDateFormat("yyyyMMdd_hhmmss").format(new Date()) + "voice.pcm";
//        filePath = getCacheDir() + "/" + new SimpleDateFormat("yyyyMMdd_hhmmss").format(new Date()) + "voice.pcm";
        mFileName = filePath;
        File file = new File(filePath);
        if (file.exists()) {

        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int bufferSize = AudioRecord.getMinBufferSize(64000, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
        //int bufferSize = AudioRecord.getMinBufferSize(128000, AudioFormat.CHANNEL_IN_STEREO,  AudioFormat.ENCODING_PCM_16BIT);
        Log.d(TAG, "record bufferSize=(" + bufferSize + ")");
        byte[] tempBuffer = new byte[bufferSize];
        OutputStream os = null;

        try {
            os = new FileOutputStream(filePath);
            Log.d(TAG, "after recorder create file");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //以上为关于录音文件的配置
        //开始录音
        mAudioRecord.startRecording();

        while (mIsRecording) {
            // gets the voice output from microphone to byte format
            bytesRecord = mAudioRecord.read(tempBuffer, 0, bufferSize);
            Log.d(TAG, "record byte=(" + bytesRecord + ")");
            if (bytesRecord == AudioRecord.ERROR_INVALID_OPERATION || bytesRecord == AudioRecord.ERROR_BAD_VALUE) {
                Log.d(TAG, " Audio record read err");
            }
            if (bytesRecord != 0 && bytesRecord != -1) {
                try {
                    os.write(splitStereoPcm(tempBuffer), 0, bytesRecord / 2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, "$$$record byte=0");
            }
        }

        Log.d(TAG, "after writer record");
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private byte[] splitStereoPcm(byte[] data) {
        int monoLength = data.length / 2;
        byte[] pcmData = new byte[monoLength];
        for (int i = 0; i < monoLength; i++) {
            if (i % 2 == 0) {
                System.arraycopy(data, i * 2, pcmData, i, 2);
            } else {
                System.arraycopy(data, i * 2, pcmData, i - 1, 2);
            }
        }
        return pcmData;
    }
}