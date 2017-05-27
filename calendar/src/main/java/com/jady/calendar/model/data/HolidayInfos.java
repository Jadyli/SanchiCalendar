package com.jady.calendar.model.data;

public class HolidayInfos {
    //补休日
    private HolidayInfo defferedInfo;
    //节假日
    private HolidayInfo holidayInfo;

    public HolidayInfo getDefferedInfo() {
        return defferedInfo;
    }

    public void setDefferedInfo(HolidayInfo defferedInfo) {
        this.defferedInfo = defferedInfo;
    }

    public HolidayInfo getHolidayInfo() {
        return holidayInfo;
    }

    public void setHolidayInfo(HolidayInfo holidayInfo) {
        this.holidayInfo = holidayInfo;
    }
}
