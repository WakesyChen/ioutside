package com.xiaoxiang.ioutside.api;

/**
 * Created by oubin6666 on 2016/3/30.
 */
public interface Api {


    //////
    String getWellChosenList(int pageSize, int pageNo, String token);

    String getVideoDetailList(int pageSize, int pageNo, String token);

    String getArticleDetail(int articleID, String token);

    String getCommentListForArticle(int id, int pageSize, int pageNo);

    String recommend(String url, String token, String recommendReason);

    String collectArticle(int id, String token);

    String addReport(int postID, int postType, int reportUserID, int reportType, String token);

    String addCommentForArticle(int postID, String content, String token);

    String likeArticle(int id, String token);

    String getArticleListOfUserObservedSubject(int pageSize, int pageNo, String token);

    String getThumbList(int pageSize, int pageNo, String token);

    String getDetail(int id, String token);

    String collectFootprint(int id, String token);

    String likeFootprint(int id, String token);

    String cancelLikeFootprint(int id, String token);

    String getCommentListForFootprint(int id, int pageSize, int pageNo, String token);

    String getThumbListOfObservedUser(int pageSize, int pageNo, String token);

    String observeUser(int userID, String token);

    String cancelObserveUser(int userID, String token);

    String addCommentForFootprint(int postID, String content, String token);

    String addCommentForFootprint(int postID, int receiverID, int referCommentID, String content, String token);

    String cancelLikeArticle(int id, String token);

    String publish(String thoughts, String token, String footprintType, String photoList);

    String getHotTypeList();

    String getEquipmentTypeList();

    String getOutdoorTypeList();

    String deleteArticleComment(int commentID, int id, String token);

    String addCommentForArticle(int postID, int receiverID, int referCommentID, String content, String token);

    String getCommentDialogForArticle(int pageNo, int pageSize, int id, int receiverID, int userID, int referID);

    String getCommentDialogForFootprint(int pageNo, int pageSize, int id, int receiverID, int userID, int referID);

    //////
    String domainName = "www.ioutside.com";


    //我的

    String getMyCollectEssayIn(int pageSize, int pageNo, String token);

    String getFansInterIn(int pageSize, int pageNo, String token);   //粉丝

    String getOtherFansIn(int pageSize, int pageNo, int userID);   //未登录状态下看别人的粉丝，下面的类似

    String getOtherFansIn(int pageSize, int pageNo, int userID, String token);


    String getObserverIn(int pageSize, int pageNo, String token);  //我的关注人

    String getOtherObserIn(int pageSize, int pageNo, int userID);

    String getOtherObserIn(int pageSize, int pageNo, int othersID, String token);


    String getPersonalInfoIn(String token);     //个人信息

    String getOtherPersonIn(int otherID);  //他人信息

    String getNumsIn(String token);            //获取推荐，动态，粉丝，关注人数目

    String getOtherPersonNumsIn(int userID);

    String getMyCollectFootPrintIn(int pageSize, int pageNo, String token);

    String getFeedBackIn();

    String getMyReCommendIn(int pageSize, int pageNo, String token);  //我的推荐

    String getOtherRecommendIn(int pageSize, int pageNo, int userID);

    String getMyDynamicIn(int pageSize, int pageNo, String token);

    String getOtherDynamicIn(int pageSize, int pageNo, int userID);

    String getModifyPhoneIn(String phone, String token);

    String getModifyEmailIn(String email, String token);

    String getAddObserIn(int subjectID, String token); //添加关注人

    String getCancelObserIn(int subjectID, String token);  //取消关注

    String getEssayCommentIn();  //文章评论接口

    String getEssayLikedIn(int id, String token);    //文章点赞接口

    String getMyEssayNewsIn(int pageNo, int pageSize, String token);

    String getMyDynamicNewsIn(int pageNo, int pageSize, String token);

    //注册登录
    String getSignInIn(int type, String uniqueCode, String systemVersion, String userName, String password, int loginDeviceCategory);  //登陆

    String getRegiByPhoneIn(String phone, String name, String password);

    String getRegiByEmailIn(String email, String name, String password);  //注册

    String getFindPwByPhoneIn(String phone);

    String getFindPwByEmailIn(String email);

    String getSetNewPwIn(String password, String phone);

    String getRegiVeriIn(String phone);

    //专题
    String getRecomSubjectIn(int pageSize, int pageNo);

    String getMySubjectIn(int pageSize, int pageNo, String token);

    String getMoreSubjectIn(int type, int pageSize, int pageNo);

    String getSubjectConIn(int subjectID);

    String getChildSubEssayIn(int subjectID, int pageSize, int pageNo);  //获取子专题的列表

    String getChildSubIn(int subjectID);

    String getObserSubIn(int subjectID, String token);

    String getUnObserSubIn(int subjectID, String token);

    String getSubjectConIn(int subjectID, String token);

    String getThirdPartyLoginIn();

    //沙龙活动规格订单
    String getSalonForm(String token, int salonPeriods);

    //    沙龙banner
    String getSalonBannerInfo();

    //    沙龙活动订单
    String addSalonOrder(String token, int salonPeriods, int salonstandardID, String name, String phone, double needPayAmount);

    //    沙龙活动订单(皮划艇) salonstandardID，周六传6，周日传7
    String addSalonOrder(String token, int salonstandardID, String name, String phone);

    //    活动QA接口
    String getActivityQa(int activityID, int pageNo, int pageSize);

    //    活动详情接口
    String getActivityDetail(int activityId, String token);

    //分享活动  类型-->1:微信;2:qq;3:微博
    String shareActivityDetail(int id, String token, int thirdPartType);

    //    通过ActivityId获取到ActivityModuleId
    String getActivityModuleId(int ActivityId);

    //        取消收藏活动接口
    String getCancelColleted(int id, String token);

    //    收藏活动接口
    String getColleted(int id, String token);

    //    按月获取活动规格接口
    String getActivityTypeByMonth(int activityId, String startDate, String endDate);

    //    获取常用游客列表
    String getOffenUsedTravelers(String token, int pageNo, int pageSize);

    //    添加常用游客信息
    String addNewTraveleInfo(String token, String idCard, String name, String phone, String passport);

    //    修改游客信息
    String modifyTravelerInfor(String token, int id, String name, String idCard, String passport, String phone);

    //    删除游客信息
    String deleteTravelerInfor(String token, int touristID);

    //    添加活动订单
    String addActivityOrder(String token, int activityID, int sellerID, int activityQuantity, String contactUser,
                            int activitySpecID, String contactPhone, String participants, String remark,
                            double needPayAmount, String activityTime);

    //    根据订单id获取信息
    String getOrderInforByOrderId(String token, int orderId);

//    申请退款
    String getOrderReturn(String token,int orderID);

//获得用户的圈子
    String getUsersCircle(String token,int pageNo,int pageSize);
//    加入圈子
    String joinCircle(String token,int circleID);

//    通过四个小组的ID得到圈子列表
    String getCircleListByGroupID(String token,int groupID,int pageNo,int pageSize);
//    获取热帖
    String getHotNote(String token,int pageNo,int pageSize);
//    获得大V问答的列表
    String getBigVQAList(String token,int circleID,int pageNo,int pageSize);

}
