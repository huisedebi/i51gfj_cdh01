package com.i51gfj.www.model;

import java.util.List;

/**
 * Created by Administrator on 2016/8/20.
 */
public class MineIndexWrapper {
   private int status;
    private String info;
    private String amount;
    private String userType;
    private String storeId;
    private String storeStatus;

    public String getInfo() {
        return info;
    }

    public int getStatus() {
        return status;
    }

    public String getAmount() {
        return amount;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getUserType() {
        return userType;
    }

    public String getStoreStatus() {
        return storeStatus;
    }


    private List<ShareData> share;
    public class ShareData{
        private String share_name;
        private String share_url;
        private String share_title;
        private String share_des;
        private String share_img;
        private String share_num;

        public String getShare_name() {
            return share_name;
        }

        public void setShare_name(String share_name) {
            this.share_name = share_name;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public String getShare_title() {
            return share_title;
        }

        public void setShare_title(String share_title) {
            this.share_title = share_title;
        }

        public String getShare_des() {
            return share_des;
        }

        public void setShare_des(String share_des) {
            this.share_des = share_des;
        }

        public String getShare_img() {
            return share_img;
        }

        public void setShare_img(String share_img) {
            this.share_img = share_img;
        }

        public String getShare_num() {
            return share_num;
        }

        public void setShare_num(String share_num) {
            this.share_num = share_num;
        }
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setStoreStatus(String storeStatus) {
        this.storeStatus = storeStatus;
    }

    public List<ShareData> getShare() {
        return share;
    }

    public void setShare(List<ShareData> share) {
        this.share = share;
    }
}
