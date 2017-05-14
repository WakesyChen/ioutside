package com.xiaoxiang.ioutside.mine.activity;

import android.app.Activity;
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
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyPhoneActivity extends Activity {
    private String TAG=getClass().getSimpleName();

    @Bind(R.id.iv_back)
    ImageView imgBackbtn;
    @Bind(R.id.tv_title)
    TextView tvTitleSetting;
    @Bind(R.id.edt_newphone)
    EditText edtNewphone;
    @Bind(R.id.btn_sure_phone)
    Button btnSure;

    private String token;
    private ApiInterImpl apiImpl;
    private OkHttpManager mOkHttpManager;
    private Gson gson;
    private BaseResponse modifyRe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_phone);
        ButterKnife.bind(this);
        tvTitleSetting.setText("绑定新手机");
        token = getIntent().getStringExtra("token");
        Log.d(TAG,token);
        gson = new Gson();
        apiImpl = new ApiInterImpl();
        mOkHttpManager = OkHttpManager.getInstance();
    }

    @OnClick({R.id.iv_back, R.id.btn_sure_phone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_sure_phone:
                String newphone=edtNewphone.getText().toString();
                if(newphone.equals("")){
                    Toast.makeText(ModifyPhoneActivity.this,"请输入手机号",Toast.LENGTH_SHORT).show();
                }else{
//                    Intent in=new Intent(ModifyPhoneActivity.this,PhoneVerifiActivity.class);
//                    in.putExtra("phone",newphone);
//                    startActivity(in);
                }
                break;
        }
    }
}
