package com.xiaoxiang.ioutside.homepage.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.common.ProgressWebView;

/**
 * Created by Wakesy on 2016/7/29.
 */

/*
* 用于图片轮播的链接跳转界面
* */
public class ItemDetailActivity extends Activity {
    private ProgressWebView webview;
    private String url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        webview= (ProgressWebView) findViewById(R.id.item_detail_webview);

        initEvent();
    }

    private void initEvent() {
        url=getIntent().getStringExtra("url");

//覆盖WebView通过第三方浏览器访问网页的行为，使得网页在WebView中打开
        webview.loadUrl(url);
        webview.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制网页在webview中打开,false则通过第三方打开
                //return super.shouldOverrideUrlLoading(view, url);
                view.loadUrl(url);
                return true;
            }

        });


//                三、在webView中使用Javascript

//        在WebView中启用JavaScript，通过WebView带有的WebSttings,；

        //启用支持JavaScript
        WebSettings websetting=webview.getSettings();
        websetting.setJavaScriptEnabled(true);
        //WebView加载页面优先使用缓存加载
        websetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //网页全屏显示
        websetting.setLoadWithOverviewMode(true);
        //支持手指缩放
        websetting.setBuiltInZoomControls(true);
//        WebView双击变大，再双击后变小，当手动放大后，双击可以恢复到原始大小，如下设置：
        websetting.setUseWideViewPort(true);
//      开启DOM storage API 功能
        websetting.setDomStorageEnabled(true);
        // 开启database storage API功能
        websetting.setDatabaseEnabled(true);
        // 开启Application Cache功能
        websetting.setAppCacheEnabled(true);

    }

    //        四、改写物理按键一次返回就退出应用的逻辑
//在protected void onCreate(Bundle savedInstanceState) {}外定义
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode== KeyEvent.KEYCODE_BACK){
            if(webview.canGoBack()){//判断前面是否能返回
                webview.goBack();

                return true;//直接返回
            }
            else{
                finish();//退出当前Activity
            }

        }
        return super.onKeyDown(keyCode, event);
    }






}
