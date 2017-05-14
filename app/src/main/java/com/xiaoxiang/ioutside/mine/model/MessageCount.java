package com.xiaoxiang.ioutside.mine.model;

import java.io.Serializable;

/**
 * Created by 15119 on 2016/8/15.
 */
public class MessageCount {

    /**
     * errorMessage : df
     * errorCode : 2
     * data : {"messageCount":{"at_count":0,"comment_count":8,"collect_count":5,"official_count":6,"like_count":6,"sum_count":25,"atState":false,"commentState":true,"collectState":true,"officialState":true,"likeState":true,"sumState":true}}
     * accessAdmin : false
     * success : true
     */

    private String errorMessage;
    private int errorCode;
    /**
     * messageCount : {"at_count":0,"comment_count":8,"collect_count":5,"official_count":6,"like_count":6,"sum_count":25,"atState":false,"commentState":true,"collectState":true,"officialState":true,"likeState":true,"sumState":true}
     */

    private DataBean data;
    private boolean accessAdmin;
    private boolean success;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public boolean isAccessAdmin() {
        return accessAdmin;
    }

    public void setAccessAdmin(boolean accessAdmin) {
        this.accessAdmin = accessAdmin;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class DataBean implements Serializable {
        /**
         * at_count : 0
         * comment_count : 8
         * collect_count : 5
         * official_count : 6
         * like_count : 6
         * sum_count : 25
         * atState : false
         * commentState : true
         * collectState : true
         * officialState : true
         * likeState : true
         * sumState : true
         */

        private MessageCountBean messageCount;

        public MessageCountBean getMessageCount() {
            return messageCount;
        }

        public void setMessageCount(MessageCountBean messageCount) {
            this.messageCount = messageCount;
        }

        public static class MessageCountBean implements Serializable {
            private int at_count;
            private int comment_count;
            private int collect_count;
            private int official_count;
            private int like_count;
            private int sum_count;
            private boolean atState;
            private boolean commentState;
            private boolean collectState;
            private boolean officialState;
            private boolean likeState;
            private boolean sumState;

            public int getAt_count() {
                return at_count;
            }

            public void setAt_count(int at_count) {
                this.at_count = at_count;
            }

            public int getComment_count() {
                return comment_count;
            }

            public void setComment_count(int comment_count) {
                this.comment_count = comment_count;
            }

            public int getCollect_count() {
                return collect_count;
            }

            public void setCollect_count(int collect_count) {
                this.collect_count = collect_count;
            }

            public int getOfficial_count() {
                return official_count;
            }

            public void setOfficial_count(int official_count) {
                this.official_count = official_count;
            }

            public int getLike_count() {
                return like_count;
            }

            public void setLike_count(int like_count) {
                this.like_count = like_count;
            }

            public int getSum_count() {
                return sum_count;
            }

            public void setSum_count(int sum_count) {
                this.sum_count = sum_count;
            }

            public boolean isAtState() {
                return atState;
            }

            public void setAtState(boolean atState) {
                this.atState = atState;
            }

            public boolean isCommentState() {
                return commentState;
            }

            public void setCommentState(boolean commentState) {
                this.commentState = commentState;
            }

            public boolean isCollectState() {
                return collectState;
            }

            public void setCollectState(boolean collectState) {
                this.collectState = collectState;
            }

            public boolean isOfficialState() {
                return officialState;
            }

            public void setOfficialState(boolean officialState) {
                this.officialState = officialState;
            }

            public boolean isLikeState() {
                return likeState;
            }

            public void setLikeState(boolean likeState) {
                this.likeState = likeState;
            }

            public boolean isSumState() {
                return sumState;
            }

            public void setSumState(boolean sumState) {
                this.sumState = sumState;
            }
        }
    }
}
