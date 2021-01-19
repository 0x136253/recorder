package com.smd.recorder.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;


@Entity(tableName = RecorderInfo.RECORDER_TABLE_NAME)
public class RecorderInfo implements Serializable {
    public static final String RECORDER_TABLE_NAME = "recorderInfo" ;
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private Integer year;
    private Integer month;
    private Integer day;
    private Integer week;
    private Integer moodNum = 1;
    private String moodTitle;
    private String Title;
    private String path;
    private Integer length;

    public RecorderInfo() {
    }

    public RecorderInfo(Calendar calendar){
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        week = calendar.get(Calendar.DAY_OF_WEEK);
    }

    public RecorderInfo(Integer id, Integer year, Integer month, Integer day, Integer week, Integer moodNum, String moodTitle, String title, String path, Integer length) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.week = week;
        this.moodNum = moodNum;
        this.moodTitle = moodTitle;
        Title = title;
        this.path = path;
        this.length = length;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RecorderInfo{" +
                "id=" + id +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", week=" + week +
                ", moodNum=" + moodNum +
                ", moodTitle='" + moodTitle + '\'' +
                ", Title='" + Title + '\'' +
                ", path='" + path + '\'' +
                ", length=" + length +
                '}';
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public Integer getMoodNum() {
        return moodNum;
    }

    public void setMoodNum(Integer moodNum) {
        this.moodNum = moodNum;
    }

    public String getMoodTitle() {
        return moodTitle;
    }

    public void setMoodTitle(String moodTitle) {
        this.moodTitle = moodTitle;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
