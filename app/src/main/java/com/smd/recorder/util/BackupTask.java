package com.smd.recorder.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class BackupTask extends AsyncTask<String, Void, Integer> {
    private static final String COMMAND_BACKUP = "backupDatabase";
    public static final String COMMAND_RESTORE = "restroeDatabase";
    private Context mContext;

    public BackupTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected Integer doInBackground(String... params) {
        // TODO Auto-generated method stub
        // 获得正在使用的数据库路径，我的是 sdcard 目录下的 /dlion/db_dlion.db
        // 默认路径是 /data/data/(包名)/databases/*.db
        File dbFile1 = mContext.getDatabasePath("recorder_demo");
        File dbFile2 = mContext.getDatabasePath("recorder_demo-shm");
        File dbFile3 = mContext.getDatabasePath("recorder_demo-wal");
        File exportDir = new File(mContext.getExternalFilesDir(null), "RecordDbBackup");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File backup1 = new File(exportDir, dbFile1.getName());
        File backup2 = new File(exportDir, dbFile2.getName());
        File backup3= new File(exportDir, dbFile3.getName());
        String command = params[0];
        if (command.equals(COMMAND_BACKUP)) {
            try {
                backup1.createNewFile();
                backup2.createNewFile();
                backup3.createNewFile();
                fileCopy(dbFile1, backup1);
                fileCopy(dbFile2, backup2);
                fileCopy(dbFile3, backup3);
                return Log.d("backup", "ok");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                return Log.d("backup", "fail");
            }
        } else if (command.equals(COMMAND_RESTORE)) {
            try {
                fileCopy(backup1, dbFile1);
                fileCopy(backup2, dbFile2);
                fileCopy(backup3, dbFile3);
                return Log.d("restore", "success");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                return Log.d("restore", "fail");
            }
        } else {
            return null;
        }
    }

    private void fileCopy(File dbFile, File backup) throws IOException {
        // TODO Auto-generated method stub
        FileChannel inChannel = new FileInputStream(dbFile).getChannel();
        FileChannel outChannel = new FileOutputStream(backup).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }
}
