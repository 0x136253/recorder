package com.smd.recorder.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.smd.recorder.R;
import com.smd.recorder.bean.CalendarTransfer;
import com.smd.recorder.calendar.LunarCalendar;
import com.smd.recorder.calendar.SpecialCalendar;
import com.smd.recorder.util.DateUtil;

import java.util.ArrayList;

public class CalendarGridAdapter extends BaseAdapter {
    private static final String TAG = "CalendarGridAdapter";
    private Context mContext; // 声明一个上下文对象
    private boolean isLeapyear = false; // 是否为闰年
    private int daysOfMonth = 0; // 某月的天数
    private int dayOfWeek = 0; // 具体某一天是星期几
    private int lastDaysOfMonth = 0; // 上一个月的总天数
    private String[] dayNumber = new String[49]; // 一个gridview中的日期存入此数组中
    private ArrayList<CalendarTransfer> transArray = new ArrayList<CalendarTransfer>();
    private static String weekTitle[] = {"一", "二", "三", "四", "五", "六", "日"};
    private LunarCalendar lc;
    private int currentDay = -1; // 用于标记当天
    private Integer nowYear;
    private Integer nowMonth;
    private Integer nowDay;

    public Integer getNowYear() {
        return nowYear;
    }

    public void setNowYear(Integer nowYear) {
        this.nowYear = nowYear;
    }

    public Integer getNowMonth() {
        return nowMonth;
    }

    public void setNowMonth(Integer nowMonth) {
        this.nowMonth = nowMonth;
    }

    public Integer getNowDay() {
        return nowDay;
    }

    public void setNowDay(Integer nowDay) {
        this.nowDay = nowDay;
    }

    public CalendarGridAdapter(Context context, int year, int month, int day) {
        mContext = context;
        nowYear =year;nowMonth=month;nowDay=day;
        lc = new LunarCalendar();
        Log.d(TAG, "currentYear=" + year + ", currentMonth=" + month + ", currentDay=" + day);
        // 得到某年的某月的天数且这月的第一天是星期几
        isLeapyear = SpecialCalendar.isLeapYear(year); // 是否为闰年
        daysOfMonth = SpecialCalendar.getDaysOfMonth(isLeapyear, month); // 某月的总天数
        dayOfWeek = SpecialCalendar.getWeekdayOfMonth(year, month); // 某月第一天为星期几
        lastDaysOfMonth = SpecialCalendar.getDaysOfMonth(isLeapyear, month - 1);  //上一个月的总天数
        if (month ==1){
            lastDaysOfMonth = 31;
        }
        Log.d(TAG, isLeapyear + " ======  " + daysOfMonth + "  ============  " + dayOfWeek + "  =========   " + lastDaysOfMonth);
        getWeekDays(year, month);
    }

    // 将一个月中的每一天的值添加入数组dayNumber中
    private void getWeekDays(int year, int month) {
        int nextMonthDay = 1;
        String lunarDay = "";
        Log.d(TAG, "begin getWeekDays");
        for (int i = 0; i < dayNumber.length; i++) {
            CalendarTransfer trans = new CalendarTransfer();
            int weekday = (i - 7) % 7 + 1;
            if (i < 7) {
                dayNumber[i] = weekTitle[i] + " ";
            } else if (i < dayOfWeek + 7 - 1) { // 前一个月
                int temp = lastDaysOfMonth - dayOfWeek + 1 - 7 + 1;
                trans = lc.getSubDate(trans, year, month - 1, temp + i, weekday, false);
                dayNumber[i] = (temp + i)+"";
            } else if (i < daysOfMonth + dayOfWeek + 7 - 1) { // 本月
                int day = i - dayOfWeek + 1 - 7 + 1;
                trans = lc.getSubDate(trans, year, month, day, weekday, false);
                dayNumber[i] = day + "";
                // 对于当前月才去标记当前日期
                if (year == DateUtil.getNowYear() && month == DateUtil.getNowMonth() && day == DateUtil.getNowDay()) {
                    currentDay = i;
                }
            } else { // 下一个月
                int next_month = month + 1;
                int next_year = year;
                if (next_month >= 13) {
                    next_month = 1;
                    next_year++;
                }
                trans = lc.getSubDate(trans, next_year, next_month, nextMonthDay, weekday, false);
                dayNumber[i] = nextMonthDay + "";
                nextMonthDay++;
            }
            transArray.add(trans);
        }
//        //把日期数据打印到日志中
//        String abc = "";
//        for (String aDay : dayNumber) {
//            abc = abc + aDay + ":";
//        }
//        Log.d(TAG, abc);
    }

    @Override
    public int getCount() {
        return dayNumber.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            // 根据布局文件item_calendar.xml生成转换视图对象
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_calendar, null);
            holder.tv_day = convertView.findViewById(R.id.tv_day);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_day.setText(dayNumber[position]);
        holder.tv_day.setTextColor(Color.GRAY);
        if (position < 7) {
            // 设置周一到周日的标题
            holder.tv_day.setTextColor(Color.BLACK);
            Drawable drawable = ContextCompat.getDrawable(mContext,R.drawable.defaultBac);
            holder.tv_day.setBackground(drawable);
        } else if (currentDay == position) {
            holder.tv_day.setBackgroundColor(Color.GREEN); // 设置当天的背景
        } else {
            Drawable drawable = ContextCompat.getDrawable(mContext,R.drawable.defaultBac);
            holder.tv_day.setBackground(drawable); // 设置其他日期的背景
        }
        if (position < daysOfMonth + dayOfWeek + 7 - 1 && position >= dayOfWeek + 7 - 1) {
            holder.tv_day.setTextColor(Color.BLACK); // 当月字体设黑
        }
        return convertView;
    }

    public CalendarTransfer getCalendarList(int pos) {
        return transArray.get(pos);
    }

    public final class ViewHolder {
        public TextView tv_day;
    }
}
