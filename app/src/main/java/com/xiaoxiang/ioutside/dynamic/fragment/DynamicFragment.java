package com.xiaoxiang.ioutside.dynamic.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.common.CachedInfo;
import com.xiaoxiang.ioutside.common.MyApplication;
import com.xiaoxiang.ioutside.dynamic.activity.PhotoPickActivity;
import com.xiaoxiang.ioutside.dynamic.activity.AddFriendActivity;
import com.xiaoxiang.ioutside.dynamic.adapter.DynamicAdapter;
import com.xiaoxiang.ioutside.mine.activity.LoginActivity;

/**
 * Created by zhang on 2016/4/15 0015.
 */
public class DynamicFragment extends Fragment implements View.OnClickListener{
    private View view;
    private ImageView discovery_top_add;
    private ImageView discovery_add_friend;
    private ViewPager discovery_viewPager;
    private TabLayout discovery_top_titles;
    private String[] titles= new String[]{"推荐", "关注"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.dynamic_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        discovery_top_add=(ImageView)view.findViewById(R.id.discovery_photo);
        discovery_add_friend=(ImageView)view.findViewById(R.id.discovery_add_friend);
        discovery_viewPager = (ViewPager) view.findViewById(R.id.discovery_viewPager);
        discovery_viewPager.setAdapter(new DynamicAdapter(getFragmentManager(),titles));
        discovery_top_titles = (TabLayout) view.findViewById(R.id.discovery_top_titles);
        discovery_top_titles.setupWithViewPager(discovery_viewPager);
        discovery_add_friend.setOnClickListener(this);
        discovery_top_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String token=getActivity().getIntent().getStringExtra("token");
        CachedInfo cachedInfo= MyApplication.getInstance().getCachedInfo();
        String fileToken=cachedInfo.getToken();
        if (token==null){
            token=fileToken;
        }
        switch (v.getId()) {
            case R.id.discovery_add_friend:
                Intent intentAdd = new Intent(getActivity(), AddFriendActivity.class);
                intentAdd.putExtra("token", token);
                startActivity(intentAdd);
                break;
            case R.id.discovery_photo:
                if (token==null){
                    String[] items=new String[]{"登录","取消"};
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case 0:
                                    Intent intent=new Intent(getActivity(), LoginActivity.class);
                                    startActivity(intent);
                                    break;
                                case 1:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    }).show();
                }else {
                    Intent intent = new Intent(getActivity(), PhotoPickActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra(PhotoPickActivity.MAX_PICK_COUNT, 9);//最多选9张
                    intent.putExtra(PhotoPickActivity.IS_SHOW_CAMERA, true);//是否显示相机
                    startActivity(intent);
                }
                break;
        }
    }

}