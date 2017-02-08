package com.i51gfj.www.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.i51gfj.www.R;
import com.i51gfj.www.holder.CountDetailHolder;
import com.i51gfj.www.holder.IndexHolder;
import com.i51gfj.www.model.CountDetailWrapper;


/**
 */
public class CountDetailAdapter extends RecyclerView.Adapter<CountDetailHolder>{

    private CountDetailWrapper data;
    private Context mActivity;

    public CountDetailAdapter(FragmentActivity activity) {mActivity=activity;
    }


    @Override
    public CountDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        return  new CountDetailHolder(layoutInflater.inflate(R.layout.item_count_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(CountDetailHolder holder, int position) {
            holder.tv_count.setText(data.getData().get(position).getAmount());
            holder.tv_intro.setText(data.getData().get(position).getDesText());
            holder.tv_time.setText(data.getData().get(position).getTimeText());
    }

    @Override
    public int getItemCount() {
        if(data==null || data.getData()==null){

            return 0;
        }
        return data.getData().size();
    }

    public void setData(CountDetailWrapper response) {
        data =response;
        notifyDataSetChanged();
    }
}
