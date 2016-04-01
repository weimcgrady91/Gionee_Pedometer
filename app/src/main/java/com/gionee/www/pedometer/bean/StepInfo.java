package com.gionee.www.pedometer.bean;

import com.gionee.www.pedometer.util.LogUtil;

/**
 * Created by weiqun on 2016/3/28 0028.
 */
public class StepInfo {
    private int id;
    private int count; //步数
    private float distance; //距离
    private float calories; //卡路里
    private int personId;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }
}
