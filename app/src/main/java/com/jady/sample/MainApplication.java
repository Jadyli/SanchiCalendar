package com.jady.sample;

import android.app.Application;

import com.jady.calendar.model.data.CalendarDataCenter;

/**
 * @author jady
 * @email jady1257984872@gmail.com
 * @create 2017-10-13 17:40
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        CalendarDataCenter.init(this);
    }
}
