package com.qeeniao.mobile.recordincomej.modules.calendar.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.jady.calendar.R;
import com.jady.calendar.model.annotations.CalendarType;
import com.jady.calendar.model.annotations.DateType;
import com.jady.calendar.model.data.BaseCalendarData;
import com.jady.calendar.model.data.CalendarDataCenter;
import com.jady.calendar.model.data.DayInfo;
import com.jady.calendar.presenter.CalendarPresenter;
import com.jady.calendar.utils.DataUtility;
import com.jady.calendar.utils.TimeUtils;
import com.jady.calendar.view.ICalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @Description: Created by jadyli on 2017/5/9.
 */
public class BaseCalendarView extends View implements ICalendarView {

    //颜色
    //今天，当月日期颜色，上个月和下个月日期颜色,周六日期颜色，周日日期颜色
    //选中日期的描边颜色，未选中日期的描边颜色，普通背景颜色，选中背景颜色，特定背景颜色，波浪图描边颜色，波浪图填充颜色
    protected int mTodayColor, mCurMonDateColor, mOtherMonDateColor, mSaturdayColor, mSundayColor,
            mSelectedStrokeColor, mNormalStrokeColor, mNormalBgColor, mSelectedBgColor, mSpecialBgColor, mWaveViewStrokeColor, mWaveViewFillColor;
    //日期字体大小，描边大小
    protected float mDateSize, mNormalStrokeSize, mSelectedStrokeSize;
    //数据：时间图标大小/时间字体大小，金钱字体大小，波浪图描边宽度
    protected float mHourIconSize, mHourDataSize, mMoneyDataSize, mWaveViewStrokeSize;

    //当前月数据
    protected List<BaseCalendarData> mDataList = new ArrayList<>();

    //节假日、补休日图标
    protected Bitmap mHolodayBitmap, mDefferedBitmap;
    //当前的日历类型：周历/月历
    @CalendarType
    protected int curCalendarType = CalendarType.CALENDAR_MONTH;
    //每个日历格子的列宽和行高
    protected float mColumnWidth, mRowHeight;
    //总行数，为了扩展成可变行数预留
    protected int rowNums = 6;
    //日期画笔，背景画笔，描边画笔
    protected Paint mDatePaint, mBgPaint, mNormalStrokePaint, mSelectedStrokePaint;
    //点击事件监听
    protected GestureDetector mGestureDetector;
    //处理数据的Presenter
    protected CalendarPresenter mCalendarPresenter;
    //选中日期考勤周期，今天所在的考勤周期
    protected Calendar[] checkinDate, todayCheckinDate;
    //当前选中日期所在行
    protected int mWeekRow = 1;
    //今天
    protected Calendar mToday;
    //当前正在渲染的Calendar,ViewPager会提前渲染前后两页的Calendar
    protected Calendar mCurRenderCalendar;

    public BaseCalendarView(Context context, TypedArray array, @CalendarType int calendarType) {
        this(context, null, array, calendarType);
    }

    public BaseCalendarView(Context context, @Nullable AttributeSet attrs, TypedArray array, @CalendarType int calendarType) {
        this(context, attrs, 0, array, calendarType);
    }

