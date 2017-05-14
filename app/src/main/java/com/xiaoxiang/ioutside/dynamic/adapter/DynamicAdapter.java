package com.xiaoxiang.ioutside.dynamic.adapter;

import android.support.v4.app.FragmentPagerAdapter;

import com.xiaoxiang.ioutside.dynamic.fragment.DynamicFocusFragment;
import com.xiaoxiang.ioutside.dynamic.fragment.DynamicRecommendFragment;

/**
 * Created by zhang on 2016/6/22,0022.
 */
public class DynamicAdapter extends FragmentPagerAdapter {
    private String[] titles;

    public DynamicAdapter(android.support.v4.app.FragmentManager fm, String[] titles) {
        super(fm);
        this.titles=titles;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DynamicRecommendFragment();
            case 1:
                return new DynamicFocusFragment();
            default:
                return new DynamicRecommendFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}

