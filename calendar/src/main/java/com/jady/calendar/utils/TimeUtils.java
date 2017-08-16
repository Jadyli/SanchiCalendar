package com.jady.calendar.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/8/15.
 */
public class TimeUtils {

    public static final String FORMAT_HOUR_MINITE_1 = "HH:mm";
    public static final String FORMAT_HOUR_MINITE_2 = "H时mm分";
    public static final String FORMAT_YEAR_MONTH_DAY_1 = "yyyy-MM-dd";
    public static final String FORMAT_YEAR_MONTH_DAY_2 = "yyyy/MM/dd";

    public static String formatDate(long timeInMillis, String pattern) {
        return formatDate(new Date(timeInMillis), pattern);
    }

    public static String formatDate(Calendar calendar, String pattern) {
        return formatDate(calendar.getTime(), pattern);
    }

    public static String formatDate(Date date, String pattern) {
        return formatDate(date, pattern, null);
    }

    public static String formatDate(Date date, String pattern, Locale locale) {
        SimpleDateFormat simpleDateFormat;
        if (locale == null) {
            simpleDateFormat = new SimpleDateFormat(pattern);
        } else {
            simpleDateFormat = new SimpleDateFormat(pattern, locale);
        }
        return simpleDateFormat.format(date);
    }

    /**
     * 判断给定日期是否为今天
     *
     * @param year  某年
     * @param month 某月
     * @param day   某天
     * @return ...
     */
    public static boolean isToday(int year, int month, int day) {
        Calendar c1 = Calendar.getInstance();
        c1.set(year, month - 1, day);
        return (c1.get(Calendar.YEAR) == year) &&
                (c1.get(Calendar.MONTH) == month - 1) &&
                (c1.get(Calendar.DAY_OF_MONTH) == day);
    }

