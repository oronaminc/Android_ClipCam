package com.tisquare.cloud;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.skbb.api.skApi;
import com.skbb.callback.OnTaskCompleted;
import com.tisquare.params.cameraDetailData;
import com.tisquare.params.cameraListData;
import com.tisquare.ti2me.rplayer.Ti2RPlayer;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;


import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;



public class CameraPlayBackActivity extends Activity  implements  Callback , View.OnClickListener{

    String TAG = getClass().getName().toString();

    public ArrayList<cameraDetailData> cmDetailArr = new ArrayList<cameraDetailData>();

    public ArrayList<cameraListData> cmArr = new ArrayList<cameraListData>();

    Calendar cal;


    Ti2RPlayer mRPlayer = null ;
    int	mStreamType  = AudioManager.STREAM_MUSIC;
    SurfaceView mSv;
    SurfaceHolder mHolder;


    TextView cam_name, cam_group_name , cam_play_date,  cam_play_time,  cam_play_scal;

    ImageButton rtsp_start_bt, rtsp_stop_bt, date_bt, time_bt, scal_bt;
    ProgressBar progressBar;
    boolean layFlag=false;
    boolean medeaFlag = false;
    LinearLayout button_layout;


    // 카메라번호, 카메라이름, 그룹이름, rtspURL, 시간, 배속
    String camNum=null, camName=null, camGroupName=null, dateData=null,timeData=null, scalData=null , playbackUrl=null;

    int scalFlag = 0;

    ApplicationClass applicationClass;


    skApi skApi;
    String apiName="";
    String playBackResult = "";

