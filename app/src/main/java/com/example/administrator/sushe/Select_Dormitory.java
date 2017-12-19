package com.example.administrator.sushe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.test.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/12/11.
 */

public class Select_Dormitory extends Activity implements View.OnClickListener {
    private Button mOne, mTwo, mThree, mFour;
    private  String num;
    private TextView mBuilding5,mBuilding8, mBuilding9, mBuilding13, mBuilding14;
    private String building;
    private static int a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_dormitory);
        mBuilding5 = (TextView) findViewById(R.id.building5);
        mBuilding8 = (TextView) findViewById(R.id.building8);
        mBuilding9 = (TextView) findViewById(R.id.building9);
        mBuilding13 = (TextView) findViewById(R.id.building13);
        mBuilding14= (TextView) findViewById(R.id.building14);
        String studentid = ((User)getApplication()).getStudentid();


        String name =((User)getApplication()).getName();

        //User sex=(User)getApplicationContext();
        String gender = ((User)getApplication()).getGender();

        initView();
        buildingGet(gender);
    }
    void initView(){
        mOne = (Button)findViewById(R.id.one1);
        mOne.setOnClickListener(this);
        mTwo = (Button)findViewById(R.id.two2);
        mTwo.setOnClickListener(this);
        mThree = (Button)findViewById(R.id.three3);
        mThree .setOnClickListener(this);
        mFour = (Button)findViewById(R.id.four4);
        mFour.setOnClickListener(this);
    }
    public void buildingGet(String gender) {
        if(gender.equals("女")){
            a = 2;
        }else{
            a = 1;
        }
        String data = "gender=" + a;
        final String ip = "https://api.mysspku.com/index.php/V1/MobileCourse/getRoom?" + data;
        Log.d("select_dormitory", ip);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(ip);
                    //信任所有证书
                    if ("https".equalsIgnoreCase(url.getProtocol())) {
                        HTTPSTrust.ignoreSsl();
                    }
                    //打开链接
                    conn = (HttpURLConnection) url.openConnection();
                    //GET请求
                    conn.setRequestMethod("GET");
                    //设置属性
                    conn.setReadTimeout(8000);//读取数据超时时间
                    conn.setConnectTimeout(8000);//连接的超时时间

                    InputStream is = conn.getInputStream();//字节流转换成字符串
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        response.append(str + "\n");
                        Log.d("activity_login", str);
                    }
                    //获取字符串
                    String responseStr = response.toString();
                    Log.d("select_dormitory", responseStr);
                    //解析JSON格式
                    getJSON(responseStr);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }

                }
            }
        }).start();

    }

    /**
     * JSON解析方法
     *
     * @param build
     */
    private void getJSON(String build) {
        try {
            //字符串转换为JSONObject对象
            JSONObject a = new JSONObject(build);
            String code = a.getString("errcode");
            if (code == "0") {//请求成功
                //从jsonObject对象中取出来key是data的对象
                JSONObject data = a.getJSONObject("data");
                if (data != null) {
                    //从data对象里取出
                    String bui5 = data.getString("5");
                    String bui8 = data.getString("8");
                    String bui9 = data.getString("9");
                    String bui13 = data.getString("13");
                    String buil4 = data.getString("14");
                    mBuilding5.setText(bui5);
                    mBuilding8.setText(bui8);
                    mBuilding9.setText(bui9);
                    mBuilding13.setText(bui13);
                    mBuilding14.setText(buil4);

                } else {
                    Looper.prepare();
                    Toast.makeText(this, "请求失败！", Toast.LENGTH_SHORT).show();
                    Looper.loop();


                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.one1:
                //num=String.valueOf(b);
                //((User)getApplication()).setNum(num);
                Intent intent1 = new Intent(this,One_Person.class);//页面跳转
                startActivity(intent1);//加载页面
                finish(); // 关闭页面
                break;
            case R.id.two2:
                //num=String.valueOf(1);
                //((User)getApplication()).setNum(num);
                Intent intent2 = new Intent(this,Two_Persons.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.three3:
                //num=String.valueOf(2);
                //((User)getApplication()).setNum(num);
                Intent intent3 = new Intent(this,Three_Persons.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.four4:
                //num=String.valueOf(3);
                //((User)getApplication()).setNum(num);
                Intent intent4 = new Intent(this,Four_Persons.class);
                startActivity(intent4);
                finish();
                break;
            default:
                break;
        }
    }
}
