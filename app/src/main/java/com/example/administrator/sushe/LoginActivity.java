package com.example.administrator.sushe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText mAccount, mPassword;   //定义控件
    private Button mLogin;

    //String password=mpassword.getText().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        initView();
    }

    void initView() {
        mAccount = (EditText) findViewById(R.id.login_edit_account);//拿到布局的控件，并强制类型转换
        mPassword = (EditText) findViewById(R.id.login_edit_pwd);
        mLogin = (Button) findViewById(R.id.login_btn_login);
        mLogin.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        final String studentid = mAccount.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();

        if (TextUtils.isEmpty(studentid.trim()) || TextUtils.isEmpty(password.trim())) {
            Toast.makeText(LoginActivity.this, "请输入账户或密码", Toast.LENGTH_SHORT).show();
        } else {
            //调用get请求的方法
            loginByGet(mAccount.getText().toString(), mPassword.getText().toString());

        }


    }

    /*
            queryLogin(mAccount.getText().toString(), mPassword.getText().toString());
            if (mAccount.getText().toString().length() == 0 ||
                    mPassword.getText().toString().length() == 0) {
                //ToastUtil.showToast(Login.this,"请输入账户或密码");
                Toast.makeText(LoginActivity.this, "请输入账户或密码", Toast.LENGTH_SHORT).show();
            } else if (mAccount.getText().toString().equals("000000") &&
                    mPassword.getText().toString().equals("000000")) {
                Intent i = new Intent(this,     Select_Begin.class);
                startActivity(i);
            } else {
                //ToastUtil.showToast(Login.this,"账户或密码不正确");
                Toast.makeText(LoginActivity.this, "账户或密码不正确", Toast.LENGTH_SHORT).show();
            }
        }
    */
    public void loginByGet(String username, String password) {


        String data = "username=" + username + "&password=" + password;
        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/Login?" + data;
        Log.d("Login", address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                try {
                    URL url = new URL(address);
                    if ("https".equalsIgnoreCase(url.getProtocol())) {
                        HTTPSTrust.ignoreSsl();
                    }
                    //HTTPSTrust.allowAllSSL();
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);

                    InputStream is = con.getInputStream();//字节流转换成字符串
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder response = new StringBuilder();
                    String str;
                    //String content=JsonTool.readStream(is);//将json数据转化为字符串  
                    // String weather=JsonTool.readJsonString(content,"live","weather");//获得值 
                    while ((str = reader.readLine()) != null) {
                        response.append(str + "\n");
                        Log.d("login_activity", str);
                    }
                    String responseStr = response.toString();
                    Log.d("login_activity", responseStr);
                    getJSON(responseStr);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }

                }
            }
        }).start();

    }

    private void getJSON(String Jsondata) {
        //String username = mAccount.getText().toString();

        try {

            JSONObject a = new JSONObject(Jsondata);
            System.out.println(a.get("errcode"));
            String code = a.getString("errcode");
            if (code.equals("0")) {
                System.out.println("OK");
                Intent intent = new Intent(this, Select_Begin.class);
                //intent.putExtra("username",username);

                startActivity(intent);
                this.finish();

                String studentid=mAccount.getText().toString();
                ((User)getApplication()).setStudentid(studentid);
            } else if (code.equals("40001")) {
                Looper.prepare();
                Toast.makeText(LoginActivity.this, "学号不存在", Toast.LENGTH_SHORT).show();
                Looper.loop();
            } else if (code.equals("40002")) {
                Looper.prepare();
                Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                Looper.loop();
            } else if (code.equals("40009")) {
                Looper.prepare();
                Toast.makeText(LoginActivity.this, "参数错误", Toast.LENGTH_SHORT).show();
                Looper.loop();
            } else {
                Looper.prepare();
                Toast.makeText(LoginActivity.this, "请求失败！", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
/*public class LoginActivity extends  Activity implements View.OnClickListener {
    private EditText mAccount,mPassword;
    private Button mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);//加载布局
        initView();//调用一个方法
    }

    //获取数据的方法
    void initView() {
        mAccount = (EditText) findViewById(R.id.login_edit_account);//拿到布局的控件，并强制类型转换
        mPassword = (EditText) findViewById(R.id.login_edit_pwd);
        mLogin = (Button) findViewById(R.id.login_btn_login);
        mLogin.setOnClickListener(this);

    }

    //添加OnClick事件
    @Override
    public void onClick(View view) {

        //拿到学号,密码的值
        final String username = mAccount.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();
        //进行输入验证,如果没有输入的话进行提示
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getApplicationContext(), "学号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(username.trim()) || TextUtils.isEmpty(password.trim())) {
            Toast.makeText(this, "用户名或者密码不能为空", Toast.LENGTH_LONG).show();
        } else {
            //调用get请求的方法
            loginByGet(mAccount.getText().toString(), mPassword.getText().toString());
        }

    }


    public void loginByGet(String username, String password) {

        //if (username.length() == 0 || password.length() == 0) {
        //  NetUtil.showToast(activity_login.this, "请输入账户或密码");
        //  return;
        // }
        String data = "username=" + username + "&password=" + password;
        final String ip = "https://api.mysspku.com/index.php/V1/MobileCourse/Login?" + data;
        Log.d("activity_login", ip);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(ip);
                    if ("https".equalsIgnoreCase(url.getProtocol())) {
                        HTTPSTrust.ignoreSsl();
                    }
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    //设置属性
                    conn.setReadTimeout(8000);//读取数据超时时间
                    conn.setConnectTimeout(8000);//连接的超时时间

                    InputStream is = conn.getInputStream();//字节流转换成字符串
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder response = new StringBuilder();
                    String str;
                    //String content=JsonTool.readStream(is);//将json数据转化为字符串  
                    // String weather=JsonTool.readJsonString(content,"live","weather");//获得值 
                    while ((str = reader.readLine()) != null) {
                        response.append(str + "\n");
                        Log.d("activity_login", str);
                    }
                    String responseStr = response.toString();
                    Log.d("activity_login", responseStr);
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

    private void getJSON(String Jsondata) {
        try {

            JSONObject a = new JSONObject(Jsondata);
            System.out.println(a.get("errcode"));
            String code = a.getString("errcode");
            if (code.equals("0")) {
                System.out.println("OK");
                Intent intent = new Intent(this, Select_Begin.class);
                startActivity(intent);
                this.finish();
            } else if (code.equals("40001")) {
                Looper.prepare();
                Toast.makeText(LoginActivity.this, "学号不存在", Toast.LENGTH_SHORT).show();
                Looper.loop();
            } else if (code.equals("40002")) {
                Looper.prepare();
                Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                Looper.loop();
            } else if (code.equals("40009")) {
                Looper.prepare();
                Toast.makeText(LoginActivity.this, "参数错误", Toast.LENGTH_SHORT).show();
                Looper.loop();
            } else {
                Looper.prepare();
                Toast.makeText(LoginActivity.this, "请求失败！", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}*/


