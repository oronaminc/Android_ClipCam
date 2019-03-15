package com.tisquare.cloud;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.WindowManager;

import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.auth.data.model.User;
import com.skbb.api.skApi;
import com.skbb.callback.OnTaskCompleted;
import com.tisquare.adapter.cameraListAdapter;
import com.tisquare.params.cameraDetailListData;
import com.tisquare.ti2me.rplayer.Ti2RPlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CameraListActivity extends AppCompatActivity implements  Callback, View.OnClickListener {




    private DatabaseReference mDatabase;

    ApplicationClass applicationClass;
    String TAG = getClass().getName().toString();

    Ti2RPlayer mRPlayer = null ;
    int	mStreamType  = AudioManager.STREAM_MUSIC;
    SurfaceView mSv2;
    SurfaceHolder mHolder2;

    ImageButton camera_search_btn;
    ListView lv_main_camera;
    ProgressBar pb;
    boolean layFlag=false;
    TextView TEXT;
    Button CheckButton;
    android.support.design.widget.TextInputEditText EditText;

    // 카메라번호, 카메라이름, 그룹이름, rtspURL, 시간, 배속
    String camNum=null, camName=null, camGroupName=null, liveUrl2=null, camId=null;

    TextView cam_name, cam_group_name , cam_play_time;

    public cameraListAdapter camAdapter;

    public ArrayList<cameraDetailListData> cmDetailListArr = new ArrayList<cameraDetailListData>();

    public static sendListHandler sendHandler;



    skApi skApi;
    String apiName="";
    String logOutResult = "";


    public class sendListHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            TEXT = (TextView) findViewById(R.id.TEXT);
            CheckButton = (Button) findViewById(R.id.CheckButton);
            EditText = findViewById(R.id.EditText);

            //RTSP
            if (msg.what == 0) {
                cameraDetailListData DD = (cameraDetailListData) msg.obj;
                CustomDialog2 CD2 = new CustomDialog2(CameraListActivity.this,0,DD);
                CD2.show();

            //PlayBack
            } else if (msg.what == 1) {
                cameraDetailListData DD = (cameraDetailListData) msg.obj;
                CustomDialog2 CD2 = new CustomDialog2(CameraListActivity.this,1,DD);
                CD2.show();

            } else if (msg.what == 2){
                cameraDetailListData DD = (cameraDetailListData) msg.obj;

                TEXT.setText("");

                if (mRPlayer!=null) {
                    mRPlayer.release();
                    mRPlayer = null;}

                camNum = DD.getCamId();
                camName = DD.getCamName();
                liveUrl2 = DD.getLiveUrl();
                camGroupName = DD.getSharingGroupName();
                camId = DD.getCamId();
                cam_name.setText(camName);
                cam_group_name.setText(camGroupName);
                playMedia();
            }
        }


    }

        String testToast ="";
    @Override
    public void onCreate(Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference();



        super.onCreate(savedInstanceState);
        applicationClass = (ApplicationClass)getApplicationContext();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera_list);
        sendHandler = new sendListHandler();



        skApi = new skApi();
        skApi.setListener(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject, String s) {


                Log.e(TAG,"StringData" + s);
                if(s.equals("getCameraList")){

                    try{
                        JSONObject json;
                        JSONArray jres;
                        json = new JSONObject(jsonObject.toString());
                        String result = json.getString("resultCode");
                        //Login Success
                        if(result.equals("0")){

                            testToast = json.getString("resultMsg");
                            jres = json.getJSONArray("cameraList");

                            for(int a=0; a< jres.length(); a++){

                                String camId = jres.getJSONObject(a).getString("camId");
                                String camName = jres.getJSONObject(a).getString("camName");
                                String cameraOwnerId = jres.getJSONObject(a).getString("cameraOwnerId");
                                String sharingGroupName = jres.getJSONObject(a).getString("sharingGroupName");
                                String imageUrl = jres.getJSONObject(a).getString("imageUri");
                                String liveUri = jres.getJSONObject(a).getString("liveUri");
                                String lowLiveUri = jres.getJSONObject(a).getString("lowLiveUri");
                                String zeroConf =jres.getJSONObject(a).getString("zeroConf");
                                String status = jres.getJSONObject(a).getString("camStatus");
                                int cancelKeepPeriod = jres.getJSONObject(a).getInt("cancelKeepPeriod");
                                cameraDetailListData camData = new cameraDetailListData();
                                camData.setCamId(camId);
                                camData.setCamName(camName);
                                camData.setCameraOwnerId(cameraOwnerId);
                                camData.setSharingGroupName(sharingGroupName);
                                camData.setThumbnailUrl(imageUrl);
                                camData.setLiveUrl(liveUri);
                                camData.setLowLiveUrl(lowLiveUri);
                                camData.setZeroConf(zeroConf);
                                camData.setStatus(status);
                                camData.setCancelKeepPerlod(cancelKeepPeriod);
                                cmDetailListArr.add(camData);

                                //초기 세팅
                                /*
                                camNum = camId;
                                camName = camName;
                                liveUrl2 = liveUri;
                                camGroupName = sharingGroupName;
                                */

                            }
                            pb.setVisibility(View.GONE);
                            camNum = cmDetailListArr.get(0).getCamId();
                            camName = cmDetailListArr.get(0).getCamName();
                            liveUrl2 = cmDetailListArr.get(0).getLiveUrl();
                            camGroupName = cmDetailListArr.get(0).getSharingGroupName();
                            Toast.makeText(getApplicationContext(),camNum, Toast.LENGTH_SHORT).show();
                            camAdapter.notifyDataSetChanged();
                        }else{
                            testToast = json.getString("resultMsg");

                        }

                    }catch (JSONException je){
                        Log.e(TAG,"JSoneExeption" + je);
                    }


                }else if(s.equals("logOut")){
                    Toast.makeText(getBaseContext(),"logOut",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getBaseContext(),"Network상태가 불안합니다\n재시도 해주세요.",Toast.LENGTH_SHORT).show();
                    // onRetry(5);
                }


            }
        }, apiName);


        initBtn();
        playMedia();



        ImageView insta = (ImageView) findViewById(R.id.insta);
        ImageView download = (ImageView) findViewById(R.id.download);
        ImageView facebook = (ImageView) findViewById(R.id.facebook);
        TEXT = (TextView) findViewById(R.id.TEXT);
        CheckButton = (Button) findViewById(R.id.CheckButton);

        insta.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/?hl=ko"));
                intent.setPackage("com.android.chrome");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        facebook.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.facebook.com/?hl=ko"));
                intent.setPackage("com.android.chrome");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });


        download.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                mDatabase.child("connect").setValue(true);
                mDatabase.child("camid").setValue("galaxy7");

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://firebasestorage.googleapis.com/v0/b/cloudcam-sk.appspot.com/o/ClipCam_2019-03-14-13-42-41.mp4?alt=media&token=3fd56799-71df-4455-9be3-c0d404e3b330"));
                intent.setPackage("com.android.chrome");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

                //writeNewUser("1234","NEW","newnewenw@naver.com");
                Toast.makeText(getApplicationContext(), "다운로드 중입니다" , Toast.LENGTH_SHORT).show();

                //skApi.getPlaybackDownLoad(applicationClass, applicationClass.getPlayBackCom(), applicationClass.getProjectId(),
                //        applicationClass.getProjectAuth(), applicationClass.getDeviceId(), camId, String.valueOf(2019031412), String.valueOf(2019031413));

            }
        });
        CheckButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                String WORD = EditText.getText().toString();
                EditText.setText(null);
                TEXT.setText(WORD);
                TEXT.setVisibility(View.VISIBLE);
            }
        });
    }
    private void writeNewUser(String userId, String name, String email) {
        mDatabase.child("users").child(userId).child("username").setValue(name);
    }



    void initBtn(){

        camera_search_btn = (ImageButton) findViewById(R.id.camera_search_btn);
        camera_search_btn.setOnClickListener(this);

        pb = (ProgressBar)findViewById(R.id.pb);
        pb.setVisibility(View.VISIBLE);

        lv_main_camera = (ListView)findViewById(R.id.lv_main_camera);
        camAdapter = new cameraListAdapter(applicationClass, cmDetailListArr);

        //Context con, String cmd, String projectId, String projectAuth, String deviceId, String order
        skApi.getCameraList(applicationClass, applicationClass.getCameraListCom(), applicationClass.getProjectId(),
                                applicationClass.getProjectAuth(), applicationClass.getDeviceId(),"0");


        lv_main_camera.setAdapter(camAdapter);

        cam_name = (TextView)findViewById(R.id.cam_name);

        cam_group_name = (TextView)findViewById(R.id.cam_group_name) ;

        cam_play_time = (TextView)findViewById(R.id.cam_play_time);

        mSv2 = (SurfaceView)findViewById(R.id.live_svPlayer2);
        mSv2.getHolder().addCallback(this);


    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){

            // 카메라 검색
            case R.id.camera_search_btn :
                if(cmDetailListArr.size() >0){

                    Log.e(TAG , "카메라 리스트 0보다 큼");
                    // 입력된 카메라 비교 로직 필요.
                    searchDialog CD2 = new searchDialog(CameraListActivity.this,cmDetailListArr);
                    CD2.show();
                }else{
                    Log.e(TAG , "카메라 리스트 작음~~~~~~~~~~~~~~~~~");
                }
            break;


        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mRPlayer!=null) {
            mRPlayer.release();
            mRPlayer = null;
            //context,cmd,projectId,Auth,deviceId
            //skApi.logOut(applicationClass,applicationClass.getLogOutCom(),applicationClass.getProjectId(), applicationClass.getProjectAuth(), applicationClass.getDeviceId());
        }else{
            //skApi.logOut(applicationClass,applicationClass.getLogOutCom(),applicationClass.getProjectId(), applicationClass.getProjectAuth(), applicationClass.getDeviceId());
        }

        //skApi.logOut(applicationClass,applicationClass.getLogOutCom(),applicationClass.getProjectId(), applicationClass.getProjectAuth(), applicationClass.getDeviceId());
    }


    @Override
    public void surfaceCreated(SurfaceHolder arg0) {

        Log.i(TAG,"surfaceCreated");

        mRPlayer.setDisplay( arg0);
        mHolder2 = arg0;
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int width, int height) {

        Log.i(TAG,"surfaceChanged");
        mRPlayer.setDisplay(arg0);
        mHolder2 = arg0;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        Log.i(TAG,"surfaceDestroyed");
        if(mRPlayer!=null){
            mRPlayer.setDisplay(null);
            mHolder2 = null;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();
        // rest of the code
    }

    public void playMedia(){

        try{
            mRPlayer = new Ti2RPlayer();

            final Chronometer chronometer = (Chronometer) findViewById(R.id.cam_play_time);

            mRPlayer.setAudioStreamType(mStreamType);
            mRPlayer.setDisplay(mHolder2);

            mRPlayer.setOnErrorListener( new Ti2RPlayer.OnErrorListener() {
                @Override
                public boolean onError(Ti2RPlayer mp, int what, int extra) {
                    Log.e(TAG, "Error " + what + " " + extra);

                    mRPlayer.release();
                    mRPlayer = null;
                    pb.setVisibility(View.GONE);
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
                            pb.setVisibility(View.VISIBLE);
                            break;
                        case 100 :
                            Log.e(TAG,"end" );
                            pb.setVisibility(View.GONE);
                            chronometer.start();
                            Log.e(TAG, "RTSP    GetDuration ::        " + mRPlayer.getDuration());
                            break;
                    }
                }
            });
            //liveUrl2 = cmDetailListArr.get(0).getLiveUrl();
            Log.e(TAG , "urlData    ::      "   +   liveUrl2);
            mRPlayer.setDataSource(liveUrl2);
            mRPlayer.prepareAsync();

        }catch (Exception e){
            Log.e(TAG , "errorData  "   +   e);
        }



    }


}


