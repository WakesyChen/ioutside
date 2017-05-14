package com.xiaoxiang.ioutside.mine.common;

import android.os.Build;
import android.provider.Settings;

import com.xiaoxiang.ioutside.common.MyApplication;


/**
 * Created by 15119 on 2016/5/24.
 */
public class SystemInfo {
    public static String systemVersion = Build.VERSION.RELEASE;
    public static String uniqueCode = Settings.Secure.getString(MyApplication.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
}
