package com.smd.recorder.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.smd.recorder.fragment.MoodFragment;

public class MoodPagerAdapter  extends FragmentStatePagerAdapter {
    // 碎片页适配器的构造函数，传入碎片管理器与年份
    public MoodPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return MoodFragment.newInstance(position+1);
    }

    @Override
    public int getCount() {
        return 5;
    }

    // 获得指定月份的标题文本
    public CharSequence getPageTitle(int position) {
        return new String("第"+position+1+ "种心情");
    }
}
