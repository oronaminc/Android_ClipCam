package com.tisquare.params;

public class cameraDetailData {


//
    //카메라 ID
    private String camId;
    // 카메라 이름
    private String camName;
    // 썸네일 이미지
    private String thumbnailUrl;
    // liveUrl ( 기본 해상도 )
    private String liveUrl;
    // LowLiveUrl ( 저해상도 )
    private String lowLiveUrl;
    // 제로 컨프
    private String zeroConf;


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


}
