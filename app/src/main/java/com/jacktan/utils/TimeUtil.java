package com.jacktan.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
    private static final String TAG = "TimeUtil";

    public static final long ONE_MINUTE_MS = 60 * 1000L;
    public static final long ONE_HOUR_MS = 60 * ONE_MINUTE_MS;
    public static final long ONE_DAY_MS = 24 * ONE_HOUR_MS;

    /**
     * 获取自从1970年1月1日到今天一共多少天
     */
    private static final long MillisPerDay = 1000 * 60 * 60 * 24;

    public static long getDayFrom1970() {

        try {
            Calendar cal = Calendar.getInstance();
            cal.set(1970, Calendar.JANUARY, 1);
            long m0 = cal.getTimeInMillis();

            cal.setTime(new Date());
            long m1 = cal.getTimeInMillis();

            return ((m1 - m0) / MillisPerDay);
        } catch (Exception ex) {
            return 0;
        }
    }

    public static long getDay(long m1) {
        try {
            return (m1 / MillisPerDay);
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     *
     * @return返回字符串格式 yyyy-MM-dd
     */
    public static String getStringDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String dateString = formatter.format(time);
        return dateString;
    }

    public static String getStringDate(String format, long time) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.CHINA);
        String dateString = formatter.format(time);
        return dateString;
    }

    public static String getYMDDateForChina(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        String dateString = formatter.format(time * 1000);
        return dateString;
    }

    public static String getMDStringDateForChina(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日", Locale.CHINA);
        String dateString = formatter.format(time * 1000);
        return dateString;
    }

    /**
     *
     * @return返回字符串格式 yyyy/MM/dd HH:mm:ss
     */
    public static String getStringFullDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);
        String dateString = formatter.format(time);
        return dateString;
    }

    /**
     *
     * @return返回字符串格式 HH:mm
     */
    public static String getStringTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.CHINA);
        return formatter.format(time);
    }

    public static String getStringMomentDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm", Locale.CHINA);
        String dateString = formatter.format(time);
        return dateString;
    }

    public static boolean isToday(long timeInMillis) {
        boolean flag;
        if (timeInMillis <= 0) {
            return false;
        }
        Calendar today = Calendar.getInstance();
        Calendar cale = Calendar.getInstance();
        cale.setTimeInMillis(timeInMillis);
        flag = cale.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                && cale.get(Calendar.MONTH) == today.get(Calendar.MONTH)
                && cale.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
        return flag;
    }

    public static boolean isYesterday(long timeInMillis) {
        boolean flag = false;
        if (timeInMillis <= 0) {
            return false;
        }
        long today = System.currentTimeMillis();
        long between = today - timeInMillis;
        //间隔大于1天，小于2天
        if (between > ONE_DAY_MS && between < 2 * ONE_DAY_MS) {
            flag = true;
        }
        return flag;
    }

    public static int getDaysBetween(long startDate, long endDate) {
        Calendar startC = Calendar.getInstance();
        startC.setTimeInMillis(startDate);
        startC.set(Calendar.HOUR, 0);
        startC.set(Calendar.MINUTE, 0);
        startC.set(Calendar.SECOND, 0);
        startC.set(Calendar.MILLISECOND, 0);
        Calendar endC = Calendar.getInstance();
        endC.setTimeInMillis(endDate);
        endC.set(Calendar.HOUR, 0);
        endC.set(Calendar.MINUTE, 0);
        endC.set(Calendar.SECOND, 0);
        endC.set(Calendar.MILLISECOND, 0);
        return (int) ((endC.getTimeInMillis() - startC.getTimeInMillis()) / 24L / 60L / 60L / 1000L);
    }


    public static String secondToTime(int second) {
        long hours = second / 3600;//转换小时数
        long minutes = second % 3600 / 60;//转换分钟
        long sec  = second % 3600 % 60;//剩余秒数

        if (second > 3600) {
            return String.format("%02d:%02d:%02d", hours, minutes, sec);
        } else if (second > 60) {
            return String.format("%02d:%02d", minutes, sec);
        } else {
            return String.format("%02d", sec);
        }
    }

    /*
     * 将时间转换为时间戳 ms
     */
    public static long dateToStamp(String s) throws ParseException {
        if (s == null) {
            return 0L;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        return date.getTime();
    }
    public static boolean isSameDay(long millis1, long millis2){
        int day1 = (int) (millis1 / (1000 * 60 * 60 * 24));
        int day2 = (int) (millis2 / (1000 * 60 * 60 * 24));
        return day1 == day2;
    }
}
