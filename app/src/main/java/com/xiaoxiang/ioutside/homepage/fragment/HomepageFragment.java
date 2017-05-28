package com.xiaoxiang.ioutside.homepage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.homepage.activity.ArticleShareActivity;
import com.xiaoxiang.ioutside.homepage.activity.WeatherActivity;

/**
 * Created by zhang on 2016/4/15 0015.
 */
public class HomepageFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ImageView homepageShare;
    private ViewPager homepage_viewPager;
    private TabLayout homepage_titles;
    private static final String TAG = "HomepageFragment";
//    private String[] titles=new String[]{"精选", "订阅", "专题", "光影"};
    private String[] titles=new String[]{"精选", "专题", "光影"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.homepage_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homepageShare=(ImageView)view.findViewById(R.id.homepage_share);
        homepage_viewPager = (ViewPager) view.findViewById(R.id.homepage_viewPager);
        Log.i(TAG, "onActivityCreated: "+homepage_viewPager);
        homepage_viewPager.setAdapter(new HomepageAdapter(getChildFragmentManager()));
        homepage_viewPager.setOffscreenPageLimit(3);
        homepage_titles = (TabLayout) view.findViewById(R.id.homepage_titles);
        homepage_titles.setupWithViewPager(homepage_viewPager);
        homepageShare.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String token = getActivity().getIntent().getStringExtra("token");
        switch (view.getId()) {
            case R.id.homepage_share:
//                homepageShare.setSelected(true);//要跳转到推荐外链
//                Intent intent = new Intent(getActivity(), ArticleShareActivity.class);
//                intent.putExtra("token", token);
//                startActivity(intent);
//                跳到天气
                Intent intent=new Intent(getActivity(), WeatherActivity.class);
                startActivity(intent);
                break;
        }
    }


    public class HomepageAdapter extends FragmentStatePagerAdapter {

        public HomepageAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new HomeChoiceFragment();
//                case 1:
//                    return new HomeSubscribeFragment();
                case 1:
                    return new SubMainFragment();
                case 2:
                    return new HomeLightFragment();
                default:
                    return new HomeChoiceFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

    }

}
