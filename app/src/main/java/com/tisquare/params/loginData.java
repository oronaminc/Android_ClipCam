package com.tisquare.params;


public class loginData {



    private String id;
    private String pwd;
    private String deviceId;
    private String appId;
    private String cflag;
    private String deviceType;
    private String  pwdEncNew;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCflag() {
        return cflag;
    }

    public void setCflag(String cflag) {
        this.cflag = cflag;
    }


    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getPwdEncNew() {
        return pwdEncNew;
    }

    public void setPwdEncNew(String pwdEncNew) {
        this.pwdEncNew = pwdEncNew;
    }

}