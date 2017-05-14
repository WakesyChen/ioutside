package com.xiaoxiang.ioutside.homepage.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.homepage.CustomVideoView;
import com.xiaoxiang.ioutside.util.NetworkUtil;


public class LightPlayActivity extends AppCompatActivity {
    //private static final String TAG=LightPlayActivity.class.getSimpleName();
    private RelativeLayout light_play_relativeLayout;
    private CustomVideoView videoView;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent=getIntent();
        url=intent.getStringExtra("url");
        initView();
        initData();
    }

    private void initView() {
        setContentView(R.layout.activity_home_light_play);
        light_play_relativeLayout=(RelativeLayout)findViewById(R.id.light_play_relativeLayout);
        videoView=(CustomVideoView)findViewById(R.id.video);
    }

    //光影详情是跟文章详情一样的接口
    private void initData() {
        if (NetworkUtil.isNetworkConnected(this)){
            Uri uri=Uri.parse(url);
            videoView.setVideoURI(uri);
            MediaController mediaController=new MediaController(this);
            videoView.setMediaController(mediaController);//设置mediaController与videoview建立关联
            mediaController.setMediaPlayer(videoView);
            videoView.requestFocus();//获取焦点
            videoView.start();
            //videoView.getBufferPercentage()
        }
        else {
            light_play_relativeLayout.setVisibility(View.GONE);
            Toast.makeText(this,"网络有点问题!",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
