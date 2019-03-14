package com.tisquare.cloud;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.skbb.api.skApi;
import com.skbb.callback.OnTaskCompleted;
import com.tisquare.params.cameraDetailData;
import com.tisquare.params.cameraListData;
import com.tisquare.ti2me.rplayer.Ti2RPlayer;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;


public class CameraLiveActivity extends Activity  implements  Callback , View.OnClickListener{

    String TAG = getClass().getName().toString();

    public ArrayList<cameraDetailData> cmDetailArr = new ArrayList<cameraDetailData>();

    public ArrayList<cameraListData> cmArr = new ArrayList<cameraListData>();

    Calendar cal;


    Ti2RPlayer mRPlayer = null ;
    int	mStreamType  = AudioManager.STREAM_MUSIC;
    SurfaceView mSv;
    SurfaceHolder mHolder;

    TextView cam_name, cam_group_name , cam_play_time;
    ImageButton rtsp_start_bt, rtsp_stop_bt;
    ProgressBar progressBar;
    boolean layFlag=false;

    // 카메라번호, 카메라이름, 그룹이름, rtspURL, 시간, 배속
    String camNum=null, camName=null, camGroupName=null, liveUrl=null;


    ApplicationClass applicationClass;
    skApi skApi;
    String apiName="";
    String logOutResult = "";

    public void initBtn(){
        cam_name = (TextView)findViewById(R.id.cam_name);
        cam_name.setText(camName);
        cam_group_name = (TextView)findViewById(R.id.cam_group_name) ;
        cam_group_name.setText(camGroupName);
        cam_play_time = (TextView)findViewById(R.id.cam_play_time);

        rtsp_start_bt = (ImageButton) findViewById(R.id.rtsp_play_bt);
        rtsp_start_bt.setOnClickListener(this);

        rtsp_stop_bt = (ImageButton) findViewById(R.id.rtsp_stop_bt);
        rtsp_stop_bt.setOnClickListener(this);


        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        mSv = (SurfaceView)findViewById(R.id.live_svPlayer);
        mSv.getHolder().addCallback(this);

        mSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!layFlag){
                    Log.e(TAG ,"숨기고");
                    rtsp_start_bt.setVisibility(View.GONE);
                    rtsp_stop_bt.setVisibility(View.GONE);
                    layFlag = true;
                }else{
                    Log.e(TAG ,"보이고");
                    rtsp_start_bt.setVisibility(View.VISIBLE);
                    rtsp_stop_bt.setVisibility(View.VISIBLE);
                    layFlag = false;
                }

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cam_live);



        Intent intent = getIntent();

        camNum = intent.getStringExtra("camId");
        camName = intent.getStringExtra("camName");
        camGroupName = intent.getStringExtra("camGroupName");
        liveUrl = intent.getStringExtra("rtspUrl");
        Log.e(TAG , "urlData    ::      "   +   liveUrl);
        applicationClass = (ApplicationClass)getApplicationContext();
        initBtn();
        playMedia();
        skApi = new skApi();
        skApi.setListener(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject, String s) {

                if(apiName.equals("logOut")){
                    Log.e(TAG , "logOut  callBack   !!!!!!!!!!!!!!!!!!!!! "    );


                    try{
                        JSONObject json;
                        json = new JSONObject(jsonObject.toString());
                        String result = json.getString("status");
                        //Login Success

                        if(result.equals("200")){/*
                            logOutResult = json.getString("message");
                            applicationClass.authData = json.getString("authKey");
                            Toast.makeText(getBaseContext(),logOutResult,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getBaseContext(), CameraListActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();*/
                        }else{
                            logOutResult = json.getString("message");
                            Toast.makeText(getBaseContext(),logOutResult,Toast.LENGTH_SHORT).show();
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
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        Log.e(TAG , "onBackPresed!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        if(mRPlayer!=null){
            mRPlayer.release();
            mRPlayer = null;
            Intent back = new Intent(CameraLiveActivity.this, CameraListActivity.class);
            startActivity(back);
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRPlayer!=null) {
            mRPlayer.release();
            mRPlayer = null;
            //context,cmd,projectId,Auth,deviceId
            //skApi.logOut(applicationClass,applicationClass.getLogOutCom(),applicationClass.getProjectId(), applicationClass.getProjectAuth(), applicationClass.getDeviceId());
        }else{
            //skApi.logOut(applicationClass,applicationClass.getLogOutCom(),applicationClass.getProjectId(), applicationClass.getProjectAuth(), applicationClass.getDeviceId());
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {

        Log.i(TAG,"surfaceCreated");

        mRPlayer.setDisplay( arg0);
        mHolder = arg0;
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int width, int height) {

        Log.i(TAG,"surfaceChanged");
        mRPlayer.setDisplay(arg0);
        mHolder = arg0;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        Log.i(TAG,"surfaceDestroyed");
        if(mRPlayer!=null){
            mRPlayer.setDisplay(null);
            mHolder = null;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.rtsp_play_bt :
                    mRPlayer.start();
                break;

            case R.id.rtsp_stop_bt :
                    mRPlayer.pause();
                break;

        }
    }

    public void playMedia(){

        try{
            mRPlayer = new Ti2RPlayer();

            final Chronometer chronometer = (Chronometer) findViewById(R.id.cam_play_time);

            mRPlayer.setAudioStreamType(mStreamType);
            mRPlayer.setDisplay(mHolder);

            mRPlayer.setOnErrorListener( new Ti2RPlayer.OnErrorListener() {
                @Override
                public boolean onError(Ti2RPlayer mp, int what, int extra) {
                    Log.e(TAG, "Error " + what + " " + extra);

                    mRPlayer.release();
                    mRPlayer = null;
                    progressBar.setVisibility(View.GONE);
                   //

                    Intent intent = new Intent(getBaseContext(), CameraListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    Toast.makeText(getBaseContext(),"권한이 없습니다.\n재실행해주세요..",Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error2 " + what + " " + extra);
                    return false;
                }
            });
            mRPlayer.setOnPreparedListener( new Ti2RPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(Ti2RPlayer mp) {
                    Log.v(TAG, "Prepared");
                    //   progressBar.setVisibility(View.VISIBLE);
                    mp.start();
                }
            });
            mRPlayer.setOnBufferingUpdateListener( new Ti2RPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(Ti2RPlayer mp, int percent) {
                    Log.w(TAG, "Buffering " + percent);
                }
            });
            mRPlayer.setOnCompletionListener( new Ti2RPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(Ti2RPlayer mp) {
                    //   progressBar.setVisibility(View.GONE);
                    Log.w(TAG, "onCompletion");
                }
            });
            mRPlayer.setOnBufferingUpdateListener(new Ti2RPlayer.OnBufferingUpdateListener() {

                @Override
                public void onBufferingUpdate(Ti2RPlayer ti2RPlayer, int i) {
                    Log.e(TAG,"onBufferingUpdate" );
                    switch (i){
                        case 0 :
                            Log.e(TAG,"start" );
                            progressBar.setVisibility(View.VISIBLE);
                            break;
                        case 100 :
                            Log.e(TAG,"end" );
                            progressBar.setVisibility(View.GONE);
                            chronometer.start();
                            Log.e(TAG, "RTSP    GetDuration ::        " + mRPlayer.getDuration());
                            break;
                    }
                }
            });
            mRPlayer.setDataSource(liveUrl);
            mRPlayer.prepareAsync();

        }catch (Exception e){
            Log.e(TAG , "errorData  "   +   e);
        }



    }


}


