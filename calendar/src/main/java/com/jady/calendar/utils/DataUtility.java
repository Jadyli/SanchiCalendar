package com.jady.calendar.utils;

import android.text.TextUtils;
import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by jady on 2016/10/19.
 */
public class DataUtility {

    private static Scale curScale = Scale.two;

    /**
     * 精度
     */
    public enum Scale {
        zero, one, two, three, four, five, six, seven
    }

    public static void setCurScale(int defaultScale) {
        switch (defaultScale) {
            case 1:
                curScale = Scale.one;
                break;
            case 2:
                curScale = Scale.two;
                break;
            case 3:
                curScale = Scale.three;
                break;
            case 4:
                curScale = Scale.four;
                break;
            default:
                curScale = Scale.two;
                break;
        }
    }

    public static Scale getNumberScaleLen() {
        return curScale;
    }

    /**
     * 一维数组转换为二维数组
     *
     * @param src    ...
     * @param row    ...
     * @param column ...
     * @return ...
     */
    public static String[][] arraysConvert(String[] src, int row, int column) {
        String[][] tmp = new String[row][column];
        for (int i = 0; i < row; i++) {
            tmp[i] = new String[column];
            System.arraycopy(src, i * column, tmp[i], 0, column);
        }
        return tmp;
    }

    /**
     * 加法,默认精确到两位小数点
     *
     * @param numbers
     * @return
     */
    public static long add(long... numbers) {
        return add(Scale.four, numbers);
    }

    /**
     * 加法，可指定精确几位
     *
     * @param scale   精确到几位小数点
     * @param numbers
     * @return
     */
    public static long add(Scale scale, long... numbers) {
        BigDecimal sum = new BigDecimal(Double.toString(numbers[0]));
        for (int i = 1; i < numbers.length; i++) {
            sum = sum.add(new BigDecimal(Double.toString(numbers[i])));
        }
        return sum.setScale(scale.ordinal(), RoundingMode.HALF_EVEN).longValue();
    }

    /**
     * 加法,默认精确到两位小数点
     *
     * @param numbers
     * @return
     */
    public static float add(float... numbers) {
        return add(Scale.four, numbers);
    }

    /**
     * 加法，可指定精确几位
     *
     * @param scale   精确到几位小数点
     * @param numbers
     * @return
     */
    public static float add(Scale scale, float... numbers) {
        BigDecimal sum = new BigDecimal(Float.toString(numbers[0]));
        for (int i = 1; i < numbers.length; i++) {
            sum = sum.add(new BigDecimal(Float.toString(numbers[i])));
        }
        return sum.setScale(scale.ordinal(), RoundingMode.HALF_EVEN).floatValue();
    }

    /**
     * 加法,默认精确到两位小数点
     *
     * @param numbers
     * @return
     */
    public static double add(double... numbers) {
        return add(Scale.four, numbers);
    }

    /**
     * 加法，可指定精确几位
     *
     * @param scale   精确到几位小数点
     * @param numbers
     * @return
     */
    public static double add(Scale scale, double... numbers) {
        BigDecimal sum = new BigDecimal(Double.toString(numbers[0]));
        for (int i = 1; i < numbers.length; i++) {
            sum = sum.add(new BigDecimal(Double.toString(numbers[i])));
        }
        return sum.setScale(scale.ordinal(), RoundingMode.HALF_EVEN).doubleValue();
    }

    /**
     * 减法,默认精确到两位小数点
     *
     * @param numbers
     * @return
     */
    public static long subtract(long... numbers) {
        return subtract(Scale.four, numbers);
    }

    /**
     * 减法，可指定精确几位
     *
     * @param scale   精确到几位小数点
     * @param numbers
     * @return
     */
    public static long subtract(Scale scale, long... numbers) {
        BigDecimal sum = new BigDecimal(Double.toString(numbers[0]));
        for (int i = 1; i < numbers.length; i++) {
            sum = sum.subtract(new BigDecimal(Double.toString(numbers[i])));
        }
        return sum.setScale(scale.ordinal(), RoundingMode.HALF_EVEN).longValue();
    }

    /**
     * 减法,默认精确到两位小数点
     *
     * @param numbers
     * @return
     */
    public static float subtract(float... numbers) {
        return subtract(Scale.four, numbers);
    }

    /**
     * 减法，可指定精确几位
     *
     * @param scale   精确到几位小数点
     * @param numbers
     * @return
     */
    public static float subtract(Scale scale, float... numbers) {
        BigDecimal sum = new BigDecimal(Float.toString(numbers[0]));
        for (int i = 1; i < numbers.length; i++) {
            sum = sum.subtract(new BigDecimal(Float.toString(numbers[i])));
        }
        return sum.setScale(scale.ordinal(), RoundingMode.HALF_EVEN).floatValue();
    }

