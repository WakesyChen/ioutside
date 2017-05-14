package com.xiaoxiang.ioutside.activities.retrofit;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by lwenkun on 16/9/2.
 */
public interface Server {

    String HOST_PATH  = "http://ioutside.com/xiaoxiang-backend/";

    @GET("activity/get-activity-lunbo")
    Observable<Bean.Banner> activityBanner();

    @GET("activity/get-recmmend-activity-subject")
    Observable<Bean.RecommendActivitySubject>  recommendActivitySubject(@Query("activityNum") int activityNum
            , @Query("pageSize") int pageSize, @Query("pageNo") int pageNo);

    @GET("activity/get--activity-list-under-subject")
    Observable<Bean.SubjectActivities> subjectActivities(@Query("subjectId") int subjectId
            , @Query("pageSize") int pageSize, @Query("pageNo") int pageNo);

    @GET("order/get-order-list-by-status")
    Observable<Bean.OrderList> orderList(@Query("token") String token, @Query("orderStatus") int orderStatus,
                                         @Query("pageNo") int pageNo, @Query("pageSize") int pageSize);

    @GET("activity/get-activity-by-condition")
    Observable<Bean.FilterActivities> filterActivites(@Query("type") int type, @Query("pageNo") int pageNo,
                                                      @Query("pageSize") int pageSize, @Query("subType") Integer subType,
                                                      @Query("destination") Integer destination, @Query("startDate") String startDate,
                                                      @Query("endDate") String endDate, @Query("lowPrice") Integer lowPrice,
                                                      @Query("highPrice") Integer highPrice);

    @GET("activity/get-all-activity-destination")
    Observable<Bean.Destination> destination(@Query("type") int type);

    @GET("activity/get-activity-start-date-list")
    Observable<Bean.ActivityDate> startDateList(@Query("type") int type);

    @GET("activity/get-activity-sub-type")
    Observable<Bean.ActivityType> activityType(@Query("type") int type);

    @GET("activity/get-activity-destination-under-type")
    Observable<Bean.Destination> activityDestinationUnderType(@Query("type") int type, @Query("subType") int subType);


}
