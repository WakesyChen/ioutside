package com.xiaoxiang.ioutside.mine.common;

import android.text.TextUtils;
import android.util.Log;

import com.xiaoxiang.ioutside.util.FormatUtil;


/**
 * Created by 15119 on 2016/5/27.
 */

/**
 * 验证格式的方法都可以往这里塞
 */
public class Validator {

    public static final String TAG = "Validator";

    public static final int ERROR_PWD_INCONSISTENT = 1;
    public static final int ERROR_PWD_ILLEGAL = 2;
    public static final int ERROR_PWD_TOO_SHORT = 3;
    public static final int ERROR_PWD_TOO_LONG = 4;

    public static final int ERROR_NICK_NAME_EMPTY = 0;
    public static final int ERROR_NICK_NAME_TOO_LONG = 1;
    public static final int ERROR_NICK_NAME_ILLEGAL = 2;

    public interface OnValidatePwdListener {
        void onPasswordInvalid(String pwd, String pwdConfirm, int errorCode);
    }

    public interface OnValidateNickNameListener {
        void onNickNameInvalid(String email, int error);
    }


    /**
     * @param pwd
     * @param pwdConfirm
     * @return true if pwd is valid and consistent with pwsConfirm, false otherwise
     */
    public static boolean validatePassword(String pwd, String pwdConfirm, OnValidatePwdListener listener) {

        if (! TextUtils.equals(pwd, pwdConfirm)) {
            if (listener != null) {
                listener.onPasswordInvalid(pwd, pwdConfirm, ERROR_PWD_INCONSISTENT);
            }
            return false;
        }

        if (! FormatUtil.isPasswordFormat(pwd)) {
            if (listener != null) {
                listener.onPasswordInvalid(pwd, pwdConfirm, ERROR_PWD_ILLEGAL);
                return false;
            }
        }

        if (pwd.length() < 8) {
            if (listener != null)
                listener.onPasswordInvalid(pwd, pwdConfirm, ERROR_PWD_TOO_SHORT);
            return false;
        } else if (pwd.length() > 16) {
            if (listener != null)
                listener.onPasswordInvalid(pwd, pwdConfirm, ERROR_PWD_TOO_LONG);
            return false;
        }
        return true;
    }

    /**
     * @param nickName
     * @return true if nickName is valid, false otherwise
     */
    public static boolean validateNickName(String nickName, OnValidateNickNameListener mListener) {

        if (!FormatUtil.isLegalNickName(nickName)) {
            if (mListener != null)
                mListener.onNickNameInvalid(nickName, ERROR_NICK_NAME_ILLEGAL);
            return false;
        }

        if (getNickNameLength(nickName) == -1) {
            if (mListener != null)
                mListener.onNickNameInvalid(nickName, ERROR_NICK_NAME_ILLEGAL);
            return false;
        } else if (getNickNameLength(nickName) > 16) {
            if (mListener != null)
                mListener.onNickNameInvalid(nickName, ERROR_NICK_NAME_TOO_LONG);
            return false;
        }

        return true;
    }

    private static int getNickNameLength(String nickName) {

        final String REGEX_ENG = "[a-zA-Z]";
        final String REGEX_CHN = "[\u4e00-\u9fa5]";

        int length = 0;
        String temp;
        for (int i = 0; i < nickName.length(); i++) {

            temp = nickName.substring(i, i + 1);
            if (temp.matches(REGEX_ENG)) {
                length ++;
            } else if (temp.matches(REGEX_CHN)) {
                length += 2;
            } else {
                length = -1;
                break;
            }
        }

        Log.d(TAG, "nickName --> " + length);
        return length;
    }


}
