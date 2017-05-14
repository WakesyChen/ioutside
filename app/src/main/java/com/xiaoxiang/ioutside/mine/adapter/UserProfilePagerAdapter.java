package com.xiaoxiang.ioutside.mine.adapter;

import android.view.View;

import com.xiaoxiang.ioutside.homepage.adapter.SimplePagerAdapter;

import java.util.List;

/**
 * Created by 15119 on 2016/7/14.
 */
public class UserProfilePagerAdapter extends SimplePagerAdapter {
    private String[] titles;

    public UserProfilePagerAdapter(List<View> viewList, String[] titles) {
        super(viewList);
        this.titles = titles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
