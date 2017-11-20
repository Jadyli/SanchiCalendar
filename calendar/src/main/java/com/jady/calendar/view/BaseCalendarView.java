package com.jady.calendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.jady.calendar.model.annotations.CalendarType;
import com.jady.calendar.model.data.BaseDayInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @Description: Created by jadyli on 2017/5/9.
 */
public abstract class BaseCalendarView extends View {

    protected List<BaseDayInfo> mDayInfoList = new ArrayList<>();
    //当前正在渲染的Calendar,ViewPager会提前渲染前后两页的Calendar
    protected Calendar mCurRendingCalendar;
    @CalendarType
    protected int curCalendarType = CalendarType.CALENDAR_MONTH;

    public BaseCalendarView(Context context) {
        this(context, null);
    }

    public BaseCalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDayInfoList == null || mDayInfoList.size() == 0) {
            return;
        }

        onPreDrawGrid(canvas);

        if (curCalendarType == CalendarType.CALENDAR_MONTH) {
            //月历
            if (mDayInfoList.size() < getRowNumbers() * 7) {
                return;
            }
            for (int i = 0; i < getRowNumbers(); i++) {
                for (int j = 0; j < 7; j++) {
                    int dataIndex = i * 7 + j;
                    onDrawGrid(canvas, mDayInfoList.get(dataIndex), i, j);
                }
            }
        } else {
            //周历
            for (int i = 0; i < 7; i++) {
                onDrawGrid(canvas, mDayInfoList.get(i), 0, i);
            }
        }

        onPostDrawGrid(canvas);
    }

    public void updateData(List<BaseDayInfo> dayInfoList) {
        this.mDayInfoList.clear();
        this.mDayInfoList.addAll(dayInfoList);
    }

    protected abstract void onPreDrawGrid(Canvas canvas);

    protected abstract void onDrawGrid(Canvas canvas, BaseDayInfo baseDayInfo, int row, int column);

    protected abstract void onPostDrawGrid(Canvas canvas);

    protected abstract int getRowNumbers();
}
