package com.jady.calendar.model.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by lipingfa on 2017/6/6.
 */
@IntDef({WeekFirstDay.SUNDAY, WeekFirstDay.MONDAY})
@Retention(RetentionPolicy.SOURCE)
public @interface WeekFirstDay {
    int SUNDAY = 1;
    int MONDAY = 2;
}
