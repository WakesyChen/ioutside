package com.xiaoxiang.ioutside.homepage.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.activities.model.PersonInfor;
import com.xiaoxiang.ioutside.common.ProgressWebView;
import com.xiaoxiang.ioutside.common.methods.UmengShare;
import com.xiaoxiang.ioutside.homepage.PersonInfoAll;
import com.xiaoxiang.ioutside.util.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Wakesy on 2016/9/1.
 */
public class ItemWebView2 extends Activity implements View.OnClickListener{

    @Bind(R.id.banner_detail_back2)
    ImageView banner_detail_back2;
    @Bind(R.id.webview_title2)
    TextView webview_title2;
    @Bind(R.id.activity_banner_share2)
    ImageView activity_banner_share2;
    @Bind(R.id.banner_webview2)
    ProgressWebView banner_webview2;
    private PersonInfoAll.UserBean personInfor;
    private String title;
//    private String voteUrl="http://ioutside.com/xiaoxiang-backend/special-outside-spread-activity?outsideType=1&userID=244";
    private String voteUrl="http://ioutside.com/xiaoxiang-backend/special-outside-spread-activity?outsideType=1&userID=";
    private int userId;
    private String userPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_webview2);
        ButterKnife.bind(this);

        initData();
        initEvent();
    }

    private void initEvent() {
        banner_detail_back2.setOnClickListener(this);
        activity_banner_share2.setOnClickListener(this);
        webview_title2.setText("争夺参与皮划艇活动名额");
    }

    private void initData() {
        personInfor= (PersonInfoAll.UserBean) getIntent().getSerializableExtra("personInfor");
        userId=personInfor.getId();

        userPhoto=personInfor.getPhoto();
        voteUrl=voteUrl+userId;

        title="我参加了皮划艇活动，快来投我一票吧";
        banner_webview2.loadUrl(voteUrl);
        banner_webview2.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//              返回值是true的时候控制网页在webview中打开,false则通过第三方打开
                view.loadUrl(url);
                return true;
            }
        });
        //启用支持JavaScript
        WebSettings websetting = banner_webview2.getSettings();
        websetting.setJavaScriptEnabled(true);
        //WebView加载页面优先使用缓存加载
        websetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        websetting.setLoadWithOverviewMode(true);
        websetting.setBuiltInZoomControls(true);
        websetting.setUseWideViewPort(true);
        websetting.setDomStorageEnabled(true);
        websetting.setDatabaseEnabled(true);
        websetting.setAppCacheEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.banner_detail_back2:
                finish();
                break;

            case R.id.activity_banner_share2:
                ToastUtils.show("分享");

                String content = "爱户外，玩出精彩，享受生活，贴近自然！";

                UmengShare.setShareContent(ItemWebView2.this, title, voteUrl,userPhoto, content);
                break;
        }



    }
}
