package com.tisquare.params;

import android.view.View;

public class cameraDetailListData {


    //카메라 ID
    private String camId;
    // 카메라 이름
    private String camName;
    //사용자이름
    private String cameraOwnerId;
    //그룹이름
    private String sharingGroupName;

    // 썸네일 이미지
    private String thumbnailUrl;
    //고해상도 RTSP
    private String liveUrl;
    //저해상도 RTSP
    private String lowLiveUrl;
    // 제로 컨프
    private String zeroConf;
    //카메라 상태 ( 1 - 연결 , 2 - 연결끊김 , 3 - 녹화중 , 4 - 인증 대기 , 5 펌웨어 업데이트중, 10 -중지, 90 - 해지 )
    private String status;
    // 해지유지 기간으로 status 값이 90일때만 전달 ( open 항목 으로 Default: 0 )
    private int cancelKeepPerlod;

        public String getCamId() {
            return camId;
        }

        public void setCamId(String camId) {
            this.camId = camId;
        }

        public String getCamName() {
            return camName;
        }

        public void setCamName(String camName) {
            this.camName = camName;
        }

        public String getCameraOwnerId() {
            return cameraOwnerId;
        }

        public void setCameraOwnerId(String cameraOwnerId) {
            this.cameraOwnerId = cameraOwnerId;
        }

        public String getSharingGroupName() {
            return sharingGroupName;
        }

        public void setSharingGroupName(String sharingGroupName) {
            this.sharingGroupName = sharingGroupName;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }


        public String getLiveUrl() {
            return liveUrl;
        }

        public void setLiveUrl(String liveUrl) {
            this.liveUrl = liveUrl;
        }

        public String getLowLiveUrl() {
            return lowLiveUrl;
        }

        public void setLowLiveUrl(String lowLiveUrl) {
            this.lowLiveUrl = lowLiveUrl;
        }

        public String getZeroConf() {
            return zeroConf;
        }

        public void setZeroConf(String zeroConf) {
            this.zeroConf = zeroConf;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getCancelKeepPerlod() {
            return cancelKeepPerlod;
        }

        public void setCancelKeepPerlod(int cancelKeepPerlod) {
            this.cancelKeepPerlod = cancelKeepPerlod;
        }
}
