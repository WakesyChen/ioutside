package com.xiaoxiang.ioutside.dynamic.activity;

import android.view.View;

import com.xiaoxiang.ioutside.homepage.adapter.SimplePagerAdapter;

import java.util.List;

/**
 * Created by 15119 on 2016/8/7.
 */
public class TitlePagerAdapter extends SimplePagerAdapter {

    private String[] mTitles;

    public TitlePagerAdapter(List<View> viewList, String[] titles) {
        super(viewList);
        mTitles = titles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
