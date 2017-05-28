package com.xiaoxiang.ioutside.homepage.model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Wakesy on 2017/5/27.
 */
public class WeatherData {


    /**
     * resultcode : 200
     * reason : successed!
     * result : {"sk":{"temp":"24","wind_direction":"北风","wind_strength":"4级","humidity":"45%","time":"16:05"},"today":{"temperature":"15℃~26℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"wind":"微风","week":"星期三","city":"武汉","date_y":"2017年05月24日","dressing_index":"舒适","dressing_advice":"建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。","uv_index":"弱","comfort_index":"","wash_index":"较适宜","travel_index":"较适宜","exercise_index":"较适宜","drying_index":""},"future":{"day_20170524":{"temperature":"15℃~26℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"wind":"微风","week":"星期三","date":"20170524"},"day_20170525":{"temperature":"16℃~28℃","weather":"晴","weather_id":{"fa":"00","fb":"00"},"wind":"微风","week":"星期四","date":"20170525"},"day_20170526":{"temperature":"17℃~31℃","weather":"晴","weather_id":{"fa":"00","fb":"00"},"wind":"微风","week":"星期五","date":"20170526"},"day_20170527":{"temperature":"19℃~31℃","weather":"多云转晴","weather_id":{"fa":"01","fb":"00"},"wind":"微风","week":"星期六","date":"20170527"},"day_20170528":{"temperature":"20℃~32℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"wind":"微风","week":"星期日","date":"20170528"},"day_20170529":{"temperature":"16℃~28℃","weather":"晴","weather_id":{"fa":"00","fb":"00"},"wind":"微风","week":"星期一","date":"20170529"},"day_20170530":{"temperature":"19℃~31℃","weather":"多云转晴","weather_id":{"fa":"01","fb":"00"},"wind":"微风","week":"星期二","date":"20170530"}}}
     * error_code : 0
     */

    private int resultcode;
    private String reason;
    /**
     * sk : {"temp":"24","wind_direction":"北风","wind_strength":"4级","humidity":"45%","time":"16:05"}
     * today : {"temperature":"15℃~26℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"wind":"微风","week":"星期三","city":"武汉","date_y":"2017年05月24日","dressing_index":"舒适","dressing_advice":"建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。","uv_index":"弱","comfort_index":"","wash_index":"较适宜","travel_index":"较适宜","exercise_index":"较适宜","drying_index":""}
     * future : {"day_20170524":{"temperature":"15℃~26℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"wind":"微风","week":"星期三","date":"20170524"},"day_20170525":{"temperature":"16℃~28℃","weather":"晴","weather_id":{"fa":"00","fb":"00"},"wind":"微风","week":"星期四","date":"20170525"},"day_20170526":{"temperature":"17℃~31℃","weather":"晴","weather_id":{"fa":"00","fb":"00"},"wind":"微风","week":"星期五","date":"20170526"},"day_20170527":{"temperature":"19℃~31℃","weather":"多云转晴","weather_id":{"fa":"01","fb":"00"},"wind":"微风","week":"星期六","date":"20170527"},"day_20170528":{"temperature":"20℃~32℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"wind":"微风","week":"星期日","date":"20170528"},"day_20170529":{"temperature":"16℃~28℃","weather":"晴","weather_id":{"fa":"00","fb":"00"},"wind":"微风","week":"星期一","date":"20170529"},"day_20170530":{"temperature":"19℃~31℃","weather":"多云转晴","weather_id":{"fa":"01","fb":"00"},"wind":"微风","week":"星期二","date":"20170530"}}
     */

    private ResultBean result;

    public int getResultcode() {
        return resultcode;
    }

    public void setResultcode(int resultcode) {
        this.resultcode = resultcode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * temp : 24
         * wind_direction : 北风
         * wind_strength : 4级
         * humidity : 45%
         * time : 16:05
         */

        private SkBean sk;
        /**
         * temperature : 15℃~26℃
         * weather : 多云
         * weather_id : {"fa":"01","fb":"01"}
         * wind : 微风
         * week : 星期三
         * city : 武汉
         * date_y : 2017年05月24日
         * dressing_index : 舒适
         * dressing_advice : 建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。
         * uv_index : 弱
         * comfort_index :
         * wash_index : 较适宜
         * travel_index : 较适宜
         * exercise_index : 较适宜
         * drying_index :
         */

        private TodayBean today;

        public SkBean getSk() {
            return sk;
        }

        public void setSk(SkBean sk) {
            this.sk = sk;
        }

        public TodayBean getToday() {
            return today;
        }

        public void setToday(TodayBean today) {
            this.today = today;
        }

        public static class SkBean {
            private String temp;
            private String time;

            public String getTemp() {
                return temp;
            }

            public void setTemp(String temp) {
                this.temp = temp;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }

        public static class TodayBean {
            private String temperature;
            private String weather;
            private String week;
            private String city;
            private String date_y;

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getWeather() {
                return weather;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getDate_y() {
                return date_y;
            }

            public void setDate_y(String date_y) {
                this.date_y = date_y;
            }
        }


    }

}
