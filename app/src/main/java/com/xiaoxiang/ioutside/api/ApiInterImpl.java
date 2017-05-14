package com.xiaoxiang.ioutside.api;

import android.util.Log;

/**
 * Created by oubin6666 on 2016/4/8.
 */
public class ApiInterImpl implements Api {
    //首页-精选列表
    @Override
    public String getWellChosenList(int pageSize, int pageNo ,String token) {
        return "http://"+domainName+"/xiaoxiang-backend/article/get-well-chosen-list?pageSize="
                +pageSize+"&pageNo="+pageNo+"&token="+token;
    }
    //首页-光影列表
    public String getVideoDetailList(int pageSize, int pageNo,String token) {
        return "http://"+domainName+"/xiaoxiang-backend/article/get-video-detail-list?pageSize="
                +pageSize+"&pageNo="+pageNo+"&token="+token;
    }
    //首页-文章或光影详情
    @Override
    public String getArticleDetail(int articleID,String token){
        return "http://"+domainName+"/xiaoxiang-backend/article/get-article-detail.do?articleID="
                +articleID+"&token="+token;
    }

    //文章评论列表
    @Override
    public String getCommentListForArticle(int id,int pageSize,int pageNo){
        return "http://"+domainName+"/xiaoxiang-backend/comment/get-comment-list-for-article.do?id="
                +id+"&pageSize="+pageSize+"&pageNo="+pageNo;
    }

    //首页-订阅列表
    @Override
    public String  getArticleListOfUserObservedSubject(int pageSize,int pageNo,String token){
        return "http://"+domainName+"/xiaoxiang-backend/article/get-article-list-of-user-observed-subject.do?pageSize="
                +pageSize+"&pageNo="+pageNo+"&token="+token;
    }

    //首页推荐外链文章
    @Override
    public String recommend(String url,String token,String recommendReason){
        return "http://"+domainName+"/xiaoxiang-backend/article/recommend.do?url="+url+"&token="+token+"&recommendReason="+recommendReason;
    }

    //点赞文章
    @Override
    public String likeArticle(int id,String token){
        return "http://"+domainName+"/xiaoxiang-backend/user-social/like-article.do?id="+id+"&token="+token;
    }

    //取消点赞文章
    @Override
    public String cancelLikeArticle(int id,String token){
        return "http://"+domainName+"/xiaoxiang-backend/user-social/cancel-like-article.do?id="+id+"&token="+token;
    }

    //收藏文章
    @Override
    public String collectArticle(int id,String token){
        return "http://"+domainName+"/xiaoxiang-backend/user-social/collect-article.do?id="+id+"&token="+token;
    }

    //举报
    @Override
    public String addReport(int postID,int postType,int reportUserID,int reportType,String token){
        return "http://"+domainName+"/xiaoxiang-backend/user/add-report.do?postID="+postID+"&postType="+postType
                +"&reportUserID="+reportUserID+"&reportType="+reportType+"&token="+token;
    }

    //回复他人评论
    @Override
    public String addCommentForArticle(int postID,int receiverID,int referCommentID,String content,String token){
        return "http://"+domainName+"/xiaoxiang-backend/comment/add-comment-for-article.do?postID="+postID+
                "&receiverID="+receiverID+"&referCommentID="+referCommentID+ "&content="+content +"&token="+token;
    }

    //直接评论文章
    @Override
    public String addCommentForArticle(int postID,String content,String token){
        return "http://"+domainName+"/xiaoxiang-backend/comment/add-comment-for-article.do?postID="+postID+
                 "&content="+content +"&token="+token;
    }

    //评论对话列表
    @Override
    public String getCommentDialogForArticle(int pageNo,int pageSize,int id,int receiverID,int userID,int referID){
        return "http://"+domainName+"/xiaoxiang-backend/comment/get-comment-dialog-for-article?pageNo="+pageNo+
                "&pageSize="+pageSize+"&id="+id+"&receiverID="+receiverID+"&userID="+userID+"&referID="+referID;
    }

    //删除文章评论
    @Override
    public String deleteArticleComment(int commentID,int id,String token){
        return "http://"+domainName+"/xiaoxiang-backend/comment/delete-article-comment?commentID="+commentID+"&id="+id+"&token="+token;
    }

