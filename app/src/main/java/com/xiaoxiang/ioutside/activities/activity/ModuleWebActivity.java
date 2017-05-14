package com.xiaoxiang.ioutside.activities.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.common.ProgressWebView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Wakesy on 2016/8/16.
 */
public class ModuleWebActivity extends Activity {
    @Bind(R.id.common_back)
    ImageView common_back;
    @Bind(R.id.common_title)
    TextView common_title;
    @Bind(R.id.common_webview)
    ProgressWebView webview;
    private String url="http://ioutside.com/xiaoxiang-backend/activity/descript-modules/2.html";
   private  String title="活动详情";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_webview);
        ButterKnife.bind(this);
        url=getIntent().getStringExtra("url");
        title=getIntent().getStringExtra("title");
        common_title.setText(title+"");
        webview.loadUrl(url);
        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//              返回值是true的时候控制网页在webview中打开,false则通过第三方打开
                view.loadUrl(url);
                return true;
            }
        });
        //启用支持JavaScript
        WebSettings websetting = webview.getSettings();
        websetting.setJavaScriptEnabled(true);
        //WebView加载页面优先使用缓存加载
        websetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        websetting.setLoadWithOverviewMode(true);
        websetting.setBuiltInZoomControls(true);
        websetting.setUseWideViewPort(true);
        websetting.setDomStorageEnabled(true);
        websetting.setDatabaseEnabled(true);
        websetting.setAppCacheEnabled(true);



        common_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    //        改写物理按键一次返回就退出应用的逻辑
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webview.canGoBack()) {//判断前面是否能返回
                webview.goBack();

                return true;
            } else {


                    finish();//退出当前Activity
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
