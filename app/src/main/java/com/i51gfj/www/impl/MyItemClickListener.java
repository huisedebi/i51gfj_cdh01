package com.i51gfj.www.impl;

/**
 * item点击接口
 */
public interface MyItemClickListener {
    void onMyItemClick();
    void onMyItemClick(Object data);
    void onMyItemClick(Object data, int position);
    void onMyItemClick(String jump, int position);
    void onMyItemClick(String jump, String id);
    void onMyItemClick(String jump, String store_id, String id);
}
