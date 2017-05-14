package com.xiaoxiang.ioutside.mine.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.mine.fragment.DiscoveryFragment;
import com.xiaoxiang.ioutside.mine.fragment.EssayFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyCollectActivity extends AppCompatActivity {

    String TAG = getClass().getSimpleName();
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tl_title)
    TabLayout tlTitle;
    @Bind(R.id.vp_content)
    ViewPager vpContent;

    private String[] titles;

    private String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_collect_my);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        vpContent.setAdapter(mPagerAdapter);
        tlTitle.setupWithViewPager(vpContent);
    }

    private void initData() {
        token = getIntent().getStringExtra("token");
        titles = new String[]{"文章", "动态"};
    }

    private PagerAdapter mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return EssayFragment.instantiate(token);
            } else if (position == 1) {
                return DiscoveryFragment.instantiate(token);
            }
            return DiscoveryFragment.instantiate(token);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    };

    @OnClick({R.id.iv_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back :
                finish();
                break;
        }
    }


}
