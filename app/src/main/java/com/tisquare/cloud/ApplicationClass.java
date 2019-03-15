package com.tisquare.cloud;


import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by han on 2016-10-25.
 */

public class ApplicationClass extends Application {

    /** onCreate()
     * 액티비티, 리시버, 서비스가 생성되기전 어플리케이션이 시작 중일때
     * Application onCreate() 메서드가 만들어 진다고 나와 있습니다.
     * by. Developer 사이트
     *
     */

    /*
    public String username;
    public String email;

    public ApplicationClass() {

    }

    public ApplicationClass(String username, String email) {
        this.username = username;
        this.email = email;
    }
    */

    private String loginCom = "login.t_view_platform";
    private String logOutCom = "logout.t_view_platform";
    private String sessionCheckCom = "heartbeat.t_view_platform";
    private String cameraListCom = "camera_list_search.t_view_platform";
    private String cameraListOrderCom = "camera_list_sort.t_view_platform";
    private String cameraInfoCom = "camera_info.t_view_platform";
    private String playBackCom = "camera_playback_url.t_view_platform";
    private String cameraLiveURLCom = "camera_live_url.t_view_platform";
    private String cameraDailyRecCom = "camera_daily_rec_list.t_view_platform";
    private String cameraTimeLineCom = "camera_timeline_list.t_view_platform";
    private String cameraListSimpleCom = "cameras_list_simple_search.t_view_platform";
    private String cameraLiveListRTSPCom = "cameras_live_rtsp_search.t_view_platform(RTSP)";
    private String cameraLiveListRTMPCom = "cameras_live_rtmp_search.t_view_platform(RTMP)";
    private String cameraPlaybackListRTSPCom = "cameras_recvideo_rtsp_search.t_view_platform(RTSP)";
    private String cameraPlaybackListRTMPCom = "cameras_recvideo_rtmp_search.t_view_platform(RTMP)";
    private String cameraDownloadListCom = "cameras_rec_download_history.t_view_platform";
    private String cameraDownloadCom = "cameras_rec_download.t_view_platform";
    private String cameraDownloadInfoCom = "cameras_rec_download_req_detail.t_view_platform";





    public String getPlayBackCom() {
        return playBackCom;
    }

    public String getLogOutCom() {
        return logOutCom;
    }

    public String getLoginCom() {
        return loginCom;
    }

    public String getCameraListCom() {
        return cameraListCom;
    }
    public String projectId;

    public String getProjectAuth() {
        return projectAuth;
    }

    public void setProjectAuth(String projectAuth) {
        this.projectAuth = projectAuth;
    }

    public String projectAuth;
    public String deviceId;
    public String key;
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }



    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }


    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }



    public String TAG = getClass().getName().toString();


    @Override
    public Context createConfigurationContext(Configuration overrideConfiguration) {
        return super.createConfigurationContext(overrideConfiguration);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }

    // IMEI 값 가져오기.
    public String getImeiData(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return "";
            }
            String imei = telephonyManager.getDeviceId();
            Log.e(TAG, "imeiData    =       " + imei);
            if (imei != null && !imei.isEmpty()) {
                return imei;
            } else {
                return android.os.Build.SERIAL;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e( TAG, "Exception  =   " + e );
        }
        return "not_found";
    }

}
