package com.jady.calendar.utils;

import android.text.TextUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.jady.calendar.utils.DataUtility.castToInt;
import static com.jady.calendar.utils.DataUtility.divide;
import static com.jady.calendar.utils.DataUtility.multiply;
import static com.jady.calendar.utils.DataUtility.subtract;

/**
 * Created by Administrator on 2016/9/6.
 */
public class TimeUtility {

    private static int[] DAYSINMONTH = null;

    /**
     * 例如：10/14
     */
    public static final String FORMAT_MONTH_DATE = "MM/dd";
    public static final String FORMAT_MONTH_DATE2 = "MM月dd日";
    /**
     * 例如：2016/10/14
     */
    public static final String FORMAT_YEAR_MONTH_DATE = "yyyy/MM/dd";
    /**
     * 例如：2016-10-14
     */
    public static final String FORMAT_YEAR_MONTH_DATE1 = "yyyy-MM-dd";
    public static final String FORMAT_HOUR_MINITE = "HH:mm";
    public static final String FORMAT_HOUR_MINITE_NO_ZERO = "H时mm分";
    /**
     * 例如：星期四
     */
    public static final String FORMAT_WEEK = "EEEE";

    public static String timeFromNow(long time) {
        //当前时间
        Calendar curCalendar = Calendar.getInstance();
        int comSec = (int) (curCalendar.getTimeInMillis() / 1000 - time);
        String result = "";
        if (comSec < 60) {
            result = comSec + "秒前";
        } else if (comSec < 60 * 60) {
            result = comSec / 60 + "分钟前";
        } else if (comSec < 60 * 60 * 24) {
            result = comSec / (60 * 60) + "小时前";
        } else {
            result = comSec / (60 * 60 * 24) + "天前";
        }
        return result;
    }

    /**
     * 通过生日获取年龄
     * @param birth
     * @return
     */
    public static String getAgeFromBirthday(String birth) {
        String age="";
        if (TextUtility.isNotEmpty(birth)) {
            int birthYear = Integer.parseInt(birth.substring(0,4));
            Calendar calendar = Calendar.getInstance();
            int curYear = calendar.get(Calendar.YEAR);
            age += (curYear - birthYear + 1);
        }
        return age;
    }

