package com.smd.recorder.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.smd.recorder.R;
import com.smd.recorder.bean.RecorderInfo;

import java.util.List;

public class RecordListAdapter extends ArrayAdapter<RecorderInfo> {
    private int resourceId;
    private Context mContext;
    // 适配器的构造函数，把要适配的数据传入这里
    public RecordListAdapter(Context context, int textViewResourceId, List<RecorderInfo> objects){
        super(context,textViewResourceId,objects);
        mContext = context;
        resourceId=textViewResourceId;
    }

    // convertView 参数用于将之前加载好的布局进行缓存
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        RecorderInfo recorderInfo=getItem(position); //获取当前项的Fruit实例

        // 加个判断，以免ListView每次滚动时都要重新加载布局，以提高运行效率
        View view;
        ViewHolder viewHolder;
        if (convertView==null){

            // 避免ListView每次滚动时都要重新加载布局，以提高运行效率
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

            // 避免每次调用getView()时都要重新获取控件实例
            viewHolder=new ViewHolder();
            viewHolder.recordName=view.findViewById(R.id.record_name);
            viewHolder.recordImage=view.findViewById(R.id.record_image);

            // 将ViewHolder存储在View中（即将控件的实例存储在其中）
            view.setTag(viewHolder);
        } else{
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }

        // 获取控件实例，并调用set...方法使其显示出来
        viewHolder.recordName.setText(recorderInfo.getYear()+"/"+recorderInfo.getMonth()+1+"/"+recorderInfo.getDay());
        Drawable drawable;
        switch(recorderInfo.getMoodNum()){
            case 1:
                drawable= ContextCompat.getDrawable(mContext,R.drawable.ic_face1);
                break;
            case 2:
                drawable= ContextCompat.getDrawable(mContext,R.drawable.ic_face2);
                break;
            case 3:
                drawable= ContextCompat.getDrawable(mContext,R.drawable.ic_face3);
                break;
            case 4:
                drawable= ContextCompat.getDrawable(mContext,R.drawable.ic_face4);
                break;
            case 5:
                drawable= ContextCompat.getDrawable(mContext,R.drawable.ic_face5);
                break;
            default:
                drawable= ContextCompat.getDrawable(mContext,R.drawable.ic_delet_or_error);
        }
        viewHolder.recordImage.setImageDrawable(drawable);
        return view;
    }

    // 定义一个内部类，用于对控件的实例进行缓存
    class ViewHolder{
        TextView recordName;
        ImageView recordImage;
    }
}
