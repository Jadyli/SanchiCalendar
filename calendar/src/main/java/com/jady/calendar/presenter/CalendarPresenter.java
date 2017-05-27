package com.jady.calendar.presenter;

import com.jady.calendar.model.data.BaseCalendarData;
import com.jady.calendar.view.ICalendarView;

import java.util.Calendar;
import java.util.List;

/**
 * @Description: Created by jadyli on 2017/5/9.
 */
public class CalendarPresenter {
    private ICalendarView mCalendarView;
    private List<BaseCalendarData> dataList;

    public CalendarPresenter(ICalendarView mCalendarView) {
        this.mCalendarView = mCalendarView;
    }

    public void loadData(final Calendar calendar) {
    }
}
