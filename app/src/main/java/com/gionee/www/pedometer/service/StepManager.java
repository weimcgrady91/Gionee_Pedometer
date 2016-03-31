package com.gionee.www.pedometer.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;

import com.gionee.www.pedometer.bean.PersonInfo;
import com.gionee.www.pedometer.bean.StepInfo;
import com.gionee.www.pedometer.constants.Constants;
import com.gionee.www.pedometer.dao.StepDao;
import com.gionee.www.pedometer.view.*;

/**
 * Created by weiqun on 2016/3/27 0027.
 */
public class StepManager {
    public static StepManager sStepManager;
    private Context mContext;
    private Intent mStepServiceIntent;
    private boolean isBind = false;
    private boolean forwardExits = false;
    private StepService mStepService;
    private StepActivity.MainHandler mMainHandler;

    private StepManager() {

    }

    private StepManager(Context context) {
        mContext = context;
        mStepServiceIntent = new Intent(context.getApplicationContext(), StepService.class);
        mContext.startService(mStepServiceIntent);
        mContext.startService(new Intent(context.getApplicationContext(), GuardStepService.class));
        mContext.bindService(mStepServiceIntent, connection, Context.BIND_AUTO_CREATE);
    }

    public static StepManager getInstance(Context context) {
        if (sStepManager == null) {
            synchronized (StepManager.class) {
                sStepManager = new StepManager(context);
            }
        }
        return sStepManager;
    }

    public static StepManager getInstance() {
        if (sStepManager == null) {
            synchronized (StepManager.class) {
                sStepManager = new StepManager();
            }
        }
        return sStepManager;
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mStepService = ((StepService.StepBinder) service).getService();
            isBind = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    public void onDestroy() {
        if (isBind) {
            mContext.unbindService(connection);
            isBind = false;
        }
    }

    public void onStep(StepInfo stepInfo) {
        StepDao.saveCurrentStepInfo(mContext, stepInfo);
        if (forwardExits) {
            updateView(stepInfo);
        }
    }

    /**
     * 保存个人信息
     *
     * @param p
     */
    public void savePersonInfo(PersonInfo p) {
        StepDao.savePersonInfo(mContext, p);
    }

    /**
     * 通知刷新UI
     *
     * @param stepInfo
     */
    private void updateView(StepInfo stepInfo) {
        if(mMainHandler != null) {
            Message message = mMainHandler.obtainMessage();
            message.what = Constants.STEP_INFO;
            message.obj = stepInfo;
            mMainHandler.sendMessage(message);
        }
    }

    public void onStart() {
        forwardExits = true;
        StepInfo stepInfo = StepDao.getCurrentStepInfo(mContext);
        updateView(stepInfo);
    }

    public void onStop() {
        forwardExits = false;
    }

    public void init(StepActivity.MainHandler handler) {
        mMainHandler = handler;
    }

    public void launchSetting(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    public void clearData(Context context) {
        StepDao.clearData(context);
        mStepService.resetData();
        StepInfo stepInfo = new StepInfo();
        updateView(stepInfo);
    }
}