    //发现-推荐好友列表
    public String getRecommendUserFootprint(int pageSize,int pageNo,int footprintNum ,String token){

        return "http://"+domainName+"/xiaoxiang-backend/footprint/get-recommend-user-footprint?pageSize="+pageSize+"&pageNo="
                +pageNo+"&footprintNum="+footprintNum+"&token="+token;
    }

    //发现-推荐列表
    @Override
    public String getThumbList(int pageSize, int pageNo,String token) {
        return "http://"+domainName+"/xiaoxiang-backend/footprint/get-thumb-list.do?pageSize="+pageSize
                +"&pageNo="+pageNo+"&token="+token;
    }

    //发现-推荐详情列表
    @Override
    public String getDetail(int id,String token) {
        return "http://"+domainName+"/xiaoxiang-backend/footprint/get-detail.do?id="+id+"&token="+token;
    }

    //发现-关注人足迹列表
    @Override
    public String getThumbListOfObservedUser(int pageSize,int pageNo,String token){
        return "http://"+domainName+"/xiaoxiang-backend/footprint/get-thumb-list-of-observed-user.do?pageSize="+pageSize
                +"&pageNo="+pageNo+"&token="+token;
    }

    //足迹评论列表
    @Override
    public String getCommentListForFootprint(int id,int pageSize,int pageNo,String token){
        return "http://"+domainName+"/xiaoxiang-backend/comment/get-comment-list-for-footprint.do?id="
                +id+"&pageSize="+pageSize+"&pageNo="+pageNo+"&token="+token;
    }

    //收藏足迹
    @Override
    public String collectFootprint(int id,String token){
        return "http://"+domainName+"/xiaoxiang-backend/user-social/collect-footprint.do?id="+id+"&token="+token;
    }

    //删除足迹评论
    public String deleteFootprintComment(int commentID,int id,String token){
        return "http://"+domainName+"/xiaoxiang-backend/comment/delete-footprint-comment?commentID="+commentID+"&id="+id+"&token="+token;
    }

    //评论足迹
    @Override
    public String addCommentForFootprint(int postID,String content,String token ){
        return "http://"+domainName+"/xiaoxiang-backend/comment/add-comment-for-footprint.do?postID="+postID+"&content="
                +content+"&token="+token;
    }

    //评论足迹
    @Override
    public String addCommentForFootprint(int postID,int receiverID,int referCommentID, String content,String token ){
        return "http://"+domainName+"/xiaoxiang-backend/comment/add-comment-for-footprint.do?postID="+postID+"&receiverID="+receiverID+
                "&referCommentID="+referCommentID+"&content="+content+"&token="+token;
    }

    public String getCommentDialogForFootprint(int pageNo,int pageSize,int id,int receiverID,int userID,int referID){
        return "http://"+domainName+"/xiaoxiang-backend/comment/get-comment-dialog-for-footprint?pageNo="+pageNo+"&pageSize="+pageSize+
                "&id="+id+"&receiverID="+receiverID+"&userID="+userID+"&referID="+referID;
    }

    //点赞足迹
    @Override
    public String likeFootprint(int id,String token){
        return "http://"+domainName+"/xiaoxiang-backend/user-social/like-footprint.do?id="+id+"&token="+token;
    }

    //取消点赞足迹
    @Override
    public String cancelLikeFootprint(int id ,String token){
        return "http://"+domainName+"/xiaoxiang-backend/user-social/cancel-like-footprint.do?id="+id+"&token="+token;
    }

    //关注用户
    @Override
    public String observeUser(int userID,String token){
        return "http://"+domainName+"/xiaoxiang-backend/observe/observe-user.do?userID="+userID+"&token="+token;
    }
    //取消关注用户
    @Override
    public String cancelObserveUser(int userID,String token){
        return "http://"+domainName+"/xiaoxiang-backend/observe/cancel-observe-user.do?userID="+userID+"&token="+token;
    }

    //动态发布
    @Override
    public String publish(String thoughts,String token,String footprintType,String photoList){
        return "http://"+domainName+"/xiaoxiang-backend/footprint/publish.do?thoughts="+thoughts+"&token="+token+
                "&footprintType="+footprintType+"&photoList="+photoList;
    }

    //热门标签
    @Override
    public String getHotTypeList(){
        return "http://"+domainName+"/xiaoxiang-backend/footprint/get-hot-type-list";
    }

    //装备标签
    @Override
    public String getEquipmentTypeList(){
        return "http://"+domainName+"/xiaoxiang-backend/footprint/get-equipment-type-list";
    }

