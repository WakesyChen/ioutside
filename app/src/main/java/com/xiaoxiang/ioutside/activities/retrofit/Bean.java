package com.xiaoxiang.ioutside.activities.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lwenkun on 16/8/31.
 * 所有的 Bean 放在一起,防止类爆炸
 */
public class Bean {

    @SerializedName("errorMessage") public String errorMessage;
    @SerializedName("errorCode") public int errorCode;
    @SerializedName("success") public boolean success;
    @SerializedName("accessAdmin") public boolean accessAdmin;

    /**
     * 活动首页轮播图
     */
    public static class Banner extends Bean {
       @SerializedName("data") public Data data;

        public static class Data {
            @SerializedName("list") public List<Item> list;

            public static class Item {
                @SerializedName("activityId") public int activityId;
                @SerializedName("title") public String title;
                @SerializedName("photo") public String photo;
                @SerializedName("photoList") public ArrayList<String> photoList;
            }
        }
    }

    /**
     * 活动目的地,包括所有目的地和某类型下的目的地
     */
    public static class Destination extends Bean {
        @SerializedName("data") public Data data;

        public static class Data {
            @SerializedName("activityDestination") public List<Item> activityDestination;

            public static class Item {
                @SerializedName("id") public int id;
                @SerializedName("name") public String name;
            }
        }
    }

    /**
     * 活动列表,包括活动筛选结果和专题下的活动列表
     */
    public static class FilterActivities extends Bean {
        @SerializedName("data") public Data data;

        public static class Data {
            @SerializedName("activity") public List<Item> activity;

            public static class Item {
                @SerializedName("activityId") public int activityId;
                @SerializedName("title") public String title;
                @SerializedName("photo") public String photo;
                @SerializedName("photoList") public List<String> photoList;
                @SerializedName("content") public String content;
                @SerializedName("startPlace") public String startPlace;
                @SerializedName("subType") public String subType;
                @SerializedName("startDate") public String startDate;
                @SerializedName("subTitle") public String subTitle;
                @SerializedName("price") public float price;
            }
        }
    }

    public static class SubjectActivities extends Bean {
        @SerializedName("data") public Data data;

        public static class Data {
            @SerializedName("list") public List<Item> activities;

            public static class Item {
                @SerializedName("activityId") public int activityId;
                @SerializedName("title") public String title;
                @SerializedName("photo") public String photo;
                @SerializedName("photoList") public List<String> photoList;
                @SerializedName("content") public String content;
                @SerializedName("startPlace") public String startPlace;
                @SerializedName("subType") public String subType;
                @SerializedName("startDate") public String startDate;
                @SerializedName("subTitle") public String subTitle;
            }
        }
    }

    /**
     * 活动类型
     */
    public static class ActivityType extends Bean {
        @SerializedName("data") public Data data;

        public static class Data {
            @SerializedName("activitySubType") public List<Item> activitySubType;

            public static class Item {
                @SerializedName("id") public int id;
                @SerializedName("name") public String name;
            }
        }
    }

    /**
     *   活动日期
     */
    public static class ActivityDate extends Bean {
        @SerializedName("data") public Data data;

        public static class Data {
            @SerializedName("activityDate") public List<String> activityDate;
        }
    }

    /**
     * 活动收藏数目
     */
    public static class ActivityCollectedCount extends Bean {
        @SerializedName("Data") public Data data;

        public static class Data {
            @SerializedName("collectCount") public int collectCount;
        }
    }

    /**
     * 活动专题信息
     */
    public static class ActivitySubjectInfo extends Bean {
        @SerializedName("activitySubject")
        public ActivitySubject activitySubject;

        public static class ActivitySubject {
            @SerializedName("id") public int id;
            @SerializedName("title") public String title;
            @SerializedName("content") public String content;
            @SerializedName("activityNum") public int activityNum;
            @SerializedName("viewNum") public int viewNum;
            @SerializedName("photo") public String photo;
        }
    }

    /**
     * 首页推荐专题及下面的活动
     */
    public static class RecommendActivitySubject extends Bean {
        @SerializedName("data")public Data data;

        public static class Data {
            @SerializedName("list")public List<ActivitySubject> activitySubjects;

            public static class ActivitySubject {
                @SerializedName("subjectId")public int subjectId;
                @SerializedName("subjectTitle")public String subjectTitle;
                @SerializedName("subjectContent")public String subjectContent;
                @SerializedName("subjectPhoto")public String subjectPhoto;
                @SerializedName("activityList")public List<Activity> activityList;

                public static class Activity {
                    @SerializedName("activityId")public int activityId;
                    @SerializedName("title")public String title;
                    @SerializedName("photo")public String photo;
                    @SerializedName("photoList")public List<String> photoList;
                    @SerializedName("content")public String content;
                    @SerializedName("startPlace")public String startPlace;
                    @SerializedName("subType")public String subType;
                    @SerializedName("startDate")public String startDate;
                    @SerializedName("subTitle")public String subTitle;
                }
            }
        }
    }

    public static class OrderList extends Bean {
        @SerializedName("data") public Data data;

        public static class Data {
            @SerializedName("list") public List<Order> orderList;

            public static class Order {
                @SerializedName("id") public int id;
                @SerializedName("startDate") public String startDate;
                @SerializedName("activityID") public int activityId;
                @SerializedName("oderUserID") public int orderUserId;
                @SerializedName("sellerID") public int sellerID;
                @SerializedName("activitySpecID") public int activitySpecId;
                @SerializedName("activityQuantity") public int activityQuantity;
                @SerializedName("contactUser") public String contactUser;
                @SerializedName("contactPhone") public String contactPhone;
                @SerializedName("orderNumber") public String orderNumber;
                @SerializedName("orderCode") public String orderCode;
                @SerializedName("orderPrice") public String orderPrice;
                @SerializedName("remark") public String remark;
                @SerializedName("orderStatus") public int orderStatus;
                @SerializedName("activityTitle") public String activityTitle;
                @SerializedName("activitySpecDesc") public String activitySpecDesc;
                @SerializedName("activityPhoto") public String activityPhoto;
                @SerializedName("payWay") public int payWay;
            }
        }
    }

}
