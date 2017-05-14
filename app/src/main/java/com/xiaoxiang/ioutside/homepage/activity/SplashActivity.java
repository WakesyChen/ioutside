package com.xiaoxiang.ioutside.homepage.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.xiaoxiang.ioutside.R;

import java.lang.ref.WeakReference;


public class SplashActivity extends AppCompatActivity {

    private boolean isFirstUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler(this);

        Message message = handler.obtainMessage();
        message.what = 110;
        handler.sendMessageDelayed(message, 1000);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp = getSharedPreferences("global_data", MODE_PRIVATE);
                isFirstUse = sp.getBoolean("isFirstUse", true);
            }
        }).start();
    }

    static class Handler extends android.os.Handler {

        WeakReference<SplashActivity> refSplashActivity;

        public Handler(SplashActivity activity) {
            refSplashActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            if (refSplashActivity.get() == null) return;
            SplashActivity activity = refSplashActivity.get();

            if (msg.what == 110) {
                if (! activity.isFirstUse) {
                    Intent intent=new Intent();
                    intent.setClass(activity, MainActivity.class);
                    activity.startActivity(intent);
                } else {
                    Intent i = new Intent(activity, IntroduceActivity.class);
                    activity.startActivity(i);
                }
            }
            SharedPreferences.Editor editor = activity.getSharedPreferences("global_data", MODE_PRIVATE).edit();
            editor.putBoolean("isFirstUse", false);
            editor.apply();
            activity.finish();
        }

    }


}
