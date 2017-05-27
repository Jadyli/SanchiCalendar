package com.jady.calendar.model.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Description: Created by jadyli on 2017/5/17.
 */ //日期类型：当月，上月，下月
@IntDef({DateType.LAST_MONTH, DateType.NEXT_MONTH, DateType.CUR_MONTH})
@Retention(RetentionPolicy.SOURCE)
public @interface DateType {

    //按上个月处理
    int LAST_MONTH = 0;
    //按下个月处理
    int NEXT_MONTH = 1;
    //按当前月处理
    int CUR_MONTH = 2;
}
