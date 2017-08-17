package com.jady.calendar.presenter;

import com.jady.calendar.model.annotations.CalendarType;
import com.jady.calendar.model.data.CalendarDataCenter;
import com.jady.calendar.model.data.DayInfo;
import com.jady.calendar.utils.ThreadCallback;
import com.jady.calendar.utils.ThreadUtils;
import com.jady.calendar.view.ICalendarView;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @Description: Created by jadyli on 2017/5/9.
 */
public class CalendarPresenter {
    private ICalendarView mCalendarView;
    private List<DayInfo> dataList;

    public CalendarPresenter(ICalendarView mCalendarView) {
        this.mCalendarView = mCalendarView;
    }

    public void loadData(final Calendar calendar,@CalendarType final int type) {
        ThreadUtils.startChildThread(new ThreadCallback() {
            @Override
            public void runOperate() throws InterruptedException, ExecutionException, IOException {
                if (type == CalendarType.CALENDAR_MONTH) {
                    dataList = CalendarDataCenter.getInstance().getMonthDayInfoList(calendar);
                } else {
                    dataList = CalendarDataCenter.getInstance().getWeekDayInfoList(calendar);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onCompleted() {
                mCalendarView.updateData(dataList);
            }
        });
    }
}
