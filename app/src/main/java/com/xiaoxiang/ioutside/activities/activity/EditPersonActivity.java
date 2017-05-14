package com.xiaoxiang.ioutside.activities.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.activities.model.TravelerInfor;
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
public class EditPersonActivity extends Activity implements View.OnClickListener {
    @Bind(R.id.common_back3)
    ImageView common_back3;
    @Bind(R.id.personlist_delete)
    TextView personlist_delete;
    @Bind(R.id.personlist_editName)
    EditText personlist_editName;
    @Bind(R.id.personlist_editPhone)
    EditText personlist_editPhone;
    @Bind(R.id.personlist_editID)
    EditText personlist_editID;
    @Bind(R.id.personlist_editPassport)
    EditText personlist_editPassport;
    @Bind(R.id.personlist_editDone)
    TextView personlist_editDone;
    private String name;
    private String personId;//身份证
    private String phone;
    private String passport;
    private int id;//游客id
    private String token;
    private static final String TAG = "EditPersonActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editperson);
        ButterKnife.bind(this);

        common_back3.setOnClickListener(this);
        personlist_delete.setOnClickListener(this);
        personlist_editDone.setOnClickListener(this);


        TravelerInfor travelerInfor = (TravelerInfor) getIntent().getSerializableExtra("travelerInfor");
        id = getIntent().getIntExtra("id", 1);
        token = getIntent().getStringExtra("token");
        String name_old = travelerInfor.getName();
        String personId_old = travelerInfor.getPersonID();
        String phone_old = travelerInfor.getPhone();
        String passport_old = travelerInfor.getPassport();
        personlist_editName.setText(name_old);
        personlist_editPhone.setText(phone_old);
        personlist_editID.setText(personId_old);
        personlist_editPassport.setText(passport_old);

    }


    @Override
    public void onClick(View v) {
        name = personlist_editName.getText().toString().trim();
        phone = personlist_editPhone.getText().toString().trim();
        personId = personlist_editID.getText().toString().trim();
        passport = personlist_editPassport.getText().toString().trim();
        switch (v.getId()) {


            case R.id.common_back3:
                finish();
                break;

            case R.id.personlist_delete:


                ApiInterImpl api = new ApiInterImpl();
                OkHttpManager okHttpManager = OkHttpManager.getInstance();
                okHttpManager.postAsyn(api.deleteTravelerInfor(token, id), new OkHttpManager.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        super.onError(request, e);
                    }

                    @Override
                    public void onResponse(String response) {
                        super.onResponse(response);
                        Gson gson = new Gson();
                        Type type = new TypeToken<BaseResponse>() {
                        }.getType();
                        BaseResponse baseResponse = gson.fromJson(response, type);
                        if (baseResponse.isSuccess()) {
                            ToastUtils.show("删除成功！");
                            finish();

                        } else {
                            ToastUtils.show("删除失败，" + baseResponse.getErrorMessage());
                        }
                    }
                });
                break;


            case R.id.personlist_editDone:
                if (!FormatUtil.isLegalNickName(name)) {
                    ToastUtils.show("请输入合法名字!");

                } else {
                    if (!FormatUtil.isPhoneNum(phone)) {
                        ToastUtils.show("请输入正确电话号码!");

                    } else {
                        if (!FormatUtil.checkIdCard(personId)) {
                            ToastUtils.show("请输入正确身份证号！");
                        } else {

                            ApiInterImpl api1 = new ApiInterImpl();
                            OkHttpManager okHttpManager1 = OkHttpManager.getInstance();
                            Log.i(TAG, "onClick: " + token + "-" + id + "-" + name + "-" + personId + "-" + passport + "-" + phone);
                            okHttpManager1.postAsyn(api1.modifyTravelerInfor(token, id, name, personId, passport, phone), new OkHttpManager.ResultCallback<String>() {
                                @Override
                                public void onError(Request request, Exception e) {
                                    super.onError(request, e);
                                }

                                @Override
                                public void onResponse(String response) {
                                    super.onResponse(response);
                                    Gson gson = new Gson();
                                    Log.i(TAG, "onResponse: modify:" + response);
                                    Type type = new TypeToken<BaseResponse>() {
                                    }.getType();
                                    BaseResponse baseResponse = gson.fromJson(response, type);
                                    if (baseResponse.isSuccess()) {
                                        ToastUtils.show("修改成功！");
                                        finish();

                                    } else {
                                        ToastUtils.show("修改失败，" + baseResponse.getErrorMessage());
                                    }

                                }
                            });
                        }
                        break;

                    }

                }
        }
    }
}