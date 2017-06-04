package com.xiaoxiang.ioutside.homepage.activity;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.activities.fragment.ActivitiesFragment;
import com.xiaoxiang.ioutside.common.CachedInfo;
import com.xiaoxiang.ioutside.common.MyApplication;
import com.xiaoxiang.ioutside.dynamic.fragment.DynamicFragment;
import com.xiaoxiang.ioutside.homepage.fragment.HomepageFragment;
import com.xiaoxiang.ioutside.mine.fragment.AfterLoginFragment;
import com.xiaoxiang.ioutside.mine.fragment.BeforeLoginFragment;
import com.xiaoxiang.ioutside.mine.listener.OnMsgCountReceivedListener;
import com.xiaoxiang.ioutside.mine.model.MessageCount;
import com.xiaoxiang.ioutside.mine.model.PersonalInfo;
import com.xiaoxiang.ioutside.network.postengine.HttpUtil;
import com.xiaoxiang.ioutside.network.postengine.Request;
import com.xiaoxiang.ioutside.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {
    private OnMsgCountReceivedListener msgCountReceivedListener;
    @Bind(R.id.vp_home_page)
    ViewPager pages;
    @Bind(R.id.tl_nav)
    TabLayout tlNav;
    private MessageCount.DataBean messageCountInfo;
    private int[] tabDrawableIds = {R.drawable.bottom_layout_homepage,
            R.drawable.bottom_layout_discovery, R.drawable.bottom_layout_activities, R.drawable.bottom_layout_me};
    private String[] tabText = new String[]{"首页", "动态", "活动", "我"};

    private FragmentManager fragmentManager;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 10;
    private static final int CAMERA_REQUEST_CODE = 11;
    private long exitTime = 0;
    private String token;
    private static final String TAG = "MainActivity";
    private int localVersion;//当前本地版本号
    private int onlineVersion;//当前最新版本
    private String urlUpdate="http://a.app.qq.com/o/simple.jsp?pkgname=com.xiaoxiang.ioutside";//下载链接
//
    private String versionUrl="http://ioutside.com/xiaoxiang-backend/app/get-recent-version?type=2";//获取version
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.i(TAG, "onCreate: getVersion:"+getVersionCode());
        localVersion=getVersionCode();
        getOnlineVersion();//获取最新版本并提示更新

        fragmentManager = getSupportFragmentManager();
        PagerAdapter pagerAdapter = new MainPageAdapter(fragmentManager);

        pages.setAdapter(pagerAdapter);
        pages.setOffscreenPageLimit(3);

        tlNav.setupWithViewPager(pages);
        setupTabs();

        token = getIntent().getStringExtra("token");
        CachedInfo mCachedInfo = MyApplication.getInstance().getCachedInfo();

        int userId = getIntent().getIntExtra("userId", -1);
        if (token != null && userId != -1) {
            mCachedInfo.setToken(token);
            mCachedInfo.setUserId(userId);
        }
        if (token == null || userId == -1) {
            token = mCachedInfo.getToken();
        }
        initEvent();
        registerLogoutReceiver();
        Log.d(TAG, "userId -- >" + mCachedInfo.getUserId());
    }

    private void setupTabs() {
        for (int i = 0; i < 4; i++) {
            TabLayout.Tab tab = tlNav.getTabAt(i);
            View view = LayoutInflater.from(this).inflate(R.layout.bottom_layout, null);
            tab.setCustomView(view);
            TextView tabView = (TextView) view.findViewById(R.id.tv_tab);
            ImageView tabImage = (ImageView) view.findViewById(R.id.iv_tab);
            tabView.setText(tabText[i]);
            tabImage.setImageResource(tabDrawableIds[i]);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getMessageCount();
    }

    private void getMessageCount() {

        if (TextUtils.isEmpty(token)) return;

        String url = "http://ioutside.com/xiaoxiang-backend/message/get-message-count";

        HttpUtil.Callback callback = new HttpUtil.Callback() {
            @Override
            public void onSuccess(String response) {

                Gson gson = new Gson();
                Log.i(TAG, "onSuccess: "+response);
                MessageCount messageCount = gson.fromJson(response, MessageCount.class);
                if (!messageCount.isSuccess()) return;

                messageCountInfo = messageCount.getData();
                if (msgCountReceivedListener != null) {
                    msgCountReceivedListener.onReceivedMsg(messageCountInfo);
                }

                refreshMessageNotification();
            }

            @Override
            public void onError(Request request) {

            }
        };

        Request request = new Request.Builder()
                .url(url)
                .addParam("token", token)
                .method(Request.METHOD_GET)
                .callback(callback)
                .build();

        HttpUtil.sendHttpRequest(request);
    }

    private void refreshMessageNotification() {

        View tabView = tlNav.getTabAt(2).getCustomView();
        ImageView dot = (ImageView) tabView.findViewById(R.id.iv_dot);

        TextView tvMsgCount = (TextView) tabView.findViewById(R.id.tv_msg_count);

        if (messageCountInfo.getMessageCount().getSum_count() == 0) {
            tvMsgCount.setVisibility(View.GONE);
        } else {
            tvMsgCount.setVisibility(View.VISIBLE);
            tvMsgCount.setText(String.valueOf(messageCountInfo.getMessageCount().getSum_count()));
        }

    }

    private void initEvent() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new
                    String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
            } else {
                // Permission Denied
            }
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();//相隔2秒再次点击back键就退出
            }
            else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    private BroadcastReceiver stopSelfReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    private void registerLogoutReceiver() {
        IntentFilter intentFilter = new IntentFilter("com.xiaoxiang.ioutside.MAIN_FINISH_SELF");
        LocalBroadcastManager.getInstance(this).registerReceiver(stopSelfReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(stopSelfReceiver);
    }

    //----------------------new  ---------------------------------------//


    public class MainPageAdapter extends FragmentPagerAdapter {
        public MainPageAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }
        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new HomepageFragment();
                case 1:
                    return new DynamicFragment();
                case 2:
                    return new ActivitiesFragment();
                case 3:
                    if (token != null) {
                        AfterLoginFragment f = new AfterLoginFragment();
                        msgCountReceivedListener = f;
                        return f;
                    } else {
                        return new BeforeLoginFragment();
                    }
                default:
                    return new HomepageFragment();
            }
        }
        @Override
        public int getCount() {
            return 4;
        }
    }

    //---------------------------new  -----------------------------------//


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getStringExtra("action");
        if ("login".equals(action)) {
            pages.setCurrentItem(2);
        } else {
            token = intent.getStringExtra("token");
            pages.setCurrentItem(0);
        }
    }
    /**
     * 获取本地版本号
     */
    public int getVersionCode() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            int versionCode=info.versionCode;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    /**
     * 获取当前最新版本号
     */
    private void getOnlineVersion() {
       new JsonAsync().execute(versionUrl);

    }
    public class JsonAsync extends AsyncTask<String,Void,Integer>{


        @Override
        protected Integer doInBackground(String... params) {
            String jsonString= HttpUtil.getJsonContent(versionUrl);
            try {
                JSONObject jsonObject=new JSONObject(jsonString);
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                onlineVersion= (int)jsonObject1.getDouble("version");//获取到版本号
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return onlineVersion;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
//            notifyUpdate(integer);//提示更新版本

        }
    }
/*
* 提醒更新
* */
    private void notifyUpdate(int onlineVersion) {
        //有最新版本，提示下载
        Log.i(TAG, "online--version:"+onlineVersion+"-----localversion:"+localVersion);

        if(onlineVersion>localVersion){
            showUpdateDialog();

        }


    }
    //显示提示更新框
    public void showUpdateDialog(){

        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("发现新版本");
        builder.setMessage("是否前往更新？");
        builder.setIcon(R.mipmap.head_ele);
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastUtils.show("更新版本");
                dialog.dismiss();
                Uri uri = Uri.parse(urlUpdate); // url为你要链接的地址,用浏览器打开
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
//                Intent intent=new Intent(MainActivity.this,UpdateWebView.class);
//                intent.putExtra("url",urlUpdate);
//                intent.putExtra("title","欢迎更新版本");
//                startActivity(intent);
            }
        });

        builder.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastUtils.show("暂不更新");
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);//设置点击窗口外不会退出
        builder.show();


    }


}



