package com.gionee.www.pedometer.view;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gionee.www.pedometer.R;
import com.gionee.www.pedometer.bean.StepInfo;
import com.gionee.www.pedometer.constants.Constants;
import com.gionee.www.pedometer.service.StepManager;
import com.gionee.www.pedometer.util.StepUtil;

/**
 * Created by weiqun on 2016/3/27 0027.
 */
public class StepActivity extends AppCompatActivity {

    private StepManager mStepManager;
    private TextView mStepCount;
    private MainHandler mMainHandler;
    private TextView mStepDistance;
    private TextView mStepCal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initViews();
    }

    private void init() {
        mMainHandler = new MainHandler(StepActivity.this);
        mStepManager = StepManager.getInstance(getApplicationContext());
        mStepManager.init(mMainHandler);
    }

    private void initViews() {
        FrameLayout mainContainer = (FrameLayout)findViewById(R.id.frame_content);
        View contentView = LayoutInflater.from(this).inflate(R.layout.main_content,mainContainer);
        mStepCount = (TextView) contentView.findViewById(R.id.tv_stepCount);
        mStepDistance = (TextView) contentView.findViewById(R.id.tv_stepDistance);
        mStepCal = (TextView) contentView.findViewById(R.id.tv_stepCal);
    }

    /**
     * 更新当前信息
     */
    private void updateInfo(StepInfo stepInfo) {
        mStepCount.setText(stepInfo.getCount() + "");
        mStepDistance.setText(StepUtil.formatFloat(stepInfo.getDistance()) + " 米");
        mStepCal.setText(StepUtil.formatFloat(stepInfo.getCalories()) + "卡路里");

    }



    @Override
    protected void onStart() {
        super.onStart();
        mStepManager.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mStepManager.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mStepManager.onDestroy();
    }

    public class MainHandler extends Handler {
        private StepActivity mActivity;

        public MainHandler(StepActivity activity) {
            super();
            mActivity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.STEP_INFO:
                    StepInfo stepInfo = (StepInfo) msg.obj;
                    updateInfo(stepInfo);
                    break;
            }
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.step_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                mStepManager.launchSetting(this);
                break;
            case R.id.menu_clear:
                mStepManager.clearData(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
