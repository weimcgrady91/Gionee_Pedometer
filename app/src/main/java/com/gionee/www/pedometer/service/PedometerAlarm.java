package com.gionee.www.pedometer.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liyy on 16-3-31.
 */
public class PedometerAlarm {
    public static String mAction = "com.gionee.www.pedometer.service.ClearDataReceiver";
    public static long mHour_MilliSeconds_24 = 24 * 60 * 60 * 1000;

    public static  void startAlarm(Context context, Long leftTime){
        Intent intent = new Intent(mAction);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
        alarmManager.set(android.app.AlarmManager.RTC, leftTime, pi);
    }

    public static long getLeftTime(){
        Date current_Date = new Date(System.currentTimeMillis());
        SimpleDateFormat current_DateFormatter = new SimpleDateFormat("HH:mm:ss");
        String formatData = current_DateFormatter.format(current_Date);

        String splitTime[] = formatData.split(":");
        long currentTime_millisecond = ((Integer.parseInt(splitTime[0]) * 60 + Integer.parseInt(splitTime[1])) * 60  + Integer.parseInt(splitTime[2])) * 1000;
        long leftTime_millisecond  = mHour_MilliSeconds_24 - currentTime_millisecond;

        return leftTime_millisecond;
    }
}