    /**
     * 判断给定日期是否为今天
     *
     * @param calendar
     * @return
     */
    public static boolean isToday(Calendar calendar) {
        Calendar today = Calendar.getInstance();
        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                && calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH)
                && calendar.get(Calendar.DATE) == today.get(Calendar.DATE)) {
            return true;
        }
        return false;
    }

    /**
     * 判断某年是否为闰年
     *
     * @param year ...
     * @return true表示闰年
     */
    public static boolean isLeapYear(int year) {
        return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
    }

    /**
     * 是否是同一天
     *
     * @param calendar1
     * @param calendar2
     * @return
     */
    public static boolean isSameDay(Calendar calendar1, Calendar calendar2) {
        if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.DATE) == calendar2.get(Calendar.DATE)) {
            return true;
        }
        return false;
    }

    /**
     * 是否是同一天
     *
     * @param time1
     * @param time2
     * @return
     */
    public static boolean isSameDay(long time1, long time2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(time1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(time2);
        if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.DATE) == calendar2.get(Calendar.DATE)) {
            return true;
        }
        return false;
    }

    /**
     * 判断某个日期是否在两个日期之间
     *
     * @param calendar
     * @param calendars
     * @return
     */
    public static boolean isDayBetweenTwoDate(Calendar calendar, Calendar[] calendars) {
        if (calendar.getTimeInMillis() >= calendars[0].getTimeInMillis()
                && calendar.getTimeInMillis() < calendars[1].getTimeInMillis()) {
            return true;
        }
        return false;
    }

    /**
     * 比较两个calendar
     *
     * @param calendar1
     * @param calendar2
     * @return
     */
    public static int compareCalendar(Calendar calendar1, Calendar calendar2) {
        return compare(calendar1.getTimeInMillis(), calendar2.getTimeInMillis());
    }

    /**
     * @param time1
     * @param time2
     * @return
     */
    public static int compare(long time1, long time2) {
        if (time1 < time2) {
            return -1;
        } else if (time1 == time2) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * 获取今天
     *
     * @return
     */
    public static Calendar getToday() {
        Calendar today = Calendar.getInstance();
        today.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE), 0, 0, 0);
        today.set(Calendar.MILLISECOND, 0);
        return today;
    }

    /**
     * 设置到周日
     *
     * @param calendar
     * @param isSundayFirday 是否以周日为周起始日
     */
    public static void setToSunday(Calendar calendar, boolean isSundayFirday) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
        if (dayOfWeek == 0) {
            return;
        }
        if (!isSundayFirday) {
            dayOfWeek = dayOfWeek - 7;
        }
        calendar.add(Calendar.DATE, -dayOfWeek);
    }

    /**
     * 将Calendar设置到周一
     *
     * @param calendar
     * @param isSundayFirstDay 是否以周日为起始日
     */
    public static void setToMonday(Calendar calendar, boolean isSundayFirstDay) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
        if (dayOfWeek == 0) {
            return;
        }
        if (dayOfWeek == -1 && !isSundayFirstDay) {
            dayOfWeek = 6;
        }
        calendar.add(Calendar.DATE, -dayOfWeek);
    }

    /**
     * 获取周日
     *
     * @param isSundayFirstDay
     * @return
     */
    public static Calendar getSunday(boolean isSundayFirstDay) {
        Calendar today = Calendar.getInstance();
        setToSunday(today, isSundayFirstDay);
        today.set(0, 0, 0);
        today.setTimeInMillis(0);
        return today;
    }

    /**
     * 获取周一
     *
     * @return
     */
    public static Calendar getMonday(boolean isSundayFirstDay) {
        Calendar today = Calendar.getInstance();
        setToMonday(today, isSundayFirstDay);
        today.set(0, 0, 0);
        today.setTimeInMillis(0);
        return today;
    }

    /**
     * 获取今年第一天
     *
     * @return
     */
    public static Calendar getYearStart() {
        Calendar today = Calendar.getInstance();
        today.set(today.get(Calendar.YEAR), 0, 1);
        today.set(0, 0, 0);
        today.setTimeInMillis(0);
        return today;
    }

    /**
     * 获取今年最后一天
     *
     * @return
     */
    public static Calendar getYearEnd() {
        Calendar today = Calendar.getInstance();
        today.set(today.get(Calendar.YEAR), 11, 31);
        today.set(0, 0, 0);
        today.setTimeInMillis(0);
        return today;
    }

    /**
     * 获取当月第一天
     *
     * @return
     */
    public static Calendar getMonthStart() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.DATE, 1);
        today.set(0, 0, 0);
        today.setTimeInMillis(0);
        return today;
    }

    /**
     * 获取当月最后一天
     *
     * @return
     */
    public static Calendar getMonthEnd() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.DATE, getDaysInMonth(today));
        today.set(0, 0, 0);
        today.setTimeInMillis(0);
        return today;
    }

    public static int getDaysInMonth(Calendar calendar) {
        return calendar.getActualMaximum(Calendar.DATE);
    }

    private static int[] DAYSINMONTH;

    /**
     * @param year  年份
     * @param month 月份，真实月份减一
     * @return
     */
    public static int getDaysInMonth(int year, int month) {
        if (DAYSINMONTH == null) {
            DAYSINMONTH = new int[]{
                    31, 28, 31, 30, 31,
                    30, 31, 31, 30, 31,
                    30, 31};
        }
        if (isLeapYear(year) && month == 1) {
            return 29;
        }
        int days = 0;
        try {
            days = DAYSINMONTH[month];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 通过毫秒数设置日期
     *
     * @param originCalendar 需要设置的
     * @param newCalendar    被copy的
     */
    public static void copyCalendar(Calendar originCalendar, Calendar newCalendar) {
        originCalendar.setTimeInMillis(newCalendar.getTimeInMillis());
    }

    /**
     * 拷贝一个Calendar
     *
     * @param calendar
     * @return
     */
    public static Calendar copyCalendar(Calendar calendar) {
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTimeInMillis(calendar.getTimeInMillis());
        return newCalendar;
    }

    /**
     * 将Calendar的时分秒毫秒值设为0
     *
     * @param calendar
     */
    public static void resetCalendarHour(Calendar calendar) {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    /**
     * 获取两个日期间隔的月数
     *
     * @param calendar1
     * @param calendar2
     * @return
     */
    public static int getMonthCountBetweenDate(Calendar calendar1, Calendar calendar2) {
        return (calendar1.get(Calendar.YEAR) - calendar2.get(Calendar.YEAR)) * 12 + (calendar1.get(Calendar.MONTH) - calendar2.get(Calendar.MONTH));
    }

    /**
     * 获取两个日期间隔的天数
     *
     * @param calendar1
     * @param calendar2
     * @return 有正负
     */
    public static int getDaysBetweenDate(Calendar calendar1, Calendar calendar2) {
        return getDaysBetweenDate(calendar1.getTimeInMillis(), calendar2.getTimeInMillis());
    }

    /**
     * 获取两个日期间隔的天数
     *
     * @param date1
     * @param date2
     * @return 有正负
     */
    public static int getDaysBetweenDate(long date1, long date2) {
        return (int) ((date1 - date2) / (1000 * 3600 * 24));
    }

    /**
     * 这个只是针对日历格子上相差的周数
     *
     * @param calendar1 前的日期
     * @param calendar2 后面的日期
     * @return
     */
    public static int getWeeksBetweenDate(Calendar calendar1, Calendar calendar2) {
        Calendar tmp1 = copyCalendar(calendar1);
        Calendar tmp2 = copyCalendar(calendar2);
        setToMonday(tmp1, false);
        setToMonday(tmp2, false);
        return getDaysBetweenDate(tmp1, tmp2) / 7;
    }

}
