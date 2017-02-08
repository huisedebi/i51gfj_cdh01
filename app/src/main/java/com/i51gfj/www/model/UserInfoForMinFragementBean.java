package com.i51gfj.www.model;

import android.widget.ImageView;

import java.io.Serializable;

public class UserInfoForMinFragementBean implements Serializable {

    private int status;
    private String info;

    private DataBean data;
    private String  img;
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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public class DataBean{
        private String mobile;
        private String sex;
        private String realName;
        private String hometown;
        private String qq;
        private String email;


        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getHometown() {
            return hometown;
        }

        public void setHometown(String hometown) {
            this.hometown = hometown;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }


    @Override
    public String toString() {
        return "UserInfoForMinFragementBean{" +
                "status=" + status +
                ", info='" + info + '\'' +
                ", data=" + data +
                ", img='" + img + '\'' +
                '}';
    }
}
