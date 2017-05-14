package com.xiaoxiang.ioutside.network.postengine;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A class used to send http request, including only get and post request
 * Created by 15119 on 2016/7/12.
 */
public class HttpUtil {

    private static final String TAG = "HttpUtil";

    private static Handler handler;

    private static GsonTransformer mTransformer;

    public interface Callback {
        /**
         * if success, this method will be called
         * @param response response from server
         */
        void onSuccess(String response);

        /**
         * if error, this method will be called
         * @param request request that you build previously
         */
        void onError(Request request);
    }


    public interface Callback2<T> extends Callback {

        void onSuccess2(T response);
    }

    public interface Transformer {
        <T> T transformer(String response, Type typeOfT);
    }

    public static class GsonTransformer implements Transformer {

        private Gson mGson;

        public GsonTransformer() {
            mGson = new Gson();
        }

        @Override
        public <T> T transformer(String response, Type typeOfT) {
            return mGson.fromJson(response, typeOfT);
        }
    }

    /**
     * we use the thread pool to improve performance of http request
     */
    private static final ThreadPoolExecutor networkThreadPool = new ThreadPoolExecutor(2 * Runtime.getRuntime().availableProcessors() + 1,
            Integer.MAX_VALUE, 1000, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100));

    public static void sendHttpRequest(final Request request) {

        if (handler == null) handler = new Handler();

        networkThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "current networkThreadPool size --> " + networkThreadPool.getPoolSize());
                if (Request.METHOD_GET.equals(request.getMethod())) {
                    doGet(request);
                } else if (Request.METHOD_POST.equals(request.getMethod())) {
                    doPost(request);
                }
            }
        });

    }

    /**
     * use method to send get request
     * @param request request you should build using Request.Builder
     */
    private static void doGet(final Request request) {

        InputStream is = null;
        try {
            String urlString = buildGetUrl(request.getUrl(), request.getParams());
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setReadTimeout(8000);
            conn.setConnectTimeout(8000);
            conn.setRequestMethod(request.getMethod());
            is = conn.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            final StringBuilder builder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }

            if (conn.getResponseCode() == 200)
                postSuccess(request.getCallback(), builder.toString());

        } catch (Exception e) {
            postError(request);
            e.printStackTrace();
        } finally {
            close(is);
        }
    }

    private static void postSuccess(final Callback callback, final String response) {

        Runnable r;

        if (callback instanceof Callback2) {
            final Callback2 c = (Callback2) callback;

            final Type superClass = c.getClass().getGenericSuperclass();
            System.out.println(superClass.toString());
          //  final Type type = ((ParameterizedType) superClass).getActualTypeArguments()[0];

            r = new Runnable() {
                @Override
                public void run() {
                    mTransformer = new GsonTransformer();
                    c.onSuccess2(mTransformer.transformer(response, superClass));
                }
            };

        } else {
            r = new Runnable() {
                @Override
                public void run() {
                    if (callback != null)
                        callback.onSuccess(response);
                }
            };
        }

        handler.post(r);
    }

    private static void postError(Request request) {

        final Request r = request;

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (r.getCallback() != null)
                    r.getCallback().onError(r);
            }
        });
    }

    /**
     * use this method to send post method
     * @param request
     */
    private static void doPost(final Request request) {

        InputStream is = null;
        try {
            URL url = new URL(request.getUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);
            conn.setRequestMethod(request.getMethod());
            conn.setDoOutput(true);
            conn.setDoInput(true);

            OutputStream out = conn.getOutputStream();
            writeParams(out, request.getParams());

            is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            final StringBuilder builder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }

            postSuccess(request.getCallback(), builder.toString());

        } catch (Exception e) {
            postError(request);
            e.printStackTrace();
        } finally {
            close(is);
        }
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * use this method to build url for get request
     * @param baseUrl base url
     * @param params params for post request
     * @return
     * @throws IOException
     */
    private static String buildGetUrl(String baseUrl, Map<String, String> params) throws IOException {

        if (params == null) return baseUrl;
        StringBuilder builder = new StringBuilder(baseUrl);
        Set<Map.Entry<String, String>> paramEntries = params.entrySet();

        boolean isFirstParam = true;
        for (Map.Entry<String, String> entry : paramEntries) {
            if (isFirstParam) {
                builder.append("?");
                isFirstParam = false;
            } else {
                builder.append("&");
            }

            builder.append(URLEncoder.encode(entry.getKey(), "utf-8"));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue(), "utf-8"));
        }

        return builder.toString();
    }

    /**
     * write params to server for post method
     * @param out OutputStream
     * @param params params
     * @throws IOException we should throw this Exception but not catch it
     */
    private static void writeParams(OutputStream out, Map<String, String> params) throws IOException {
        out.write(buildPostParams(params).getBytes("utf-8"));
    }

    /**
     * you can use this method to build params for post request
     * @param params
     * @return
     * @throws IOException
     */
    private static String buildPostParams(Map<String, String> params) throws IOException {

        StringBuilder paramStr = new StringBuilder();

        Set<Map.Entry<String, String>> paramEntries = params.entrySet();

        for (Map.Entry<String, String> entry : paramEntries) {

            if (!TextUtils.isEmpty(paramStr)) {
                paramStr.append("&");
            }

            paramStr.append(URLEncoder.encode(entry.getKey(), "utf-8"));
            paramStr.append("=");
            paramStr.append(URLEncoder.encode(entry.getValue(), "utf-8"));
        }

        return paramStr.toString();
    }

    //传入url获取Json数据
    public static String getJsonContent(String params) {
        String result = "";
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            URL url = new URL(params);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            is = connection.getInputStream();
            isr = new InputStreamReader(is, "utf-8");
            br = new BufferedReader(isr);
            String line = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}
