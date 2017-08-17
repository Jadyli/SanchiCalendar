package com.jady.calendar.view;

import com.jady.calendar.model.data.DayInfo;

import java.util.List;

/**
 * @Description: Created by jadyli on 2017/5/9.
 */
public interface ICalendarView {
    void updateData(List<DayInfo> dayInfoList);
}
