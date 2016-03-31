package com.gionee.www.pedometer.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gionee.www.pedometer.R;
import com.gionee.www.pedometer.bean.PersonInfo;
import com.gionee.www.pedometer.service.StepManager;
import com.gionee.www.pedometer.util.StepUtil;

import java.util.Date;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mBirthday;
    private EditText mGender;
    private EditText mBloodType;
    private EditText mHeight;
    private EditText mWeight;
    private EditText mStepLength;
    private EditText mBmi;
    private Button mSave;
    private StepManager mStepManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
        initViews();
    }

    private void init() {
        mStepManager = StepManager.getInstance(getApplicationContext());
    }

    private void initViews() {
        mBirthday = (EditText)findViewById(R.id.et_birthday);
        mGender = (EditText)findViewById(R.id.et_gender);
        mBloodType = (EditText)findViewById(R.id.et_bloodType);
        mHeight = (EditText)findViewById(R.id.et_height);
        mWeight = (EditText)findViewById(R.id.et_weight);
        mStepLength = (EditText)findViewById(R.id.et_stepLength);
        mBmi = (EditText)findViewById(R.id.et_bmi);
        mSave = (Button) findViewById(R.id.btn_save);
        mSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
//                checkData();
                PersonInfo p = preData();
                mStepManager.savePersonInfo(p);
                break;
        }
    }

    private PersonInfo preData() {
        Date date = StepUtil.formatDate(mBirthday.getText().toString());
        float height = Float.parseFloat(mHeight.getText().toString());
        float weight = Float.parseFloat(mWeight.getText().toString());
        float stepLength = Float.parseFloat(mStepLength.getText().toString());
        int gender = mGender.equals("男") ? 0 : 1;
        float bmi = (float)(weight / Math.pow(height,2));
        PersonInfo personInfo = new PersonInfo();
        personInfo.setBirthday(date);
        personInfo.setGender(gender);
        personInfo.setBloodType(1);

        personInfo.setHeight(height);

        personInfo.setWeight(weight);
        personInfo.setStepLength(stepLength);
        personInfo.setBmi(bmi);
        return personInfo;
    }
}
