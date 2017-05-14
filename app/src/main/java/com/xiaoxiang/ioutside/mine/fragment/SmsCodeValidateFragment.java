package com.xiaoxiang.ioutside.mine.fragment;

/**
 * Created by 15119 on 2016/5/28.
 */

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.xiaoxiang.ioutside.mine.common.Constants;
import com.xiaoxiang.ioutside.util.MD5Helper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 手机验证码验证的Fragment
 */
public class SmsCodeValidateFragment extends BaseFragment implements TextWatcher {

    private String TAG = getClass().getSimpleName();

    private String mPhoneNum;
    private String mSmsCodeMd5;

    @Bind(R.id.tv_phone_num)
    TextView tvPhoneNum;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.tv_get_sms_code)
    TextView tvGetSmsCode;
    @Bind(R.id.tv_protocol)
    TextView tvProtocol;
    @Bind(R.id.btn_finish)
    Button btnFinish;
    
    public interface OnValidateSmsCodeListener {
        void onValidateSmsCodeSuccess(String smsCodeMd5);
        void onReacquireValidateSmsCode();
    }
    
    private OnValidateSmsCodeListener onValidateSmsCodeListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mPhoneNum = getArguments().getString(Constants.key.PHONE_NUM);
            mSmsCodeMd5 = getArguments().getString(Constants.key.SMS_CODE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sms_code_validate, container, false);
        ButterKnife.bind(this, v);
        initView();
        startCountDown();
        return v;
    }
    
    private void initView() {
        tvPhoneNum.setText(mPhoneNum);
        tvGetSmsCode.setClickable(false);
    }

    @Override
    public String getTitle() {
        return "手机验证";
    }

    //这里有个坑，tvGetSmsCode 只要在这里设置了点击事件，那么就一定会变成 clickable
    @OnClick({R.id.tv_protocol, R.id.btn_finish, R.id.tv_get_sms_code})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_protocol:
                showProtocolDialog();
                break;
            case R.id.btn_finish:
                validateSmsCode(etCode.getText().toString());
                break;
            case R.id.tv_get_sms_code:
                tvGetSmsCode.setClickable(false);
                startCountDown();
                onValidateSmsCodeListener.onReacquireValidateSmsCode();
                break;
        }
    }

    /**
     * 验证码倒计时
     */
    private CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            tvGetSmsCode.setText(getString(R.string.tv_re_get_sms_code, millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            tvGetSmsCode.setClickable(true);
            tvGetSmsCode.setText("重新获取");
        }
    };

    private void startCountDown() {
        Log.d(TAG, "startCountDown() --> called");
        timer.start();
    }

    private void showProtocolDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(new TextView(getActivity()));
        loadProtocol();
    }

    /**
     * 将协议加载到对话框，之后可能会将这个抽出作为一个类
     */
    private void loadProtocol() {

    }

    private void validateSmsCode(String smsCode) {

        String smsCodeMd5 = MD5Helper.getMd5(smsCode);
        if (! TextUtils.equals(smsCodeMd5, mSmsCodeMd5)) {
            showErrorDialog("验证码不正确");
        } else {
            if (onValidateSmsCodeListener == null) return;
            onValidateSmsCodeListener.onValidateSmsCodeSuccess(smsCodeMd5);
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
        if (! TextUtils.isEmpty(etCode.getText().toString())) {
            btnFinish.setEnabled(true);
        } else {
            btnFinish.setEnabled(false);
        }
    }

    /**
     * 设置验证成功的监听器
     */
    public void setOnValidateSmsCodeListener(OnValidateSmsCodeListener listener) {
        this.onValidateSmsCodeListener = listener;
    }

    public void onSmsCodeUpdated(String smsCodeMd5) {
        mSmsCodeMd5 = smsCodeMd5;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

