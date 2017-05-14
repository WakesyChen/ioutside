package com.xiaoxiang.ioutside.homepage.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ImageView;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.homepage.activity.ItemWebVIew;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wakesy on 2016/8/1.
 */
//暂未使用
public class HomeChoiceBannerAdapter extends PagerAdapter {
    private List<ImageView>imglist=new ArrayList<>();
    private static final String TAG = "HomeChoiceBannerAdapter";
    private Context context;
    private String[]urls={"http://mp.weixin.qq.com/s?__biz=MzIxNTI4NDE3NQ==&mid=2247484634&idx=2&sn=2286683ff8c4b3065c50509bee8ab409&scene=4#wechat_redirect",
                        "http://mp.weixin.qq.com/s?__biz=MzIxNTI4NDE3NQ==&mid=2247484581&idx=1&sn=e05706eb77ea48c2cbd8d559cabe6ac8&scene=4#wechat_redirect",
                        "http://mp.weixin.qq.com/s?__biz=MzIxNTI4NDE3NQ==&mid=2247483938&idx=1&sn=61e7193162ab1fefb18ad8d1f5130fb3&scene=4#wechat_redirect",
                        "http://mp.weixin.qq.com/s?__biz=MzIxNTI4NDE3NQ==&mid=2247484755&idx=1&sn=64cf24e0ad6190d2fa17877bc1d0444e&scene=0#wechat_redirect"};
    private String[]titles={"爱户外club第一期","追浪专访","代子专访","爱在户外"};


    public HomeChoiceBannerAdapter( List<ImageView>imglist, Context context) {
       this.imglist=imglist;
        this.context=context;


    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imageView;

        Log.i(TAG, "instantiateItem: position"+position);
        if (imglist.size()>0){
            ImageView imageView2=imglist.get(position%imglist.size());
            imageView=imageView2;
        }

        else {
            ImageView imageView1 = new ImageView(container.getContext());
            imageView1.setImageResource(R.drawable.banner_img_1);
            imageView = imageView1;
        }
        if (imageView.getParent()!=null) {//如果当前位置上已经有了控件，就先移除
            ((ViewManager)imageView.getParent()).removeView(imageView);
        }
        container.addView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index=position%titles.length;
                String url=urls[index];
                String title=titles[index];
                Intent intent=new Intent(context, ItemWebVIew.class);
                intent.putExtra("index",index);
                intent.putExtra("url",url);
                intent.putExtra("title",title);
               context.startActivity(intent);
            }
        });
        return imageView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imglist.get(position%imglist.size()));
    }
}
