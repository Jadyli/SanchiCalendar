package com.jady.calendar.model.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by lipingfa on 2017/5/10.
 */
@IntDef({HolidayType.HOLIDAY, HolidayType.DEFFERED})
@Retention(RetentionPolicy.SOURCE)
public @interface HolidayType {
    //节假日
    int HOLIDAY = 1;
    //补休日
    int DEFFERED = 2;
}
