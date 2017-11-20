package com.jady.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jady.calendar.model.data.BaseDayInfo;
import com.jady.calendar.model.data.CalendarDataCenter;
import com.jady.calendar.view.MiCalendarView;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.calendar_view)
    MiCalendarView mCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        Flowable.create(new FlowableOnSubscribe<List<BaseDayInfo>>() {
            @Override
            public void subscribe(FlowableEmitter<List<BaseDayInfo>> e) throws Exception {
                List<BaseDayInfo> monthDayInfoList = CalendarDataCenter.getMonthDayInfoList(Calendar.getInstance());
                e.onNext(monthDayInfoList);
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<BaseDayInfo>>() {
                    @Override
                    public void accept(List<BaseDayInfo> baseDayInfos) throws Exception {
                        mCalendarView.updateData(baseDayInfos);
                    }
                });
    }
}
