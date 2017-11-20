package com.jady.calendar.model.data;

import java.util.Calendar;

/**
 * @Description: Created by jadyli on 2017/5/17.
 *
 * 日期信息
 */
public class BaseDayInfo {
    private String strDayInMonth = "", strFestival = "";
    /**
     * dayOfMonth:1005(10月5日，10*100+5)
     * day:5
     */
    private int year = 0, month = 0, day = 0;
    private Calendar calendar = Calendar.getInstance();
    private int dayOfMonth = 0;
    private boolean isHoliday = false;
    private boolean isChoosed = false;
    private boolean isToday = false, isSaturday = false, isSunday;
    private boolean isSolarTerms = false, isFestival = false, isDeferred = false;
    private boolean isDecorBG = false;
    private boolean isDecorTL, isDecorT, isDecorTR, isDecorL, isDecorR;

    public BaseDayInfo() {
    }

    public BaseDayInfo(Calendar calendar) {
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.day = calendar.get(Calendar.DATE);
        this.dayOfMonth = this.month * 100 + this.day;
        this.calendar.set(year, month, day, 0, 0, 0);
        this.calendar.set(Calendar.MILLISECOND, 0);
    }

    public String getStrDayInMonth() {
        return strDayInMonth;
    }

    public void setStrDayInMonth(String strDayInMonth) {
        this.strDayInMonth = strDayInMonth;
    }

    public String getStrFestival() {
        return strFestival;
    }

    public void setStrFestival(String strFestival) {
        this.strFestival = strFestival;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public boolean isHoliday() {
        return isHoliday;
    }

    public void setHoliday(boolean holiday) {
        isHoliday = holiday;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    public boolean isSolarTerms() {
        return isSolarTerms;
    }

    public void setSolarTerms(boolean solarTerms) {
        isSolarTerms = solarTerms;
    }

    public boolean isSaturday() {
        return isSaturday;
    }

    public void setSaturday(boolean saturday) {
        isSaturday = saturday;
    }

    public boolean isSunday() {
        return isSunday;
    }

    public void setSunday(boolean sunday) {
        isSunday = sunday;
    }

    public boolean isFestival() {
        return isFestival;
    }

    public void setFestival(boolean festival) {
        isFestival = festival;
    }

    public boolean isDeferred() {
        return isDeferred;
    }

    public void setDeferred(boolean deferred) {
        isDeferred = deferred;
    }

    public boolean isDecorBG() {
        return isDecorBG;
    }

    public void setDecorBG(boolean decorBG) {
        isDecorBG = decorBG;
    }

    public boolean isDecorTL() {
        return isDecorTL;
    }

    public void setDecorTL(boolean decorTL) {
        isDecorTL = decorTL;
    }

    public boolean isDecorT() {
        return isDecorT;
    }

    public void setDecorT(boolean decorT) {
        isDecorT = decorT;
    }

    public boolean isDecorTR() {
        return isDecorTR;
    }

    public void setDecorTR(boolean decorTR) {
        isDecorTR = decorTR;
    }

    public boolean isDecorL() {
        return isDecorL;
    }

    public void setDecorL(boolean decorL) {
        isDecorL = decorL;
    }

    public boolean isDecorR() {
        return isDecorR;
    }

    public void setDecorR(boolean decorR) {
        isDecorR = decorR;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public String toString() {
        return "BaseDayInfo{" +
                "strDayInMonth='" + strDayInMonth + '\'' +
                ", strFestival='" + strFestival + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", calendar=" + calendar +
                ", dayOfMonth=" + dayOfMonth +
                ", isHoliday=" + isHoliday +
                ", isChoosed=" + isChoosed +
                ", isToday=" + isToday +
                ", isSaturday=" + isSaturday +
                ", isSunday=" + isSunday +
                ", isSolarTerms=" + isSolarTerms +
                ", isFestival=" + isFestival +
                ", isDeferred=" + isDeferred +
                ", isDecorBG=" + isDecorBG +
                ", isDecorTL=" + isDecorTL +
                ", isDecorT=" + isDecorT +
                ", isDecorTR=" + isDecorTR +
                ", isDecorL=" + isDecorL +
                ", isDecorR=" + isDecorR +
                '}';
    }
}