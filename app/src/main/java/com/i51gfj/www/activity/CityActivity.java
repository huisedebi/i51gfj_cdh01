package com.i51gfj.www.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import com.i51gfj.www.R;
import com.i51gfj.www.adapter.SortAdapter;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.dialog.LoadingDialog;
import com.i51gfj.www.fragment.IndexFragment;
import com.i51gfj.www.model.CityWrapper;
import com.i51gfj.www.util.ShpfUtil;
import com.i51gfj.www.util.Util;
import com.i51gfj.www.view.sortlistview.CharacterParser;
import com.i51gfj.www.view.sortlistview.ClearEditText;
import com.i51gfj.www.view.sortlistview.PinyinComparator;
import com.i51gfj.www.view.sortlistview.SideBar;
import com.i51gfj.www.view.sortlistview.SortModel;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 选择城市
 */
public class CityActivity extends Activity {
    private ListView sortListView;
    private SortAdapter adapter;
    protected LoadingDialog mloadingDialog;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private String errorflag = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        //获取数据
        //post_city();
//        //初始化控件
//        initViews(data);
        CityWrapper data = new CityWrapper();
        List<String > citylist = Util.cityList;

        data.setData(citylist);
        initViews(data);
    }

    /*private void post_city() {
        Util.showMyLoadingDialog(mloadingDialog,this);
        OkHttpUtils
                .postString()
                .url(MyURL.URL_CITY_LIST)
                .content("")
                .build()
                .execute(new Callback<CityWrapper>() {
                    @Override
                    public CityWrapper parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, CityWrapper.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        Util.dissMyLoadingDialog(mloadingDialog);
                        if (StringUtils.isEmpty(errorflag)) {
                            post_city();
                            errorflag = "error";
                        }
                    }

                    @Override
                    public void onResponse(CityWrapper response) {
                        if (response.getStatus() == 1) {
                            initViews(response);
                        }
                        Util.dissMyLoadingDialog(mloadingDialog);
                    }
                });
    }
*/

    private void initViews(final CityWrapper data) {
        TextView top_left = (TextView) findViewById(R.id.left_tv);
        TextView top_title = (TextView) findViewById(R.id.title_tv);
        top_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        top_left.setText("");
        top_title.setText("选择城市");

        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        SideBar sideBar = (SideBar) findViewById(R.id.sidrbar);
        TextView dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
//                Toast.makeText(getApplication(), adapter.getItem(position), Toast.LENGTH_SHORT).show();
                if (position != 0) {
                    ShpfUtil.setValue("city_name", adapter.getItem(position));
                } else {
                    ShpfUtil.setValue("city_name", ShpfUtil.getStringValue("location_city"));
                }
               // ShpfUtil.setValue("last_city", adapter.getItem(position));
                Intent intent = new Intent(CityActivity.this, IndexFragment.class);
                intent.putExtra("city",adapter.getItem(position));
                setResult(92014, intent);
                finish();
            }
        });

        SourceDateList = filledData(data.getData());

        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        if(data.getLast_call_city() == null){
            List<String> stringList = new ArrayList<>();
            stringList.add("");
            data.setLast_call_city(stringList);
        }
        adapter = new SortAdapter(this, SourceDateList, data.getLast_call_city());
        adapter.setData(data);
        sortListView.setAdapter(adapter);


        ClearEditText mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    /**
     * 为ListView填充数据
     *
     * @param data List<String>
     * @return List<SortModel>
     */
    private List<SortModel> filledData(List<String> data) {
        List<SortModel> mSortList = new ArrayList<>();
        for (String city : data) {
            if (city.equals("")) {
                continue;
            }
            SortModel sortModel = new SortModel();
            sortModel.setName(city);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(city);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr string
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.contains(filterStr) || characterParser.getSelling(name).startsWith(filterStr)) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
