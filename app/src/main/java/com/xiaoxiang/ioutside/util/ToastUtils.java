package com.xiaoxiang.ioutside.util;

import android.content.Context;
import android.widget.Toast;

import com.xiaoxiang.ioutside.common.MyApplication;

/**
 * Created by 15119 on 2016/5/24.
 */
public class ToastUtils {

    public static void show(Context context, String msg) {
        Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void show(String msg) {
        Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }

}
