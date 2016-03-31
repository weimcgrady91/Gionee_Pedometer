package com.gionee.www.pedometer.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.gionee.www.pedometer.bean.PersonInfo;
import com.gionee.www.pedometer.bean.StepInfo;
import com.gionee.www.pedometer.constants.Constants;

import java.util.Date;

/**
 * Created by weiqun on 2016/3/29 0029.
 */
public class StepDao {
    private static PersonInfo mPersonInfo;

    public static void saveCurrentStepInfo(Context context, StepInfo stepInfo) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Constants.PROPERTIES_STEP_COUNT, stepInfo.getCount());
        editor.putFloat(Constants.PROPERTIES_STEP_DISTANCE, stepInfo.getDistance());
        editor.putFloat(Constants.PROPERTIES_STEP_CALORIES,stepInfo.getCalories());
        editor.commit();
    }

    public static StepInfo getCurrentStepInfo(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        StepInfo stepInfo = new StepInfo();
        stepInfo.setCount(sharedPreferences.getInt(Constants.PROPERTIES_STEP_COUNT, 0));
        stepInfo.setDistance(sharedPreferences.getFloat(Constants.PROPERTIES_STEP_DISTANCE, 0));
        stepInfo.setCalories(sharedPreferences.getFloat(Constants.PROPERTIES_STEP_CALORIES, 0));
        return stepInfo;
    }

    public static float getStepLength(Context context) {
        if (mPersonInfo != null) {
            return mPersonInfo.getStepLength();
        } else {
            getPersonInfo(context);
            return mPersonInfo.getStepLength();
        }
    }

    public static PersonInfo getPersonInfo(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        PersonInfo personInfo = new PersonInfo();
        personInfo.setHeight(sharedPreferences.getFloat(Constants.PROPERTIES_PERSON_HEIGHT, 170.0f));
        personInfo.setWeight(sharedPreferences.getFloat(Constants.PROPERTIES_PERSON_WIDGET, 50.0f));
        personInfo.setStepLength(sharedPreferences.getFloat(Constants.PROPERTIES_PERSON_STEP_LENGTH, 70.0f));
        mPersonInfo = personInfo;
        return personInfo;
    }

    public static void savePersonInfo(Context context,PersonInfo p) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(Constants.PROPERTIES_PERSON_BIRTHDAY,p.getBirthday().getTime());
        editor.putInt(Constants.PROPERTIES_PERSON_GENDER, p.getGender());
        editor.putInt(Constants.PROPERTIES_PERSON_BLOOD,p.getBloodType());
        editor.putFloat(Constants.PROPERTIES_PERSON_HEIGHT, p.getHeight());
        editor.putFloat(Constants.PROPERTIES_PERSON_WIDGET, p.getWeight());
        editor.putFloat(Constants.PROPERTIES_PERSON_BMI, p.getBmi());
        editor.commit();
    }

    /**
     * 清楚数据
     * @param context
     */
    public static void clearData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