    /**
     * 减法,默认精确到两位小数点
     *
     * @param numbers
     * @return
     */
    public static double subtract(double... numbers) {
        return subtract(Scale.four, numbers);
    }

    /**
     * 减法，可指定精确几位
     *
     * @param scale   精确到几位小数点
     * @param numbers
     * @return
     */
    public static double subtract(Scale scale, double... numbers) {
        BigDecimal sum = new BigDecimal(Double.toString(numbers[0]));
        for (int i = 1; i < numbers.length; i++) {
            sum = sum.subtract(new BigDecimal(Double.toString(numbers[i])));
        }
        return sum.setScale(scale.ordinal(), RoundingMode.HALF_EVEN).doubleValue();
    }


    /**
     * 乘法,默认精确到两位小数点
     *
     * @param numbers
     * @return
     */
    public static long multiply(long... numbers) {
        return multiply(Scale.four, numbers);
    }

    /**
     * 乘法，可指定精确几位
     *
     * @param scale   精确到几位小数点
     * @param numbers
     * @return
     */
    public static long multiply(Scale scale, long... numbers) {
        BigDecimal sum = new BigDecimal(Double.toString(numbers[0]));
        for (int i = 1; i < numbers.length; i++) {
            sum = sum.multiply(new BigDecimal(Double.toString(numbers[i])));
        }
        return sum.setScale(scale.ordinal(), RoundingMode.HALF_EVEN).longValue();
    }

    /**
     * 乘法,默认精确到两位小数点
     *
     * @param numbers
     * @return
     */
    public static float multiply(float... numbers) {
        return multiply(Scale.four, numbers);
    }

    /**
     * 乘法，可指定精确几位
     *
     * @param scale   精确到几位小数点
     * @param numbers
     * @return
     */
    public static float multiply(Scale scale, float... numbers) {
        BigDecimal sum = new BigDecimal(Float.toString(numbers[0]));
        for (int i = 1; i < numbers.length; i++) {
            sum = sum.multiply(new BigDecimal(Float.toString(numbers[i])));
        }
        return sum.setScale(scale.ordinal(), RoundingMode.HALF_EVEN).floatValue();
    }

    /**
     * 乘法,默认精确到两位小数点
     *
     * @param numbers
     * @return
     */
    public static double multiply(double... numbers) {
        return multiply(Scale.four, numbers);
    }

    /**
     * 乘法，可指定精确几位
     *
     * @param scale   精确到几位小数点
     * @param numbers
     * @return
     */
    public static double multiply(Scale scale, double... numbers) {
        BigDecimal sum = new BigDecimal(Double.toString(numbers[0]));
        for (int i = 1; i < numbers.length; i++) {
            sum = sum.multiply(new BigDecimal(Double.toString(numbers[i])));
        }
        return sum.setScale(scale.ordinal(), RoundingMode.HALF_EVEN).doubleValue();
    }

    /**
     * 乘法,默认精确到两位小数点
     *
     * @param numbers
     * @return
     */
    public static double multiply(String... numbers) {
        return multiply(Scale.four, numbers);
    }

    /**
     * 乘法，可指定精确几位
     *
     * @param scale   精确到几位小数点
     * @param numbers
     * @return
     */
    public static double multiply(Scale scale, String... numbers) {
        BigDecimal sum = new BigDecimal(numbers[0]);
        for (int i = 1; i < numbers.length; i++) {
            sum = sum.multiply(new BigDecimal(numbers[i]));
        }
        return sum.setScale(scale.ordinal(), RoundingMode.HALF_EVEN).doubleValue();
    }

    /**
     * 除法,默认精确到两位小数点
     *
     * @param numbers
     * @return
     */
    public static double divide(long... numbers) {
        return divide(Scale.four, numbers);
    }

