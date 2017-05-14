package com.xiaoxiang.ioutside.activities.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lwenkun on 16/9/4.
 */
public class Query {

    private static Query sInstance = null;

    private Server mServer;

    private Query() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Server.HOST_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mServer = retrofit.create(Server.class);
    }

    public static Query getInstance() {
        if (sInstance == null) {
            synchronized (Query.class) {
                if (sInstance == null) {
                    sInstance = new Query();
                }
            }
        }
        return sInstance;
    }

    public Observable<Bean.Banner> activityBanner() {
        return mServer.activityBanner()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<Bean.RecommendActivitySubject> recommendActivitySubject(int activityNum, int pageSize, int pageNo) {
        return mServer.recommendActivitySubject(activityNum, pageSize, pageNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<Bean.SubjectActivities> subjectActivities(int subjectId, int pageSize, int pageNo) {
        return mServer.subjectActivities(subjectId, pageSize, pageNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<Bean.OrderList> oderList(String token, int orderStatus, int pageNo, int pageSize) {
        return mServer.orderList(token, orderStatus, pageNo, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<Bean.FilterActivities> filterActivities(int type, int pageNo, int pageSize, Integer subType,
                                                              Integer destination, String startDate, String endDate,
                                                              Integer lowPrice, Integer highPrice) {
        return mServer.filterActivites(type, pageNo, pageSize, subType, destination, startDate, endDate, lowPrice, highPrice)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<Bean.Destination> destination(int type) {
        return mServer.destination(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<Bean.Destination> activityDestinationUnderType (int type, int subType) {
        return mServer.activityDestinationUnderType(type, subType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<Bean.ActivityDate> activityDate(int type) {
        return mServer.startDateList(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<Bean.ActivityType> activityType(int type) {
        return mServer.activityType(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

}