    public void initBtn(){
        cam_name = (TextView)findViewById(R.id.cam_name2);
        cam_name.setText(camName);
        cam_group_name = (TextView)findViewById(R.id.cam_group_name2) ;
        cam_group_name.setText(camGroupName);
        cam_play_date = (TextView)findViewById(R.id.cam_play_date2);
        cam_play_time = (TextView)findViewById(R.id.cam_play_time2);
        cam_play_scal = (TextView)findViewById(R.id.cam_play_scal2);

        rtsp_start_bt = (ImageButton) findViewById(R.id.play_back_start_bt);
        rtsp_start_bt.setOnClickListener(this);

        rtsp_stop_bt = (ImageButton) findViewById(R.id.play_back_stop_bt);
        rtsp_stop_bt.setOnClickListener(this);

        button_layout = (LinearLayout)findViewById(R.id.button_layout);
        scal_bt = (ImageButton) findViewById(R.id.play_back_scal_bt);
        scal_bt.setOnClickListener(this);


        progressBar = (ProgressBar)findViewById(R.id.play_back_progressBar);
        mSv = (SurfaceView)findViewById(R.id.svPlayer);
        mSv.getHolder().addCallback(this);

        mSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideMenu();
            }
        });


        date_bt = (ImageButton) findViewById(R.id.play_back_date_bt);
        time_bt = (ImageButton)findViewById(R.id.play_back_time_bt);
        initPicker();
    }


    public void hideMenu(){
        if(layFlag){
            Log.e(TAG,"보여줘");
            button_layout.setVisibility(View.VISIBLE);
            layFlag = false;
        }else{
            Log.e(TAG,"숨겨줘");
            button_layout.setVisibility(View.GONE);
            layFlag = true;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cam_playback);
        Intent intent = getIntent();
        camNum = intent.getStringExtra("camId");
        camName = intent.getStringExtra("camName");
        camGroupName = intent.getStringExtra("camGroupName");

        applicationClass = (ApplicationClass)getApplicationContext();
        initBtn();
        initMedea();

        skApi = new skApi();
        skApi.setListener(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject, String s) {


                if(s.equals("getPlayBackUrl")){

                    try{
                        JSONObject json;
                        json = new JSONObject(jsonObject.toString());
                        String result = json.getString("resultCode");
                        //Login Success

                        if(result.equals("0")){
                            playBackResult = json.getString("resultMsg");
                            String play = json.getString("playbackUri");
                            playbackUrl = play;
                            Toast.makeText(getBaseContext(),playbackUrl,Toast.LENGTH_SHORT).show();
                        }else{
                            playBackResult = json.getString("resultMsg");
                            Toast.makeText(getBaseContext(),playBackResult,Toast.LENGTH_SHORT).show();
                        }

                    }catch (JSONException je){
                        Log.e(TAG,"JSoneExeption" + je);
                    }


                }else{
                    Toast.makeText(getBaseContext(),"Network상태가 불안합니다\n재시도 해주세요.",Toast.LENGTH_SHORT).show();

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
        if(mRPlayer!=null){

            mRPlayer.release();
            mRPlayer = null;
            Log.e(TAG , "onBackPresed!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            Intent back = new Intent(CameraPlayBackActivity.this, CameraListActivity.class);
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

            case R.id.play_back_start_bt :
                if(playbackUrl!=null){
                    if(medeaFlag==true){
                        mRPlayer.start();
                    }else{
                        try{
                            Log.e(TAG , "playBackUrl    :     " +   playbackUrl);
                            mRPlayer.setDataSource(playbackUrl);
                            mRPlayer.prepareAsync();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"PlayBackUrL이 없습니다.",Toast.LENGTH_SHORT).show();
                }

            break;

            case R.id.play_back_stop_bt :
                if(playbackUrl!=null){
                    mRPlayer.pause();
                }

            break;

            case R.id.play_back_scal_bt :
                Log.e(TAG , "배속 클릭");
                switch (scalFlag){
                    case 0 :
                        cam_play_scal.setText("1배속");
                        scalData = "1";
                        break;
                    case 1 :
                        cam_play_scal.setText("2배속");
                        scalData = "2";
                        break;
                    case 2 :
                        cam_play_scal.setText("4배속");
                        scalData = "4";
                        break;
                    case 3 :
                        cam_play_scal.setText("8배속");
                        scalData = "8";
                        break;
                    case 4 :
                        cam_play_scal.setText("16배속");
                        scalData = "16";
                        break;
                    case 5 :
                        cam_play_scal.setText("32배속");
                        scalData = "32";
                        scalFlag = 0;
                        break;
                }
                scalFlag++;
                if(dateData!=null&&timeData!=null&&scalData!=null){
                    Log.e(TAG,"날짜,시간입력 후");
                    //Context con, String cmd, String projectId, String projectAuth, String deviceId, String camId, String dateTime, String scale
                    skApi.getPlayBackUrl(applicationClass, applicationClass.getPlayBackCom(),
                                        applicationClass.getProjectId(), applicationClass.getProjectAuth(), applicationClass.getDeviceId(),
                                        camNum, dateData+timeData, scalData);

                }else{
                    Toast.makeText(CameraPlayBackActivity.this, "시간,날짜를 입력해주세요.", Toast.LENGTH_SHORT).show();

                }
                break;

        }
    }

    public void initMedea(){


        try{
            mRPlayer = new Ti2RPlayer();
            mRPlayer.setAudioStreamType(mStreamType);
           // mRPlayer.setDisplay(mHolder);
            mRPlayer.setOnErrorListener( new Ti2RPlayer.OnErrorListener() {
                @Override
                public boolean onError(Ti2RPlayer mp, int what, int extra) {
                    Log.e(TAG, "Error " + what + " " + extra);
                    mRPlayer.release();
                    mRPlayer = null;
                    medeaFlag = false;
                    Log.e(TAG, "Error2 " + what + " " + extra);
                    return false;
                }
            });
            mRPlayer.setOnPreparedListener( new Ti2RPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(Ti2RPlayer mp) {
                    Log.v(TAG, "Prepared");
                    //   progressBar.setVisibility(View.VISIBLE);
                    medeaFlag = true;
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
                            // Log.e(TAG, "PlayBack      GetDuration ::  " + mRPlayer.getDuration());
                            break;
                    }
                }
            });

               // mRPlayer.prepareAsync();

        }catch (Exception e){
            Log.e(TAG , "errorData  "   +   e);
        }

    }


    public void logOut (Context con, String authKey) {

        Log.e(TAG, "logOut  =====================================");
        Log.e(TAG, "authKey     ::      "   +   authKey );


        try {
            String baseUrl = "https://cloudcam.skbroadband.com";
            String loginUrl = "/do/vcapi/mobile/user/logout";
            String totalUrl = baseUrl+loginUrl;
            RequestParams params = new RequestParams();
            params.put("","");
            AsyncHttpClient client = new AsyncHttpClient();
            StringEntity jsonEntity = null;
            jsonEntity = new StringEntity("");
            client.addHeader("X-Scq-Auth-Key", authKey);
            jsonEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
            client.get(con, totalUrl,jsonEntity , "application/json",new JsonHttpResponseHandler(){

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.e(TAG, "onFailure  statusCode    "   +   statusCode);
                    Log.e(TAG, "onFailure  errorResponse    "   +   errorResponse);

                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Json Exception       "   +   e);
        }

    }


    public String checkDigit(int number){
        return  number <=9?"0"+number:String.valueOf(number);
    }

    public String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(date);
    }

    //timePicker & datePicker
    private void initPicker(){

        //Calendar를 이용하여 년, 월, 일, 시간, 분을 PICKER에 넣어준다.
        final Calendar cal = Calendar.getInstance();

        Log.e(TAG, cal.get(Calendar.YEAR)+"");
        Log.e(TAG, cal.get(Calendar.MONTH)+1+"");
        Log.e(TAG, cal.get(Calendar.DATE)+"");
        Log.e(TAG, cal.get(Calendar.HOUR_OF_DAY)+"");
        Log.e(TAG, cal.get(Calendar.MINUTE)+"");

        //DATE PICKER DIALOG
        date_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(CameraPlayBackActivity.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                        String dateData1 = checkDigit(month+1);
                        String dateData2 = checkDigit(date);
                        dateData = year + dateData1+dateData2+"T";
                        Log.e(TAG,"dateData     ::  "   +   dateData);
                        String msg = String.format("%d 년 %d 월 %d 일", year, month+1, date);
                        cam_play_date.setText(msg);
                        Toast.makeText(CameraPlayBackActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dialog.getDatePicker().setMaxDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();

            }
        });


        //TIME PICKER DIALOG
        time_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog dialog = new TimePickerDialog(CameraPlayBackActivity.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {

                        String timeData1= checkDigit(hour);
                        String timeData2 = checkDigit(min);
                        timeData = timeData1+timeData2+"00Z";
                        Log.e(TAG,"dateData     ::  "   +   timeData);
                        long t = System.currentTimeMillis();
                        String logData = convertTime(t);
                        StringBuffer sb = new StringBuffer(logData);
                        sb.insert(8,"T");
                        sb.insert(15,"Z");

                        Log.e(TAG,"TimeData       ::  "   +   sb);


                        String msg = String.format("%d 시 %d 분", hour, min);
                        cam_play_time.setText(msg);
                        Toast.makeText(CameraPlayBackActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);  //마지막 boolean 값은 시간을 24시간으로 보일지 아닐지

                dialog.show();

            }
        });
    }


}
