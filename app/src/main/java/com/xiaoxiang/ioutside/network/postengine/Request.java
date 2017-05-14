package com.xiaoxiang.ioutside.network.postengine;

import java.util.HashMap;
import java.util.Map;

public class Request {

    private String mUrl;
    private Map<String, String> mParams;
    private String mMethod;
    private HttpUtil.Callback mCallback;

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";

    public Request() {

    }

    private Request(Builder builder) {
        setUrl(builder.mUrl);
        setParams(builder.mParams);
        setMethod(builder.mMethod);
        setCallback(builder.mCallback);
        mUrl = builder.mUrl;
        mParams = builder.mParams;
        mMethod = builder.mMethod;
        mCallback = builder.mCallback;
    }

    public String getUrl() {
        return mUrl;
    }

    private void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public Map<String, String> getParams() {
        return mParams;
    }

    private void setParams(Map<String, String> mParams) {
        this.mParams = mParams;
    }

    public String getMethod() {
        return mMethod;
    }

    private void setCallback(HttpUtil.Callback callback) {
        this.mCallback = callback;
    }

    private void setMethod(String mMethod) {
        this.mMethod = mMethod;
    }

    public HttpUtil.Callback getCallback() {
        return mCallback;
    }

    public static class Builder {

        private String mUrl;
        private Map<String, String> mParams = new HashMap<>();
        private String mMethod;
        private HttpUtil.Callback mCallback;

        public Builder() {

        }

        public Builder url(String url) {
            mUrl = url;
            return this;
        }

        public Builder params(Map<String, String> params) {
            mParams = params;
            return this;
        }

        public Builder method(String method) {
            mMethod = method;
            return this;
        }

        public Request build() {
            return new Request(this);
        }

        public Builder callback(HttpUtil.Callback callback) {
            mCallback = callback;
            return this;
        }

        public Builder addParam(String key, String value) {
            mParams.put(key, value);
            return this;
        }

    }

}