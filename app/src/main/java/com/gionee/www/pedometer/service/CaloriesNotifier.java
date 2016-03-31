package com.gionee.www.pedometer.service;

import android.content.Context;

import com.gionee.www.pedometer.bean.PersonInfo;
import com.gionee.www.pedometer.bean.StepInfo;
import com.gionee.www.pedometer.dao.StepDao;

/**
 * Created by weiqun on 2016/3/29 0029.
 */
public class CaloriesNotifier implements StepService.StepListener{
    private static double METRIC_WALKING_FACTOR = 0.708;
    private float calories;
    private PersonInfo mPersonInfo;
    private Context mContext;

    public CaloriesNotifier(Context context) {
        mContext = context;
        StepInfo stepInfo = StepDao.getCurrentStepInfo(context);
        calories = stepInfo.getCalories();
        mPersonInfo = StepDao.getPersonInfo(context);

    }

    @Override
    public void onForecastStep(int forecastStepCount) {
        calories += forecastStepCount * (mPersonInfo.getWeight() *  METRIC_WALKING_FACTOR) * mPersonInfo.getStepLength() / 100000.0;
    }

    @Override
    public void onStep() {
        calories += (mPersonInfo.getWeight() *  METRIC_WALKING_FACTOR) * mPersonInfo.getStepLength() / 100000.0;
    }

    @Override
    public void resetData() {
        StepInfo stepInfo = StepDao.getCurrentStepInfo(mContext);
        calories = stepInfo.getCalories();
    }

    public float getCalories() {
        return calories;
    }
}
