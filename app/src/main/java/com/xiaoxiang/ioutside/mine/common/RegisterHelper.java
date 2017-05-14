package com.xiaoxiang.ioutside.mine.common;

import android.util.Log;

import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GMessage;
import com.xiaoxiang.ioutside.util.FormatUtil;
import com.xiaoxiang.ioutside.util.MD5Helper;

import okhttp3.Request;

/**
 * Created by 15119 on 2016/5/27.
 */

/**
 * 管理注册的helper类
 */
public class RegisterHelper {

    private static final String TAG = "RegisterHelper";

    private String smsCodeTemp;

    private boolean isSmsCodeAcquired;
    private boolean isRegisterByPhone;

    private String phoneNum;
    /**
     * 注册请求错误类型
     */
    public static final int TYPE_NO_ERROR = -1;
    public static final int TYPE_REQUEST_ERROR = 0;
    public static final int TYPE_RESPONSE_ERROR = 1;

    public interface OnRegisterListener {

        /**
         * 邮箱注册请求完成回调
         *
         * @param success   请求是否成功
         * @param errorMsg    错误信息
         * @param errorType 错误类型
         */
        void onEmailSent(boolean success, int errorType, String errorMsg);

        /**
         * 密码无效时回调
         *
         * @param pwd        密码
         * @param pwdConfirm 确认密码
         * @param errorCode  密码无效的原因
         */
        void onPasswordInvalid(String pwd, String pwdConfirm, int errorCode);

        /**
         * 验证昵称是否有效
         *
         * @param nickName
         * @param errorCode
         */
        void onNickNameInvalid(String nickName, int errorCode);

        /**
         * 手机或者邮箱号无效时回调
         *
         * @param PhoneOrEmail 邮箱或者手机号
         */
        void onEmailOrPhoneNumInvalid(String PhoneOrEmail);


        /**
         *
         * @param success
         * @param errorType
         * @param msg 如果请求成功并且成功获取验证码则是验证码，否则为错误信息
         */
        void onSmsCodeResult(boolean success, int errorType, String msg);

        /**
         *
         * @param success
         * @param errorType
         * @param errorMsg
         */
        void onRegistrationCommit(boolean success, int errorType, String errorMsg);
    }

    private OnRegisterListener mListener;

    /**
     * 验证密码的回调接口
     */
    private Validator.OnValidatePwdListener onValidatePwdListener;

    /**
     * 验证昵称的回调接口
     */
    private Validator.OnValidateNickNameListener onValidateNickNameListener;

    /**
     * 执行注册
     * @param emailOrPhoneNum
     * @param pwd
     * @param pwdConfirm
     * @param nickName
     */
    public void register(String emailOrPhoneNum, String pwd, String pwdConfirm, String nickName,
                         OnRegisterListener l) {

        mListener = l;

        setupListeners(l);

        if (!Validator.validatePassword(pwd, pwdConfirm, onValidatePwdListener) ||
                !Validator.validateNickName(nickName, onValidateNickNameListener)) {
            return;
        }

        if (FormatUtil.isEmailFormat(emailOrPhoneNum)) {

            isRegisterByPhone = false;
            registerByEmail(emailOrPhoneNum, pwd, nickName);

        } else if (FormatUtil.isPhoneNum(emailOrPhoneNum)) {

            isRegisterByPhone = true;
            phoneNum = emailOrPhoneNum;

            sendSmsCodeRequest(phoneNum);

        } else {

            if (l != null)
                l.onEmailOrPhoneNumInvalid(emailOrPhoneNum);
        }

    }


    /**
     * 设置回调接口
     *
     * @param l
     */
    private void setupListeners(final OnRegisterListener l) {

        onValidatePwdListener = new Validator.OnValidatePwdListener() {
            @Override
            public void onPasswordInvalid(String pwd, String pwdConfirm, int errorCode) {
                if (l != null)
                    l.onPasswordInvalid(pwd, pwdConfirm, errorCode);
            }
        };

        onValidateNickNameListener = new Validator.OnValidateNickNameListener() {
            @Override
            public void onNickNameInvalid(String email, int error) {
                if (l != null) {
                    l.onNickNameInvalid(email, error);
                }
            }
        };

    }

