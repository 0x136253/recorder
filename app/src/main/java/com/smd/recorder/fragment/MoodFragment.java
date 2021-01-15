package com.smd.recorder.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.smd.recorder.R;
import com.smd.recorder.adapter.CalendarGridAdapter;

public class MoodFragment extends Fragment {
    private static final String TAG = "MoodFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象
    private Integer moodNum;
    private ImageView moodView; // 声明一个网格视图对象

    public static MoodFragment newInstance(int num) {
        MoodFragment fragment = new MoodFragment(); // 创建该碎片的一个实例
        Bundle bundle = new Bundle(); // 创建一个新包裹
        bundle.putInt("moodNum", num); // 往包裹存入位置
        fragment.setArguments(bundle); // 把包裹塞给碎片
        return fragment; // 返回碎片实例
    }

    // 创建碎片视图
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        if (getArguments() != null) { // 如果碎片携带有包裹，则打开包裹获取参数信息
            moodNum = getArguments().getInt("moodNum", 1);
        }
        // 根据布局文件fragment_calendar.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_info, container, false);
        // 从布局视图中获取名叫gv_calendar的网格视图
        moodView = mView.findViewById(R.id.moodImage);
        return mView; // 返回该碎片的视图对象
    }

    @Override
    public void onResume() {
        super.onResume();
        Drawable drawable;
        switch(moodNum){
            case 0:
                drawable= ContextCompat.getDrawable(mContext,R.drawable.ic_face1);
                break;
            case 1:
                drawable= ContextCompat.getDrawable(mContext,R.drawable.ic_face2);
                break;
            case 2:
                drawable= ContextCompat.getDrawable(mContext,R.drawable.ic_face3);
                break;
            case 3:
                drawable= ContextCompat.getDrawable(mContext,R.drawable.ic_face4);
                break;
            case 4:
                drawable= ContextCompat.getDrawable(mContext,R.drawable.ic_face5);
                break;
            default:
                Log.d("MoodFragment","Error postion "+moodNum);
                drawable= ContextCompat.getDrawable(mContext,R.drawable.ic_delet_or_error);
        }
        moodView.setImageDrawable(drawable);
    }
}
