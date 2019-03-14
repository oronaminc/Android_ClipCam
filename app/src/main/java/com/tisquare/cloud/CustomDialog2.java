package com.tisquare.cloud;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tisquare.params.cameraDetailListData;

public class CustomDialog2 extends Dialog implements View.OnClickListener {



    private Context context;
    private int flag=0;

    ImageButton ok_custom_dialog_btn, cancel_custom_dialog_btn;
    TextView message;

    String camId, camName, camGroupName, liveUrl;
    cameraDetailListData DD;
    public CustomDialog2(Context context, int flag, cameraDetailListData dd) {
        super(context);
        this.context = context;
        this.flag = flag;
        this.DD = dd;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);
        initBtn();

    }

    void initBtn(){
        ok_custom_dialog_btn     = (ImageButton) findViewById(R.id.ok_custom_dialog_btn);
        ok_custom_dialog_btn.setOnClickListener(this);
        cancel_custom_dialog_btn  = (ImageButton) findViewById(R.id.cancel_custom_dialog_btn);
        cancel_custom_dialog_btn.setOnClickListener(this);
        message = (TextView) findViewById(R.id.mesgase);
        switch (flag){
            case 0 :
                message.setText("라이브 영상을 시청 하시겠습니까?");
                break;
            case 1 :
                message.setText("녹화 영상을 시청 하시겠습니까?");
                break;
        }
        camId = DD.getCamId();
        camName=DD.getCamName();
        camGroupName= DD.getSharingGroupName();
        liveUrl= DD.getLiveUrl();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.ok_custom_dialog_btn :


                switch (flag){
                    case 0 :

                        Intent live = new Intent(context, CameraLiveActivity.class);
                        live.putExtra("camId",camId);
                        live.putExtra("camName",camName);
                        live.putExtra("camGroupName",camGroupName);
                        live.putExtra("rtspUrl",liveUrl);
                        context.startActivity(live);
                        dismiss();
                        break;
                    case 1 :
                        Intent play = new Intent(context, CameraPlayBackActivity.class);

                        play.putExtra("camId",camId);
                        play.putExtra("camName",camName);
                        play.putExtra("camGroupName",camGroupName);
                        context.startActivity(play);
                        dismiss();
                        break;
                }


                break;
            case R.id.cancel_custom_dialog_btn :
                Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();
                dismiss();
                break;

        }
    }
}