    /**
     * 邮箱注册
     *
     * @param email
     * @param pwd
     * @param nickName
     */
    private void registerByEmail(String email, String pwd, String nickName) {

        String url = new ApiInterImpl().getRegiByEmailIn(email, nickName, MD5Helper.getMd5(pwd));
        Log.d(TAG, "api for register by email -->" + url);

        final OkHttpManager manager = OkHttpManager.getInstance();
        manager.getStringAsyn(url, new OkHttpManager.ResultCallback<BaseResponse>() {

            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();

                if (mListener == null) return;

                mListener.onEmailSent(false, TYPE_REQUEST_ERROR, null);
            }

            @Override
            public void onResponse(BaseResponse response) {
                super.onResponse(response);

                if (mListener == null) return;

                if (response.isSuccess()) {
                    mListener.onEmailSent(true, TYPE_NO_ERROR, null);
                } else {
                    mListener.onEmailSent(false, TYPE_RESPONSE_ERROR, response.getErrorMessage());
                }

            }
        });

    }

    /**
     * 手机注册
     *
     * @param phoneNum
     */
    private void sendSmsCodeRequest(String phoneNum) {

        smsCodeTemp = null;
        isSmsCodeAcquired = true;

        String url = new ApiInterImpl().getRegiVeriIn(phoneNum);
        Log.d(TAG, url);

        OkHttpManager manager = OkHttpManager.getInstance();
        manager.getStringAsyn(url, new OkHttpManager.ResultCallback<BaseResponse<GMessage>>() {

            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();

                if (mListener != null) {
                    mListener.onSmsCodeResult(false, TYPE_REQUEST_ERROR, null);
                }
            }

            @Override
            public void onResponse(BaseResponse<GMessage> response) {
                super.onResponse(response);

                if (mListener == null) return;

                if (response.isSuccess()) {
                    mListener.onSmsCodeResult(true, TYPE_NO_ERROR, response.getData().getCyptograph());
                } else {
                    mListener.onSmsCodeResult(false, TYPE_RESPONSE_ERROR, response.getErrorMessage());
                }
            }
        });

    }

    /**
     * 重新获取验证码
     */
    public void reacquireSmsCode() {

        if (!isSmsCodeAcquired) {
            System.out.println("you can call this method only if you have called register()");
            return;
        }

        if (! isRegisterByPhone) {
            System.out.println("you can call this method only if you are registering by phone");
            return;
        }

        if (phoneNum == null) {
            System.out.println("phoneNum is null");
            return;
        }

        sendSmsCodeRequest(phoneNum);

    }

    /**
     * 验证码验证成功后调用此方法向服务器注册新用户
     * @param phoneNum
     * @param pwd
     * @param nickName
     */
    public void commitPhoneRegistration(String phoneNum, String pwd, String nickName) {

        String url = new ApiInterImpl().getRegiByPhoneIn(phoneNum, nickName, MD5Helper.getMd5(pwd));
        Log.d(TAG, "url for register by phone --> " + phoneNum);

        OkHttpManager manager = OkHttpManager.getInstance();
        manager.getStringAsyn(url, new OkHttpManager.ResultCallback<BaseResponse>() {

            @Override
            public void onError(Request request, Exception e) {
                if (mListener != null) {
                    mListener.onSmsCodeResult(false, TYPE_REQUEST_ERROR, null);
                }
                e.printStackTrace();
            }

            @Override
            public void onResponse(BaseResponse response) {
                super.onResponse(response);
                Log.d(TAG, response.toString());

                if (mListener == null) return;

                if (response.isSuccess()) {
                    mListener.onRegistrationCommit(true, TYPE_NO_ERROR, null);

                } else {
                    mListener.onRegistrationCommit(true, TYPE_RESPONSE_ERROR, response.getErrorMessage());
                }
            }
        });
    }


}


