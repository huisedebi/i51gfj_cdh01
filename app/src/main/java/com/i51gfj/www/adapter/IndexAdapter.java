package com.i51gfj.www.adapter;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.i51gfj.www.R;
import com.i51gfj.www.activity.WebViewActivity;
import com.i51gfj.www.banner.ADInfo;
import com.i51gfj.www.banner.ImageCycleView;
import com.i51gfj.www.fragment.BaseFragment;
import com.i51gfj.www.holder.IndexHolder;
import com.i51gfj.www.model.MainBean;
import com.i51gfj.www.util.AppUtil;
import com.i51gfj.www.util.Util;
import com.i51gfj.www.view.GlideImageLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 */
public class IndexAdapter extends RecyclerView.Adapter<IndexHolder>{

    private MainBean data;
    private BaseFragment fragment;
    private FragmentActivity mActivity;
    private IndexViewPagerAdapter viewPagerAdapter;
    private List<ImageView> dotList = new ArrayList<>();
    private int currentIndex;



    @Override
    public IndexHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        View view = null;
        switch (viewType) {
            case 0:
                //广告条
                view = layoutInflater.inflate(R.layout.item_list_banner_main, parent, false);
                break;
            case 1:
                view = layoutInflater.inflate(R.layout.item_list_index_two,parent,false);
                break;
            default:
                view = layoutInflater.inflate(R.layout.item_list_index_three,parent,false);
                break;
        }
        return new IndexHolder(view,fragment,viewType);
    }

    @Override
    public void onBindViewHolder(final IndexHolder holder, final int position) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = AppUtil.getNormalImageOptions();
        switch (position) {
            case 0:
                //fragment.infos = new ArrayList<ADInfo>();
                List<String > imgurls = new ArrayList<>();
                List<String > titles = new ArrayList<>();
                final List<String > urls = new ArrayList<>();
                for (int i = 0; i < data.getAdvs().size(); i++) {
                    imgurls.add(data.getAdvs().get(i).getImg());
                    titles.add(data.getAdvs().get(i).getTitle());
                    Log.i("Advs", "onBindViewHolder: "+data.getAdvs().get(i).getUrl()+"_____"+i);
                    urls.add(data.getAdvs().get(i).getUrl());
                }
                holder.banner.setImageLoader(new GlideImageLoader());
                holder.banner.setImages(imgurls);
                holder.banner.setBannerTitles(titles);
                holder.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
                holder.banner.setIndicatorGravity(BannerConfig.RIGHT);
                holder.banner.setOnBannerClickListener(new OnBannerClickListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        Log.i("url", "OnBannerClick: "+urls.get(position-1)+"___"+urls.get(position-1).equals(""));
                       if(!urls.get(position-1).equals("")){
                           Intent intent = new Intent(fragment.getActivity(), WebViewActivity.class);
                           intent.putExtra("url",urls.get(position-1));
                           fragment.getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left);
                           fragment.getActivity().startActivity(intent);
                       }
                    }
                });
                holder.banner.start();
                break;
            case 1:
                setViewPager(data.getMenu(), holder.viewPager, holder.dot_layout);
                holder.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        if (currentIndex == position) {
                            return;
                        }
                        dotList.get(position).setEnabled(false);
                        dotList.get(currentIndex).setEnabled(true);
                        currentIndex = position;
                    }
                    @Override
                    public void onPageSelected(int position) {

                    }
                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                break;
            default:
                if(position-2 == 0){
                    holder.rlout.setVisibility(View.VISIBLE);
                    holder.bg.setVisibility(View.GONE);
                }
                imageLoader.displayImage(data.getStore().get(position-2).getImg(),holder.big,options);
                holder.collect.setText(data.getStore().get(position - 2).getCollect());
                holder.distance.setText(data.getStore().get(position - 2).getDistance());
                holder.title.setText(data.getStore().get(position - 2).getName());
                holder.address.setText(data.getStore().get(position-2).getAddress());
                holder.img_star.setImageResource(Util.getStartResource(data.getStore().get(position-2).getAvgPoint()));
                holder.tv_where.setText(data.getText1());
                holder.img_frash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.tv_where.setText("定位中...");
                        fragment.onMyItemClick();
                    }
                });
                holder.shop_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment.onMyItemClick(data.getStore().get(position-2).getId());
                    }
                });
                break;
        }
    }

    private void setViewPager(final List<MainBean.MenuInMainBean> menu, ViewPager viewPager, LinearLayout dot_layout) {
        dotList.clear();
        dot_layout.removeAllViews();
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = AppUtil.getNormalImageOptions();
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        View view = null;
        int a  = menu.size()/8;
        a = menu.size()%8==0?a-1:a;
        Log.i("a", "setViewPager: "+a);
        List<View> viesList = new ArrayList<>();
        currentIndex =0;
        for (int i = 0; i <= a; i++) {
            ImageView dot =new ImageView(mActivity);//原点指标
            dot.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.dot_selecter));
            dot.setPadding(5, 0, 5, 0);
            dot.setEnabled(i==0?false:true);// 都设为灰色
            dotList.add(dot);
            dot_layout.addView(dot);
            view = layoutInflater.inflate(R.layout.item_index_two_circle, null);
            TextView[] tvlist = {(TextView) view.findViewById(R.id.tv_nav1), (TextView) view.findViewById(R.id.tv_nav2), (TextView) view.findViewById(R.id.tv_nav3), (TextView) view.findViewById(R.id.tv_nav4), (TextView) view.findViewById(R.id.tv_nav5), (TextView) view.findViewById(R.id.tv_nav6), (TextView) view.findViewById(R.id.tv_nav7), (TextView) view.findViewById(R.id.tv_nav8)};
            ImageView[] ivlist={(ImageView) view.findViewById(R.id.img_nav1),(ImageView) view.findViewById(R.id.img_nav2),(ImageView) view.findViewById(R.id.img_nav3),(ImageView) view.findViewById(R.id.img_nav4),(ImageView) view.findViewById(R.id.img_nav5),(ImageView) view.findViewById(R.id.img_nav6),(ImageView) view.findViewById(R.id.img_nav7),(ImageView) view.findViewById(R.id.img_nav8)};
            for (int j = 0; j < 8; j++) {
                if(menu.size()>i*8+j){
                    tvlist[j].setText(menu.get(i*8+j).getName());
                    imageLoader.displayImage(menu.get(i * 8 + j).getImg(), ivlist[j], options);
                    final int finalI = i;
                    final int finalJ = j;
                    ivlist[j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fragment.onMyItemClick(menu.get(finalI * 8 + finalJ).getId(), menu.get(finalI * 8 + finalJ).getUrl(), menu.get(finalI * 8 + finalJ).getName());
                        }
                    });
                }else{
                    break;
                }
            }
            viesList.add(view);
        }
        viewPager.setAdapter(new IndexViewPagerAdapter(viesList));

    }

    @Override
    public int getItemCount() {
        if(data==null){
            return 0;
        }else{
           return data.getStore().size()+2;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void setData(MainBean response) {
        this.data =response;
        notifyDataSetChanged();
    }

    public IndexAdapter(BaseFragment fragment, FragmentActivity mActivity) {
        this.fragment = fragment;
        this.mActivity = mActivity;
    }
}
