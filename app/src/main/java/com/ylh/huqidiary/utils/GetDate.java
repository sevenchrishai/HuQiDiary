package com.ylh.huqidiary.utils;

import java.text.SimpleDateFormat;
import java.util.*;

public class GetDate {

    public static StringBuilder getDate() {
        StringBuilder stringBuilder = new StringBuilder();
        Calendar now = Calendar.getInstance();
        stringBuilder.append(now.get(Calendar.YEAR) + "年");
        stringBuilder.append((now.get(Calendar.MONTH) + 1) + "月");
        stringBuilder.append(now.get(Calendar.DAY_OF_MONTH) + "日");
        return stringBuilder;
    }

    public static StringBuilder getDate2() {
        StringBuilder stringBuilder = new StringBuilder();
        Calendar now = Calendar.getInstance();
        stringBuilder.append(now.get(Calendar.YEAR) + "-");
        stringBuilder.append((now.get(Calendar.MONTH) + 1) + "-");
        stringBuilder.append(now.get(Calendar.DAY_OF_MONTH) + "");
        return stringBuilder;
    }

    public static String getTime() {
        return new SimpleDateFormat("HH:mm", Locale.US).format(new Date());
    }

    public static String getWeek() {
        Calendar now = Calendar.getInstance();
        String mWay = String.valueOf(now.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "星期天";
        } else if ("2".equals(mWay)) {
            mWay = "星期一";
        } else if ("3".equals(mWay)) {
            mWay = "星期二";
        } else if ("4".equals(mWay)) {
            mWay = "星期三";
        } else if ("5".equals(mWay)) {
            mWay = "星期四";
        } else if ("6".equals(mWay)) {
            mWay = "星期五";
        } else if ("7".equals(mWay)) {
            mWay = "星期六";
        }
        return mWay;
    }


}