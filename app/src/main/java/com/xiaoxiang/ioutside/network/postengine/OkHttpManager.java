package com.xiaoxiang.ioutside.network.postengine;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.homepage.activity.ItemWebVIew;
import com.xiaoxiang.ioutside.network.response.BaseResponse;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by oubin6666 on 2016/3/28.
 */
public class OkHttpManager {

    String TAG = getClass().getSimpleName();

    private static final String TYPE_NAME_PREFIX = "class ";
    private static final String STRING_CLASS="java.lang.String";
    private static final String BASERESPONSE_CLASS="com.xiaoxiang.ioutside.network.postengine";

    private static final String CHARSET_NAME = "UTF-8";
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Gson gson;
    private OkHttpManager() {
        mOkHttpClient = new OkHttpClient();
        mDelivery=new Handler(Looper.getMainLooper());
        gson=new Gson();
    }
    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }
    public static OkHttpManager getInstance() {
        return OkHttpManagerHolder.sInstance;
    }
    private static class OkHttpManagerHolder {
        private static final OkHttpManager sInstance = new OkHttpManager();
    }
    //*********************
    //内部实现方法逻辑
    /*
   * 异步的get请求
   * */
    public void getStringAsyn(String url,final ResultCallback callback){
        final Request request=new Request.Builder().url(url).header("User-Agent","xiaoxiang/andriod").
                addHeader("Accept", "application/json").build();
        deliveryResult(callback, request);
    }

    public void getStringAsyn(String url, List<Param> params, final ResultCallback callback) {
        Request request = new Request.Builder().url(buildUrl(url, params)).
                header("User-Agent","xiaoxiang/andriod").build();
        deliveryResult(callback, request);
    }

    public String buildUrl(String baseUrl, List<Param> params) {

        boolean isFirstParam = true;

        String s = "";

        for (Param p : params) {

            if (isFirstParam) {
                s += '?' + p.key + '=' + p.value;
                isFirstParam = false;
            } else {
                s += '&' + p.key + '=' + p.value;
            }

        }

        return baseUrl + s;
    }


    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    public void postAsyn(String url, final ResultCallback callback, Param... params)
    {
        Request request = buildPostRequest(url, params);
        deliveryResult(callback, request);
    }

    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    public void postAsyn(String url, final ResultCallback callback, Map<String, String> params)
    {
        Param[] paramsArr = map2Params(params);
        Request request = buildPostRequest(url, paramsArr);
        deliveryResult(callback, request);
    }

    private Request buildPostRequest(String url, @NonNull Param[] params)
    {
        FormBody.Builder builder = new FormBody.Builder();

        for (Param param : params)
        {
            builder.add(param.key, param.value);
        }

        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    private Param[] map2Params(Map<String, String> params)
    {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries)
        {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    private void deliveryResult(final ResultCallback callback, final Request request){
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedStringCallback(request, e, callback);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final String string = response.body().string();
                    Log.d(TAG, string);
                    if (callback.mType == String.class) {
                        sendSuccessStringCallback(string, callback);
                    } else {
                        Object o = gson.fromJson(string, callback.mType);              //这里会出错
                        sendSuccessStringCallback(o, callback);
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                }
            }
        });
    }
    private void sendFailedStringCallback(final Request request, final IOException e, final ResultCallback callback)
    {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onError(request, e);
            }
        });
    }
    private void sendSuccessStringCallback(final Object object, final ResultCallback callback)
    {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onResponse(object);
            }
        });
    }
    //回调接口
    public static class ResultCallback<T>{
        private Gson gson=new Gson();
        Type mType;

        public ResultCallback(ItemWebVIew itemWebVIew) {

        }


        public interface CommonErrorListener {
            void onCommonError(int errorCode);
        }
        private CommonErrorListener commonErrorListener;

        public  ResultCallback(CommonErrorListener commonErrorListener) {
            mType=getSuperclassTypeParameter(getClass());
            this.commonErrorListener = commonErrorListener;
        }


        public ResultCallback(){
            mType=getSuperclassTypeParameter(getClass());
        }
        static Type getSuperclassTypeParameter(Class<?> subclass)
        {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class)
            {
                throw new RuntimeException("Missing type parameter.");
            }
            //This interface represents a parameterized type such as 'Set<String>'.
            ParameterizedType parameterized = (ParameterizedType) superclass;
            Log.d("OkHttpManager","type:"+$Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]));
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public void onError(Request request,Exception e){
            //commonErrorListener.onCommonError(401); // 这里传入解析出来的错误码,401是举个例子
        }
        public void onResponse(T response) {
            int errorCode=0;
            if(getClassName(mType).equals(STRING_CLASS)){
                String res=(String)response;
                Type type=new TypeToken<BaseResponse>(){}.getType();
                BaseResponse re=gson.fromJson(res,type);
                if(re.getErrorCode()==0){
                    errorCode=0;
                }else if(re.getErrorCode()==405){
                    errorCode=405;
                }else if(re.getErrorCode()==406){
                    errorCode=406;
                }else if(re.getErrorCode()==500){
                    errorCode=500;
                }
                if (commonErrorListener != null)
                commonErrorListener.onCommonError(errorCode); // 这里传入解析出来的错误码
            }else if(getClassName(mType).equals(BASERESPONSE_CLASS)){
                BaseResponse rew=(BaseResponse)response;
                if(rew.getErrorCode()==0){
                    errorCode=0;
                }else if(rew.getErrorCode()==405){
                    errorCode=405;
                }else if(rew.getErrorCode()==406){
                    errorCode=406;
                }else if(rew.getErrorCode()==500){
                    errorCode=500;
                }
                if (commonErrorListener != null)
                commonErrorListener.onCommonError(errorCode);
               // Log.d("MyFansActivity","baseresponse进入到resultcallback内部了"+errorCode);
            }
        }
    }



    //键值对
    public static class Param{
        public String key;
        public String value;

        public Param(){
        }

        public Param(String key,String value){
            this.key=key;
            this.value=value;
        }
    }

    public static String getClassName(Type type) {
        if (type==null) {
            return "";
        }
        String className = type.toString();
        if (className.startsWith(TYPE_NAME_PREFIX)) {
            className = className.substring(TYPE_NAME_PREFIX.length());
        }
        return className;
    }

}


