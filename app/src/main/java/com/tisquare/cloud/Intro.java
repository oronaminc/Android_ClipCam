package com.tisquare.cloud;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;


public class Intro extends Activity {



    // TODO: Intro -> get Imei 값 check 없으면 앱 종료.

    ApplicationClass applicationClass;
    String imei;
    String TAG = getClass().getName().toString();

    private Handler Intro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        applicationClass = (ApplicationClass)getApplicationContext();
        setContentView(R.layout.activity_intro);

       // Intro = new Handler();
       // Intro.postDelayed(irun, 1000);

        // Internet , Location
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("권한수락을 하지않으셨습니다.\n어플을 사용하시려면 셋팅 -> 권한을 부여해 주시기 바랍니다.")
                .setPermissions(Manifest.permission.INTERNET,  Manifest.permission.READ_PHONE_STATE , Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();


    }
    PermissionListener permissionlistener = new PermissionListener() {

        @Override
        public void onPermissionGranted() {
            Log.e(TAG,"Permission Granted");
            Intro = new Handler();
            Intro.postDelayed(irun, 2000);
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(Intro.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            finish();
            Log.e(TAG,"Permission Denied    "   +   deniedPermissions.toString());

        }
    };

    Runnable irun = new Runnable() {

        @Override
        public void run() {

          /* if(imei.equals("")){
               Toast.makeText(getBaseContext(), "USIM없는 장비는 지원하지 않습니다.", Toast.LENGTH_SHORT ).show();
               finish();
           }else{*/

               imei = applicationClass.getImeiData(applicationClass);
               Log.e(TAG,"--------------       "       +   imei);
               Intent firstLogin = new Intent(Intro.this ,  Login.class);
               firstLogin.putExtra("imei",imei);
               startActivity(firstLogin);
               finish();
           //}

        }
    };


}
