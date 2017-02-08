package com.i51gfj.www.adapter;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.i51gfj.www.R;
import com.i51gfj.www.fragment.BaseFragment;
import com.i51gfj.www.fragment.MineMyCommentFragment;
import com.i51gfj.www.holder.IndexHolder;
import com.i51gfj.www.holder.MyCommentHolder;
import com.i51gfj.www.model.MyCommentBean;


/**
 */
public class MyCommentAdapter extends RecyclerView.Adapter<MyCommentHolder>{
    private MyCommentBean data;
    private MineMyCommentFragment fragment;
    private Activity mActivity;

    public MyCommentAdapter(MyCommentBean response, MineMyCommentFragment mineMyCommentFragment, FragmentActivity mActivity) {
        data =response;
        fragment =mineMyCommentFragment;
        this.mActivity = mActivity;
    }

    @Override
    public MyCommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MyCommentHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_mycomment,parent,false));
    }

    @Override
    public void onBindViewHolder(MyCommentHolder holder, final int position) {
        holder.tv_name.setText(data.getData().get(position).getName());
        holder.tv_content.setText(data.getData().get(position).getContent());
        holder.tv_time.setText(data.getData().get(position).getCreate_time());
       //holder.tv_name.setText(data.getData().get(position).getName());缺星星
        holder.layout_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.onMyItemClick(data.getData().get(position).getSid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.getData().size();
    }

    public void setData(MyCommentBean response) {
        this.data = response;
        notifyDataSetChanged();
    }
}
