package com.i51gfj.www.model;

import java.util.List;

/**
 * Created by Administrator on 2016/8/12.
 */
public class ShopDetailBean {
    private int status;
    private String info;
    private StoreBean store_info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public StoreBean getStore_info() {
        return store_info;
    }

    public void setStore_info(StoreBean store_info) {
        this.store_info = store_info;
    }

    public class StoreBean {
        private String lng;
        private String lat;
        private String img;
        private String id;
        private String collect;
        private String avgPoint;
        private String address;
        private String name;
        private String tel;
        private String uid;
        private String text1;
        private String url1;


        public String getText1() {
            return text1;
        }

        public void setText1(String text1) {
            this.text1 = text1;
        }

        public String getUrl1() {
            return url1;
        }

        public void setUrl1(String url1) {
            this.url1 = url1;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        private List<StoreImagesBean> storeImages;

        public class StoreImagesBean {
            private String img;
            private int w;
            private int h;


            public int getW() {
                return w;
            }

            public void setW(int w) {
                this.w = w;
            }

            public int getH() {
                return h;
            }

            public void setH(int h) {
                this.h = h;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }
        }

        private String adText;
        private String manageArea;
        private String distance;
        private String route;
        private String review;
        private String isCollect;

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAvgPoint() {
            return avgPoint;
        }

        public void setAvgPoint(String avgPoint) {
            this.avgPoint = avgPoint;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCollect() {
            return collect;
        }

        public void setCollect(String collect) {
            this.collect = collect;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getManageArea() {
            return manageArea;
        }

        public void setManageArea(String manageArea) {
            this.manageArea = manageArea;
        }

        public String getRoute() {
            return route;
        }

        public void setRoute(String route) {
            this.route = route;
        }

        public String getReview() {
            return review;
        }

        public void setReview(String review) {
            this.review = review;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getAdText() {
            return adText;
        }

        public void setAdText(String adText) {
            this.adText = adText;
        }

        public List<StoreImagesBean> getStoreImages() {
            return storeImages;
        }

        public void setStoreImages(List<StoreImagesBean> storeImages) {
            this.storeImages = storeImages;
        }

        public String getIsCollect() {
            return isCollect;
        }

        public void setIsCollect(String isCollect) {
            this.isCollect = isCollect;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }
    }
}
