package com.example.administrator.sushe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.test.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/12.
 */


public class One_Person extends Activity implements View.OnClickListener{
    private TextView mName, mStuNum, mSex;

    private Button mBtn;
    //private Spinner building;

    private String building;



    //private ConfigUtil configUtil;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.one_person);


        mStuNum = (TextView) findViewById(R.id.stuNum);
        mName = (TextView) findViewById(R.id.name);
        mSex = (TextView) findViewById(R.id.sex);


        mBtn=(Button)findViewById(R.id.begin_btn);
        mBtn.setOnClickListener(this);

        //User stuid=(User)getApplicationContext();
        String studentid = ((User)getApplication()).getStudentid();


        String name =((User)getApplication()).getName();

        //User sex=(User)getApplicationContext();
        String gender = ((User)getApplication()).getGender();

        mStuNum.setText(studentid);
        mName.setText(name);
        mSex.setText(gender);

       initViews();

   }
    void initViews() {
        List<String> list = new ArrayList<String>();
        list.add("5号楼");
        list.add("8号楼");
        list.add("9号楼");
        list.add("13号楼");
        list.add("14号楼");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        final Spinner sp = (Spinner) findViewById(R.id.choose_buliding_number);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // parent： 为控件Spinner view：显示文字的TextView position：下拉选项的位置从0开始
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tvResult = (TextView) findViewById(R.id.show_buliding);
                //((MyApplication)getApplication()).setBuildnum(tvResult);
                //获取Spinner控件的适配器
                building = (String)sp.getSelectedItem();
                ((User)getApplication()).setBuilding(building);
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
                tvResult.setText(adapter.getItem(position));

            }
            //没有选中时的处理
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


   @Override
   public void onClick(View view) {
        String studentid = ((User)getApplication()).getStudentid();
       //创建map类
       Map<String,Object> params = new HashMap<String, Object>();
       //获取人数
       //
       params.put("num",1);
       params.put("stuid",studentid);
       ByPost(params, "utf-8");
//       setContentView(R.layout.Sucessful);
//       bt = (Button) findViewById(R.id.btn);
//       bt.setOnClickListener(this);
//        Intent intent = new Intent(this,Sucessful.class);//页面跳转
//        startActivity(intent);//加载页面
//        this.finish();//关闭此页面
   }
    public void ByPost(Map<String,Object> params,String encode){
        final byte[] data = getRequestData(params, encode).toString().getBytes();//获得请求体
        final String ip = "https://api.mysspku.com/index.php/V1/MobileCourse/SelectRoom";
        Log.d("activity_login", ip);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(ip);
                    //信任所有证书
                    if("https".equalsIgnoreCase(url.getProtocol())){
                        HTTPSTrust.ignoreSsl();
                    }
                    //打开链接
                    conn = (HttpURLConnection) url.openConnection();
                    //POST请求
                    conn.setRequestMethod("POST");
                    //设置属性
                    conn.setReadTimeout(8000);//读取数据超时时间
                    conn.setConnectTimeout(8000);//连接的超时时间
                    //拼接处要提交的字符串
                    // @SuppressWarnings("deprecation")
                    //String data ="num"+b+"stuid"+ URLEncoder.encode(xh,"UTF-8")+"stu1id"+URLEncoder.encode(stu1,"UTF-8")+"v1code"+URLEncoder.encode(v1cd,"UTF-8")+"buildingNO"+i;
                    //为post添加两行属性
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(data.length));
                    //因为post是通过流往服务器提交数据的，所以我们需要设置一个输出流
                    //设置打开输出流
                    conn.setDoOutput(true);
                    //拿到输出流
                    OutputStream os = conn.getOutputStream();
                    //使用输出流向服务器提交数据
                    os.write(data);
                    os.flush();
                    InputStream is = conn.getInputStream();//字节流转换成字符串
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        response.append(str + "\n");
                        Log.d("activity_login",str);
                    }
                    //获取字符串
                    String responseStr1 = response.toString();
                    Log.d("activity_login",responseStr1 );
                    getReturn(responseStr1);

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
    public static StringBuffer getRequestData(Map<String, Object>params,String encode){
        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {
            for(Map.Entry<String,Object> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode((String) entry.getValue(), encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;

    }
    private void getReturn(String data){
        try {
            //字符串转换为JSONObject对象
            JSONObject a = new JSONObject(data);
            String code = a.getString("errcode");
            if (code == "0") {//请求成功
                Intent intent = new Intent(this, Result.class);//页面跳转
                startActivity(intent);//加载页面
                this.finish();//关闭此页面
            } else {
                Looper.prepare();
                Toast.makeText(this, "请求失败！", Toast.LENGTH_SHORT).show();
                Looper.loop();


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}


