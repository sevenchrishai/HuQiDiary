package com.ylh.huqidiary.bean;

import java.io.Serializable;

public class DiaryBean implements Serializable {

    /**
     * 日期
     */
    private String date;
    private String date2;

    public String getDate2() {
        return date2;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    /**
     * 时间
     */
    private String time;
    private String week;
    private String weather;
    /**
     * 内容
     */
    private String content;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private String tag;

    public DiaryBean() {
    }

    public DiaryBean(String date, String date2, String time, String week, String weather, String content, String tag) {
        this.date = date;
        this.date2 = date2;
        this.time = time;
        this.week = week;
        this.weather = weather;
        this.content = content;
        this.tag = tag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
