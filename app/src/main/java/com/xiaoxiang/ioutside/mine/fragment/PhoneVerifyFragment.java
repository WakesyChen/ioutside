package com.xiaoxiang.ioutside.mine.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.util.FormatUtil;
import com.xiaoxiang.ioutside.util.MD5Helper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;

/**
 * Created by 15119 on 2016/5/25.
 */
public class PhoneVerifyFragment extends BaseFragment implements TextWatcher {

    private final String TAG = getClass().getSimpleName();

    private String smsMd5;
    private String phoneNum;

    @Bind(R.id.tv_phone_num)
    TextView tvPhoneNum;
    @Bind(R.id.et_codes)
    EditText etCodes;
    @Bind(R.id.et_pwd)
    EditText etPwd;
    @Bind(R.id.et_pwd_confirm)
    EditText etPwdConfirm;
    @Bind(R.id.btn_next_step)
    Button btnNestStep;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        smsMd5 = getArguments().getString("smsMd5");
        phoneNum = getArguments().getString("phoneNum");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_phone_verify, container, false);
        ButterKnife.bind(this, v);
        initView();
        return v;
    }

    private void initView() {
        etCodes.addTextChangedListener(this);
        etPwd.addTextChangedListener(this);
        etPwdConfirm.addTextChangedListener(this);
        tvPhoneNum.setText(phoneNum);
    }

    @OnClick({R.id.btn_next_step})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_next_step :
                if (! TextUtils.equals(smsMd5, MD5Helper.getMd5(etCodes.getText().toString())) ) {
                    showErrorDialog("验证码错误");
                } else if (checkPassword()) {
                    modifyPassword(etPwd.getText().toString());
                }


        }
    }

    private boolean checkPassword() {

        final String pwd = etPwd.getText().toString();
        final String pwdConfirm = etPwdConfirm.getText().toString();

        if(! TextUtils.equals(pwd, pwdConfirm)) {
            showErrorDialog("两次输入的密码不同");
            return false;
        }

        if (! FormatUtil.isPasswordFormat(pwd)) {
            showErrorDialog("密码无效");
            return false;
        }

        return true;
    }

    private void modifyPassword(String newPwd) {

        String pwdMd5 = MD5Helper.getMd5(newPwd);
        String newpassIn= new ApiInterImpl().getSetNewPwIn(pwdMd5, phoneNum);
        Log.d(TAG,newpassIn);

        OkHttpManager manager = OkHttpManager.getInstance();
        manager.getStringAsyn(newpassIn, new OkHttpManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                showErrorDialog("修改密码失败");
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Log.d(TAG,response);
                showErrorDialog("密码修改成功");
                getActivity().finish();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    //------------
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (! TextUtils.isEmpty(etCodes.getText().toString()) &&
                ! TextUtils.isEmpty(etPwd.getText().toString()) &&
                ! TextUtils.isEmpty(etPwdConfirm.getText().toString())) {
            btnNestStep.setEnabled(true);
        } else {
            btnNestStep.setEnabled(false);
        }
    }

    //---------------

    @Override
    protected String getTitle() {
        return "手机验证";
    }


}
