package com.xiaoxiang.ioutside.mine.mvp;

import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.xiaoxiang.ioutside.mine.model.RecommendPeople;
import com.xiaoxiang.ioutside.mine.model.RecommendTopic;
import com.xiaoxiang.ioutside.network.response.BaseResponse;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 15119 on 2016/6/29.
 */
public class RecommendRepository implements RecommendDataSource {

    private static final String URL_GET_RECOMMEND_PEOPLE = "http://ioutside.com/xiaoxiang-backend/user/get-recommend-user-list";
    private static final String URL_GET_RECOMMEND_TOPICS = "http://ioutside.com/xiaoxiang-backend/article/get-recommend-subject-list.do";
    private static final String URL_POST_RECOMMEND_TOPICS = "http://ioutside.com/xiaoxiang-backend/observe/batch-observe-subject.do";
    private static final String URL_POST_RECOMMEND_PEOPLE= "http://ioutside.com/xiaoxiang-backend/observe/batch-observe-user";

    private static final int MSG_GET_RECOMMEND_PEOPLE = 0x00;
    private static final int MSG_GET_RECOMMEND_TOPICS = 0x01;
    private static final int MSG_POST_RECOMMEND_PEOPLE = 0x02;
    private static final int MSG_POST_RECOMMEND_TOPICS = 0x03;

    private List<Integer> mFollowedPeople = new ArrayList<>();
    private List<Integer> mInterestedTopics = new ArrayList<>();

    private List<RecommendPeople.DataBean.ListBean> peopleList = new ArrayList<>();
    private List<RecommendTopic.DataBean.ListBean> topicList = new ArrayList<>();

    //这里的网络请求最好包装一下，时间原因暂时没有
    private OkHttpClient client;

    public RecommendRepository() {
        client = new OkHttpClient();
    }

    //这个handler主要用来处理请求结果的回调，因为 onResponse()这个接口的回调发生在工作线程中
    public Handler mHandler = new Handler(this);

    public static class Handler extends android.os.Handler {

        WeakReference<RecommendRepository> mObjRef;

        public Handler(RecommendRepository obj) {
            mObjRef = new WeakReference<>(obj);
        }

        @Override
        public void handleMessage(Message msg) {

            RecommendRepository recommendRepository = mObjRef.get();

            switch (msg.what) {
                case MSG_GET_RECOMMEND_PEOPLE :
                    if (recommendRepository != null) {
                        ((GetRecommendPeopleCallback)msg.obj).onRecommendPeopleLoaded(recommendRepository.peopleList);
                    }
                    break;
                case MSG_GET_RECOMMEND_TOPICS :
                    if (recommendRepository != null) {
                        ((GetRecommendTopicsCallback)msg.obj).onRecommendTopicsLoaded(recommendRepository.topicList);
                    }
                    break;
                case MSG_POST_RECOMMEND_PEOPLE :
                    if (recommendRepository != null) {
                        if (msg.arg1 == 1)
                        ((DataPostedCallback)msg.obj).onSuccess();
                     }
                    break;
                case MSG_POST_RECOMMEND_TOPICS :
                    if (recommendRepository != null) {
                        if (msg.arg1 == 1)
                            ((DataPostedCallback)msg.obj).onSuccess();
                        else ((DataPostedCallback)msg.obj).onFailed();
                    }
                    break;
            }
        }
    }