    public BaseCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, TypedArray array, @CalendarType int calendarType) {
        super(context, attrs, defStyleAttr);

        initArray(array);
        initVarible(calendarType);
    }

    private void initArray(TypedArray array) {
        mTodayColor = array.getColor(R.styleable.BaseCalendarView_today_color, 0xff3aa3db);
        mCurMonDateColor = array.getColor(R.styleable.BaseCalendarView_cur_month_date_color, 0xff555555);
        mOtherMonDateColor = array.getColor(R.styleable.BaseCalendarView_other_month_date_color, 0xffbdbcc1);
        mSaturdayColor = array.getColor(R.styleable.BaseCalendarView_saturday_color, 0xff0d6c97);
        mSundayColor = array.getColor(R.styleable.BaseCalendarView_sunday_color, 0xffb5545c);
        mSelectedStrokeColor = array.getColor(R.styleable.BaseCalendarView_selected_stroke_color, 0xff3aa3da);
        mNormalStrokeColor = array.getColor(R.styleable.BaseCalendarView_normal_stroke_color, 0xffeeeeee);
        mNormalBgColor = array.getColor(R.styleable.BaseCalendarView_normal_bg_color, 0xffffffff);
        mSelectedBgColor = array.getColor(R.styleable.BaseCalendarView_selected_bg_color, 0xffffffff);
        mSpecialBgColor = array.getColor(R.styleable.BaseCalendarView_special_bg_color, 0xfff1f7ff);

        mDateSize = array.getDimension(R.styleable.BaseCalendarView_date_size, dp2px(10));
        mNormalStrokeSize = array.getDimension(R.styleable.BaseCalendarView_normal_stroke_size, 1);
        mSelectedStrokeSize = array.getDimension(R.styleable.BaseCalendarView_selected_stroke_size, dp2px(1));

        mHourIconSize = array.getDimension(R.styleable.BaseCalendarView_hour_icon_size, dp2px(12));
        mHourDataSize = array.getDimension(R.styleable.BaseCalendarView_hour_data_size, dp2px(12));
        mMoneyDataSize = array.getDimension(R.styleable.BaseCalendarView_money_data_size, dp2px(10));
        mWaveViewStrokeSize = array.getDimension(R.styleable.BaseCalendarView_wave_view_stroke_size, 1);
        mWaveViewStrokeColor = array.getColor(R.styleable.BaseCalendarView_wave_view_stroke_color, 0);
        mWaveViewFillColor = array.getColor(R.styleable.BaseCalendarView_wave_view_fill_color, 0xffdff5ad);

        //这里不能回收，需要在外面回收，因为array是各个CalendarView共同引用的
//        array.recycle();
    }

    private void initVarible(@CalendarType int calendarType) {
        //日历类型
        this.curCalendarType = calendarType;
        //节假日、补休
        mHolodayBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.xiu_zhi);
        mDefferedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ban_zhi);
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
        //点击事件监听
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                System.out.println("onSingleTap");
                onClick(e.getX(), e.getY());
                return true;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                System.out.println("onDoubleTap");
                onClick(e.getX(), e.getY());
                return true;
            }
        });
        //处理数据交互的Presenter
        mCalendarPresenter = new CalendarPresenter(this);
        mToday = TimeUtils.getToday();
        mCurRenderCalendar = TimeUtils.getToday();
    }

    /**
     * 设置当前日期（当前绘制的，ViewPager会缓存两个）
     *
     * @param timeInMillis
     */
    public void setCurRenderCalendar(long timeInMillis) {
        mCurRenderCalendar.setTimeInMillis(timeInMillis);
    }

    /**
     * 获取当前日期（当前绘制的，ViewPager会缓存两个）
     *
     * @return
     */
    public Calendar getCurRenderCalendar() {
        return mCurRenderCalendar;
    }

    /**
     * 加载/更新数据
     */
    public void updateData() {
        mCalendarPresenter.loadData(mCurRenderCalendar);
    }

    @Override
    public void updateData(List<BaseCalendarData> daySumValueList) {
        this.mDataList.clear();
        this.mDataList.addAll(daySumValueList);

        updateWaveViewData(daySumValueList, mCurRenderCalendar);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDataList == null || mDataList.size() == 0) {
            return;
        }

        //当前选中日期所在的考勤周期
        checkinDate = CalendarDataCenter.getMonthPeriod(mCurRenderCalendar);
        todayCheckinDate = CalendarDataCenter.getMonthPeriod(mToday);
        if (curCalendarType == CalendarType.CALENDAR_MONTH) {
            //月历
            for (int i = 0; i < rowNums; i++) {
                for (int j = 0; j < 7; j++) {
                    draw(canvas, i, j);
                }
            }
        } else {
            //周历
            for (int i = 0; i < 7; i++) {
                draw(canvas, 0, i);
            }
        }
        drawSelectedView(canvas);
    }

    private void drawSelectedView(Canvas canvas) {
        int daysToFirstDay;
        if (curCalendarType == CalendarType.CALENDAR_MONTH) {
            daysToFirstDay = CalendarDataCenter.getDaysToMonthFirstDay(mCurRenderCalendar);
        } else {
            daysToFirstDay = CalendarDataCenter.getDaysToWeekFirstDay(mCurRenderCalendar);
        }
        int row = daysToFirstDay / 7;
        int column = daysToFirstDay % 7;
        RectF rect = new RectF(column * mColumnWidth, row * mRowHeight, (column + 1) * mColumnWidth, (row + 1) * mRowHeight);
        canvas.drawRect(rect, mSelectedStrokePaint);
    }

    /**
     * @param canvas
     * @param row    行
     * @param column 列
     */
    private void draw(Canvas canvas, int row, int column) {
        RectF rect = new RectF(column * mColumnWidth, row * mRowHeight, (column + 1) * mColumnWidth, (row + 1) * mRowHeight);
        int dataIndex = row * 7 + column;
        BaseCalendarData calendarDaySumValue = mDataList.get(dataIndex);
        @DateType int dayType = DateController.getDayType(calendarDaySumValue.getDayInfo().getCalendar(), checkinDate);
        drawCommonDateView(canvas, row, rect, calendarDaySumValue, dayType);
        if (calendarDaySumValue.isHasData()) {
            drawHourDataView(canvas, rect, calendarDaySumValue);
            if (showMoney) {
                drawMoneyDataView(canvas, rect, calendarDaySumValue);
            }
        }
    }

    /**
     * 绘制日期和节假日标记，周历和月历通用
     *
     * @param canvas
     * @param row
     * @param rect
     * @param daySumValue
     * @param dayType
     */
    private void drawCommonDateView(Canvas canvas, int row, RectF rect, BaseCalendarData daySumValue, @DateType int dayType) {
        DayInfo dayInfo = daySumValue.getDayInfo();
        if (TimeUtils.isSameDay(mCurRenderCalendar, dayInfo.getCalendar())) {
            setWeekRow(row);
        }
        int bgPaintColor;
        if (daySumValue.isHasData()) {
            bgPaintColor = mSpecialBgColor;
        } else {
            bgPaintColor = mNormalBgColor;
        }
        mBgPaint.setColor(bgPaintColor);
        canvas.drawRect(rect, mBgPaint);
        canvas.drawRect(rect, mNormalStrokePaint);

        int datePaintColor = mCurMonDateColor;
        datePaintColor = getDatePaintColor(dayInfo, dayType, datePaintColor);

        mDatePaint.setColor(datePaintColor);
        String date = dayInfo.getDay() + "";
        float dateStartX = rect.left + dp2px(3);
        //mDatePaint.ascent() < 0,mDatePaint.descent() > 0,- (mDatePaint.ascent() - mDatePaint.descent()）是字体高度
        float dateStartY = rect.top + dp2px(2) + Math.abs(mDatePaint.ascent());
        canvas.drawText(date, dateStartX, dateStartY, mDatePaint);

        RectF holidayRect = new RectF(rect.right - dp2px(16), rect.top, rect.right, rect.top + dp2px(16));
        if (dayInfo.isHoliday()) {
            canvas.drawBitmap(mHolodayBitmap, null, holidayRect, null);
        }
        if (dayInfo.isDeferred()) {
            canvas.drawBitmap(mDefferedBitmap, null, holidayRect, null);
        }
    }

    private int getDatePaintColor(DayInfo dayInfo, @DateType int dayType, int datePaintColor) {
        if (curCalendarType == CalendarType.CALENDAR_MONTH) {
            switch (dayType) {
                case DateType.LAST_MONTH:
                case DateType.NEXT_MONTH:
                    datePaintColor = mOtherMonDateColor;
                    break;
                case DateType.CUR_MONTH:
                    datePaintColor = getSpecialDatePaintColor(dayInfo);
                    break;
            }
        } else {
            if (dayInfo.getCalendar().getTimeInMillis() >= todayCheckinDate[1].getTimeInMillis()) {
                datePaintColor = mOtherMonDateColor;
            } else {
                datePaintColor = getSpecialDatePaintColor(dayInfo);
            }
        }
        return datePaintColor;
    }

    private void drawHourDataView(Canvas canvas, RectF rect, BaseCalendarData daySumHourValue) {
        String hour = "";

        String prefix = "";
        String icon = "";
        double hour1 = daySumHourValue.getHour();
        String unit = "h";
        int color = daySumHourValue.getColor();
//        boolean handled = false;
//        if (tmpHvTypeUuid.equals(ConstGlobal.UUID_TYPE_ALL)) {
//            handled = true;
        if (daySumHourValue.getLeaveDays() != 0) {
            icon = "\ue60f";
            color = daySumHourValue.getLeaveColor();
            int leave_unit = DataCenter.getInstance().getSettingModel().getLeave_unit();
            if (leave_unit == SettingModel.LeaveUnit.UNIT_DAY) {
                unit = "天";
            } else {
                unit = "h";
            }
            hour1 = daySumHourValue.getLeaveDays();
        } else if (daySumHourValue.getHour() != 0) {
            switch (DataCenter.getCurUserType()) {
                case ConstGlobal.USER_TYPE_JIABAN:
                case ConstGlobal.USER_TYPE_XIAOSHIGONG:
                    unit = "h";
                    break;
                case ConstGlobal.USER_TYPE_JIGONG:
                    unit = "工";
                    break;
                case ConstGlobal.USER_TYPE_JIJIAN:
                    unit = "件";
                    break;
                case ConstGlobal.USER_TYPE_JIXIANGMU:
                    unit = "个";
                    break;
                default:
                    unit = "h";
                    break;
            }
        } else if (daySumHourValue.getWorkHours() != 0) {
            hour1 = daySumHourValue.getWorkHours();
            unit = "h";
        } else if (daySumHourValue.getNormalWorkDays() != 0) {
            unit = "天";
            hour1 = daySumHourValue.getNormalWorkDays();
        } else if (daySumHourValue.getNoteCount() != 0) {
            icon = "\ue60b";
            unit = "条";
            hour1 = daySumHourValue.getNoteCount();
            color = daySumHourValue.getNoteColor();
        }

        if (hour1 == 0) {
            if (!DataUtility.formatNumber(Math.abs(daySumHourValue.getMoney())).equals("0")) {
                return;
            }
        }
        hour = DataUtility.formatNumber(hour1) + unit;

        if (!TextUtils.isEmpty(icon)) {
            mHourIconPaint.setColor(color);
            canvas.drawText(icon, rect.right - dp2px(16), rect.top + TextUtility.getTextHeight(mHourPaint) + Math.abs(mHourIconPaint.ascent()), mHourIconPaint);
        }
        String resizedHour = TextUtility.getResizedText(hour, dp2px(12), mColumnWidth);
        float textWidth = mHourPaint.measureText(resizedHour);

        //画背景
        float hourStartX = rect.right - textWidth - dp2px(4);
        //mDatePaint.ascent() < 0,mDatePaint.descent() > 0,- (mDatePaint.ascent() - mDatePaint.descent()）是字体高度
        float hourStartY = rect.bottom - Math.abs(mHourPaint.descent()) - dp2px(2);
        mHourPaint.setColor(0xff5aa2cf);
        canvas.drawRect(hourStartX, hourStartY - Math.abs(mHourPaint.ascent()), hourStartX + textWidth + dp2px(1), rect.bottom - dp2px(2), mHourPaint);

        mHourPaint.setColor(Color.WHITE);
        canvas.drawText(resizedHour, hourStartX, hourStartY, mHourPaint);
    }

    private void drawMoneyDataView(Canvas canvas, RectF rect, BaseCalendarData daySumValue) {
        String moneyStr = DataUtility.formatNumber(Math.abs(daySumValue.getMoney()));
        if (!moneyStr.equals("0")) {
            if (daySumValue.getMoney() < 0) {
//                mMoneyPaint.setColor(0xffff5a5a);
                moneyStr = "-" + "¥" + moneyStr;
            } else {
//                mMoneyPaint.setColor(0xff6a8d25);
                moneyStr = "¥" + moneyStr;
            }
            mMoneyPaint.setColor(0xff1e92c7);
            String resizedMoney = TextUtility.getResizedText(moneyStr, dp2px(10), mColumnWidth);
            float textWidth = mMoneyPaint.measureText(resizedMoney);
            canvas.drawText(moneyStr, rect.right - textWidth - dp2px(2),
                    rect.bottom - TextUtility.getTextHeight(mHourPaint) - Math.abs(mMoneyPaint.descent()) - dp2px(2),
                    mMoneyPaint);
        }
    }

    private int getSpecialDatePaintColor(DayInfo dayInfo) {
        int datePaintColor = mCurMonDateColor;
        if (dayInfo.isSaturday()) {
            datePaintColor = mSaturdayColor;
        }
        if (dayInfo.isSunday()) {
            datePaintColor = mSundayColor;
        }
        if (dayInfo.isToday()) {
            datePaintColor = mTodayColor;
        }
        return datePaintColor;
    }

    private void onClick(float x, float y) {
        int column = (int) (x / mColumnWidth);
        int row = (int) (y / mRowHeight);
        System.out.println("x:" + x + ",y:" + y + ",column:" + column + ",row:" + row);
        if (column < 0 || row < 0) {
            return;
        }
        int dataIndex = row * 7 + column;
        if (dataIndex > mDataList.size() - 1) {
            return;
        }
        BaseCalendarData calendarDaySumValue = mDataList.get(dataIndex);
        Calendar clickCalendar = calendarDaySumValue.getDayInfo().getCalendar();
        if (clickCalendar.getTimeInMillis() >= todayCheckinDate[1].getTimeInMillis()) {
            return;
        }

        globalCalendar = TimeUtils.copyCalendar(clickCalendar);
        //抛事件表示当前选择日期改变了
        EventCenter.post(new CalendarDateChangedEvent(calendarDaySumValue.isHasData()));
    }

    public int getWeekRow() {
        return mWeekRow;
    }

    public void setWeekRow(int weekRow) {
        this.mWeekRow = weekRow + 1;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = (int) (getResources().getDisplayMetrics().densityDpi * 200 + 0.5);
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mColumnWidth = w / 7;
        if (curCalendarType == CalendarType.CALENDAR_MONTH) {
            mRowHeight = h / rowNums;
        } else {
            mRowHeight = h;
        }
    }

