package com.i51gfj.www.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.i51gfj.www.R;
import com.i51gfj.www.banner.ImageCycleView;
import com.i51gfj.www.fragment.RedDetaileFragment;
import com.i51gfj.www.holder.IndexHolder;
import com.i51gfj.www.holder.RedDetailHolder;
import com.i51gfj.www.holder.RedListHolder;
import com.i51gfj.www.model.RedDetailBean;
import com.i51gfj.www.model.RedListBean;
import com.i51gfj.www.util.AppUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 */
public class RedDetailAdapter extends RecyclerView.Adapter<RedDetailHolder>{


    private RedDetailBean data;
    FragmentActivity mActivity;
    RedDetaileFragment fragment;

    public RedDetailAdapter(FragmentActivity mActivity, RedDetaileFragment redDetaileFragment) {
        this.mActivity = mActivity;
        fragment = redDetaileFragment;
    }

    @Override
    public RedDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        if(viewType==data.getImgList().size()){
            view =layoutInflater.inflate(R.layout.item_red_detail_two,parent,false);
        }else{
            view = layoutInflater.inflate(R.layout.item_red_detail_one,parent,false);
        }
        return  new RedDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(RedDetailHolder holder, int position) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = AppUtil.getNormalImageOptions();
        if(position==data.getImgList().size()){
            holder.tv_remark.setText(data.getData().getRemark());
            holder.tv_money.setText("￥"+data.getData().getAmount());
            if(data.getData().getPhone()==null || data.getData().getPhone().equals("")){
                holder.layout_phone.setVisibility(View.GONE);
            }else{
                holder.tv_phone.setText(data.getData().getPhone());
                holder.tv_phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri num = Uri.parse("tel:" + data.getData().getPhone());
                        intent.setData(num);
                        mActivity.startActivity(intent);
                    }
                });
            }
            if( data.getIsGet().equals("0")){
                holder.bt_yilingqu.setVisibility(View.VISIBLE);
                holder.bt_lingqu.setVisibility(View.GONE);
            }else{
                holder.bt_yilingqu.setVisibility(View.GONE);
                holder.bt_lingqu.setVisibility(View.VISIBLE);
                holder.bt_lingqu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment.onMyItemClick(data.getData().getId());
                    }
                });
            }

        }else{
            WindowManager wm = mActivity.getWindowManager();
            int width = wm.getDefaultDisplay().getWidth();
            ViewGroup.LayoutParams lp = holder.img_main.getLayoutParams();
            lp.width = width;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.img_main.setLayoutParams(lp);
            holder.img_main.setMaxWidth(width);
            holder.img_main.setMaxHeight(width*data.getImgList().get(position).getH()/data.getImgList().get(position).getW());
            Log.i("Img", "onBindViewHolder: width__"+width);
            Log.i("Img", "onBindViewHolder:w__ "+data.getImgList().get(position).getW());
            Log.i("Img", "onBindViewHolder:h__ "+data.getImgList().get(position).getH());
            Log.i("Img", "onBindViewHolder:height__ "+width*data.getImgList().get(position).getH()/data.getImgList().get(position).getW());
            if(position==0){
                holder.tv_title.setVisibility(View.VISIBLE);
                holder.line.setVisibility(View.VISIBLE);
                holder.tv_title.setText(data.getData().getTitle());
            }else {
                holder.tv_title.setVisibility(View.GONE);
                holder.line.setVisibility(View.GONE);
            }

            imageLoader.displayImage(data.getImgList().get(position).getImg(),holder.img_main,options);
        }
    }

    @Override
    public int getItemCount() {
        if(data!=null && data.getImgList()!=null){
            return data.getImgList().size()+1;
        }
        return 0;
    }

    public void setData(RedDetailBean data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
      return position;
    }
}
