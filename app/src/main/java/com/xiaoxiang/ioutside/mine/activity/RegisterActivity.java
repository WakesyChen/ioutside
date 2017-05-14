package com.xiaoxiang.ioutside.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.mine.common.ThirdPartyLogin;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15119 on 2016/5/26.
 */
public class RegisterActivity extends Activity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_login_qq)
    ImageView ivLoginQQ;
    @Bind(R.id.iv_login_wechat)
    ImageView ivLoginWechat;

    private ThirdPartyLogin mThirdPartyLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        mThirdPartyLogin = ThirdPartyLogin.getInstance(this);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        tvTitle.setText(title);
        tvTitle.setTextColor(Color.parseColor("#eeeeee"));
    }

    @OnClick({R.id.iv_login_qq, R.id.iv_login_wechat})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_login_qq:
                mThirdPartyLogin.doQQAuth();
                break;
            case R.id.iv_login_wechat:
                mThirdPartyLogin.doWeChatAuth();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mThirdPartyLogin.onActivityResult(requestCode, resultCode, data);
    }
}
