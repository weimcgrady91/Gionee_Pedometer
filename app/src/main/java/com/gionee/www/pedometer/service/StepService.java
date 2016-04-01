package com.gionee.www.pedometer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.gionee.www.pedometer.R;
import com.gionee.www.pedometer.bean.StepInfo;
import com.gionee.www.pedometer.constants.Constants;
import com.gionee.www.pedometer.dao.SaveTimeDao;
import com.gionee.www.pedometer.util.LogUtil;
import com.gionee.www.pedometer.view.StepActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class StepService extends Service implements StepDetector.StepDetectorListener {
    private StepDetector mStepDetector;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private StepBinder mStepBinder;
    private List<StepListener> mStepListeners = new ArrayList<>();
    private StepNotifier mStepNotifier;
    private StepManager mStepManager;
    private DistanceNotifier mDistanceNotifier;
    private BroadcastReceiver mReceiver;
    private Intent mGuardStepService;
    private Notification mNotification;
    private NotificationManager mNotificationManager;
    private CaloriesNotifier mCaloriesNotifier;
    private Intent notificationIntent;
    private PendingIntent pendingIntent;

    public interface StepListener {
        void onStep();
        void onForecastStep(int forecastStepCount);
        void resetData();
    }

    public class StepBinder extends Binder {

        public StepService getService() {
            return StepService.this;
        }

    }



    @Override
    public IBinder onBind(Intent intent) {
        return mStepBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.e("StepService onCreate");
        mStepManager = StepManager.getInstance(this);
        mStepBinder = new StepBinder();

        registerSensor();
        setForeground();
        registerGuardReceiver();

        clearDatas();
        saveNextClearDataTime();
        startAlarm();
    }

    private void saveNextClearDataTime() {
        long nextTime = System.currentTimeMillis() + PedometerAlarm.getLeftTime();
        SaveTimeDao.saveNextClearDataTime(this,nextTime);
    }

    private void clearDatas() {
        if(mStepManager != null){
            mStepManager.clearDatas();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    /**
     * 开启数据清理 计时器
     */
    private void startAlarm() {
        PedometerAlarm.startAlarm(this, PedometerAlarm.getLeftTime());
    }


    /**
     * 注册守护进程
     */
    private void registerGuardReceiver() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mGuardStepService = new Intent(getApplicationContext(), GuardStepService.class);
                startService(mGuardStepService);
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.START_GUARD_STEP_ACTION);
        registerReceiver(mReceiver, intentFilter);
    }

    /**
     * 注销守护进程
     */
    private void unregisterGuardReceiver() {
        unregisterReceiver(mReceiver);
    }

    /**
     * 注册notification 提高优先级
     */
    private void setForeground() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationIntent = new Intent(this, StepActivity.class);
        pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        mNotification = new Notification.Builder(this)
                .setContentTitle("GioneeSupport")
                .setContentText("正在记步")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(R.mipmap.ic_launcher, mNotification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e("StepService onDestroy");
        unregisterSensor();
        callGuardStepService();
        unregisterGuardReceiver();
    }

    /**
     * 呼叫守护进程
     */
    private void callGuardStepService() {
        Intent intent = new Intent();
        intent.setAction(Constants.START_STEP_ACTION);
        sendBroadcast(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.e("StepService onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        LogUtil.e("StepService onRebind");
        super.onRebind(intent);
    }

    /**
     * 注册传感器
     */
    private void registerSensor() {
        addStepListener();
        if (mStepDetector == null) {
            mStepDetector = new StepDetector(this);
        }
        if (mSensorManager == null) {
            mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        }
        if (mSensor == null) {
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//获得传感器的类型，这里获得的类型是加速度传感器
        }
        //此方法用来注册，只有注册过才会生效，参数：SensorEventListener的实例，Sensor的实例，更新速率
        mSensorManager.registerListener(mStepDetector, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    /**
     * 添加数据采集器
     */
    private void addStepListener() {
        mStepNotifier = new StepNotifier(getApplicationContext());
        mStepListeners.add(mStepNotifier);

        mDistanceNotifier = new DistanceNotifier(getApplicationContext());
        mStepListeners.add(mDistanceNotifier);

        mCaloriesNotifier = new CaloriesNotifier(getApplicationContext());
        mStepListeners.add(mCaloriesNotifier);
    }

    /**
     * 注销传感器
     */
    private void unregisterSensor() {
        mSensorManager.unregisterListener(mStepDetector, mSensor);
    }


    @Override
    public void onOneStepOver() {
        for (Iterator<StepListener> iterator = mStepListeners.iterator(); iterator.hasNext(); ) {
            StepListener stepListener = iterator.next();
            stepListener.onStep();
        }
        StepInfo stepInfo = new StepInfo();
        stepInfo.setCount(mStepNotifier.getStepCount());
        stepInfo.setDistance(mDistanceNotifier.getDistance());
        stepInfo.setCalories(mCaloriesNotifier.getCalories());
        mStepManager.onStep(stepInfo);
        updateNotification(mStepNotifier.getStepCount());
    }

    @Override
    public void onStartStep(int forecastStepCount) {
        for (Iterator<StepListener> iterator = mStepListeners.iterator(); iterator.hasNext(); ) {
            StepListener stepListener = iterator.next();
            stepListener.onForecastStep(forecastStepCount);
        }
    }

    /**
     * 更新通知
     */
    private void updateNotification(int stepCount) {
        mNotification = new Notification.Builder(this)
                .setContentTitle("GioneeSupport")
                .setContentText("今日步数" + stepCount+"步")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        mNotificationManager.notify(R.mipmap.ic_launcher,mNotification);
    }

    /**
     * 重置数据
     */
    public void resetData() {
        for (Iterator<StepListener> iterator = mStepListeners.iterator(); iterator.hasNext(); ) {
            StepListener stepListener = iterator.next();
            stepListener.resetData();
        }
    }
}
