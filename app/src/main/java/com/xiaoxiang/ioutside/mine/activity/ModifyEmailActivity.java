package com.xiaoxiang.ioutside.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.common.CachedInfo;
import com.xiaoxiang.ioutside.common.Constants;
import com.xiaoxiang.ioutside.common.MyApplication;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.util.CacheCleaner;
import com.xiaoxiang.ioutside.util.FormatUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;

public class ModifyEmailActivity extends Activity implements Constants,OkHttpManager.ResultCallback.CommonErrorListener {
    private String TAG=getClass().getSimpleName();

    @Bind(R.id.edt_newemail)
    EditText edtNewemail;
    @Bind(R.id.tv_title)
    TextView tvTitleSetting;
    @Bind(R.id.iv_back)
    ImageView imgBackbtn;
    @Bind(R.id.btn_sure)
    Button btnSure;

    private String token;
    private ApiInterImpl apiImpl;
    private OkHttpManager mOkHttpManager;
    private Gson gson;
    private BaseResponse modifyRe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_email);
        ButterKnife.bind(this);
        tvTitleSetting.setText("绑定新邮箱");
        token = getIntent().getStringExtra("token");
        Log.d(TAG,token);
        gson = new Gson();
        apiImpl = new ApiInterImpl();
        mOkHttpManager = OkHttpManager.getInstance();
    }


    @OnClick({R.id.iv_back, R.id.btn_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_sure:
                String newemail = edtNewemail.getText().toString();
                if (newemail.equals("")) {
                    Toast.makeText(ModifyEmailActivity.this, "请填入新邮箱", Toast.LENGTH_SHORT).show();
                } else if (!FormatUtil.isEmailFormat(newemail)) {
                    Toast.makeText(ModifyEmailActivity.this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
                } else {
                    String modiIn = apiImpl.getModifyEmailIn(newemail, token);
                    mOkHttpManager.getStringAsyn(modiIn, new OkHttpManager.ResultCallback<BaseResponse>(ModifyEmailActivity.this) {
                        @Override
                        public void onError(Request request, Exception e) {
                            Log.d(TAG, "error");
                        }

                        @Override
                        public void onResponse(BaseResponse response) {
                            super.onResponse(response);
                            if (response.isSuccess()) {
//                                Intent in = new Intent(ModifyEmailActivity.this, EmailVeri2Activity.class);
//                                startActivity(in);
                            } else {
                                Toast.makeText(ModifyEmailActivity.this, response.getErrorMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void onCommonError(int errorCode) {
        // 这里处理通用逻辑
        if (errorCode == USER_OUTLINE) {
            Toast.makeText(this,"你已在别的地方登录，你被迫下线，请重新登录！",Toast.LENGTH_SHORT).show();
            CachedInfo info= MyApplication.getInstance().getCachedInfo();
            info.clear();
            CacheCleaner.deleteFolderFile(getFilesDir()+"/cachedInfo",true);
            Intent in=new Intent(ModifyEmailActivity.this,LoginActivity.class);
            startActivity(in);
        }else if(errorCode==TOKEN_OVERTIME){
            Toast.makeText(this,"你的登录信息已过期，请重新登录",Toast.LENGTH_SHORT).show();
            CachedInfo info= MyApplication.getInstance().getCachedInfo();
            info.clear();
            CacheCleaner.deleteFolderFile(getFilesDir()+"/cachedInfo",true);
            Intent in=new Intent(ModifyEmailActivity.this,LoginActivity.class);
            startActivity(in);
        }else if(errorCode==SERVER_ERROR){
            Toast.makeText(this,"服务器内部错误，请重新登录",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(ModifyEmailActivity.this,LoginActivity.class);
            startActivity(in);
        }
    }
}
