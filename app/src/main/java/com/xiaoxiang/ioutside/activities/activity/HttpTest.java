package com.xiaoxiang.ioutside.activities.activity;

import android.app.Activity;
import android.os.Bundle;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Wakesy on 2016/9/7.
 */


/**
 * 与项目无关的代码
 */
public class HttpTest {


    public void httpGet(){

        OkHttpClient okHttpClient=new OkHttpClient();
        final Request request=new Request.Builder()
                .url("http://www.baidu.com")
                .build();
        Call call=okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

               String json= response.body().toString();
            }
        });

    }



}
