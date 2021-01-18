package com.smd.recorder.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.smd.recorder.bean.RecorderInfo;

import java.util.List;

@Dao
public interface RecorderInfoDao {
    @Query("SELECT * FROM "+RecorderInfo.RECORDER_TABLE_NAME)
    List<RecorderInfo> selectAll();

    @Query("SELECT * FROM "+RecorderInfo.RECORDER_TABLE_NAME +" WHERE year = :year AND month = :month AND day = :day")
    List<RecorderInfo> selectByDate(Integer year,Integer month,Integer day);

    @Query("DELETE FROM " + RecorderInfo.RECORDER_TABLE_NAME + " WHERE id = :id")
    int deleteByFaceId(Integer id);

    @Update
    int updateUsers(List<RecorderInfo> recorderInfoList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertUser(RecorderInfo recorderInfo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAllUser(List<RecorderInfo> recorderInfoList);
}
