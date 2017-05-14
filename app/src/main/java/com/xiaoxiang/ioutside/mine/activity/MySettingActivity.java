package com.xiaoxiang.ioutside.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.common.CachedInfo;
import com.xiaoxiang.ioutside.common.MyApplication;
import com.xiaoxiang.ioutside.util.CacheCleaner;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MySettingActivity extends AppCompatActivity {
    private String TAG=getClass().getSimpleName();

    @Bind(R.id.iv_back)
    ImageView imgBackbtn;
    @Bind(R.id.tv_title)
    TextView tvTitleSetting;
    @Bind(R.id.lay_aboutus_setting)
    RelativeLayout layAboutusSetting;
    @Bind(R.id.lay_clearcache_setting)
    RelativeLayout layClearcacheSetting;
    @Bind(R.id.btn_signout)
    Button btnSignout;
    @Bind(R.id.tv_cache_setting)
    TextView tvCacheSize;
    @Bind(R.id.img_clearcache)
    ImageView imgClean;
    @Bind(R.id.lay_clearcache)
    RelativeLayout layClearcache;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);
        ButterKnife.bind(this);
        tvTitleSetting.setText("设置");
        try {
            String cachedSize = CacheCleaner.getCacheSize(this.getCacheDir());
            tvCacheSize.setText(cachedSize);
        } catch (Exception e) {
            e.printStackTrace();
        }

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Bundle b=msg.getData();
                String size=b.getString("size");
                tvCacheSize.setText(size);
            }
        };
    }

    @OnClick({R.id.iv_back, R.id.lay_aboutus_setting, R.id.lay_clearcache_setting, R.id.btn_signout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.lay_aboutus_setting:
                Intent usIn = new Intent(MySettingActivity.this, AboutUsActivity.class);
                startActivity(usIn);
                break;
            case R.id.lay_clearcache_setting:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CacheCleaner.cleanInternalCache(MySettingActivity.this);
                        Message msg=handler.obtainMessage();
                        try{
                            String cachedSize = CacheCleaner.getCacheSize(MySettingActivity.this.getCacheDir());
                            Bundle b=new Bundle();
                            b.putString("size",cachedSize);
                            Log.d("CacheCleaner","最终剩下这么多"+cachedSize);
                            msg.setData(b);
                            handler.sendMessage(msg);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.btn_signout:
                //CacheCleaner.cleanApplicationData(this);
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("com.xiaoxiang.ioutside.MAIN_FINISH_SELF"));
                CachedInfo info= MyApplication.getInstance().getCachedInfo();
                info.clear();
                CacheCleaner.deleteFolderFile(getFilesDir()+"/cachedInfo",true);
                Log.d(TAG,getFilesDir()+"cachedInfo");
                Intent in = new Intent(MySettingActivity.this, LoginActivity.class);
                startActivity(in);
                finish();
                break;
        }
    }

}
