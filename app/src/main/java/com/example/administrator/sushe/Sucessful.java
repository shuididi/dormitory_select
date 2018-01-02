package com.example.administrator.sushe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.test.R;

/**
 * Created by Administrator on 2017/12/17.
 */

public class Sucessful extends Activity implements View.OnClickListener{
    private Button mBtn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.successful);

        mBtn1 = (Button) findViewById(R.id.en_button1);
        mBtn1.setOnClickListener(this);


    }
    @Override
    public void onClick(View view) {

        Intent intent = new Intent(this, Result.class);
        startActivity(intent);
        this.finish();
    }

}
