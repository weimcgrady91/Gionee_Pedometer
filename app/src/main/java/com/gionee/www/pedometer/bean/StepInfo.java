package com.gionee.www.pedometer.bean;

import com.gionee.www.pedometer.util.LogUtil;

/**
 * Created by weiqun on 2016/3/28 0028.
 */
public class StepInfo {
    private int count; //步数
    private float distance; //距离
    private float calories; //卡路里

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getDistance() {
        LogUtil.e("stepInfo " + distance);
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }
}
