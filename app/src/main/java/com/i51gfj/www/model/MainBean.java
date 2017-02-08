package com.i51gfj.www.model;

import java.util.List;

/**
 * Created by Administrator on 2016/8/12.
 */
public class MainBean {
    private int status;
    private String info;
    private List<AdvsInMainBean> advs;
    private List<MenuInMainBean> menu;
    private List<StoreInMainBean> store;
    private String text1;
    private String city_name;
    private Page page;

    public class Page{
        private String page;
        private String pageTotal;
        private String pageSize;
        private String dataTotal;


        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getPageTotal() {
            return pageTotal;
        }

        public void setPageTotal(String pageTotal) {
            this.pageTotal = pageTotal;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public String getDataTotal() {
            return dataTotal;
        }

        public void setDataTotal(String dataTotal) {
            this.dataTotal = dataTotal;
        }
    }

    public class AdvsInMainBean{
        private String title;
        private String img;
        private String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
    public class MenuInMainBean{
        private String id;
        private String name;
        private String type;
        private String img;
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
    public class StoreInMainBean{
        private String img;
        private String id;
        private String avgPoint;
        private String collect;
        private String address;
        private String name;
        private String distance;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
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

        public String getCollect() {
            return collect;
        }

        public void setCollect(String collect) {
            this.collect = collect;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }
    }

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

    public List<AdvsInMainBean> getAdvs() {
        return advs;
    }

    public void setAdvs(List<AdvsInMainBean> advs) {
        this.advs = advs;
    }

    public List<MenuInMainBean> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuInMainBean> menu) {
        this.menu = menu;
    }

    public List<StoreInMainBean> getStore() {
        return store;
    }

    public void setStore(List<StoreInMainBean> store) {
        this.store = store;
    }


    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
