package com.xiaoxiang.ioutside.mine.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xiaoxiang.ioutside.api.Api;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.homepage.activity.MainActivity;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GLogin;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by 15119 on 2016/7/24.
 */
public class ThirdPartyLogin {

    private final String TAG = "ThirdPartyLogin";

    private final String PLATFORM_QQ = "1";
    private final String PLATFORM_WECHAT = "2";

    private ProgressDialog waitingDialog;

    private static ThirdPartyLogin instance;

    private UMShareAPI umShareAPI;

    private WeakReference<Activity> activityRef;

    public static ThirdPartyLogin getInstance(Activity activity) {
        if (instance == null || instance.updateActivity(activity)) {
            synchronized (ThirdPartyLogin.class) {
                if (instance == null || instance.updateActivity(activity)) {
                    instance = new ThirdPartyLogin(activity);
                }
            }
        }
        return instance;
    }

    //check whether activity need updating
    private boolean updateActivity(Activity activity) {
        return instance.activityRef.get() == null || instance.activityRef.get() != activity;
    }


    private ThirdPartyLogin(Activity activity) {
        if (activityRef == null) {
            activityRef = new WeakReference<>(activity);
        } else if (activityRef.get() != null && activityRef.get() != activity) {
            activityRef = new WeakReference<>(activity);
        }
    }


    private void doAuth(SHARE_MEDIA platform, UMAuthListener listener) throws Exception {

        Activity activity = activityRef.get();
        if (activity == null) {
            throw new Exception("activity has been already destroyed");
           // return;
        }

        if (umShareAPI == null) {
            umShareAPI = UMShareAPI.get(activity.getApplicationContext());
        }

        umShareAPI.doOauthVerify(activity, platform, listener);
    }


    private UMAuthListener listener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            ToastUtils.show("登陆成功");

            Log.d(TAG, "auth --> " + map.toString());

            Api api = new ApiInterImpl();

            Map<String, String> params = buildPlatformParams(share_media, map);

            OkHttpManager.getInstance().postAsyn(api.getThirdPartyLoginIn(), new OkHttpManager.ResultCallback<BaseResponse<GLogin>>() {

                @Override
                public void onError(Request request, Exception e) {

                }

                @Override
                public void onResponse(BaseResponse<GLogin> response) {

                    Log.d(TAG, "third party login response -->" + response);


                    hideWaitingDialog();

                    //transform json to specific Object
                    GLogin gLogin = response.getData();

                    Activity activity = activityRef.get();

                    if (activity == null) return;

                    LocalBroadcastManager.getInstance(activity.getApplicationContext()).
                            sendBroadcast(new Intent("com.xiaoxiang.ioutside.MAIN_FINISH_SELF"));
                    //show MainPage after logging in
                    if (gLogin.getIsFirstLogin() == 0) {
                        Intent in = new Intent(activity, MainActivity.class);
                        in.putExtra("token", gLogin.getToken());
                        activity.startActivity(in);
                    }
//                } else {
//                    Intent in = new Intent(LoginActivity.this, LoginRecommendActivity.class);
//                    in.putExtra("token", gLogin.getToken());
//                    startActivity(in);
//                }

                    //remember to finish this activity
                    activity.finish();
                }
            }, params);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            ToastUtils.show("授权错误");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            ToastUtils.show("取消授权");
        }
    };

    private Map<String, String> buildPlatformParams(SHARE_MEDIA platform, Map<String, String> info) {

        Map<String, String> params = new HashMap<>();
        params.put("type", "0");
        params.put("uniqueCode", SystemInfo.uniqueCode);
        params.put("systemVersion", SystemInfo.systemVersion);
        params.put("openID", info.get("openid"));
        params.put("loginDeviceCategory", "0");
        params.put("deviceType", "手机");

        String platformType = PLATFORM_QQ;
        if (platform == SHARE_MEDIA.QQ) {
            platformType = PLATFORM_QQ;
        } else if (platform == SHARE_MEDIA.WEIXIN) {
            platformType = PLATFORM_WECHAT;
        }

        params.put("thirdPartType", platformType);

        Log.d(TAG, "QQ login params --> " + params.toString());

        return params;
    }


    public void doQQAuth() {
        SHARE_MEDIA platform = SHARE_MEDIA.QQ;
        try {
            doAuth(platform, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doWeChatAuth() {
        SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;
        try {
            doAuth(platform, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public void showWaitingDialog() {
//        Activity activity = activityRef.get();
//        if (activity == null) return;
//
//        if (waitingDialog == null) {
//            waitingDialog = new ProgressDialog(activity);
//        }
//
//        waitingDialog.setMessage("正在登陆，请稍后...");
//        waitingDialog.show();
//    }

    private void hideWaitingDialog() {

        if (waitingDialog != null && waitingDialog.isShowing()) {
            waitingDialog.hide();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        umShareAPI.onActivityResult(requestCode, resultCode, data);
    }

}
