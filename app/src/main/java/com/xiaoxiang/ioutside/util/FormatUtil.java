package com.xiaoxiang.ioutside.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by oubin6666 on 2016/5/1.
 */
public class FormatUtil {

    /*
    * 检查邮箱格式
    * */
    public static boolean isEmailFormat(String email){
        Pattern emailPattern=Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+\\.[a-zA-Z0-9_-]+$");
        Matcher emailMatcher=emailPattern.matcher(email);
        return emailMatcher.find();
    }


    /**
     * 只检查密码格式
     * @param password
     * @return
     */
    public static boolean isPasswordFormat(String password) {
        final String REGEX_PWD = "^[a-zA-Z0-9]*$";
        return Pattern.matches(REGEX_PWD, password);
    }

    /**
     * 检查密码格式和数位
     * @param password
     * @return
     */
    public static boolean isPasswordValid(String password) {
        final String REGEX_PWD = "^[a-zA-Z0-9]{8,16}$";
        return Pattern.matches(REGEX_PWD, password);
    }

    /**
     *
     * @param phoneNum
     * @return true if it's in valid format of phone number
     */
    public static boolean isPhoneNum(String phoneNum) {
        final String REGEX_MOBILE = "^1(3[0-9]|4[57]|5[0-35-9]|8[0-9]|70)\\d{8}$";
       // final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        return Pattern.matches(REGEX_MOBILE, phoneNum);
    }


    /**
     * 规则: 中英文字符，可组合；
     * 注意: 这里只对形式进行检查，并没有对长度进行检查
     * 因为需要在代码中对长度进行判断并给出提示
     * @param nickName
     * @return true if nickName is valid
     */
    public static boolean isLegalNickName(String nickName) {
        final String REGEX_NICK_NAME = "^[a-zA-z\u4e00-\u9fff]*$";
        return Pattern.matches(REGEX_NICK_NAME, nickName);
    }

    /**
     * 验证身份证号码
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIdCard(String idCard) {
        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex,idCard);
    }

}
