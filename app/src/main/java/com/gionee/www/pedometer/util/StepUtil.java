package com.gionee.www.pedometer.util;


import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by weiqun on 2016/3/29 0029.
 */
public class StepUtil {
    /**
     * 计算并格式化float数值，保留两位有效数字
     *
     * @param values
     * @return 返回当前路程
     */
    public static String formatFloat(float values) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        return decimalFormat.format(values);
    }

    public static Date formatDate(String date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