    //足迹标签
    @Override
    public String getOutdoorTypeList(){
        return "http://"+domainName+"/xiaoxiang-backend/footprint/get-outdoor-type-list";
    }



    ////////////////////////////////////

    public String bindEmail(String email, String token) {
        return "http://" + domainName + "/xiaoxiang-backend/user/bind-email?email=" + email + "&token=" + token;
    }

    //首页-文章-专题列表
    public String getUserObservedArticleSubjectList(int pageSize,int pageNo,String token){
        return "http://"+domainName+"/xiaoxiang-backend/article/get-user-observed-article-subject-list.do?pageSize="
                +pageSize+"&pageNo="+pageNo+"&token="+token;
    }

    //专题
    @Override
    public String getRecomSubjectIn(int pageSize, int pageNo) {
        return "http://"+domainName+"/xiaoxiang-backend/article/get-hot-subject-list.do?pageSize="+pageSize+"&pageNo="+pageNo;
    }

    @Override
    public String getMySubjectIn(int pageSize, int pageNo, String token) {
        return "http://"+domainName+"/xiaoxiang-backend/article/get-user-observed-article-subject-list.do?pageSize="+pageSize+"&pageNo="+pageNo+"&token="+token;
    }

    @Override
    public String getMoreSubjectIn(int type, int pageSize, int pageNo) {
        return "http://"+domainName+"/xiaoxiang-backend/article/get-article-subject-list.do?type="+type+"&pageSize="+pageSize+"&pageNo="+pageNo;
    }

    @Override
    public String getSubjectConIn(int subjectID) {
        return "http://"+domainName+"/xiaoxiang-backend/article/get-subject-detail.do?subjectID="+subjectID;
    }

    //////////////

    @Override
    public String getSubjectConIn(int subjectID, String token) {
        return "http://"+domainName+"/xiaoxiang-backend/article/get-subject-detail.do?subjectID="+subjectID+"&token="+token;
    }

    @Override
    public String getChildSubIn(int subjectID) {
        return "http://"+domainName+"/xiaoxiang-backend/article/get-child-subject-list?subjectID="+subjectID;
    }

    @Override
    public String getChildSubEssayIn(int subjectID, int pageSize, int pageNo) {
        return "http://"+domainName+"/xiaoxiang-backend/article/get-article-list-under-subject.do?subjectID="+subjectID
                +"&pageSize="+pageSize+"&pageNo="+pageNo;
    }
    //////////////////

    @Override
    public String getObserSubIn(int subjectID, String token) {
        return "http://"+domainName+"/xiaoxiang-backend/observe/observe-subject.do?subjectID="+subjectID+"&token="+token;
    }

    @Override
    public String getUnObserSubIn(int subjectID, String token) {
        return "http://"+domainName+"/xiaoxiang-backend/observe/cancel-observe-subject.do?subjectID="+subjectID+"&token="+token;
    }

    //我的
    @Override
    public String getFansInterIn(int pageSize, int pageNo, String token) {
        return "http://"+domainName+"/xiaoxiang-backend/observe/get-fans-list.do?pageSize="+pageSize
                +"&pageNo="+pageNo+"&token="+token;
    }


    @Override
    public String getObserverIn(int pageSize, int pageNo, String token) {
        return "http://"+domainName+"/xiaoxiang-backend/observe/get-observer-list.do?pageSize="+pageSize
                +"&pageNo="+pageNo+"&token="+token;
    }

    @Override
    public String getMyCollectFootPrintIn(int pageSize, int pageNo, String token) {
        return "http://"+domainName+"/xiaoxiang-backend/user-social/get-collect-footprint-list.do?pageSize="+pageSize
                +"&pageNo="+pageNo+"&token="+token;
    }

    @Override
    public String getMyCollectEssayIn(int pageSize, int pageNo, String token) {
        return "http://"+domainName+"/xiaoxiang-backend/user-social/get-collect-article-list.do?pageSize="
                +pageSize+"&pageNo="+pageNo+"&token="+token;
    }

//    根据token获取个人信息
    @Override
    public String getPersonalInfoIn(String token) {
        return "http://"+domainName+"/xiaoxiang-backend/user/get-user-info.do?token="+token;
    }
    @Override
    public String getNumsIn(String token) {
        return "http://"+domainName+"/xiaoxiang-backend/user/get-feeds-count?token="+token;
    }

