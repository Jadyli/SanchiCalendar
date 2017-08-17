package com.jady.calendar.model.data;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.jady.calendar.model.annotations.CalendarType;
import com.jady.calendar.model.annotations.DateType;
import com.jady.calendar.model.annotations.HolidayType;
import com.jady.calendar.model.annotations.WeekFirstDay;
import com.jady.calendar.utils.SPUtils;
import com.jady.calendar.utils.TimeUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by lipingfa on 2017/6/6.
 */
public class CalendarDataCenter {
    public static int monthFirstDay = 1;

    @WeekFirstDay
    public static int weekFirstDay = WeekFirstDay.MONDAY;
    public static Context mContext;
    private SPUtils holidayCache;
    public static boolean isInMultipleMode = false;

    private SparseArray<SparseArray<List<DayInfo>>> monthDayInfoArray;
    private SparseArray<SparseArray<List<DayInfo>>> weekDayInfoArray;
    //节假日 年/月/月节假日列表
    private SparseArray<SparseArray<List<Integer>>> holidayArray;
    //补休日
    private SparseArray<SparseArray<List<Integer>>> defferedArray;
    //周六
    private SparseArray<SparseArray<List<Integer>>> saturdayArray;
    //周日
    private SparseArray<SparseArray<List<Integer>>> sundayArray;
    /**
     * 全局共用的当前时间
     */
    public static Calendar globalCalendar = TimeUtils.getToday();
    public static Calendar mToday = TimeUtils.getToday();
    public static List<Long> mDateList = new ArrayList<>();

    private static class CalendarDataCenterHolder {
        private static final CalendarDataCenter instance = new CalendarDataCenter();
    }

    public static CalendarDataCenter getInstance() {
        return CalendarDataCenterHolder.instance;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public static void init(Context context) {
        CalendarDataCenter.mContext = context.getApplicationContext();
    }

    /**
     * 获取一个日历视图日期(42天)的数组，先从缓存中获取，如果没有就建立
     *
     * @param calendar 日历当前选中的日期
     * @return
     */
    public List<DayInfo> getMonthDayInfoList(Calendar calendar) {
        //各个月的数据，HashMap<year_month,DayInfo[][]
        Calendar tmpCalendar = getMonthPeriod(calendar)[0];

        //考勤日所在年
        int year = tmpCalendar.get(Calendar.YEAR);
        //考勤日所在月
        int month = tmpCalendar.get(Calendar.MONTH);

        if (monthDayInfoArray == null) {
            monthDayInfoArray = new SparseArray<>();
        }
        //一个月的数据
        SparseArray<List<DayInfo>> monthArray = monthDayInfoArray.get(year);
        if (monthArray == null) {
            monthArray = new SparseArray<>();
            monthDayInfoArray.put(year, monthArray);
        }
        List<DayInfo> dayInfoList = monthArray.get(month);
        if (dayInfoList == null) {
            dayInfoList = buildMonthDayInfoList(tmpCalendar);
            monthArray.put(month, dayInfoList);
        }
        return dayInfoList;
    }

    /**
     * 获取一周的日期数据
     *
     * @param calendar
     * @return
     */
    public List<DayInfo> getWeekDayInfoList(Calendar calendar) {

        Calendar tmpCalendar = TimeUtils.copyCalendar(calendar);
        int firstDay = getWeekFirstDay();
        if (firstDay == Calendar.SUNDAY) {
            TimeUtils.setToSunday(tmpCalendar, true);
        } else {
            TimeUtils.setToMonday(tmpCalendar, false);
        }

        int year = tmpCalendar.get(Calendar.YEAR);
        int week = tmpCalendar.get(Calendar.WEEK_OF_YEAR);

        if (weekDayInfoArray == null) {
            weekDayInfoArray = new SparseArray<>();
        }
        SparseArray<List<DayInfo>> weekArray = weekDayInfoArray.get(year);
        if (weekArray == null) {
            weekArray = new SparseArray<>();
            weekDayInfoArray.put(year, weekArray);
        }
        List<DayInfo> dayInfoList = weekArray.get(week);
        if (dayInfoList == null) {
            dayInfoList = buildWeekDayInfoList(tmpCalendar);
            weekArray.put(week, dayInfoList);
        }
        return dayInfoList;
    }

    /**
     * 获取周首日
     *
     * @return
     */
    public static int getWeekFirstDay() {
        return SPUtils.create(mContext, CalendarConst.SPFileName.CALENDAR_SETTING)
                .getInt(CalendarConst.SPElementKey.WEEK_FIRST_DAY, Calendar.MONDAY);
    }

    /**
     * 构建日历中一页各天的信息数组，第一行和最后一行可能包含上个月的信息
     *
     * @param calendar
     * @return
     */
    public List<DayInfo> buildMonthDayInfoList(Calendar calendar) {
        Calendar tmpCalendar = TimeUtils.copyCalendar(calendar);
        List<DayInfo> dayInfoList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            dayInfoList.addAll(getWeekDayInfoList(tmpCalendar));
            tmpCalendar.add(Calendar.DATE, 7);
        }
        return dayInfoList;
    }

