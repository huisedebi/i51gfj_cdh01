package com.i51gfj.www.model;

import java.util.List;

public class RedListBean {
    private     int status;
    private String info;
    private PageBean page;

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

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public List<AdveBean> getAdvs() {
        return advs;
    }

    public void setAdvs(List<AdveBean> advs) {
        this.advs = advs;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public class PageBean {
        private String page;
        private String page_total;
        private String page_size;
        private String data_total;
        private String pageTotal;
        private String pageSize;
        private String dataTotal;


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

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getPage_total() {
            return page_total;
        }

        public void setPage_total(String page_total) {
            this.page_total = page_total;
        }

        public String getPage_size() {
            return page_size;
        }

        public void setPage_size(String page_size) {
            this.page_size = page_size;
        }

        public String getData_total() {
            return data_total;
        }

        public void setData_total(String data_total) {
            this.data_total = data_total;
        }
    }
    private List<AdveBean> advs;
    public class AdveBean{
        private String title;
        private String img;
        private String url;
        private String  typeId;


        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

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
    }
    private List<DataBean> data;
    public class DataBean{
        private String collect;
        private String avgPoint;
        private String title;
        private String id;
        private String img;
        private String distance;
        private String amount;
        private String textH5;
        private int isGet;
        private int isAd;
        private String content;


        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getIsAd() {
            return isAd;
        }

        public void setIsAd(int isAd) {
            this.isAd = isAd;
        }

        public int getIsGet() {
            return isGet;
        }

        public void setIsGet(int isGet) {
            this.isGet = isGet;
        }

        public String getTextH5() {
            return textH5;
        }

        public void setTextH5(String textH5) {
            this.textH5 = textH5;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCollect() {
            return collect;
        }

        public void setCollect(String collect) {
            this.collect = collect;
        }

        public String getAvgPoint() {
            return avgPoint;
        }

        public void setAvgPoint(String avgPoint) {
            this.avgPoint = avgPoint;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }
    }
}
