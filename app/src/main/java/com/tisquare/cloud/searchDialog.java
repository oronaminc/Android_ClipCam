package com.tisquare.cloud;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tisquare.params.cameraDetailListData;

import java.util.ArrayList;

public class searchDialog extends Dialog implements View.OnClickListener {



    private Context context;

    ImageButton search_ok_btn, search_cancel_btn;
    EditText et_camera_search;

    public ArrayList<cameraDetailListData> searchArr = new ArrayList<cameraDetailListData>();

    public searchDialog(Context context, ArrayList<cameraDetailListData> searchArr) {
        super(context);
        this.context = context;
        this.searchArr = searchArr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_dialog);
        initBtn();

    }

    void initBtn(){
        search_ok_btn     = (ImageButton) findViewById(R.id.search_ok_btn);
        search_ok_btn.setOnClickListener(this);
        search_cancel_btn  = (ImageButton) findViewById(R.id.search_cancel_btn);
        search_cancel_btn.setOnClickListener(this);
        et_camera_search = (EditText) findViewById(R.id.et_camera_search);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.search_ok_btn :

                String camName = et_camera_search.getText().toString();
                boolean ifFlag = false;
                int selectNum=0;
                for(int a=0; a < searchArr.size(); a++){
                    String camName2 = searchArr.get(a).getCamName();
                    if(camName.equals(camName2)){
                        Log.e("search IF    ", "data     ::      "  +   searchArr.get(a).getCamName());
                        ifFlag = true;
                        selectNum= a;
                        break;
                    }

                }

                if(ifFlag){
                    Log.e("OK","입력값이 있으니까 담페이지");
                        String camId = searchArr.get(selectNum).getCamName();
                        String camGroupName = searchArr.get(selectNum).getSharingGroupName();
                        String liveUrl = searchArr.get(selectNum).getLiveUrl();
                        Intent live = new Intent(context, CameraLiveActivity.class);
                        live.putExtra("camId",camId);
                        live.putExtra("camName",camName);
                        live.putExtra("camGroupName",camGroupName);
                        live.putExtra("rtspUrl",liveUrl);
                        context.startActivity(live);
                        dismiss();
                }else {
                    Log.e("OK", "입력값이 없으니까 토스트");
                    Toast.makeText(getContext(),"잘못된 카메라이름 입니다.",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.search_cancel_btn :
                Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();
                dismiss();
                break;

        }
    }
}
