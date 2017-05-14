package com.xiaoxiang.ioutside.mine.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.mine.activity.LoginActivity;
import com.xiaoxiang.ioutside.mine.common.Constants;
import com.xiaoxiang.ioutside.mine.common.RegisterHelper;
import com.xiaoxiang.ioutside.mine.common.Validator;
import com.xiaoxiang.ioutside.mine.dialog.ConfirmDialog;
import com.xiaoxiang.ioutside.util.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15119 on 2016/5/26.
 */
public class RegisterFragment extends BaseFragment
        implements TextWatcher, RegisterHelper.OnRegisterListener
        , SmsCodeValidateFragment.OnValidateSmsCodeListener {

    private final String TAG = getClass().getSimpleName();
    private RegisterHelper registerHelper;

    private String emailOrPhoneNum;
    private String pwd;
    private String nickName;

    @Bind(R.id.btn_next_step)
    Button btnNextStep;
    @Bind(R.id.et_pwd)
    EditText etPwd;
    @Bind(R.id.et_pwd_confirm)
    EditText etPwdConfirm;
    @Bind(R.id.et_nick_name)
    EditText etNickName;
    @Bind(R.id.et_user_name)
    EditText etEmailOrPhoneNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (registerHelper == null) {
            registerHelper  = new RegisterHelper();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, v);
        initView();
        return v;
    }

    @Override
    protected String getTitle() {
        return "欢迎加入爱户外";
    }

    private void initView() {
        etNickName.addTextChangedListener(this);
        etPwd.addTextChangedListener(this);
        etPwdConfirm.addTextChangedListener(this);
        etEmailOrPhoneNum.addTextChangedListener(this);
    }

    @OnClick({R.id.btn_next_step})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next_step:
                showWaitingDialog("注册请求发送中，请稍后...");
                register();
        }
    }

    private void register() {

        emailOrPhoneNum = etEmailOrPhoneNum.getText().toString();
        pwd = etPwd.getText().toString();
        String pwdConfirm = etPwdConfirm.getText().toString();
        nickName = etNickName.getText().toString();

        registerHelper.register(emailOrPhoneNum ,pwd, pwdConfirm, nickName, this);
    }

    //--------------------------- TextWatch start -------------------//

    /**
     * 监测输入框的文字输入，除非都不为空，否则 button 状态为 enable
     */

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (!TextUtils.isEmpty(etNickName.getText().toString()) &&
                !TextUtils.isEmpty(etPwd.getText().toString()) &&
                !TextUtils.isEmpty(etPwdConfirm.getText().toString()) &&
                !TextUtils.isEmpty(etEmailOrPhoneNum.getText().toString())) {
            btnNextStep.setEnabled(true);
        } else {
            btnNextStep.setEnabled(false);
        }
    }

    //---------------------------- TextWatch end -----------------------------//


    //-------------------------- OnRegisterListener start -------------------------//

    @Override
    public void onEmailSent(boolean success, int errorType, String errorMsg) {
        hideWaitingDialog();
        if (!success) {
            if (errorType == RegisterHelper.TYPE_REQUEST_ERROR) {
                showErrorDialog("注册失败！请检查网络设置");
            } else if (errorType == RegisterHelper.TYPE_RESPONSE_ERROR) {
                showErrorDialog(errorMsg);
            }
            return;
        }

        showTipDialog();
    }

    private void showTipDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("邮箱验证")
                .setMessage("验证邮件已发送，请尽快查收验证\n（邮件可能被识别为垃圾邮件）")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getActivity().finish();

                    }
                })
                .setCancelable(false)
                .create();

        alertDialog.show();
    }

    @Override
    public void onSmsCodeResult(boolean success, int errorType, String msg) {
        hideWaitingDialog();
        if (!success) {
            if (errorType == RegisterHelper.TYPE_REQUEST_ERROR) {
                showErrorDialog("验证码发送失败！请检查网络设置");
            } else if (errorType == RegisterHelper.TYPE_RESPONSE_ERROR) {
                showErrorDialog(msg);
            }
            return;
        }
        ToastUtils.show(getActivity(), "验证码已发送");
        onReceiverSmsCode(msg);
    }

    @Override
    public void onPasswordInvalid(String pwd, String pwdConfirm, int errorCode) {
        hideWaitingDialog();
        switch (errorCode) {
            case Validator.ERROR_PWD_TOO_SHORT:
                showErrorDialog("密码太短");
                break;
            case Validator.ERROR_PWD_TOO_LONG:
                showErrorDialog("密码太长了哦");
                break;
            case Validator.ERROR_PWD_INCONSISTENT:
                showErrorDialog("两次输入的密码不一致");
                break;
            case Validator.ERROR_PWD_ILLEGAL:
                showErrorDialog("密码应为8-16位数字或字母");
        }
    }

    @Override
    public void onNickNameInvalid(String nickName, int errorCode) {
        hideWaitingDialog();
        if (errorCode == Validator.ERROR_NICK_NAME_ILLEGAL) {
            showConfirmDialog("昵称只能是中英文字符哦~", "知道啦");
        } else if (errorCode == Validator.ERROR_NICK_NAME_TOO_LONG) {
            showConfirmDialog("昵称有点长哦~", "我再减几个字");
        }
    }

    @Override
    public void onEmailOrPhoneNumInvalid(String PhoneOrEmail) {
        hideWaitingDialog();
        showErrorDialog("请输入有效的邮箱或者手机号");
    }

    @Override
    public void onRegistrationCommit(boolean success, int errorType, String errorMsg) {
        hideWaitingDialog();
        if (! success) {
            if (errorType == RegisterHelper.TYPE_REQUEST_ERROR) {
                showErrorDialog("注册失败！请检查网络设置");
            } else if (errorType == RegisterHelper.TYPE_RESPONSE_ERROR) {
                showErrorDialog(errorMsg);
            }
            return;
        }


        ToastUtils.show(getActivity(), "注册成功");
        closeMainActivity();
        login();
        getActivity().finish();
    }

    //------------------------------ OnRegisterListener end -----------------------//


    private void closeMainActivity() {
        Intent closeMainActivity = new Intent("com.xiaoxiang.ioutside.MAIN_FINISH_SELF");
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(closeMainActivity);
    }

    private void login() {
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
    }

    private void showConfirmDialog(String msg, String buttonText) {
        new ConfirmDialog(getActivity(), msg, buttonText).show();
    }


    //--------------------------- OnValidateSmsCodeListener start --------------------------//

    @Override
    public void onValidateSmsCodeSuccess(String smsCode) {
        registerHelper.commitPhoneRegistration(emailOrPhoneNum, pwd, nickName);
    }

    /**
     * 重新获取验证码
     */
    @Override
    public void onReacquireValidateSmsCode() {
        showWaitingDialog("正在发送验证码，请稍后...");
        registerHelper.reacquireSmsCode();
    }

    //--------------------------- OnValidateSmsCodeListener end --------------------------//


    private void onReceiverSmsCode(String smsCodeMd5) {

        SmsCodeValidateFragment f;

        //如果 fragment 存在，则只更新 smscode
        if (getFragmentManager().findFragmentByTag(Constants.tag.FRAGMENT_SMS_CODE_VALIDATE) != null) {
            f = (SmsCodeValidateFragment) getFragmentManager().findFragmentByTag(Constants.tag.FRAGMENT_SMS_CODE_VALIDATE);
            f.onSmsCodeUpdated(smsCodeMd5);
            return;
        }

        f = new SmsCodeValidateFragment();
        f.setOnValidateSmsCodeListener(this);

        final String pwd = etPwd.getText().toString();
        final String phoneNum = etEmailOrPhoneNum.getText().toString();
        Bundle args =  new Bundle();
        args.putString(Constants.key.PWD, pwd);
        args.putString(Constants.key.PHONE_NUM, phoneNum);
        args.putString(Constants.key.SMS_CODE, smsCodeMd5);

        f.setArguments(args);

        getFragmentManager()
                .beginTransaction()
                .hide(this)
                .add(R.id.fragment_container, f, Constants.tag.FRAGMENT_SMS_CODE_VALIDATE)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
