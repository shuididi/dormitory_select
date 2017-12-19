package com.example.administrator.sushe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.test.R;

/**
 * Created by Administrator on 2017/12/12.
 */

public class Result extends Activity implements View.OnClickListener{

    private TextView mName, mStuNum, mSex,mbuilding;
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.successful);

        mBtn = (Button)findViewById(R.id.en_button1);
        mBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        setContentView(R.layout.result);
        mStuNum = (TextView) findViewById(R.id.stuNum);
        mName = (TextView) findViewById(R.id.name);
        mSex = (TextView) findViewById(R.id.sex);
        mbuilding=(TextView) findViewById(R.id.buildNum);

        String studentid = ((User)getApplication()).getStudentid();
        String name =((User)getApplication()).getName();
        //User sex=(User)getApplicationContext();
        String gender = ((User)getApplication()).getGender();
        String building = ((User)getApplication()).getBuilding();


        mStuNum.setText(studentid);
        mName.setText(name);
        mSex.setText(gender);
        mbuilding.setText(building);

    }
}
