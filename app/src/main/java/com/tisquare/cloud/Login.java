package com.tisquare.cloud;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.skbb.api.skApi;
import com.skbb.callback.OnTaskCompleted;
import com.tisquare.params.loginData;

import org.json.JSONException;
import org.json.JSONObject;




public class Login extends Activity implements View.OnClickListener{

    // TODO: Intro -> first start add on permission
    ApplicationClass applicationClass;
    String TAG = getClass().getName().toString();
    ImageButton bt_ok, bt_cancel;
    EditText et_id, et_pass;
    ProgressBar pb;

    String imei, idData, passData;
    loginData loginData;

    String loginToast="";
    skApi skApi;
    String apiName="";
    String loginCmd = "login.t_view_platform";
    /**
    * Test ID : "yesit"   // Pass : "Seoulbusan1004"
     *
    *      ID: "gavacam"  // Pass : "1q2w3e4r5t"
     *
     *     Id : tisquare // !Tisquare12
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationClass = (ApplicationClass)getApplicationContext();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        skApi = new skApi();
        skApi.setListener(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject, String s) {

                Log.e(TAG , "sData  "   +   s       );
                if(jsonObject == null){
                    Toast.makeText(getBaseContext(),"Network상태가 불안합니다\n재시도 해주세요.",Toast.LENGTH_SHORT).show();
                }

                if(s.equals("login")){
                    Log.e(TAG , "login  callBack   !!!!!!!!!!!!!!!!!!!!! "    );

                        try{
                            JSONObject json;
                            json = new JSONObject(jsonObject.toString());
                            String result = json.getString("resultCode");
                            //Login Success
                            if(result.equals("0")){
                                loginToast = json.getString("resultMsg");
                                applicationClass.setProjectId(json.getString("projectId"));
                                applicationClass.setProjectAuth(json.getString("projectAuth"));
                                applicationClass.setKey(json.getString("key"));

                                Toast.makeText(getBaseContext(),loginToast,Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getBaseContext(), CameraListActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }else{
                                loginToast = json.getString("resultCode") + "  " + json.getString("resultMsg");
                                Toast.makeText(getBaseContext(),loginToast,Toast.LENGTH_SHORT).show();
                            }

                        }catch (JSONException je){
                            Log.e(TAG,"JSoneExeption" + je);
                        }

                    }else{
                        Toast.makeText(getBaseContext(),"Network상태가 불안합니다\n재시도 해주세요.",Toast.LENGTH_SHORT).show();
                        // onRetry(5);
                    }
            }
        }, apiName);
        Intent intent = getIntent();
        imei = intent.getStringExtra("imei");
        initBtn();
    }

    void initBtn(){
        bt_ok = (ImageButton)findViewById(R.id.login_bt_ok);
        bt_ok.setOnClickListener(this);

        bt_cancel = (ImageButton)findViewById(R.id.login_bt_cancel);
        bt_cancel.setOnClickListener(this);

        et_id = (EditText)findViewById(R.id.login_et_id);
        et_pass = (EditText)findViewById(R.id.login_et_pass);
        pb = (ProgressBar)findViewById(R.id.pb);
        pb.setVisibility(View.GONE);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.login_bt_ok :
                idData = et_id.getText().toString().trim();
                passData = et_pass.getText().toString().trim();
                logIn(idData, passData);
                break;
            case R.id.login_bt_cancel :
                Toast.makeText(getBaseContext(),"취소버튼클릭",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
    void logIn(String id , String pass){

        try{
            loginData = new loginData();
            if(id.length() == 0 && pass.length() == 0) {
                loginData.setId("09198AA");
                loginData.setDeviceId(imei);
                loginData.setPwd("a1234567890");
                loginData.setDeviceType("android");
                loginData.setAppId("skbb");
                loginData.setCflag("no");
                loginData.setPwdEncNew("Y");
            }else{
                loginData.setId(id);
                loginData.setDeviceId(imei);
                loginData.setPwd(pass);
                loginData.setDeviceType("android");
                loginData.setAppId("skbb");
                loginData.setCflag("no");
                loginData.setPwdEncNew("Y");
            }

            applicationClass.setDeviceId(imei);

            skApi.login(applicationClass,applicationClass.getLoginCom(), loginData.getId(), loginData.getDeviceId(),loginData.getPwd(),
                        loginData.getDeviceType(),loginData.getAppId(),loginData.getCflag(),loginData.getPwdEncNew());

        }catch (Exception jse){
            Log.e(TAG , "JSON Exception     ::  "   +   jse);
        }

    }
}
