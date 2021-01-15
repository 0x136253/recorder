package com.smd.recorder.bean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class RecorderInfo implements Serializable {
    private Integer year;
    private Integer month;
    private Integer day;
    private Integer week;
    private Integer moodNum = 1;
    private String moodTitle;
    private String Title;

    public RecorderInfo() {
    }

    public RecorderInfo(Calendar calendar){
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        week = calendar.get(Calendar.DAY_OF_WEEK);
    }

    public RecorderInfo(Integer year, Integer month, Integer day, Integer week, Integer moodNum, String moodTitle, String title) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.week = week;
        this.moodNum = moodNum;
        this.moodTitle = moodTitle;
        Title = title;
    }

    @Override
    public String toString() {
        return "RecorderInfo{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", week=" + week +
                ", moodNum=" + moodNum +
                ", moodTitle='" + moodTitle + '\'' +
                ", Title='" + Title + '\'' +
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
