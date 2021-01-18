package com.smd.recorder.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.smd.recorder.bean.RecorderInfo;
import com.smd.recorder.database.dao.RecorderInfoDao;

@Database(entities =  {
        RecorderInfo.class
},
        version = 1, exportSchema = true)
public abstract  class RoomDemoDatabase extends RoomDatabase {

    public abstract RecorderInfoDao recorderInfoDao();

    public static final String DATABASE_NAME = "recorder_demo";

    private static RoomDemoDatabase sInstance;

    public static RoomDemoDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (RoomDemoDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context);
                }
            }
        }
        return sInstance;
    }


    private static RoomDemoDatabase buildDatabase(final Context appContext) {
        return Room.databaseBuilder(appContext, RoomDemoDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries()
//                .openHelperFactory(new SafeHelperFactory("123456".toCharArray()))
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);

                    }

                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        super.onOpen(db);
                    }

                })
                .build();
    }
}