    /**
     * 获取传入周首日所在周的数据
     *
     * @param calendar 周首日
     * @return
     */
    public List<DayInfo> buildWeekDayInfoList(Calendar calendar) {
        List<DayInfo> dayInfoList = new ArrayList<>();
        for (int j = 0; j < 7; j++) {
            DayInfo dayInfo = new DayInfo(calendar);
            //节假日
            if (getHoliday(calendar).contains(dayInfo.getDay())) {
                dayInfo.setHoliday(true);
            }
            //补休日
            if (getDeffered(calendar).contains(dayInfo.getDay())) {
                dayInfo.setDeferred(true);
            }
            //周六
            if (getWeekend(calendar, Calendar.SATURDAY).contains(dayInfo.getDay())) {
                dayInfo.setSaturday(true);
            }
            //周日
            if (getWeekend(calendar, Calendar.SUNDAY).contains(dayInfo.getDay())) {
                dayInfo.setSunday(true);
            }
            //今天
            if (TimeUtils.isToday(calendar)) {
                dayInfo.setToday(true);
            }
            dayInfoList.add(dayInfo);
            calendar.add(Calendar.DATE, 1);
        }
        return dayInfoList;
    }

    public DayInfo getDayInfo(Calendar calendar) {
        List<DayInfo> weekDayInfoList = getWeekDayInfoList(calendar);
        for (DayInfo dayInfo : weekDayInfoList) {
            if (TimeUtils.isSameDay(calendar, dayInfo.getCalendar())) {
                return dayInfo;
            }
        }
        return new DayInfo();
    }

    /**
     * 获取指定年月的节假日数据
     *
     * @param calendar
     * @return 指定月份的节假日期数组
     */
    public List<Integer> getHoliday(Calendar calendar) {
        return getHoliday(calendar, HolidayType.HOLIDAY);
    }

    /**
     * 获取指定年月的补休日数据
     *
     * @param calendar
     * @return 指定月份的补休日期数组
     */
    public List<Integer> getDeffered(Calendar calendar) {
        return getHoliday(calendar, HolidayType.DEFFERED);
    }

