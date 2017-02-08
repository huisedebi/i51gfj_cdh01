package com.i51gfj.www.model;

import java.io.Serializable;

public class UserInfo implements Serializable {

    private static UserInfo instance = null;

    public static UserInfo getInstance() {
        if (instance == null) {
            instance = new UserInfo();
        }
        return instance;
    }
    private int status;
    private String info;
    private String uid;
    private String headImg;
    private String userName;
    private String userType;
    private String token;
    private boolean is_remenrber;//是否记录密码下次登录
    private boolean is_logining;//是否登录中

    public boolean is_remenrber() {
        return is_remenrber;
    }

    public void setIs_remenrber(boolean is_remenrber) {
        this.is_remenrber = is_remenrber;
    }

    public static void setInstance(UserInfo instance) {
        UserInfo.instance = instance;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean is_logining() {
        return is_logining;
    }

    public void setIs_logining(boolean is_logining) {
        this.is_logining = is_logining;
    }

    public static void cleanUserInfo() {
        if (instance != null) {
            instance = null;
        }
    }


    @Override
    public String toString() {
        return "UserInfo{" +
                "status=" + status +
                ", info='" + info + '\'' +
                ", uid='" + uid + '\'' +
                ", headImg='" + headImg + '\'' +
                ", userName='" + userName + '\'' +
                ", userType='" + userType + '\'' +
                ", token='" + token + '\'' +
                ", is_remenrber=" + is_remenrber +
                ", is_logining=" + is_logining +
                '}';
    }
}