    @Override
    public void getRecommendPeople(final GetRecommendPeopleCallback callback) {

        Request recommendPeopleRequest = new Request
                .Builder()
                .url(URL_GET_RECOMMEND_PEOPLE)
                .build();

        client.newCall(recommendPeopleRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
               // Log.d("recommend", response.body().string());
                Gson gson = new Gson();
                RecommendPeople recommendPeople = gson.fromJson(response.body().string(), RecommendPeople.class);
                peopleList = recommendPeople.getData().getList();

                Message msg = Message.obtain();
                msg.what = MSG_GET_RECOMMEND_PEOPLE;
                msg.obj = callback;
                mHandler.sendMessage(msg);

            }
        });
    }

    @Override
    public void getRecommendTopics(final GetRecommendTopicsCallback callback) {

        Request recommendTopicRequest = new Request
                .Builder()
                .url(URL_GET_RECOMMEND_TOPICS)
                .build();

        client.newCall(recommendTopicRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                RecommendTopic topics = gson.fromJson(response.body().string(), RecommendTopic.class);
                topicList = topics.getData().getList();

                Message msg = Message.obtain();
                msg.what = MSG_GET_RECOMMEND_TOPICS;
                msg.obj = callback;
                mHandler.sendMessage(msg);
            }
        });
    }

    //增加感兴趣的专题到内存中
    @Override
    public void addInterestedTopic(int position) {
        int id = topicList.get(position).getId();
        mInterestedTopics.add(id);
        Log.d("topic",  mInterestedTopics.toString());
    }

    //增加感兴趣的人到内存中
    @Override
    public void addFollowedPerson(int position) {
        int id = peopleList.get(position).getId();
        mFollowedPeople.add(id);
    }

    //从内存中移除感兴趣的话题
    @Override
    public void removeTopic(int position) {
        int id = topicList.get(position).getId();
        mInterestedTopics.remove(Integer.valueOf(id));
    }

    //从内存中移除感兴趣的人
    @Override
    public void removePerson(int position) {
        int id = peopleList.get(position).getId();
        mFollowedPeople.remove(Integer.valueOf(id));
    }

    //将内存中感兴趣的人提交到服务器
    @Override
    public void postFollowedPeople(final DataPostedCallback callback, String token) {

        Request request = new Request.Builder()
                .url(buildUrl(URL_POST_RECOMMEND_PEOPLE, mFollowedPeople, token))
                .build();


        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailed();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("recommend people", response.body().toString());

                Gson gson = new Gson();
                BaseResponse baseResponse = gson.fromJson(response.body().string(), BaseResponse.class);

                Message msg = Message.obtain();
                if (baseResponse.isSuccess()) {
                    //arg1 这个字段用来标识服务器响应的结果是成功还是失败，成功 1， 失败 0
                    msg.arg1 = 1;
                } else {
                    msg.arg1 = 0;
                }
                msg.what = MSG_POST_RECOMMEND_PEOPLE;
                msg.obj = callback;
                mHandler.sendMessage(msg);
            }
        });


    }

    //组装url
    private String buildUrl(String url, List<Integer> ids, String token) {

        String batchUseID = "";

        for (int i = 0; i < ids.size(); i++) {
            if (i == 0) {
                batchUseID = batchUseID + ids.get(i);
            } else {
                batchUseID = batchUseID + "," + ids.get(i);
            }
        }

      //  Log.d("buildUrl", url + "?batchUserID=" + batchUseID + "&token=" + token);

        if (URL_POST_RECOMMEND_PEOPLE.equals(url)) {
            return url + "?batchUserID=" + batchUseID + "&token=" + token;
        } else {
            return url + "?batchSubjectID=" + batchUseID + "&token=" + token;
        }

    }

    //将感兴趣的专题提交到服务器
    @Override
    public void postInterestedTopics(final DataPostedCallback callback, String token) {

        Request request = new Request.Builder()
                .url(buildUrl(URL_POST_RECOMMEND_TOPICS, mInterestedTopics, token))
                .build();


        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailed();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.d("recommend topics", response.body().toString());

                Gson gson = new Gson();
                BaseResponse baseResponse = gson.fromJson(response.body().string(), BaseResponse.class);

                Message msg = Message.obtain();
                if (baseResponse.isSuccess()) {
                    msg.arg1 = 1;
                } else {
                    msg.arg1 = 0;
                }
                msg.what = MSG_POST_RECOMMEND_TOPICS;
                msg.obj = callback;
                mHandler.sendMessage(msg);
            }
        });
    }
}
