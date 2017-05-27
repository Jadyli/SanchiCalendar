package com.jady.calendar.model.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by lipingfa on 2017/5/10.
 */
@IntDef({CalendarType.CALENDAR_MONTH,CalendarType.CALENDAR_WEEK})
@Retention(RetentionPolicy.SOURCE)
public @interface CalendarType {
    int CALENDAR_MONTH = 1;
    int CALENDAR_WEEK = 2;
}
