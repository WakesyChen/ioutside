package com.xiaoxiang.ioutside.mine.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.homepage.activity.MainActivity;
import com.xiaoxiang.ioutside.mine.common.SystemInfo;
import com.xiaoxiang.ioutside.mine.common.ThirdPartyLogin;
import com.xiaoxiang.ioutside.mine.dialog.ConfirmDialog;
import com.xiaoxiang.ioutside.mine.dialog.ErrorMsgDialog;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GLogin;
import com.xiaoxiang.ioutside.util.MD5Helper;
import com.xiaoxiang.ioutside.util.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;

public class LoginActivity extends Activity implements TextWatcher {

    String TAG = getClass().getSimpleName();

    @Bind(R.id.edt_login_account)
    EditText edtAccountLogin;
    @Bind(R.id.edt_login_password)
    EditText edtPasswordLogin;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.tv_forget_pwd)
    TextView tvForgetpw;
    @Bind(R.id.iv_login_wechat)
    ImageView ivWechatlogin;
    @Bind(R.id.tv_sign_up)
    TextView tv_sign_up;

    private ApiInterImpl api;
    private Gson gson;
    private OkHttpManager mOkHttpManager;

    private ThirdPartyLogin mThirdPartyLogin;

    private ProgressDialog waitingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_v2);
        ButterKnife.bind(this);

        edtAccountLogin.addTextChangedListener(this);
        edtPasswordLogin.addTextChangedListener(this);

        api = new ApiInterImpl();
        gson = new Gson();
        mOkHttpManager = OkHttpManager.getInstance();
        mThirdPartyLogin = ThirdPartyLogin.getInstance(this);
    }

    @OnClick({R.id.btn_login, R.id.tv_forget_pwd, R.id.iv_login_wechat, R.id.tv_sign_up, R.id.iv_login_qq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                //登陆
                showWaitingDialog();
                login(edtAccountLogin.getText().toString(),
                        edtPasswordLogin.getText().toString());
                break;
            case R.id.tv_forget_pwd:
                //忘记密码
                Intent fpw = new Intent(LoginActivity.this, ForgetPwActivity.class);
                startActivity(fpw);
                break;
            case R.id.iv_login_qq:
                //qq登陆
                mThirdPartyLogin.doQQAuth();  //test
                break;
            case R.id.iv_login_wechat:
                //wechat登陆
                mThirdPartyLogin.doWeChatAuth();
                break;
            case R.id.tv_sign_up:
                //注册
                register();
                break;

        }
    }

    private void login(String userName, String pwd) {

        String password = MD5Helper.getMd5(pwd);

        if ("".equals(userName) || "".equals(password)) {

            ToastUtils.show("请将登录信息填写完整！");

        } else {

            String apiLogin = api.getSignInIn(0, SystemInfo.uniqueCode, SystemInfo.systemVersion, userName, password, 0);

            Log.d(TAG, apiLogin);

            mOkHttpManager.getStringAsyn(apiLogin, new OkHttpManager.ResultCallback<BaseResponse<GLogin>>() {

                @Override
                public void onError(Request request, Exception e) {
                    hideWaitingDialog();
                    showErrorDialog("网络有点问题哦");
                }

                @Override
                public void onResponse(BaseResponse<GLogin> response) {
                    hideWaitingDialog();

                    Log.d(TAG, "login response --> " + response.toString());

                    boolean isSuccess = response.isSuccess();

                    if (isSuccess) {
                        closeMainActivity();
                        //Log.d(TAG, "response success");
                        //retrieve the token
                        GLogin data = response.getData();
                        //show MainPage after logging in
                        if (data.getIsFirstLogin() == 0) {
                            Intent in = new Intent(LoginActivity.this, MainActivity.class);
                            in.putExtra("token", data.getToken());
                            in.putExtra("userId", data.getUserID());
                            startActivity(in);
                        }
//                        } else {
//                            Intent in = new Intent(LoginActivity.this, LoginRecommendActivity.class);
//                            in.putExtra("token", data.getToken());
//                            startActivity(in);
//                        }

                        //remember to finish this activity
                        finish();
                    } else {
                        Log.d(TAG, response.toString());
                        //show error msg
                        String errorMessage = response.getErrorMessage();

                        showErrorDialog(errorMessage.substring(5, errorMessage.length()));
                    }
                }
            });

        }
    }

    private void closeMainActivity() {
        Intent closeMainActivity = new Intent("com.xiaoxiang.ioutside.MAIN_FINISH_SELF");
        LocalBroadcastManager.getInstance(this).sendBroadcast(closeMainActivity);

    }

    private void showErrorDialog(String errorMessage) {
        new ErrorMsgDialog(this, errorMessage).show();
    }


    //-------------------- TextWatcher start ------------------
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(edtPasswordLogin.getText().toString()) &&
                !TextUtils.isEmpty(edtAccountLogin.getText().toString())) {
            btnLogin.setEnabled(true);
        } else {
            btnLogin.setEnabled(false);
        }
    }
    //-------------------- TextWatcher end ------------------

    private void register() {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter(BROADCAST_EMAIL_SENT);
        manager.registerReceiver(emailSentReceiver, filter);
        Intent reIntent = new Intent(this, RegisterActivity.class);
        startActivity(reIntent);
    }


    public void showWaitingDialog() {
        if (waitingDialog == null) {
            waitingDialog = new ProgressDialog(this);
        }
        waitingDialog.setMessage("正在登陆，请稍后...");
        waitingDialog.show();
    }

    private void hideWaitingDialog() {
        if (waitingDialog != null && waitingDialog.isShowing()) {
            waitingDialog.hide();
        }
    }

    /**
     * 显示“邮件已发送”的弹窗
     */

    public static final String BROADCAST_EMAIL_SENT = "com.example.oubin6666.ioutside.broadcast_email_sent";

    private BroadcastReceiver emailSentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showConfirmDialog("请尽快登陆邮箱激活账号哦~", "知道啦");
            Log.d(TAG, "emailSentReceiver --> broadcast received");
        }
    };

    private void showConfirmDialog(String msg, String confirmText) {
        new ConfirmDialog(this, msg, confirmText).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (waitingDialog != null) {
            waitingDialog.dismiss();
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(emailSentReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mThirdPartyLogin.onActivityResult(requestCode, resultCode, data);
    }
}