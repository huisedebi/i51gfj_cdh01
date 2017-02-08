package com.i51gfj.www.model;

import java.util.List;

/**
 * Created by Administrator on 2016/8/20.
 */
public class ShopInfoBean {

    private int status;
    private String info;
    private Data data;
    private List<ImgList> imgList;
    private List<Cate> cate;

    public List<Cate> getCate() {
        return cate;
    }

    public void setCate(List<Cate> cate) {
        this.cate = cate;
    }

    public class Cate{
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public List<ImgList> getImgList() {
        return imgList;
    }

    public void setImgList(List<ImgList> imgList) {
        this.imgList = imgList;
    }

    public class ImgList{
        private String img;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
    public class Data{
        private String id;
        private String name;
        private String tel;
        private String intro;
        private String manageArea;
        private String address;
        private String img;
        private String route;
        private String lng;
        private String lat;
        private String cityId;
        private String city;
        private String cidStr;
        private String api_address;


        public String getApi_address() {
            return api_address;
        }

        public void setApi_address(String api_address) {
            this.api_address = api_address;
        }

        public String getCidStr() {
            return cidStr;
        }

        public void setCidStr(String cidStr) {
            this.cidStr = cidStr;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getManageArea() {
            return manageArea;
        }

        public void setManageArea(String manageArea) {
            this.manageArea = manageArea;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getRoute() {
            return route;
        }

        public void setRoute(String route) {
            this.route = route;
        }

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

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }


















    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
