package com.gionee.www.pedometer.service;

import android.content.Context;

import com.gionee.www.pedometer.bean.StepInfo;
import com.gionee.www.pedometer.dao.StepDao;

/**
 * Created by weiqun on 2016/3/28 0028.
 */
public class DistanceNotifier implements StepService.StepListener {
    private float mDistance = 0;
    private float mStepLength = 0;
    private Context mContext;

    public DistanceNotifier(Context context) {
        mContext = context;
        StepInfo stepInfo = StepDao.getCurrentStepInfo(context);
        mDistance = stepInfo.getDistance();
        mStepLength = StepDao.getStepLength(context);
    }

    @Override
    public void onForecastStep(int forecastStepCount) {
        int randomLength = (int) (Math.random() * 10 + (mStepLength - 10));
        mDistance += forecastStepCount * randomLength * 0.01f;
    }

    @Override
    public void onStep() {
        int randomLength = (int) (Math.random() * 10 + (mStepLength - 10));
        mDistance += randomLength * 0.01f;
    }

    @Override
    public void resetData() {
        StepInfo stepInfo = StepDao.getCurrentStepInfo(mContext);
        mDistance = stepInfo.getDistance();
    }

    public float getDistance() {
        return mDistance;
    }
}
