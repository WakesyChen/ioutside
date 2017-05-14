package com.xiaoxiang.ioutside.activities.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.Api;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.util.FormatUtil;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.lang.reflect.Type;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * Created by Wakesy on 2016/8/18.
 */
public class AddPersonActivity  extends Activity implements View.OnClickListener{

    @Bind(R.id.common_back2)
    ImageView common_back;
    @Bind(R.id.personlist_addName)
    EditText personlist_addName;
    @Bind(R.id.personlist_addPhone)
    EditText personlist_addPhone;
    @Bind(R.id.personlist_addID)
    EditText personlist_addID;
    @Bind(R.id.personlist_addHuZhao)
    EditText personlist_addHuZhao;
    @Bind(R.id.personlist_addSave)
    TextView personlist_addSave;

    private  String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addperson);
        ButterKnife.bind(this);
        initData();
        initEvent();
    }

    private void initEvent() {

    }

    private void initData() {

        token=getIntent().getStringExtra("token");
        common_back.setOnClickListener(this);
        personlist_addSave.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_back2:{
                finish();


                break;
            }
            case R.id.personlist_addSave:
                String newName=personlist_addName.getText().toString().trim();
                String newPhone=personlist_addPhone.getText().toString().trim();
                String newId=personlist_addID.getText().toString().trim();
                String newPassport=personlist_addHuZhao.getText().toString().trim();
                if (!FormatUtil.isLegalNickName(newName)) {
                    ToastUtils.show("请输入合法名字!");

                } else {
                    if (!FormatUtil.isPhoneNum(newPhone)) {
                        ToastUtils.show("请输入正确电话号码!");

                    } else {
                        if (!FormatUtil.checkIdCard(newId)) {
                            ToastUtils.show("请输入正确身份证号！");
                        } else {
                            ApiInterImpl api=new ApiInterImpl();
                            OkHttpManager okHttpManager=OkHttpManager.getInstance();
                            okHttpManager.postAsyn(api.addNewTraveleInfo(token,newId,newName,newPhone,newPassport),new OkHttpManager.ResultCallback<String>(){

                                @Override
                                public void onError(Request request, Exception e) {
                                    super.onError(request, e);
                                }

                                @Override
                                public void onResponse(String response) {
                                    super.onResponse(response);
                                    Gson gson=new Gson();
                                    Type type=new TypeToken<BaseResponse>(){}.getType();
                                    BaseResponse baseResponse=gson.fromJson(response,type);
                                    if (baseResponse.isSuccess()) {
                                        ToastUtils.show("保存成功！");
                                        finish();
                                    } else {
                                        ToastUtils.show(""+baseResponse.getErrorMessage());
                                    }

                                }
                            });

                        }
                    }
                }


                break;





        }

    }
}
