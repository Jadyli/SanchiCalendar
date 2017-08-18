package com.jady.calendar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.jady.calendar.R;
import com.jady.calendar.model.annotations.CalendarType;
import com.jady.calendar.model.annotations.DateType;
import com.jady.calendar.model.data.CalendarDataCenter;
import com.jady.calendar.model.data.DayInfo;
import com.jady.calendar.presenter.CalendarPresenter;
import com.jady.calendar.utils.TimeUtils;

import java.util.Calendar;

import static com.jady.calendar.model.data.CalendarDataCenter.globalCalendar;
import static com.jady.calendar.model.data.CalendarDataCenter.isInMultipleMode;
import static com.jady.calendar.model.data.CalendarDataCenter.mDateList;

/**
 * Created by lipingfa on 2017/8/17.
 */
public class MiCalendarView extends BaseCalendarView {

    public static final String TAG = "MiCalendarView";

    //颜色
    //今天，当月日期颜色，上个月和下个月日期颜色,周六日期颜色，周日日期颜色
    //选中日期的描边颜色，未选中日期的描边颜色，普通背景颜色，选中背景颜色，特定背景颜色，波浪图描边颜色，波浪图填充颜色
    protected int mTodayColor, mCurMonDateColor, mOtherMonDateColor, mSaturdayColor, mSundayColor,
            mSelectedStrokeColor, mMultipleStrokeColor, mMultipleFillColor, mMultipleCenterColor,
            mNormalStrokeColor, mNormalBgColor, mSelectedBgColor, mSpecialBgColor;

    //日期字体大小，描边大小
    protected float mDateSize, mNormalStrokeSize, mSelectedStrokeSize, mMultipleStrokeSize, mMultipleExternalRadius, mMultipleInnerRadius;

    //当前的日历类型：周历/月历
    @CalendarType
    protected int curCalendarType = CalendarType.CALENDAR_MONTH;
    //每个日历格子的列宽和行高
    protected float mColumnWidth, mRowHeight;
    //总行数，为了扩展成可变行数预留
    protected int rowNums = 7;
    //日期画笔，背景画笔，描边画笔，时间画笔，金钱画笔，波浪图画笔
    protected Paint mDatePaint, mBgPaint, mNormalStrokePaint, mSelectedStrokePaint, mMultipleFillPaint, mMultipleStrokePaint;
    //点击事件监听
    protected GestureDetector mGestureDetector;
    //选中日期考勤周期，今天所在的考勤周期
    protected Calendar[] monthPeriod, monthPeriodOfToday;
    //当前选中日期所在行
    protected int mWeekRow = 1;
    //今天
    protected Calendar mToday;


    public MiCalendarView(Context context) {
        this(context, null);
    }

