package com.gionee.www.pedometer.service;

import android.content.Context;

import com.gionee.www.pedometer.bean.StepInfo;
import com.gionee.www.pedometer.dao.StepDao;

/**
 * Created by Administrator on 2016/3/28 0028.
 */
public class StepNotifier implements StepService.StepListener {
    private int mStepCount = 0;
    private Context mContext;
    public StepNotifier(Context context) {
        mContext = context;
        StepInfo stepInfo = StepDao.getCurrentStepInfo(context);
        mStepCount = stepInfo.getCount();
    }
    @Override
    public void onStep() {
        mStepCount++;
    }

    @Override
    public void onForecastStep(int forecastStepCount) {
        mStepCount += forecastStepCount;
    }

    @Override
    public void resetData() {
        StepInfo stepInfo = StepDao.getCurrentStepInfo(mContext);
        mStepCount = stepInfo.getCount();
    }

    public int getStepCount() {
        return mStepCount;
    }

}