    @Override
    public String getFeedBackIn() {
        return "http://"+domainName+"/xiaoxiang-backend/feedback/add.do";
    }
    @Override
    public String getMyReCommendIn(int pageSize, int pageNo, String token) {
        return "http://"+domainName+"/xiaoxiang-backend/article/get-recommend-list?pageSize="+pageSize
                +"&pageNo="+pageNo+"&token="+token;
    }
    @Override
    public String getMyDynamicIn(int pageSize, int pageNo, String token) {
        return "http://"+domainName+"/xiaoxiang-backend/footprint/get-footprint-list?pageSize="+pageSize
                +"&pageNo="+pageNo+"&token="+token;
    }

    @Override
    public String getModifyEmailIn(String email, String token) {
        return  "http://"+domainName+"/xiaoxiang-backend/user/modify-bind-email?email="+email+"&token="+token;
    }
    @Override
    public String getModifyPhoneIn(String phone, String token) {
        return  "http://"+domainName+"/xiaoxiang-backend/user/modify-?phone="+phone+"&token="+token;
    }


    @Override
    public String getAddObserIn(int subjectID, String token) {
        return "http://"+domainName+"/xiaoxiang-backend/observe/observe-user.do?userID="+subjectID+"&token="+token;
    }

    @Override
    public String getCancelObserIn(int userID, String token) {
        return "http://"+domainName+"/xiaoxiang-backend/observe/cancel-observe-user.do?userID="+userID+"&token="+token;
    }

    //评论文章
    @Override
    public String getEssayCommentIn() {
        return "http://"+domainName+"/xiaoxiang-backend/comment/add-comment-for-article.do";
    }

    //喜欢文章
    @Override
    public String getEssayLikedIn(int id,String token) {
        return "http://"+domainName+"/xiaoxiang-backend/user-social/like-article.do?id="+id+"&token="+token;
    }

    @Override
    public String getRegiByPhoneIn(String phone, String name, String password) {
        return  "http://"+domainName+"/xiaoxiang-backend/user/register.do?phone="+phone
                +"&name="+name+"&password="+password;
    }

    @Override
    public String getRegiByEmailIn(String email,String name, String password) {
        return "http://"+domainName+"/xiaoxiang-backend/user/register.do?email="+email+"&name="+name+"&password="+password;
    }

    @Override
    public String getSignInIn(int type, String uniqueCode, String systemVersion, String userName, String password,int loginDeviceCategory) {
        return "http://"+domainName+"/xiaoxiang-backend/user/login.do?type=0&uniqueCode="+uniqueCode+"&systemVersion="+systemVersion
                +"&userName="+userName+"&password="+password+"&loginDeviceCategory=0";
    }

    @Override
    public String getFindPwByPhoneIn(String phone) {
        return "http://"+domainName+"/xiaoxiang-backend/user/find-password-phone?phone="+phone;
    }

    @Override
    public String getFindPwByEmailIn(String email) {
        return "http://"+domainName+"/xiaoxiang-backend/user/find-password-by-email?email="+email;
    }

    @Override
    public String getSetNewPwIn(String password,String phone) {
        return "http://"+domainName+"/xiaoxiang-backend/user/find-password-by-phone?password="+password+"&phone="+phone;
    }

    @Override
    public String getRegiVeriIn(String phone) {
        return "http://"+domainName+"/xiaoxiang-backend/user/phone?phone="+phone;
    }

    @Override
    public String getMyEssayNewsIn(int pageNo, int pageSize, String token) {
        return "http://"+domainName+"/xiaoxiang-backend/message/get-article-message-list.do?pageNo="+pageNo
                +"&pageSize="+pageSize+"&token="+token;
    }

    @Override
    public String getMyDynamicNewsIn(int pageNo, int pageSize, String token) {
        return "http://"+domainName+"/xiaoxiang-backend/message/get-footprint-message-list.do?pageNo="+pageNo
                +"&pageSize="+pageSize+"&token="+token;
    }

    @Override
    public String getOtherPersonIn(int otherID) {
        return "http://"+domainName+"/xiaoxiang-backend/user/get-other-user-info?&otherID="+otherID;
    }

    public String getOtherPersonWithToken(int otherID, String token) {
        return "http://"+domainName+"/xiaoxiang-backend/user/get-other-user-info?&otherID="+otherID+"&token="+token;
    }

