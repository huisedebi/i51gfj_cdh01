package com.i51gfj.www.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.i51gfj.www.R;
import com.i51gfj.www.holder.IndexHolder;
import com.i51gfj.www.holder.MyCountHolder;
import com.i51gfj.www.model.MyCountBean;


/**
 */
public class MyCountAdapter extends RecyclerView.Adapter<MyCountHolder>{
    private MyCountBean data;
    private Activity mActivity;

    public MyCountAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public MyCountHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyCountHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_fragment_mycount,parent,false));
    }

    @Override
    public void onBindViewHolder(MyCountHolder holder, int position) {
        holder.tv_content.setText(data.getData().get(position).getDesText());
        holder.tv_cz.setText(data.getData().get(position).getTitle());
        holder.tv_num.setText(data.getData().get(position).getAmount());
        holder.tv_time.setText(data.getData().get(position).getTimeText());

    }

    @Override
    public int getItemCount() {
        if(data==null||data.getData()==null){
            return 0;
        }
        return data.getData().size();
    }

    public void  setData(MyCountBean data){
        this.data =data;
        notifyDataSetChanged();
    }
}
