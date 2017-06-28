package com.signs.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间戳与字符串转换
 */
public class DateUtils {
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String ONLY_FORMAT = "yyyyMMddHHmmss";

    /**
     * 默认构造函数
     */
    private DateUtils() {
    }

    /**
     * 字符串转换成日期 如果转换格式为空，则利用默认格式进行转换操作
     */
    public static Date strToDate(String str) {
        return strToDate(str, null);
    }

    public static Date strToDate(String str, String format) {
        if (null == str || "".equals(str)) {
            return null;
        }
        // 如果没有指定字符串转换的格式，则用默认格式进行转换
        if (null == format || "".equals(format) || "Datetime".equals(format)) {
            format = DATETIME_FORMAT;
        } else if ("Timestamp".equals(format)) {
            format = TIMESTAMP_FORMAT;
        } else if ("Date".equals(format)) {
            format = DATE_FORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 日期转换为字符串
     */
    public static String dateToStr(Date date) {
        return dateToStr(date, null);
    }

    public static String dateToStr(Date date, String format) {
        if (null == date) {
            return null;
        }
        // 如果没有指定字符串转换的格式，则用默认格式进行转换
        if (null == format || "".equals(format) || "Datetime".equals(format)) {
            format = DATETIME_FORMAT;
        } else if ("Timestamp".equals(format)) {
            format = TIMESTAMP_FORMAT;
        } else if ("Date".equals(format)) {
            format = DATE_FORMAT;
        } else if ("only".equals(format)){
            format = ONLY_FORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 获取当前对应格式的日期
     */
    public static String getDateStr() {
        return getDateStr(null);
    }

    public static String getDateStr(String format) {
        if (null == format || "".equals(format)) {
            format = "yyyyMMddhhmmssSSS";
        }
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    /**
     * 计算两个日期之间相差的秒数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差秒数
     */
    public static int getSecsBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / 1000;

        return Math.abs(Integer.parseInt(String.valueOf(between_days)));
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     */
    public static int getDaysBetween(Date smdate, Date bdate) {
        int second = getSecsBetween(smdate, bdate);
        long between_days = second / 3600 / 24;

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 获取星期
     */
    public static String getWeekName(Calendar calendar) {
        int index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return getWeekName(index);
    }

    /**
     * 获取星期
     */
    public static String getWeekName(int week) {
        String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        if (week < 0) week = 0;
        return weeks[week];
    }

    /**
     * 获取字符串日期
     */
    public static String getCalendarStr(Calendar cal) {
        int year = cal.get(Calendar.YEAR);//获取年份
        int month = cal.get(Calendar.MONTH) + 1;//获取月份
        String monthStr = month < 10 ? "0" + month : month + "";
        int day = cal.get(Calendar.DATE);//获取日
        String dayStr = day < 10 ? "0" + day : day + "";
        return year + monthStr + dayStr;
    }
}