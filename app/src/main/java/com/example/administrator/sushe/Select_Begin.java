package com.example.administrator.sushe;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Select_Begin extends AppCompatActivity implements View.OnClickListener{

    private TextView mName, mStuNum, mSex, mCheck;
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.begin_page);

        initView();
    }

    void initView() {
        mName = (TextView) findViewById(R.id.name);
        mStuNum = (TextView) findViewById(R.id.stuNum);
        mSex = (TextView) findViewById(R.id.sex);
        mCheck = (TextView) findViewById(R.id.check_code);
        mBtn=(Button)findViewById(R.id.begin_btn);
        mBtn.setOnClickListener(this);

        //Intent intent =this.getIntent();
        //String username=intent.getStringExtra("username");

        User stuid=(User)getApplication();
        String studentid = stuid.getStudentid();

        loginByGet(studentid);


    }

    public void loginByGet(String student) {

        String data = "stuid=" + student;
        final String adrdess = "https://api.mysspku.com/index.php/V1/MobileCourse/getDetail?" + data;
        Log.d("select_begin", adrdess);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(adrdess);
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
                    Log.d("activity_login", responseStr);
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
     * @param stuid
     */
    private void getJSON(String stuid) {
        try {
            //字符串转换为JSONObject对象
            JSONObject a = new JSONObject(stuid);
            String code = a.getString("errcode");
            if (code == "0") {//请求成功
                //从jsonObject对象中取出来key是data的对象
                JSONObject data = a.getJSONObject("data");
                if (data != null) {
                    //从data对象里取出
                    String stu = data.getString("studentid");
                    String name = data.getString("name");
                    String gender = data.getString("gender");
                    String vcode = data.getString("vcode");
                    String grade = data.getString("grade");
                    mStuNum.setText(stu);
                    mName.setText(name);
                    mSex.setText(gender);
                    mCheck.setText(vcode);

                    ((User)getApplication()).setName(name);
                    ((User)getApplication()).setGender(gender);
                    ((User)getApplication()).setVcode(vcode);
                    ((User)getApplication()).setVcode(grade);

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
        Intent intent = new Intent(this, Select_Dormitory.class);
        startActivity(intent);
        finish();
    }
}