    /**
     * 除法，可指定精确几位
     *
     * @param scale   精确到几位小数点
     * @param numbers
     * @return
     */
    public static double divide(Scale scale, long... numbers) {
        BigDecimal sum = new BigDecimal(Double.toString(numbers[0]));
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] == 0) {
                return 1;
            }
            sum = sum.divide(new BigDecimal(Double.toString(numbers[i])), 4, RoundingMode.HALF_EVEN);
        }
        return sum.setScale(scale.ordinal(), RoundingMode.HALF_EVEN).doubleValue();
    }

    /**
     * 除法,默认精确到两位小数点
     *
     * @param numbers
     * @return
     */
    public static float divide(float... numbers) {
        return divide(Scale.four, numbers);
    }

    /**
     * 除法，可指定精确几位
     *
     * @param scale   精确到几位小数点
     * @param numbers
     * @return
     */
    public static float divide(Scale scale, float... numbers) {
        BigDecimal sum = new BigDecimal(Float.toString(numbers[0]));
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] == 0) {
                return 1;
            }
            sum = sum.divide(new BigDecimal(Float.toString(numbers[i])), 4, RoundingMode.HALF_EVEN);
        }
        return sum.setScale(scale.ordinal(), RoundingMode.HALF_EVEN).floatValue();
    }

    /**
     * 除法,默认精确到两位小数点
     *
     * @param numbers
     * @return
     */
    public static double divide(double... numbers) {
        return divide(Scale.four, numbers);
    }

    /**
     * 除法，可指定精确几位
     *
     * @param scale   精确到几位小数点
     * @param numbers
     * @return
     */
    public static double divide(Scale scale, double... numbers) {
        BigDecimal sum = new BigDecimal(Double.toString(numbers[0]));
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] == 0) {
                return 1;
            }
            sum = sum.divide(new BigDecimal(Double.toString(numbers[i])), scale.ordinal(), RoundingMode.HALF_EVEN);
        }
        return sum.setScale(scale.ordinal(), RoundingMode.HALF_EVEN).doubleValue();
    }

    /**
     * 除法,默认精确到两位小数点
     *
     * @param numbers
     * @return
     */
    public static double divide(String... numbers) {
        return divide(Scale.four, numbers);
    }

    /**
     * 除法，可指定精确几位
     *
     * @param scale   精确到几位小数点
     * @param numbers
     * @return
     */
    public static double divide(Scale scale, String... numbers) {
        BigDecimal sum = new BigDecimal(numbers[0]);
        for (int i = 1; i < numbers.length; i++) {
            BigDecimal divisor = new BigDecimal(numbers[i]);
            if (divisor.doubleValue() == 0) {
                return 1;
            }
            sum = sum.divide(divisor, 4, RoundingMode.HALF_EVEN);
        }
        return sum.setScale(scale.ordinal(), RoundingMode.HALF_EVEN).doubleValue();
    }


    /**
     * 取余
     *
     * @param numbers
     * @return
     */
    public static int remainder(long... numbers) {
        BigDecimal sum = new BigDecimal(Double.toString(numbers[0]));
        for (int i = 1; i < numbers.length; i++) {
            sum = sum.remainder(new BigDecimal(Double.toString(numbers[i])));
        }
        return sum.intValue();
    }

    /**
     * 取余
     *
     * @param numbers
     * @return
     */
    public static float remainder(float... numbers) {
        BigDecimal sum = new BigDecimal(Float.toString(numbers[0]));
        for (int i = 1; i < numbers.length; i++) {
            sum = sum.remainder(new BigDecimal(Float.toString(numbers[i])));
        }
        return sum.floatValue();
    }

    /**
     * 取余
     *
     * @param numbers
     * @return
     */
    public static double remainder(double... numbers) {
        BigDecimal sum = new BigDecimal(Double.toString(numbers[0]));
        for (int i = 1; i < numbers.length; i++) {
            sum = sum.remainder(new BigDecimal(Double.toString(numbers[i])));
        }
        return sum.doubleValue();
    }

    /**
     * 强转为int,四舍五入
     *
     * @param number
     * @return
     */
    public static int castToInt(double number) {
        return new BigDecimal(Double.toString(number)).setScale(0, RoundingMode.HALF_EVEN).intValue();
    }

    /**
     * 强转为long,四舍五入
     *
     * @param number
     * @return
     */
    public static long castToLong(double number) {
        return new BigDecimal(Double.toString(number)).setScale(0, RoundingMode.HALF_EVEN).longValue();
    }

    /**
     * 强转为float
     *
     * @param number
     * @return
     */
    public static float castToFloat(double number) {
        return new BigDecimal(Double.toString(number)).setScale(Scale.four.ordinal(), RoundingMode.HALF_EVEN).floatValue();
    }

    /**
     * 强转为float
     *
     * @param number
     * @return
     */
    public static float castToFloat(long number) {
        return new BigDecimal(Float.toString(number)).setScale(Scale.four.ordinal(), RoundingMode.HALF_EVEN).floatValue();
    }

    /**
     * 强转为int,四舍五入
     *
     * @param number
     * @return
     */
    public static int castToInt(float number) {
        return new BigDecimal(Float.toString(number)).setScale(0, RoundingMode.HALF_EVEN).intValue();
    }

    /**
     * 强转为long,四舍五入
     *
     * @param number
     * @return
     */
    public static long castToLong(float number) {
        return new BigDecimal(Float.toString(number)).setScale(0, RoundingMode.HALF_EVEN).longValue();
    }

    /**
     * 强转为float
     *
     * @param number
     * @return
     */
    public static float castToFloat(String number) {
        if (TextUtils.isEmpty(number)) {
            return 0;
        }
        try {
            return new BigDecimal(number).setScale(Scale.four.ordinal(), RoundingMode.HALF_EVEN).floatValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 强转为long,四舍五入
     *
     * @param number
     * @return
     */
    public static long castToLong(String number) {
        if (TextUtils.isEmpty(number)) {
            return 0;
        }
        try {
            return new BigDecimal(number).setScale(0, RoundingMode.HALF_EVEN).longValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 强转为int,四舍五入
     *
     * @param number
     * @return
     */
    public static int castToInt(String number) {
        if (TextUtils.isEmpty(number)) {
            return 0;
        }
        try {
            return new BigDecimal(number).setScale(0, RoundingMode.HALF_EVEN).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 强转为double,四舍五入
     *
     * @param number
     * @return
     */
    public static double castToDouble(float number) {
        return new BigDecimal(Float.toString(number)).setScale(Scale.four.ordinal(), RoundingMode.HALF_EVEN).doubleValue();
    }

    /**
     * 强转为double,四舍五入
     *
     * @param number
     * @return
     */
    public static double castToDouble(long number) {
        return new BigDecimal(Long.toString(number)).setScale(Scale.four.ordinal(), RoundingMode.HALF_EVEN).doubleValue();
    }

    /**
     * 强转为double,四舍五入
     *
     * @param number
     * @return
     */
    public static double castToDouble(String number) {
        if (TextUtils.isEmpty(number)) {
            return 0;
        }
        try {
            return new BigDecimal(number).setScale(Scale.four.ordinal(), RoundingMode.HALF_EVEN).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取一个数的整数部分,不四舍五入
     *
     * @param number
     * @return
     */
    public static int getInt(double number) {
        return new BigDecimal(number).intValue();
    }

    /**
     * 获取一个数的小数部分
     *
     * @param number
     * @return 例如：0.99
     */
    public static double getDecimal(double number) {
        return new BigDecimal(number).remainder(new BigDecimal(1)).doubleValue();
    }

    /**
     * 返回绝对值
     *
     * @param number
     * @return
     */
    public static int abs(int number) {
        return new BigDecimal(number).abs().intValue();
    }

    /**
     * 返回绝对值
     *
     * @param number
     * @return
     */
    public static long abs(long number) {
        return new BigDecimal(number).abs().longValue();
    }

    /**
     * 返回绝对值
     *
     * @param number
     * @return
     */
    public static float abs(float number) {
        return new BigDecimal(number).abs().floatValue();
    }

    /**
     * 返回绝对值
     *
     * @param number
     * @return
     */
    public static double abs(double number) {
        return new BigDecimal(number).abs().doubleValue();
    }

    /**
     * 得到number的String值,不保留结尾的0,默认四舍五入，
     * 可通过{@link DecimalFormat#setRoundingMode(RoundingMode)}修改截取模式
     *
     * @param number
     * @param scale  精度
     * @return
     */
    public static String formatNumber(double number, int scale) {
        String format = "#";
        if (scale > 0) {
            format += ".";
            for (int i = 0; i < scale; i++) {
                format += "#";
            }
        }
        DecimalFormat df = new DecimalFormat(format);
        return df.format(number);
    }

    /**
     * 得到number的String值,不保留结尾的0
     *
     * @param number
     * @return
     */
    public static String formatNumber(double number) {
        return formatNumber(number, curScale.ordinal());
    }

    /**
     * 得到number的String值,保留结尾的0
     *
     * @param number
     * @param len    小数位长度
     * @return
     */
    public static String formatNumberByLen(double number, int len) {
        String endStr = formatNumber(number, len);
        int indexOfDot = endStr.indexOf(".");
        int decimalLen = 0;
        if (indexOfDot != -1) {
            decimalLen = endStr.substring(indexOfDot + 1).length();
        } else if (len > 0) {
            endStr += ".";
        }
        for (int i = 0; i < len - decimalLen; i++) {
            endStr += "0";
        }
        return endStr;
    }

    /**
     * 得到number的String值,默认精度为4
     *
     * @param number
     * @return
     */
    public static String formatNumber(float number) {
        return formatNumber(castToDouble(number), curScale.ordinal());
    }

    /**
     * 得到number的String值,保留结尾的0
     *
     * @param number
     * @param len    小数位长度
     * @return
     */
    public static String formatNumberByLen(float number, int len) {
        return formatNumberByLen(castToDouble(number), len);
    }

    /**
     * 根据日期得到星座
     */
    public static String getAstro(int month, int day) {
        Log.i("astro", "==>" + month + day);
        String[] astro = new String[]{"摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"};
        int[] arr = new int[]{20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22};// 两个星座分割日
        int index = month;
        // 所查询日期在分割日之前，索引-1，否则不变
        if (day < arr[month - 1]) {
            index = index - 1;
        }
        return astro[index];
    }
}