    public MiCalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MiCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MiCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initAttr(context, attrs);
        initVarible();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BaseCalendarView);

        //这里有红色警告是正常情况，可直接无视
        curCalendarType = array.getInt(R.styleable.BaseCalendarView_calendar_type, CalendarType.CALENDAR_MONTH);

        //颜色
        mTodayColor = array.getColor(R.styleable.BaseCalendarView_today_color, 0xfff9359a);
        mCurMonDateColor = array.getColor(R.styleable.BaseCalendarView_cur_month_date_color, 0xffffffff);
        mOtherMonDateColor = array.getColor(R.styleable.BaseCalendarView_other_month_date_color, 0xff7d97d7);
        mSaturdayColor = array.getColor(R.styleable.BaseCalendarView_saturday_color, 0xff5dcb60);
        mSundayColor = array.getColor(R.styleable.BaseCalendarView_sunday_color, 0xffcfa06a);
        mSelectedStrokeColor = array.getColor(R.styleable.BaseCalendarView_selected_stroke_color, 0xff95c9ff);
        mNormalStrokeColor = array.getColor(R.styleable.BaseCalendarView_normal_stroke_color, 0xff5296dc);
        mNormalBgColor = array.getColor(R.styleable.BaseCalendarView_normal_bg_color, 0x00000000);
        mSelectedBgColor = array.getColor(R.styleable.BaseCalendarView_selected_bg_color, 0x00000000);
        mSpecialBgColor = array.getColor(R.styleable.BaseCalendarView_special_bg_color, 0x00000000);
        mMultipleCenterColor = array.getColor(R.styleable.BaseCalendarView_multiple_center_color, 0xffffffff);
        mMultipleFillColor = array.getColor(R.styleable.BaseCalendarView_multiple_fill_color, 0xff59a1eb);
        mMultipleStrokeColor = array.getColor(R.styleable.BaseCalendarView_multiple_stroke_color, 0xffffffff);

        mDateSize = array.getDimension(R.styleable.BaseCalendarView_date_size, dp2px(11));
        mNormalStrokeSize = array.getDimension(R.styleable.BaseCalendarView_normal_stroke_size, 1);
        mSelectedStrokeSize = array.getDimension(R.styleable.BaseCalendarView_selected_stroke_size, dp2px(1));
        mMultipleStrokeSize = array.getDimension(R.styleable.BaseCalendarView_selected_stroke_size, dp2px(2));
        mMultipleExternalRadius = array.getDimension(R.styleable.BaseCalendarView_multiple_external_radius, dp2px(5));
        mMultipleInnerRadius = array.getDimension(R.styleable.BaseCalendarView_multiple_inner_radius, dp2px(3));
        array.recycle();
    }

    private void initVarible() {
        //日期画笔
        mDatePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mDatePaint.setTextSize(mDateSize);
        //背景画笔
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mBgPaint.setStyle(Paint.Style.FILL);
        //描边画笔
        mNormalStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mNormalStrokePaint.setStyle(Paint.Style.STROKE);
        mNormalStrokePaint.setColor(mNormalStrokeColor);
        mNormalStrokePaint.setStrokeWidth(mNormalStrokeSize);
        mSelectedStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mSelectedStrokePaint.setStyle(Paint.Style.STROKE);
        mSelectedStrokePaint.setColor(mSelectedStrokeColor);
        mSelectedStrokePaint.setStrokeWidth(mSelectedStrokeSize);
        //多选框画笔
        mMultipleFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mMultipleFillPaint.setStyle(Paint.Style.FILL);
        mMultipleStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mMultipleStrokePaint.setStyle(Paint.Style.STROKE);
        mMultipleStrokePaint.setColor(mMultipleStrokeColor);
        mMultipleStrokePaint.setStrokeWidth(mMultipleStrokeSize);
        //点击事件监听
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d(TAG, "onSingleTapUp");
                onClick(e.getX(), e.getY());
                return true;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                Log.d(TAG, "onDoubleTapEvent");
//                onClick(e.getX(), e.getY());
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Log.d(TAG, "onDoubleTap");
                onDoubleClick(e.getX(), e.getY());
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                onLongClick(e.getX(), e.getY());
            }
        });
        mToday = TimeUtils.getToday();
        mCurRenderCalendar = TimeUtils.getToday();
    }

    @Override
    protected void onPreDrawGrid(Canvas canvas) {
        //当前选中日期所在的考勤周期
        monthPeriod = CalendarDataCenter.getMonthPeriod(mCurRenderCalendar);
        monthPeriodOfToday = CalendarDataCenter.getMonthPeriod(mToday);
    }

    @Override
    protected void onDrawGrid(Canvas canvas, DayInfo dayInfo, int row, int column) {
        RectF rect = new RectF(column * mColumnWidth, row * mRowHeight, (column + 1) * mColumnWidth, (row + 1) * mRowHeight);
        int dataIndex = row * 7 + column;
        @DateType int dayType = CalendarDataCenter.getDayType(dayInfo.getCalendar(), monthPeriod);

        Calendar calendar = dayInfo.getCalendar();
        if (TimeUtils.isSameDay(mCurRenderCalendar, calendar)) {
            setWeekRow(row);
        }
        if (dayInfo.isToday()) {
            mBgPaint.setColor(mTodayColor);
            canvas.drawCircle(rect.right - mColumnWidth / 2, rect.bottom - mRowHeight / 2, mColumnWidth * 0.6f, mBgPaint);
        }

        int datePaintColor = mCurMonDateColor;
        datePaintColor = getDatePaintColor(dayInfo, dayType, datePaintColor);

        mDatePaint.setColor(datePaintColor);
        String date = dayInfo.getDay() + "";
        float dateStartX = rect.left + dp2px(5);
        //mDatePaint.ascent() < 0,mDatePaint.descent() > 0,- (mDatePaint.ascent() - mDatePaint.descent()）是字体高度..

        float dateStartY = rect.top + dp2px(2) + Math.abs(mDatePaint.ascent());
        canvas.drawText(date, dateStartX, dateStartY, mDatePaint);

        RectF holidayRect = new RectF(rect.right - dp2px(16), rect.top, rect.right, rect.top + dp2px(16));
        if (dayInfo.isHoliday()) {
        }
        if (dayInfo.isDeferred()) {
        }

        if (isInMultipleMode && dayInfo.getCalendar().getTimeInMillis() < monthPeriodOfToday[1].getTimeInMillis()) {
            float cx = rect.right - dp2px(10);
            float cy = rect.top + dp2px(10);
            canvas.drawCircle(cx, cy, mMultipleExternalRadius, mMultipleStrokePaint);
            mMultipleFillPaint.setColor(mMultipleFillColor);
            canvas.drawCircle(cx, cy, mMultipleExternalRadius, mMultipleFillPaint);
            if (mDateList.contains(calendar.getTimeInMillis())) {
                mMultipleFillPaint.setColor(mMultipleStrokeColor);
                canvas.drawCircle(cx, cy, mMultipleInnerRadius, mMultipleFillPaint);
            }
        }
    }

    @Override
    protected void onPostDrawGrid(Canvas canvas) {

    }

    private int getDatePaintColor(DayInfo dayInfo, @DateType int dayType, int datePaintColor) {
        if (curCalendarType == CalendarType.CALENDAR_MONTH) {
            switch (dayType) {
                case DateType.LAST_MONTH:
                case DateType.NEXT_MONTH:
                    if (dayInfo.isSaturday()) {
                        datePaintColor = 0xff3b8b9d;
                    } else if (dayInfo.isSunday()) {
                        datePaintColor = 0xff7d7499;
                    } else {
                        datePaintColor = mOtherMonDateColor;
                    }
                    break;
                case DateType.CUR_MONTH:
                    datePaintColor = getSpecialDatePaintColor(dayInfo);
                    break;
            }
        } else {
            if (dayInfo.getCalendar().getTimeInMillis() >= monthPeriodOfToday[1].getTimeInMillis()) {
                datePaintColor = mOtherMonDateColor;
            } else {
                datePaintColor = getSpecialDatePaintColor(dayInfo);
            }
        }
        return datePaintColor;
    }

    private int getSpecialDatePaintColor(DayInfo dayInfo) {
        int datePaintColor = mCurMonDateColor;
        if (dayInfo.isSaturday()) {
            datePaintColor = mSaturdayColor;
        }
        if (dayInfo.isSunday()) {
            datePaintColor = mSundayColor;
        }
//        if (dayInfo.isToday()) {
//            datePaintColor = mTodayColor;
//        }
        return datePaintColor;
    }

    @Override
    protected int getRowNumbers() {
        return rowNums;
    }

    public void setWeekRow(int weekRow) {
        this.mWeekRow = weekRow + 1;
    }

    private void onClick(float x, float y) {
        int column = (int) (x / mColumnWidth);
        int row = (int) (y / mRowHeight);
        Log.d(TAG, "onClick," + "x:" + x + ",y:" + y + ",column:" + column + ",row:" + row);
        if (column < 0 || row < 0) {
            return;
        }
        int dataIndex = row * 7 + column;
        if (dataIndex > mDayInfoList.size() - 1) {
            return;
        }
        Calendar clickCalendar = mDayInfoList.get(dataIndex).getCalendar();
        long clickTimeInMillis = clickCalendar.getTimeInMillis();
        if (clickTimeInMillis >= monthPeriodOfToday[1].getTimeInMillis()) {
            return;
        }

        if (isInMultipleMode) {
            if (mDateList.contains(clickTimeInMillis)) {
                mDateList.remove(clickTimeInMillis);
            } else {
                mDateList.add(clickTimeInMillis);
            }
            invalidate();
        } else {
            globalCalendar.setTimeInMillis(clickTimeInMillis);
            //抛事件表示当前选择日期改变了
        }
    }

    private void onDoubleClick(float x, float y) {
        if (isInMultipleMode) {
            return;
        }
        int column = (int) (x / mColumnWidth);
        int row = (int) (y / mRowHeight);
        Log.d(TAG, "onDoubleClick," + "x:" + x + ",y:" + y + ",column:" + column + ",row:" + row);
        if (column < 0 || row < 0) {
            return;
        }
        int dataIndex = row * 7 + column;
        if (dataIndex > mDayInfoList.size() - 1) {
            return;
        }
        Calendar clickCalendar = mDayInfoList.get(dataIndex).getCalendar();
        long clickTimeInMillis = clickCalendar.getTimeInMillis();
        if (clickTimeInMillis >= monthPeriodOfToday[1].getTimeInMillis()) {
            return;
        }

        globalCalendar.setTimeInMillis(clickTimeInMillis);
        //抛事件表示当前选择日期改变了
    }

    private void onLongClick(float x, float y) {
        if (isInMultipleMode) {
            return;
        }
        int column = (int) (x / mColumnWidth);
        int row = (int) (y / mRowHeight);
        Log.d(TAG, "onDoubleClick," + "x:" + x + ",y:" + y + ",column:" + column + ",row:" + row);
        if (column < 0 || row < 0) {
            return;
        }
        int dataIndex = row * 7 + column;
        if (dataIndex > mDayInfoList.size() - 1) {
            return;
        }
        Calendar clickCalendar = mDayInfoList.get(dataIndex).getCalendar();
        if (clickCalendar.getTimeInMillis() >= monthPeriodOfToday[1].getTimeInMillis()) {
            return;
        }

        globalCalendar = TimeUtils.copyCalendar(clickCalendar);
        mDateList.add(clickCalendar.getTimeInMillis());
    }

    private float dp2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }
}