    public List<Integer> getHoliday(Calendar calendar, @HolidayType int type) {

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        if (year < 2013) return new ArrayList<>();

        SparseArray<SparseArray<List<Integer>>> yearArray;
        if (type == HolidayType.HOLIDAY) {
            yearArray = holidayArray;
        } else {
            yearArray = defferedArray;
        }

        if (yearArray == null) {
            yearArray = new SparseArray<>();
        }

        SparseArray<List<Integer>> monthArray = yearArray.get(year);
        if (monthArray == null) {
            monthArray = new SparseArray<>();
            yearArray.put(year, monthArray);

            String holiday = getHolidayCache().getString("holiday" + year);
            if (TextUtils.isEmpty(holiday)) {
                //仍为空
                //获取本地类文件定义的
                try {
                    Field field = CalendarConst.HolidayConst.class.getDeclaredField("calendar_holiday_" + year);
                    if (field != null) {
                        holiday = field.get(null).toString();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (TextUtils.isEmpty(holiday)) {
                    //从网络拉取
//                    getHolidayCache().putString(CalendarConst.SPElementKey.WEEK_FIRST_DAY, holiday);
                }
            }
            if (!TextUtils.isEmpty(holiday)) {
                HolidayInfos holidayInfos = new Gson().fromJson(holiday, HolidayInfos.class);
                if (holidayInfos != null) {
                    HolidayInfo info;
                    if (type == HolidayType.HOLIDAY) {
                        info = holidayInfos.getHolidayInfo();
                    } else {
                        info = holidayInfos.getDefferedInfo();
                    }
                    if (info != null) {
                        String[][] day = info.getDay();
                        for (int i = 0; i < day.length; i++) {
                            List<Integer> list = new ArrayList();
                            for (int j = 0; j < day[i].length; j++) {
                                if (TextUtils.isEmpty(day[i][j])) {
                                    continue;
                                }
                                try {
                                    list.add(Integer.parseInt(day[i][j]));
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                            monthArray.put(i, list);
                        }
                    }
                }
            }
        }
        if (monthArray != null && monthArray.get(month) != null) {
            return monthArray.get(month);
        } else {
            return new ArrayList<>();
        }
    }

    private SPUtils getHolidayCache() {
        if (holidayCache == null) {
            holidayCache = new SPUtils(mContext, CalendarConst.SPFileName.CALENDAR_SETTING);
        }
        return holidayCache;
    }

    public List<String> getWeekTitleList(Context context) {
        int firstDay = getWeekFirstDay();
        List<String> titleList = new ArrayList<>();
        if (firstDay == Calendar.SUNDAY) {
            Collections.addAll(titleList, new String[]{"日", "一", "二", "三", "四", "五", "六"});
        } else {
            Collections.addAll(titleList, new String[]{"一", "二", "三", "四", "五", "六", "日"});
        }
        return titleList;
    }

    /**
     * 获取一个月所有的周末
     *
     * @param calendar
     * @param date     {@link Calendar#DAY_OF_WEEK}
     * @return
     */
    public List<Integer> getWeekend(Calendar calendar, int date) {
        SparseArray<SparseArray<List<Integer>>> dateArray = null;
        if (date == Calendar.SATURDAY) {
            dateArray = saturdayArray;
        } else if (date == Calendar.SUNDAY) {
            dateArray = sundayArray;
        }
        if (dateArray == null) {
            dateArray = new SparseArray<>();
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        SparseArray<List<Integer>> monthArray = dateArray.get(year);

        if (monthArray == null) {
            monthArray = new SparseArray<>();
            dateArray.put(year, monthArray);
        }
        List<Integer> dateList = monthArray.get(month);
        if (dateList == null) {
            dateList = new ArrayList<>();
            Calendar tmpCalendar = Calendar.getInstance();
            tmpCalendar.set(year, month, 1, 0, 0, 0);
            int tmpMonth;
            do {
                int day = tmpCalendar.get(Calendar.DAY_OF_WEEK);
                if (day == date) {
                    dateList.add(tmpCalendar.get(Calendar.DATE));
                }
                tmpCalendar.add(Calendar.DATE, 1);
                tmpMonth = tmpCalendar.get(Calendar.MONTH);
            } while (tmpMonth == month);
            monthArray.put(month, dateList);
        }

        if (monthArray != null && monthArray.get(month) != null) {
            return monthArray.get(month);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 设置月第一天
     *
     * @param monthFirstDay
     */
    public static void setMonthFirstDay(int monthFirstDay) {
        CalendarDataCenter.monthFirstDay = monthFirstDay;
    }

    /**
     * 获取一个月的周期
     *
     * @param calendarSelected
     * @return
     */
    public static Calendar[] getMonthPeriod(Calendar calendarSelected) {
        Calendar tmpCalendar = TimeUtils.copyCalendar(calendarSelected);
        Calendar calendar1 = TimeUtils.copyCalendar(calendarSelected);
        Calendar calendar2 = TimeUtils.copyCalendar(calendarSelected);

        if (calendarSelected.get(Calendar.DATE) < monthFirstDay) {
            tmpCalendar.add(Calendar.MONTH, -1);
        }

        calendar1.set(tmpCalendar.get(Calendar.YEAR),
                tmpCalendar.get(Calendar.MONTH),
                monthFirstDay,
                0, 0, 0);
        //注意必须设毫秒，日历等很多地方都是按毫秒存数据
        calendar1.set(Calendar.MILLISECOND, 0);
        calendar2.setTimeInMillis(calendar1.getTimeInMillis());
        calendar2.add(Calendar.MONTH, 1);
        return new Calendar[]{calendar1, calendar2};
    }

    /**
     * 获取viewpager中指定position的日期
     *
     * @param position
     * @param curCalendar
     * @param count
     * @param calendarType
     * @return
     */
    public static Calendar getCalendarOnPosition(int position, Calendar curCalendar, int count, @CalendarType int calendarType) {
        int offset = position - count / 2;
        Calendar tmpCalendar = TimeUtils.copyCalendar(mToday);
        if (calendarType == CalendarType.CALENDAR_MONTH) {
            tmpCalendar.add(Calendar.MONTH, offset);
        } else {
            tmpCalendar.add(Calendar.WEEK_OF_YEAR, offset);
        }
        return tmpCalendar;
    }

    public static int getWeekRowInMonth(Calendar calendar) {
        int daysToMonthFirtDay = getDaysToMonthFirstDay(calendar);
        return daysToMonthFirtDay / 7;
    }

    public static int getMonthRows(Calendar calendar) {
        int monthDays = getInstance().getMonthDayInfoList(calendar).size();
        return monthDays / 7;
    }

    public static int getDaysToMonthFirstDay(Calendar calendar) {
        //判断选中的日期在月历第几行
        //月考勤日，在日历第一行,例如1月1日0时0分0秒0毫秒，注意毫秒必须设，否则取数据不准
        Calendar calendarStart = getMonthPeriod(calendar)[0];
        setToWeekFirstDay(calendarStart);
        int daysBetweenDate = TimeUtils.getDaysBetweenDate(calendar, calendarStart);
        //从一个月的数据中截取数据的起始位置
        return daysBetweenDate;
    }

    public static int getDaysToWeekFirstDay(Calendar calendar) {
        Calendar tmpCalendar = TimeUtils.copyCalendar(calendar);
        //设置到周一，日历第一行第一列
        setToWeekFirstDay(tmpCalendar);
        int daysBetweenDate = TimeUtils.getDaysBetweenDate(calendar, tmpCalendar);
        //从一个月的数据中截取数据的起始位置
        return daysBetweenDate;
    }

    public static void setToWeekFirstDay(Calendar calendar) {
        //设置到周一，日历第一行第一列
        int weekFirstDay = getWeekFirstDay();
        if (weekFirstDay == Calendar.SUNDAY) {
            TimeUtils.setToSunday(calendar, true);
        } else {
            TimeUtils.setToMonday(calendar, false);
        }
    }

    public static int getWeeksToToday(Calendar calendar) {
        return TimeUtils.getWeeksBetweenDate(calendar, mToday);
    }

    public static int getMonthsToToday(Calendar calendar) {
        Calendar[] curPeriod = getMonthPeriod(calendar);
        Calendar[] todayPeriod = getMonthPeriod(mToday);
        return TimeUtils.getMonthCountBetweenDate(curPeriod[0], todayPeriod[0]);
    }


    @DateType
    public static int getDayType(Calendar calendar, Calendar[] checkingDate) {
        if (TimeUtils.isDayBetweenTwoDate(calendar, checkingDate)) {
            return DateType.CUR_MONTH;
        } else {
            if (TimeUtils.compareCalendar(calendar, checkingDate[0]) < 0) {
                return DateType.LAST_MONTH;
            } else {
                return DateType.NEXT_MONTH;
            }
        }
    }

    @DateType
    public static int getDayType(Calendar targetCalendar, Calendar curCalendar) {
        return getDayType(targetCalendar, getMonthPeriod(curCalendar));
    }
}
