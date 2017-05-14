package com.xiaoxiang.ioutside.circle.view;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;


import com.xiaoxiang.ioutside.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Wakesy on 2016/9/17.
 *      入口暂时从首页Banner返回键入，ItemWebVIew;
 */
public class CircleFragment extends FragmentActivity {
    @Bind(R.id.circle_tabs)
    TabLayout tabs;
    @Bind(R.id.circle__viewpager)
    ViewPager circle_viewpager;

    private String titles[]={"社区","热帖"};
    public static  String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_main);

        ButterKnife.bind(this);
        token=getIntent().getStringExtra("token");
        initView();
        initData();
    }



    private void initData() {

        circle_viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                       return  new CommunityFragment();
                    case 1:
                       return new HotNoteFragment();
                    default:
                        return  new CommunityFragment();

                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
               return  titles[position];
            }
        });

        tabs.setupWithViewPager(circle_viewpager);


    }

    private void initView() {
    }


}
