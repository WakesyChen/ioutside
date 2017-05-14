package com.xiaoxiang.ioutside.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;

/**
 * Created by 10648 on 2016/4/11 0011.意见反馈
 */
public class MyFeedbackActivity extends AppCompatActivity implements Constants,
        OkHttpManager.ResultCallback.CommonErrorListener, TextWatcher {
    String TAG = getClass().getSimpleName();
    @Bind(R.id.iv_back)
    ImageView imgBackbtn;
    @Bind(R.id.tv_title)
    TextView tvTitleSetting;
    @Bind(R.id.edt_feedback)
    EditText edtFeedback;
    @Bind(R.id.btn_feedback_submit)
    Button btnSubmit;

    private ApiInterImpl mApi;
    private Gson gson;
    private OkHttpManager mOkHttpManager;
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_my_feedback);
        ButterKnife.bind(this);
        token = getIntent().getStringExtra("token");
        Log.d(TAG, token);
        tvTitleSetting.setText("我的反馈");
        edtFeedback.addTextChangedListener(this);
        gson = new Gson();
        mApi = new ApiInterImpl();
        mOkHttpManager = OkHttpManager.getInstance();
    }


    @OnClick({R.id.iv_back, R.id.btn_feedback_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_feedback_submit:
                OkHttpManager.Param tokenparam = new OkHttpManager.Param("token", token);
                Log.d(TAG, "测试一下token好不好药品" + token);
                OkHttpManager.Param content = new OkHttpManager.Param("content", edtFeedback.getText().toString());

                mOkHttpManager.postAsyn(mApi.getFeedBackIn(), new OkHttpManager.ResultCallback<BaseResponse>(MyFeedbackActivity.this) {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(MyFeedbackActivity.this, "网络有点问题哦！", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "反馈失败");
                    }

                    @Override
                    public void onResponse(BaseResponse response) {
                        super.onResponse(response);
                        if (response.isSuccess()) {
                            Toast.makeText(MyFeedbackActivity.this, "反馈成功！谢谢您的建议和意见！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MyFeedbackActivity.this, "反馈失败！请检查网络！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, tokenparam, content);
                finish();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(edtFeedback.getText().toString())) {
            btnSubmit.setEnabled(false);
        } else {
            btnSubmit.setEnabled(true);
        }
    }

    @Override
    public void onCommonError(int errorCode) {
        if (errorCode == USER_OUTLINE) {
            Toast.makeText(this, "你已在别的地方登录，你被迫下线，请重新登录！", Toast.LENGTH_SHORT).show();
            CachedInfo info = MyApplication.getInstance().getCachedInfo();
            info.clear();
            CacheCleaner.deleteFolderFile(getFilesDir() + "/cachedInfo", true);
            Intent in = new Intent(MyFeedbackActivity.this, LoginActivity.class);
            startActivity(in);
        } else if (errorCode == TOKEN_OVERTIME) {
            Toast.makeText(this, "你的登录信息已过期，请重新登录", Toast.LENGTH_SHORT).show();
            CachedInfo info = MyApplication.getInstance().getCachedInfo();
            info.clear();
            CacheCleaner.deleteFolderFile(getFilesDir() + "/cachedInfo", true);
            Intent in = new Intent(MyFeedbackActivity.this, LoginActivity.class);
            startActivity(in);
        } else if (errorCode == SERVER_ERROR) {
            Toast.makeText(this, "服务器内部错误，请重新登录", Toast.LENGTH_SHORT).show();
            Intent in = new Intent(MyFeedbackActivity.this, LoginActivity.class);
            startActivity(in);
        }
    }
}
