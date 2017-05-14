package com.xiaoxiang.ioutside.mine.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.mine.activity.ForgetPwActivity;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GMessage;
import com.xiaoxiang.ioutside.util.FormatUtil;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.lang.reflect.Type;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;

/**
 * Created by 15119 on 2016/5/25.
 */
public class RetrievePwdFragment extends BaseFragment implements TextWatcher {

    private final String TAG = getClass().getSimpleName();

    private OkHttpManager manager = OkHttpManager.getInstance();

    @Bind(R.id.et_by_what)
    EditText etByWhat;
    @Bind(R.id.btn_next_step)
    Button btnNextStep;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_retrieve_password, container, false);
        ButterKnife.bind(this, v);
        initView();
        return v;
    }

    public void initView() {
        etByWhat.addTextChangedListener(this);
    }



    @OnClick({R.id.btn_next_step})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_next_step :
                retrievePassword(etByWhat.getText().toString());
                break;
        }
    }

    @Override
    protected String getTitle() {
        return "找回密码";
    }

    private void retrievePassword(String byWhat) {

        if (TextUtils.isEmpty(byWhat)) showErrorDialog("邮箱或手机号不能为空");

        if (FormatUtil.isEmailFormat(byWhat)) {
            showWaitingDialog("验证邮件正在发送，请稍后");
            retrievePasswordByEmail(byWhat);
        } else if (FormatUtil.isPhoneNum(byWhat)) {
            showWaitingDialog("验证码发送中，请稍后...");
            retrievePasswordByPhoneNum(byWhat);
        } else {
            showErrorDialog("请填写正确的手机号或邮箱");
        }

    }

    private void retrievePasswordByEmail(final String email) {

        String apiEmail = new ApiInterImpl().getFindPwByEmailIn(email);
        OkHttpManager manager = OkHttpManager.getInstance();

        manager.getStringAsyn(apiEmail, new OkHttpManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {
                hideWaitingDialog();
                showErrorDialog("请求发送失败");
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response) {

                super.onResponse(response);
                hideWaitingDialog();

                FragmentManager fm = getFragmentManager();

                Fragment f = ForgetPwActivity.getFragment(fm,
                        ForgetPwActivity.TAG_FRAGMENT_EMAIL_VERIFY_RESULT);

                Bundle args = new Bundle();
                args.putString("email", email);
                f.setArguments(args);

               fm.beginTransaction()
                        .hide(RetrievePwdFragment.this)
                        .setCustomAnimations(R.animator.fragment_enter_animation, R.animator.fragment_exit_animation)
                        .add(R.id.fragment_container, f, ForgetPwActivity.TAG_FRAGMENT_EMAIL_VERIFY_RESULT)
                        .addToBackStack(null)
                        .commit();
                Log.d(TAG,response);
            }
        });
    }

    private void retrievePasswordByPhoneNum(final String phoneNum) {

        String phoneIn = new ApiInterImpl().getFindPwByPhoneIn(phoneNum);
        Log.d(TAG,phoneIn);

        manager.getStringAsyn(phoneIn, new OkHttpManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {
                hideWaitingDialog();
                showErrorDialog("请求发送失败");
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Log.d(TAG, response);

                hideWaitingDialog();

                Type objectType = new TypeToken<BaseResponse<GMessage>>() {
                }.getType();

                BaseResponse<GMessage> msgResponse = new Gson().fromJson(response, objectType);

                if (msgResponse.getData() != null) {
                    String smsMd5 = msgResponse.getData().getCyptograph();
                    startPhoneVerifyFragment(smsMd5, phoneNum);
                    ToastUtils.show("验证码已成功发送");
                   // Log.d(TAG, smsMd5);
                } else {
                    showErrorDialog(msgResponse.getErrorMessage());
                }

            }
        });
    }

    private void startPhoneVerifyFragment(String smsMd5, String phoneNum) {

        Fragment f =  ForgetPwActivity.getFragment(getFragmentManager(),
                ForgetPwActivity.TAG_FRAGMENT_PHONE_VERIFY);

        if (f.isAdded()) {
            Log.d(TAG, "startPhoneVerifyFragment --> " + "fragment is already active, so return");
            return;
        }

        //deliver md5 of sms code and the phone num to next fragment
        Bundle args = new Bundle();
        args.putString("smsMd5", smsMd5);
        args.putString("phoneNum", phoneNum);
        f.setArguments(args);

        getFragmentManager().beginTransaction()
                .hide(this)
                .setCustomAnimations(R.animator.fragment_enter_animation, R.animator.fragment_exit_animation)
                .add(R.id.fragment_container, f, ForgetPwActivity.TAG_FRAGMENT_PHONE_VERIFY)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (! TextUtils.isEmpty(etByWhat.getText().toString())) {
            btnNextStep.setEnabled(true);
        } else {
            btnNextStep.setEnabled(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
