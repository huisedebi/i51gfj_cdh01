package com.i51gfj.www.model;

import java.util.List;

/**
 * Created by Administrator on 2016/8/20.
 */
public class ShopDetailCommentWrapper {
   private int status;
    private String star_dp_width_1;
    private String star_dp_width_2;
    private String star_dp_width_3;
    private String star_dp_width_4;
    private String star_dp_width_5;
    private String buy_dp_sum;
    private String buy_dp_avg;
    private String buy_dp_width;
    private String allow_dp;
    private String type;
    private String id;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStar_dp_width_1() {
        return star_dp_width_1;
    }

    public void setStar_dp_width_1(String star_dp_width_1) {
        this.star_dp_width_1 = star_dp_width_1;
    }

    public String getStar_dp_width_3() {
        return star_dp_width_3;
    }

    public void setStar_dp_width_3(String star_dp_width_3) {
        this.star_dp_width_3 = star_dp_width_3;
    }

    public String getStar_dp_width_2() {
        return star_dp_width_2;
    }

    public void setStar_dp_width_2(String star_dp_width_2) {
        this.star_dp_width_2 = star_dp_width_2;
    }

    public String getStar_dp_width_4() {
        return star_dp_width_4;
    }

    public void setStar_dp_width_4(String star_dp_width_4) {
        this.star_dp_width_4 = star_dp_width_4;
    }

    public String getStar_dp_width_5() {
        return star_dp_width_5;
    }

    public void setStar_dp_width_5(String star_dp_width_5) {
        this.star_dp_width_5 = star_dp_width_5;
    }

    public String getBuy_dp_sum() {
        return buy_dp_sum;
    }

    public void setBuy_dp_sum(String buy_dp_sum) {
        this.buy_dp_sum = buy_dp_sum;
    }

    public String getBuy_dp_avg() {
        return buy_dp_avg;
    }

    public void setBuy_dp_avg(String buy_dp_avg) {
        this.buy_dp_avg = buy_dp_avg;
    }

    public String getBuy_dp_width() {
        return buy_dp_width;
    }

    public void setBuy_dp_width(String buy_dp_width) {
        this.buy_dp_width = buy_dp_width;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAllow_dp() {
        return allow_dp;
    }

    public void setAllow_dp(String allow_dp) {
        this.allow_dp = allow_dp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    private List<Data> data;
    public  class  Data{
        private String id;
        private String create_time;
        private String content;
        private String reply_content;
        private String user_name;
        private String img;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getReply_content() {
            return reply_content;
        }

        public void setReply_content(String reply_content) {
            this.reply_content = reply_content;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
    public Page page;
    public class Page{
        private String page;
        private String page_total;
        private String page_size;
        private String data_total;

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
}
