package com.smd.recorder.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.smd.recorder.InfoActivity;
import com.smd.recorder.R;
import com.smd.recorder.adapter.CalendarGridAdapter;
import com.smd.recorder.bean.RecorderInfo;

import java.util.Calendar;
import java.util.Date;

public class CalendarFragment extends Fragment {
    private static final String TAG = "CalendarFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象
    private int mYear, mMonth; // 当前碎片所要展示的年份和月份
    private GridView gv_calendar; // 声明一个网格视图对象
    private TextView textYear;
    private TextView textMonth;

    public static CalendarFragment newInstance(int year, int month) {
        CalendarFragment fragment = new CalendarFragment(); // 创建该碎片的一个实例
        Bundle bundle = new Bundle(); // 创建一个新包裹
        bundle.putInt("year", year); // 往包裹存入年份
        bundle.putInt("month", month); // 往包裹存入月份
        fragment.setArguments(bundle); // 把包裹塞给碎片
        return fragment; // 返回碎片实例
    }

    // 创建碎片视图
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        if (getArguments() != null) { // 如果碎片携带有包裹，则打开包裹获取参数信息
            mMonth = getArguments().getInt("month", 1);
            mYear = getArguments().getInt("year", 2000);
        }
        // 根据布局文件fragment_calendar.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_calendar, container, false);
        // 从布局视图中获取名叫gv_calendar的网格视图
        gv_calendar = mView.findViewById(R.id.gv_calendar);
        textYear = mView.findViewById(R.id.textYear);
        textMonth = mView.findViewById(R.id.textMonth);
        return mView; // 返回该碎片的视图对象
    }

    @Override
    public void onResume() {
        super.onResume();
        // 构建一个月历的网格适配器
        CalendarGridAdapter adapter = new CalendarGridAdapter(mContext, mYear, mMonth, 1);
        // 给gv_calendar设置月历网格适配器
        gv_calendar.setAdapter(adapter);
        textYear.setText(mYear+"");
        textMonth.setText(mMonth+"月");
        gv_calendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                CalendarGridAdapter adapter= (CalendarGridAdapter) parent.getAdapter();
                Calendar cal=Calendar.getInstance();
                TextView tv_day = view.findViewById(R.id.tv_day);
                cal.set(Calendar.YEAR,adapter.getNowYear());
                cal.set(Calendar.MONTH,adapter.getNowMonth()-1);
                cal.set(Calendar.DAY_OF_MONTH,Integer.valueOf((String)tv_day.getText()));
                Intent intent = new Intent(getActivity(), InfoActivity.class);
                RecorderInfo recorderInfo = new RecorderInfo(cal);
                recorderInfo.setMoodNum(3);
                intent.putExtra("date",recorderInfo);
                startActivity(intent);
            }
        });
    }
}
