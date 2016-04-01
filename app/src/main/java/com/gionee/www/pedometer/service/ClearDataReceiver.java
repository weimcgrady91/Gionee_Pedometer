package com.gionee.www.pedometer.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gionee.www.pedometer.dao.SaveTimeDao;

/**
 * Created by liyy on 16-3-31.
 */
public class ClearDataReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        StepManager stepManager = StepManager.getInstance(context);
        stepManager.clearData(context);

        long nexttime = System.currentTimeMillis() + PedometerAlarm.mHour_MilliSeconds_24;
        SaveTimeDao.updateNextClearTime(context,nexttime);

        PedometerAlarm.startAlarm(context, nexttime);
    }

}
