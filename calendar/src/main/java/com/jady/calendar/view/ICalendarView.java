package com.jady.calendar.view;

import com.jady.calendar.model.data.BaseCalendarData;
import com.jady.calendar.model.annotations.CalendarType;

import java.util.List;

/**
 * @Description: Created by jadyli on 2017/5/9.
 */
public interface ICalendarView {
    void updateData(List<BaseCalendarData> daySumValueList);

    @CalendarType
    int getType();
}