    @Override
    public String getOtherPersonNumsIn(int userID) {
        return "http://"+domainName+"/xiaoxiang-backend/user/get-feeds-count?userID="+userID;
    }

    @Override
    public String getOtherDynamicIn(int pageSize, int pageNo, int userID) {
        return "http://"+domainName+"/xiaoxiang-backend/footprint/get-others-footprint-list?othersID="+userID
                +"&pageNo="+pageNo+"&pageSize="+pageSize;
    }

    @Override
    public String getOtherFansIn(int pageSize, int pageNo, int othersID) {
        return "http://"+domainName+"/xiaoxiang-backend/observe/get-others-fans-list.do?othersID="+othersID
                +"&pageNo="+pageNo+"&pageSize="+pageSize;
    }

    @Override
    public String getOtherFansIn(int pageSize, int pageNo, int userID, String token) {
        return "http://"+domainName+"/xiaoxiang-backend/observe/get-others-fans-list.do?othersID="+userID
                +"&pageNo="+pageNo+"&pageSize="+pageSize+"&token="+token;
    }

    @Override
    public String getOtherObserIn(int pageSize, int pageNo, int userID, String token) {
        return "http://"+domainName+"/xiaoxiang-backend/observe/get-others-observer-list.do?othersID="
                +userID+"&pageNo="+pageNo+"&pageSize="+pageSize+"&token="+token;
    }

    @Override
    public String getOtherObserIn(int pageSize, int pageNo, int othersID) {
        return "http://"+domainName+"/xiaoxiang-backend/observe/get-others-observer-list.do?othersID="
                +othersID+"&pageNo="+pageNo+"&pageSize="+pageSize;

    }

    @Override
    public String getOtherRecommendIn(int pageSize, int pageNo, int userID) {
        return "http://"+domainName+"/xiaoxiang-backend/article/get-others-recommend-list?pageNo="+pageNo
                +"&pageSize="+pageSize+"&othersID="+userID;
    }

    @Override
    public String getThirdPartyLoginIn() {
        return "http://" + domainName + "/xiaoxiang-backend/user/third-part-login.do";
    }

//    沙龙规格
    @Override
    public String getSalonForm(String token, int salonPeriods) {
        return "http://ioutside.com/xiaoxiang-backend/salon-activity/get-sanlonr-standard-list-by-periods?salonPeriods="+salonPeriods+"&token="+token;
    }

    //    沙龙活动订单
    @Override
    public String addSalonOrder(String token, int salonPeriods, int salonstandardID, String name, String phone, double needPayAmount) {
        return "http://"+domainName+"/xiaoxiang-backend/salon-activity/add-order?token="+token+"&salonPeriods="+
                salonPeriods+"&salonstandardID="+salonstandardID+"&name="+name+"&phone="+phone+"&needPayAmount="+needPayAmount;
    }
//          皮划艇
    @Override
    public String addSalonOrder(String token, int salonstandardID, String name, String phone) {
        return "http://"+domainName+"/xiaoxiang-backend/salon-activity/add-order?token="+token+
                "&salonstandardID="+salonstandardID+"&name="+name+"&phone="+phone;

    }

    //沙龙Banner
    @Override
    public String getSalonBannerInfo() {
        return "http://" + domainName +"/xiaoxiang-backend/operation-activity/get-index-carousel-list.do ";
    }

//    活动QA
    @Override
    public String getActivityQa(int activityID, int pageNo, int pageSize) {
        return "http://ioutside.com/xiaoxiang-backend/activity/get-activity-qa-list?activityID="+activityID+"&pageNo="+pageNo+"&pageSize="+pageSize;

    }

//    活动详情
    @Override
    public String getActivityDetail(int activityId, String token) {

        return "http://www.ioutside.com/xiaoxiang-backend/activity/get-activity-detail"+"?activityId="+activityId+"&token="+token ;
    }

//    活动分享请求接口
    @Override
    public String shareActivityDetail(int id, String token, int thirdPartType) {
        return "http://www.ioutside.com/xiaoxiang-backend/user-social/share-activity?id="+id+"&token="+token+"&thirdPartType="+thirdPartType;
    }

//    通过ActivityId获取到ActivityModuleId

