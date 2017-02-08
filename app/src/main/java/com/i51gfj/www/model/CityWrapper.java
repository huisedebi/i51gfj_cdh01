package com.i51gfj.www.model;

import java.util.List;

/**
 * 城市列表数据
 */
public class CityWrapper {

    /**
     * state : 1
     * contents : 返回数据成功
     * now_city : 厦门市
     * data : ["厦门市","莆田市","广州市","深圳市","佛山市","汕头市","上海市","武汉市"]
     * last_call_city : ["厦门市","莆田市"]
     */

    private int status;
    private String info;
    private String contents;
    private String now_city;
    private List<String> data;
    private List<String> last_call_city;
    private String title;
    private List<CityList> cityList;


    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<CityList> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityList> cityList) {
        this.cityList = cityList;
    }

    public class CityList{
        private String id;
        private String name;


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
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setNow_city(String now_city) {
        this.now_city = now_city;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public void setLast_call_city(List<String> last_call_city) {
        this.last_call_city = last_call_city;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContents() {
        return contents;
    }

    public String getNow_city() {
        return now_city;
    }

    public List<String> getData() {
        return data;
    }

    public List<String> getLast_call_city() {
        return last_call_city;
    }
}