    public static int getCurMonthDays() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getActualMaximum(Calendar.DATE);
    }

    public static int get(int aType) {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(aType);
    }

    public static Calendar getToday0Midnight() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static String timeNumber2Str(String aTime) {
        if (TextUtils.isEmpty(aTime)) return "00:00";
        float time = Float.parseFloat(aTime);
        int front = (int) time;
        int behind = (int) ((time - front) * 60);

        String result = front + ":" + behind;
        return result;
    }

    public static String formatDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_YEAR_MONTH_DATE1, Locale.CHINA);
        return format.format(new Date(time));
    }

    /**
     * 格式化时分,保留时前面的0
     *
     * @param date
     * @return 09:00
     */
    public static String formatHourMinite(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_HOUR_MINITE, Locale.CHINA);
        return format.format(date);
    }


    public static String formatPhotoDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(time));
    }

    public static String formatPhotoDate(String path) {
        File file = new File(path);
        if (file.exists()) {
            long time = file.lastModified();
            return formatPhotoDate(time);
        }
        return "1970-01-01";
    }

    /**
     * 格式化时分，不保留时前面的0
     *
     * @param date
     * @return 9:0
     */
    public static String formatHourMiniteNoZero(long date) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_HOUR_MINITE_NO_ZERO, Locale.CHINA);
        return format.format(date);
    }

    public static long parseDate(String time) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_YEAR_MONTH_DATE1, Locale.CHINA);
        try {
            Date date = format.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    public static String formatDateYMD(Calendar calendar) {
        return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE);
    }

    public static String formatDateToMDHM(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日 hh:mm", Locale.CHINA);
        return format.format(new Date(time));
    }

    public static String formatDateToMD(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日", Locale.CHINA);
        return format.format(calendar.getTime());
    }

    public static String formatDate(String path) {
        File file = new File(path);
        if (file.exists()) {
            long time = file.lastModified();
            return formatDate(time);
        }
        return "1970-01-01";
    }

    public static boolean compareCalendarSameDate(long time1, long time2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(time2);

        if (calendar.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar.get(Calendar.DATE) == calendar2.get(Calendar.DATE)) {
            return true;
        }
        return false;
    }

    public static boolean compareCalendarSameDate(Calendar calendar, Calendar calendar2) {
        if (calendar.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar.get(Calendar.DATE) == calendar2.get(Calendar.DATE)) {
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
        if (calendar1.getTimeInMillis() < calendar2.getTimeInMillis()) {
            return -1;
        } else if (calendar1.getTimeInMillis() == calendar2.getTimeInMillis()) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * 获取星期几
     *
     * @param calendar
     * @return 例：星期四
     */
    public static String getFormatWeek(Calendar calendar) {

        int dayofweek = calendar.get(Calendar.DAY_OF_WEEK);
        return "星期" + AsdUtility.getChineseDate(dayofweek);

        //不加Local.CHINA，就会用默认的，如果默认的是英语的话，就会返回Tuesday
//        SimpleDateFormat format = new SimpleDateFormat(FORMAT_WEEK, Locale.CHINA);
//        return format.format(calendar.getTime());
    }

    /**
     * 获取日期
     *
     * @param calendar
     * @return 例：10/14
     */
    public static String getFormatMonthDate(Calendar calendar) {
        return new SimpleDateFormat(FORMAT_MONTH_DATE, Locale.CHINA).format(calendar.getTime());
    }

    /**
     * 获取日期
     *
     * @param calendar
     * @return 例：10月14日
     */
    public static String getFormatMonthDate2(Calendar calendar) {
        return new SimpleDateFormat(FORMAT_MONTH_DATE2, Locale.CHINA).format(calendar.getTime());
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
        Calendar c2 = Calendar.getInstance();
        c1.set(year, month - 1, day);
        return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) &&
                (c1.get(Calendar.MONTH) == (c2.get(Calendar.MONTH))) &&
                (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH));
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

    public static int getDaysInMonth(Calendar calendar) {
        return calendar.getActualMaximum(Calendar.DATE);
//        if (DAYSINMONTH == null) {
//            DAYSINMONTH = new int[]{
//                    31, 28, 31, 30, 31,
//                    30, 31, 31, 30, 31,
//                    30, 31};
//        }
//
//        if (isLeapYear(year) && month == 1) {
//            return 29;
//        }
//        int date = 0;
//        try {
//            date = DAYSINMONTH[month];
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return date;
    }

    /**
     * 将日历往前或往后设置一个月
     *
     * @param calendar
     * @param isAdd
     * @param lastDateOfMonth 今天所在月份的考勤日和结束日
     * @return
     */
    public static boolean changeTimeByMonth(Calendar calendar, boolean isAdd, Calendar lastDateOfMonth) {

        Calendar tmpCalendar = copyCalendar(calendar);

        if (isAdd) {
            tmpCalendar.add(Calendar.MONTH, 1);
            if (lastDateOfMonth != null && tmpCalendar.getTimeInMillis() >= lastDateOfMonth.getTimeInMillis()) {
                return false;
            }
        } else {
            tmpCalendar.add(Calendar.MONTH, -1);
        }

        calendar.setTimeInMillis(tmpCalendar.getTimeInMillis());
        return true;
    }

    public static boolean changeTimeByWeek(Calendar calendar, Calendar lastDateOfMonth, boolean isAdd) {

        Calendar tmpCalendar = copyCalendar(calendar);
        if (isAdd) {
            tmpCalendar.add(Calendar.WEEK_OF_YEAR, 1);
            Calendar tmpCalendar2 = Calendar.getInstance();
            tmpCalendar2.setTimeInMillis(tmpCalendar.getTimeInMillis());
            setToSundayAsLastDay(tmpCalendar2);
            if (tmpCalendar2.getTimeInMillis() >= lastDateOfMonth.getTimeInMillis())//大于当月最大时间则不切换
            {
                return false;
            }
        } else {
            tmpCalendar.add(Calendar.WEEK_OF_YEAR, -1);
        }
        copyCalendar(calendar, tmpCalendar);
        return true;
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
     * 判断日期是周几
     *
     * @param timeInMillis
     * @return
     */
    public static int getDateOfWeek(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 判断日期
     *
     * @param timeInMillis
     * @return
     */
    public static int getDateOfMonth(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 获取两个日期间隔的天数
     * <p>
     * 数值大了计算会产生小数，long类型必须强转
     *
     * @param calendar1
     * @param calendar2
     * @return 有正负
     */
    public static int getDaysBetweenDate(Calendar calendar1, Calendar calendar2) {
        return castToInt(divide(subtract(calendar1.getTimeInMillis(), calendar2.getTimeInMillis()), multiply(1000, 3600, 24)));
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
        setToMonday(tmp1);
        setToMonday(tmp2);
        return getDaysBetweenDate(tmp1, tmp2) / 7;
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
     * 获取周一
     *
     * @return
     */
    public static Calendar getMonday() {
        Calendar today = Calendar.getInstance();
        setToMonday(today);
        return today;
    }

    /**
     * 获取周日
     *
     * @return
     */
    public static Calendar getSunday() {
        Calendar today = Calendar.getInstance();
        setToSundayAsLastDay(today);
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
        return today;
    }

    /**
     * 获取当月特定某天
     *
     * @return
     */
    public static Calendar getMonthSpec(int pos) {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.DATE, pos);
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
        return today;
    }


    /**
     * 将Calendar设置到周一
     *
     * @param calendar
     */
    public static void setToMonday(Calendar calendar) {
//        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
//        if (day_of_week == -1) {
//            day_of_week = 6;
//        }
//        calendar.add(Calendar.DATE, -day_of_week);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.MONDAY) {
            return;
        }
        if (dayOfWeek == Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, -6);
        } else {
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        }
    }

    public static void setToToday(Calendar calendar) {
        calendar.setTimeInMillis(getToday().getTimeInMillis());
    }

    /**
     * 将Calendar设置到本周日(是本周最后一天，不是第一天了)
     *
     * @param calendar
     */
    public static void setToSundayAsLastDay(Calendar calendar) {
//        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
//        if (day_of_week == 0) {
//            day_of_week = 7;
//        }
//        calendar.add(Calendar.DATE, 7 - day_of_week);

        if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            calendar.add(Calendar.DATE, 7);
        }
    }

    /**
     * 将Calendar设置到本周日(是本周第一天)
     *
     * @param calendar
     */
    public static void setToSundayAsFirstDay(Calendar calendar) {
//        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
//        if (day_of_week == 0) {
//            day_of_week = 7;
//        }
//        calendar.add(Calendar.DATE, 7 - day_of_week);

        if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        }
    }

    /**
     * 通过毫秒数设置日期
     *
     * @param calendar1 需要设置的
     * @param calendar2 被copy的
     */
    public static void copyCalendar(Calendar calendar1, Calendar calendar2) {
        calendar1.setTimeInMillis(calendar2.getTimeInMillis());
    }

    public static void copyCalendarAndReset(Calendar calendar1, Calendar calendar2) {
        calendar1.setTimeInMillis(calendar2.getTimeInMillis());
        resetCalendarHour(calendar1);
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

    public static Calendar copyCalendarAndReset(Calendar calendar) {
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTimeInMillis(calendar.getTimeInMillis());
        resetCalendarHour(newCalendar);
        return newCalendar;
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
     * 将Calendar的时分秒毫秒值设为0
     *
     * @param calendar
     */
    public static void resetCalendarHour(Calendar calendar) {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
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

    public static int compare(String time1, String time2) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YEAR_MONTH_DATE1);
        try {
            Date date1 = sdf.parse(time1);
            Date date2 = sdf.parse(time2);
            if (date1.getTime() > date2.getTime()) {
                return -1;
            } else if (date1.getTime() < date2.getTime()) {
                return 1;
            } else {
                return 0;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return 1;
        }
    }

}