    @Override
    public String getActivityModuleId(int ActivityId) {
        return "http://www.ioutside.com/xiaoxiang-backend/activity/get-descript-module-list.do?activityID="+ActivityId;
    }
//        取消收藏活动的接口
    @Override
    public String getCancelColleted(int id, String token) {
        return "http://ioutside.com/xiaoxiang-backend/user-social/cancel-collect-activity?id="+id+"&token="+token;
    }

//    收藏活动接口
    @Override
    public String getColleted(int id, String token) {
        return "http://ioutside.com/xiaoxiang-backend/user-social/collect-activity?id="+id+"&token="+token;
    }

    //    按月获取活动规格接口   activityId="+activityId+"&startDate=2016-9-1&endDate=2016-10-1

    @Override
    public String getActivityTypeByMonth(int activityId, String startDate, String endDate) {
        return "http://ioutside.com/xiaoxiang-backend/activity/get-activity-standard-by-month?activityId="+activityId+"&startDate="+startDate+"&endDate="+endDate;
    }

    @Override
    public String getOffenUsedTravelers(String token, int pageNo, int pageSize) {
        return "http://ioutside.com/xiaoxiang-backend/tourist/get-tourist-list?token="+token+"&pageSize="+pageSize+"&pageNo="+pageNo;
    }

    public String addNewTraveleInfo(String token, String idCard, String name, String phone, String passport) {
        return "http://ioutside.com/xiaoxiang-backend/tourist/add?token="+token+"&idCard="+idCard+"&phone="+phone+"&passport="+passport+"&name="+name;
    }

    @Override
    public String modifyTravelerInfor(String token, int id, String name, String idCard, String passport, String phone) {
        return "http://ioutside.com/xiaoxiang-backend/tourist/modify-tourist-info?token="+token+"&id="+id+"&name="+name+"&idCard="+idCard+"&phone="+phone+"&passport="+passport;
    }

    @Override
    public String deleteTravelerInfor(String token, int touristID) {
        return "http://ioutside.com/xiaoxiang-backend/tourist/delete-tourist-by-id?touristID="+touristID+"&token="+token;
    }


    @Override
    public String addActivityOrder(String token, int activityID, int sellerID, int activityQuantity, String contactUser, int activitySpecID, String contactPhone, String participants, String remark, double needPayAmount, String activityTime) {
        return "http://ioutside.com/xiaoxiang-backend/order/add?token="+token +
                "&activityID="+activityID+"&sellerID="+sellerID+"&activityQuantity="+activityQuantity+"&contactUser="+contactUser+"&activitySpecID="+activitySpecID +
                "&contactPhone="+contactPhone+"&participants="+participants+"&remark="+remark+"&needPayAmount="+needPayAmount+"&activityTime="+activityTime;
    }

    @Override
    public String getOrderInforByOrderId(String token, int orderId) {
        return "http://ioutside.com/xiaoxiang-backend/order/get-order-detail-by-id?token="+token+"&orderID="+orderId;
    }


    @Override
    public String getOrderReturn(String token, int orderID) {
        return "http://ioutside.com/xiaoxiang-backend/order/apply-for-refund?token="+token+"&orderID="+orderID;
    }

    @Override
    public String getUsersCircle(String token, int pageNo, int pageSize) {
        return "http://ioutside.com/xiaoxiang-backend/community/get-user-observed-community-circle-list.do?token="+token+"&pageNo="+pageNo+"&pageSize="+pageSize;
    }

    @Override
    public String joinCircle(String token, int circleID) {
        return "http://ioutside.com/xiaoxiang-backend/observe/observe-community-circle.do?token="+token+"&circleID="+circleID;
    }


    @Override
    public String getCircleListByGroupID(String token, int groupType, int pageNo, int pageSize) {
        return "http://ioutside.com/xiaoxiang-backend/community/get-community-circle-list.do?token="+token+
                "&groupType="+groupType+"&pageNo="+pageNo+"&pageSize="+pageSize;

    }

    @Override
    public String getHotNote(String token, int pageNo, int pageSize) {
        return "http://ioutside.com/xiaoxiang-backend/community/get-hot-community-post-list.do?token="+token+"&pageNo="+pageNo+"&pageSize="+pageSize;
    }

    @Override
    public String getBigVQAList(String token, int circleID, int pageNo, int pageSize) {
        return "http://ioutside.com/xiaoxiang-backend/community/get-expert-qa-list-by-circle-id.do?token="+token+"&circleID="+circleID+"&pageNo="+pageNo+"&pageSize="+pageSize;
    }
}


