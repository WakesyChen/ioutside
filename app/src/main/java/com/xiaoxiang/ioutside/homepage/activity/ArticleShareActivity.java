package com.xiaoxiang.ioutside.homepage.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.common.CachedInfo;
import com.xiaoxiang.ioutside.common.Constants;
import com.xiaoxiang.ioutside.common.MyApplication;
import com.xiaoxiang.ioutside.mine.activity.LoginActivity;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;

import okhttp3.Request;

public class ArticleShareActivity extends Activity implements Constants,OkHttpManager.ResultCallback.CommonErrorListener{
    private static final String TAG=ArticleShareActivity.class.getSimpleName();
    private ScrollView share_ScrollView;
    private ImageView share_back;
    private EditText share_url;
    private EditText share_reason;
    private TextView share_know;
    private ImageView share_experience;
    private String token;
    private String fileToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token=getIntent().getStringExtra("token");
        CachedInfo cachedInfo= MyApplication.getInstance().getCachedInfo();
        if (cachedInfo!=null){
            fileToken=cachedInfo.getToken();
            Log.d(TAG, "fileToken=" + fileToken);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_article_share);
        share_ScrollView=(ScrollView)findViewById(R.id.share_ScrollView);
        share_back=(ImageView)findViewById(R.id.share_back);
        share_url=(EditText)findViewById(R.id.share_url);
        share_reason=(EditText)findViewById(R.id.share_reason);
        share_know=(TextView)findViewById(R.id.share_know);
        share_experience=(ImageView)findViewById(R.id.share_experience);
        share_ScrollView.setVerticalScrollBarEnabled(false);
        share_ScrollView.setHorizontalScrollBarEnabled(false);
        if (token!=null){
            initEvent();
        }
        else if (token==null&&fileToken==null){
            initEvent();
        }
        else {
            token=fileToken;
            initEvent();
        }
    }

    private void initEvent(){
      final ApiInterImpl api=new ApiInterImpl();
        share_know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView=new ImageView(ArticleShareActivity.this);
                imageView.setImageResource(R.drawable.recommend_know);
                new AlertDialog.Builder(ArticleShareActivity.this)
                        .setView(imageView).show();
            }
        });

        share_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onBackPressed();
            }
        });

        share_experience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = share_url.getText().toString().trim();
                if (token == null) {
                  noLogin();
                } else {
                    if (TextUtils.isEmpty(url)) {
                        Toast.makeText(ArticleShareActivity.this,"链接不能为空!",Toast.LENGTH_SHORT).show();
                    }else {
                        String reason = share_reason.getText().toString().trim();
                        String urlBase64 = Base64.encodeToString(url.getBytes(), Base64.DEFAULT);
                        OkHttpManager mOkHttpManager = OkHttpManager.getInstance();
                        mOkHttpManager.getStringAsyn(api.recommend(urlBase64, token, reason), new OkHttpManager.ResultCallback<String>(ArticleShareActivity.this) {
                            @Override
                            public void onError(Request request, Exception e) {
                                Toast.makeText(ArticleShareActivity.this, "网络有点问题！", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "分享失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                super.onResponse(response);
                                Toast.makeText(ArticleShareActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, response);
                                finish();
                            }
                        });
                    }
                }
            }
        });
    }

    private void noLogin() {
        String[] items = new String[]{"登录", "取消"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ArticleShareActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent intent = new Intent(ArticleShareActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        dialog.dismiss();
                        break;
                }
            }
        }).show();
    }

    @Override
    public void onCommonError(int errorCode) {
        // 这里处理通用逻辑
        if (errorCode == USER_OUTLINE) {
            Toast.makeText(this,"你已在别的地方登录，你被迫下线，请重新登录！",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(this,LoginActivity.class);
            startActivity(in);
        }else if(errorCode==TOKEN_OVERTIME){
            Toast.makeText(this,"你的登录信息已过期，请重新登录",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(this,LoginActivity.class);
            startActivity(in);
        }else if(errorCode==SERVER_ERROR){
            Toast.makeText(this,"服务器内部错误，请重新登录",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(this,LoginActivity.class);
            startActivity(in);
        }
    }

}
