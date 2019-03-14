package com.tisquare.adapter;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.tisquare.cloud.ApplicationClass;
import com.tisquare.params.cameraDetailListData;
import com.tisquare.cloud.R;
import java.util.ArrayList;


import static com.tisquare.cloud.CameraListActivity.sendHandler;

public class cameraListAdapter extends BaseAdapter {


	String TAG = getClass().getName().toString();


	private LayoutInflater inflater;
	public ArrayList<cameraDetailListData> cameraListViewData = new ArrayList<cameraDetailListData>() ;



	Context con;
	ApplicationClass applicationClass;
	// ListViewAdapter의 생성자
	public cameraListAdapter(Context context, ArrayList<cameraDetailListData> al) {
		this.con = context;
		this.cameraListViewData = al;
		inflater = LayoutInflater.from(context);
		applicationClass = (ApplicationClass)con.getApplicationContext();
		Log.e(TAG ,	"	-----------------	cameraListViewData		"	+cameraListViewData.size());
	}

	// Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
	@Override
	public int getCount() {
	//	Log.e(TAG ,	"	-----------------	listViewItemList		"	+sosListArray.size());
		return cameraListViewData.size() ;
	}


	// View lookup cache
	private static class ViewHolder {
		// Send Resource

		ImageView im_camera_thum;

		TextView tv_camera_list_item_name;
		TextView tv_camera_list_item_group;
		TextView tv_camera_list_item_status;
		ImageButton im_live_btn, im_play_back_btn;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder =null;
		final int pos = position;

		if (convertView == null) {
			//LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.custom_camera_item2, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.im_camera_thum = (ImageView) convertView.findViewById(R.id.im_camera_list_img);
			viewHolder.tv_camera_list_item_name = (TextView) convertView.findViewById(R.id.tv_camera_list_item_name);
			viewHolder.tv_camera_list_item_group = (TextView) convertView.findViewById(R.id.tv_camera_list_item_group);
			viewHolder.tv_camera_list_item_status = (TextView) convertView.findViewById(R.id.camera_list_item_status);
			viewHolder.im_live_btn = (ImageButton)convertView.findViewById(R.id.camera_list_item_live_bt);
			viewHolder.im_play_back_btn = (ImageButton)convertView.findViewById(R.id.camera_list_item_playback_bt);
			convertView.setTag(viewHolder);

		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}

		Log.e(TAG ,"url 전	::	");

		GlideUrl url = new GlideUrl(cameraListViewData.get(pos).getThumbnailUrl(), new LazyHeaders.Builder()
				.addHeader("X-Scq-Auth-Key",applicationClass.getKey())
				.build());

		Glide.with(con)
			 .load(url)
		//	 .transition(withCrossFade())
			 .thumbnail(0.5f)
		//	 .apply(options)
			 .into(viewHolder.im_camera_thum);
		viewHolder.tv_camera_list_item_name.setText(cameraListViewData.get(pos).getCamName());

		Log.e(TAG,"camName		"	+	cameraListViewData.get(pos).getCamId());
		viewHolder.tv_camera_list_item_group.setText(cameraListViewData.get(pos).getSharingGroupName());
		if(cameraListViewData.get(pos).getStatus().equals("1")){
			viewHolder.tv_camera_list_item_status.setText("연결됨");
		}else if(cameraListViewData.get(pos).getStatus().equals("2")){
			viewHolder.tv_camera_list_item_status.setText("연결끊김");
		}else if(cameraListViewData.get(pos).getStatus().equals("3")){
			viewHolder.tv_camera_list_item_status.setText("녹화중");
		}else if(cameraListViewData.get(pos).getStatus().equals("4")){
			viewHolder.tv_camera_list_item_status.setText("인증대기");
		}else if(cameraListViewData.get(pos).getStatus().equals("5")){
			viewHolder.tv_camera_list_item_status.setText("업데이트중");
		}else if(cameraListViewData.get(pos).getStatus().equals("10")){
			viewHolder.tv_camera_list_item_status.setText("중지");
		}

        viewHolder.im_live_btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Message msg0 =null;
					msg0 = new Message();
					msg0.what =0;
					msg0.obj = cameraListViewData.get(pos);
					sendHandler.sendMessage(msg0);

				}
			});
		viewHolder.im_play_back_btn.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
                    Message msg0 =null;
                    msg0 = new Message();
                    msg0.what =1;
                    msg0.obj = cameraListViewData.get(pos);
                    sendHandler.sendMessage(msg0);
				}
			});

		viewHolder.im_camera_thum.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Message msg0 = null;
				msg0 = new Message();
				msg0.what = 2;
				msg0.obj = cameraListViewData.get(pos);
				sendHandler.sendMessage(msg0);
			}
		});
		return convertView;
	}




	// 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
	@Override
	public long getItemId(int position) {
		return position ;
	}

	// 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
	@Override
	public Object getItem(int position) {
		//Log.e("getItem	" ,"Count"	+ position);
		return cameraListViewData.get(position) ;
	}
	public void remove(int position) {
		cameraListViewData.remove(position);
	}
	public void removeAll() {
		cameraListViewData.clear();
	}



}


