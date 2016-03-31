package com.gionee.www.pedometer.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.gionee.www.pedometer.constants.Constants;

public class GuardStepService extends Service {

    private BroadcastReceiver mReceiver;
    private Intent mStepServiceIntent;

    public GuardStepService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerGuardReceiver();
    }

    /**
     * 注册守护进程
     */
    private void registerGuardReceiver() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mStepServiceIntent = new Intent(getApplicationContext(), StepService.class);
                startService(mStepServiceIntent);
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.START_STEP_ACTION);
        registerReceiver(mReceiver, intentFilter);
    }

    /**
     * 注销守护进程
     */
    private void unregisterGuardReceiver() {
        unregisterReceiver(mReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callStepService();
        unregisterGuardReceiver();
    }
    /**
     * 呼叫守护进程
     */
    private void callStepService() {
        Intent intent = new Intent();
        intent.setAction(Constants.START_GUARD_STEP_ACTION);
        sendBroadcast(intent);
    }
}
