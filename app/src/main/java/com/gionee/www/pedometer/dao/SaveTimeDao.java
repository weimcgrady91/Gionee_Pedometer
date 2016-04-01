package com.gionee.www.pedometer.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.gionee.www.pedometer.constants.Constants;

/**
 * Created by liyy on 16-4-1.
 */
public class SaveTimeDao {
    public static void saveNextClearDataTime(Context context, long nextTime){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(Constants.CLEARDATA_NEXTTIME, nextTime);
        editor.commit();
    }

    public static void updateNextClearTime(Context context, long nextTime){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.CLEARDATA_NEXTTIME);
        editor.putLong(Constants.CLEARDATA_NEXTTIME, nextTime);
        editor.commit();
    }

}