//    @Override
//    protected void onVisibilityChanged(@NonNull View changedView, @IntDef(value = {View.VISIBLE, View.INVISIBLE, View.GONE}) int visibility) {
//        super.onVisibilityChanged(changedView, visibility);
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    @CalendarType
    public int getType() {
        return curCalendarType;
    }

    private float sp2px(int spValue) {
        float scale = getResources().getDisplayMetrics().scaledDensity;
        return spValue * scale + 0.5f;
    }

    private float dp2px(int dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }

    private void updateWaveViewData(List<BaseCalendarData> daySumValueList, Calendar calendar) {
        entryArrayList.clear();

        if (daySumValueList == null || daySumValueList.size() == 0) {
            return;
        }

        maxValue = DataController.getMaxValue(calendar);

        if (maxValue == 0) {
            return;
        }

        String curUserType = DataCenter.getCurUserType();
        if (daySumValueList.size() == 7) {
            for (int j = 0; j < 7; j++) {
                BaseCalendarData daySumValue = daySumValueList.get(j);
                if (setValues(entryArrayList, j, daySumValue, curUserType)) return;
            }
        } else if (daySumValueList.size() / 7 == 6) {
            Calendar[] checkingDate = DCCommonMethod.getCheckinDate(calendar);

            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {
                    int key = i * 7 + j;
                    BaseCalendarData daySumValue = daySumValueList.get(key);
                    if (TimeUtils.isDayBetweenTwoDate(daySumValue.getDayInfo().getCalendar(), checkingDate)) {
                        if (setValues(entryArrayList, j, daySumValue, curUserType)) return;
                    } else {
                        entryArrayList.add(new Entry(j, 0));
                    }
                }
            }
        }
    }

    /**
     * @param values
     * @param j
     * @param daySumHourValue
     * @param userTypeUuid
     * @return 是否有非法值
     */
    private boolean setValues(ArrayList<Entry> values, int j, BaseCalendarData daySumHourValue, String userTypeUuid) {
        if (daySumHourValue != null) {

            double yValue = 0;
            switch (userTypeUuid) {
                case ConstGlobal.USER_TYPE_JIABAN:
                case ConstGlobal.USER_TYPE_JIJIAN:
                case ConstGlobal.USER_TYPE_JIXIANGMU:
                case ConstGlobal.USER_TYPE_XIAOSHIGONG:
                    yValue = daySumHourValue.getMoney();
                    break;
                case ConstGlobal.USER_TYPE_JIGONG:
                    yValue = daySumHourValue.getHour();
                    break;
                default:
                    yValue = daySumHourValue.getMoney();
                    break;
            }

            if (Double.isNaN(yValue)) {
                return true;
            }

            yValue = Math.max(yValue, 0);

            values.add(new Entry(j, (float) yValue));
        } else {
            values.add(new Entry(j, 0));
        }
        return false;
    }

    private void drawWeekData(Canvas canvas, List<Entry> entryList, int row) {
        entryList.add(new Entry(entryList.size(), 0));
        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(entryList, "DataSet 1");
        // set the line to be drawn like this "- - - - - -"
        set1.enableDashedLine(10f, 5f, 0f);
        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);
        set1.setFillColor(0xffdff5ad);

        if (set1.getXMax() != 0 && !Float.isNaN(set1.getYMax()) && set1.getYMax() > 0) {
            drawCubicBezier(canvas, set1, row);
        }
        entryList.clear();
    }

    private void drawCubicBezier(Canvas canvas, LineDataSet dataSet, int row) {
        if (Float.isNaN(dataSet.getEntryForIndex(0).getY())) {
            return;
        }
        mXBounds.set(this, dataSet);

        float intensity = dataSet.getCubicIntensity();

        cubicPath.reset();

        if (mXBounds.range >= 1) {

            float scale = DataUtility.castToFloat(2.5f * mRowHeight / 5 / maxValue);
            List<Entry> values = new ArrayList<>();
            for (Entry entry : dataSet.getValues()) {
//                Log.d(TAG, "mGridHeight1:" + mGridHeight + ",entry.getY:" + entry.getY() + ",row:" + row);
                values.add(new Entry(entry.getX(), row * mRowHeight - scale * entry.getY()));
//                Log.d(TAG, "mGridHeight2:" + mGridHeight + ",entry.getY:" + entry.getY());
            }
            dataSet.setValues(values);

            float prevDx = 0f;
            float prevDy = 0f;
            float curDx = 0f;
            float curDy = 0f;

            // Take an extra point from the left, and an extra from the right.
            // That's because we need 4 points for a cubic bezier (cubic=4), otherwise we get lines moving and doing weird stuff on the edges of the chart.
            // So in the starting `prev` and `cur`, go -2, -1
            // And in the `lastIndex`, add +1

            final int firstIndex = mXBounds.min + 1;
            final int lastIndex = mXBounds.min + mXBounds.range;

            Entry prevPrev;
            Entry prev = dataSet.getEntryForIndex(Math.max(firstIndex - 2, 0));
            Entry cur = dataSet.getEntryForIndex(Math.max(firstIndex - 1, 0));
            Entry next = cur;
            int nextIndex = -1;

            if (cur == null || Float.isNaN(cur.getY())) return;

            // let the spline start
            cubicPath.moveTo(getLeft(), row * mRowHeight);
//            Log.d("cubicPath", "startPointX:" + cur.getX() +
//                    "startPointY:" + cur.getY() +
//                    "rectBottom:" + getBottom());

            for (int j = mXBounds.min + 1; j <= mXBounds.range + mXBounds.min; j++) {

                prevPrev = prev;
                prev = cur;
                cur = nextIndex == j ? next : dataSet.getEntryForIndex(j);

                nextIndex = j + 1 < dataSet.getEntryCount() ? j + 1 : j;
                next = dataSet.getEntryForIndex(nextIndex);

                prevDx = (cur.getX() - prevPrev.getX()) * intensity;
                prevDy = (prevPrev.getY() - cur.getY()) * intensity;
                curDx = (next.getX() - prev.getX()) * intensity;
                curDy = (prev.getY() - next.getY()) * intensity;

                float controlOneX = (prev.getX() + prevDx + 0.8f) * mColumnWidth;
                float controlOneY = Math.abs(prev.getY() + prevDy);
                float controlTwoX = (cur.getX() - curDx + 0.8f) * mColumnWidth;
                float controlTwoY = Math.abs(cur.getY() - curDy);
                float curX = (cur.getX() + 0.8f) * mColumnWidth;
                float curY = cur.getY();

                cubicPath.cubicTo(
                        controlOneX,
                        controlOneY,
                        controlTwoX,
                        controlTwoY,
                        curX,
                        curY);

                if (Math.max(prev.getY() + prevDy, 0) < 0 || Math.max(cur.getY() - curDy, 0) < 0) {
//                    Log.d("MonthView", "control point isInterceptEvent lowwer to zero");
                }

//                Log.d("cubicPath",
//                        "controlOneX:" + (prev.getX() + prevDx) +
//                                "controlOneY:" + Math.max(prev.getY() + prevDy, 0) +
//                                "controlTwoX:" + (cur.getX() - curDx) +
//                                "controlTwoY:" + Math.max(cur.getY() - curDy, 0) +
//                                "endPointX:" + cur.getX() +
//                                "endPointY:" + cur.getY());
            }
        }

        // if filled isInterceptEvent enabled, close the path
        if (dataSet.isDrawFilledEnabled()) {

            cubicFillPath.reset();
            cubicFillPath.addPath(cubicPath);

            drawCubicFill(canvas, dataSet, cubicFillPath, mXBounds);
        }

        mWaveViewPaint.setColor(Color.TRANSPARENT);

        mWaveViewPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mWaveViewPaint.setTextSize(1);
        mWaveViewPaint.setStrokeWidth(1);

//        pathValueToPixel(rect1, cubicPath, dataSet);

        canvas.drawPath(cubicPath, mWaveViewPaint);

        mWaveViewPaint.setPathEffect(null);
    }

    protected void drawCubicFill(Canvas c, ILineDataSet dataSet, Path spline, XBounds bounds) {

//        spline.quadTo((dataSet.getEntryForIndex(bounds.max).getX() + 0.8f) * mGridWidth, dataSet.getEntryForIndex(bounds.max).getY(), getWidth(), getBottom());
//        spline.lineTo(getLeft(), getBottom());
//        spline.quadTo(getLeft(), getBottom(), (dataSet.getEntryForIndex(bounds.min).getX() + 0.8f) * mGridWidth, dataSet.getEntryForIndex(bounds.min).getY());
        spline.close();

//        pathValueToPixel(rect1, spline, dataSet);

        final Drawable drawable = dataSet.getFillDrawable();
        if (drawable != null) {
            drawFilledPath(c, spline, drawable);
        } else {
            drawFilledPath(c, spline, dataSet.getFillColor(), dataSet.getFillAlpha());
        }
    }

    /**
     * Draws the provided path in filled mode with the provided drawable.
     *
     * @param c
     * @param filledPath
     * @param drawable
     */
    protected void drawFilledPath(Canvas c, Path filledPath, Drawable drawable) {

        if (clipPathSupported()) {

            int save = c.save();
            c.clipPath(filledPath);

            drawable.setBounds(getLeft(),
                    getTop(),
                    getRight(),
                    getBottom());
            drawable.draw(c);

            c.restoreToCount(save);
        } else {
            throw new RuntimeException("Fill-drawables not (yet) supported below API level 18, " +
                    "this code was run on API level " + Utils.getSDKInt() + ".");
        }
    }

    /**
     * Draws the provided path in filled mode with the provided color and alpha.
     * Special thanks to Angelo Suzuki (https://github.com/tinsukE) for this.
     *
     * @param c
     * @param filledPath
     * @param fillColor
     * @param fillAlpha
     */
    protected void drawFilledPath(Canvas c, Path filledPath, int fillColor, int fillAlpha) {

        int color = (fillAlpha << 24) | (fillColor & 0xffffff);

        if (clipPathSupported()) {

            int save = c.save();

            c.clipPath(filledPath);

            c.drawColor(color);
            c.restoreToCount(save);
        } else {

            // save
            Paint.Style previous = mWaveViewPaint.getStyle();
            int previousColor = mWaveViewPaint.getColor();

            // set
            mWaveViewPaint.setStyle(Paint.Style.FILL);
            mWaveViewPaint.setColor(color);

            c.drawPath(filledPath, mWaveViewPaint);

            // restore
            mWaveViewPaint.setColor(previousColor);
            mWaveViewPaint.setStyle(previous);
        }
    }

    /**
     * Clip path with hardware acceleration only working properly on API level 18 and above.
     *
     * @return
     */
    protected boolean clipPathSupported() {
        return Utils.getSDKInt() >= 18;
    }

}
