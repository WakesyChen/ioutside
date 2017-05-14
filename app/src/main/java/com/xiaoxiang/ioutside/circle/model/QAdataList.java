package com.xiaoxiang.ioutside.circle.model;

import java.util.List;

/**
 * Created by Wakesy on 2016/9/29.
 */
public class QAdataList {

    public List<QAdata> getList() {
        return list;
    }

    public void setList(List<QAdata> list) {
        this.list = list;
    }

    private List<QAdata> list;


    public static class QAdata{


        /**
         * id : 2
         * questionUserID : 3
         * questionUserName : lxyeinsty
         * questionUserPhoto : null
         * question : 我这里可以学
         * answer : 我这里可以学
         * questionTime : 2016/09/23 12:23
         * answerTime : 2016/09/23 13:35
         * circleID : 3
         * liked : false
         */

        private int id;
        private int questionUserID;
        private String questionUserName;
        private Object questionUserPhoto;
        private String question;
        private String answer;
        private String questionTime;
        private String answerTime;
        private int circleID;
        private boolean liked;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getQuestionUserID() {
            return questionUserID;
        }

        public void setQuestionUserID(int questionUserID) {
            this.questionUserID = questionUserID;
        }

        public String getQuestionUserName() {
            return questionUserName;
        }

        public void setQuestionUserName(String questionUserName) {
            this.questionUserName = questionUserName;
        }

        public Object getQuestionUserPhoto() {
            return questionUserPhoto;
        }

        public void setQuestionUserPhoto(Object questionUserPhoto) {
            this.questionUserPhoto = questionUserPhoto;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getQuestionTime() {
            return questionTime;
        }

        public void setQuestionTime(String questionTime) {
            this.questionTime = questionTime;
        }

        public String getAnswerTime() {
            return answerTime;
        }

        public void setAnswerTime(String answerTime) {
            this.answerTime = answerTime;
        }

        public int getCircleID() {
            return circleID;
        }

        public void setCircleID(int circleID) {
            this.circleID = circleID;
        }

        public boolean isLiked() {
            return liked;
        }

        public void setLiked(boolean liked) {
            this.liked = liked;
        }
    }
}
