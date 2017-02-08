package com.i51gfj.www.activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.i51gfj.www.R;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.model.SearchWrapper;
import com.i51gfj.www.util.ShpfUtil;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.Call;
import okhttp3.Response;

public class SearchStoreActivity extends Activity implements View.OnClickListener {

    private String search;

    private ListView listview_search;
    private MySearchAdapter adapter;

    private ArrayList<SearchWrapper.Data> searchDatas = new ArrayList<>();

    private TextView top_left;
    private TextView top_title;
    private TextView top_right;
    private EditText et_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
    }



    private void initView() {
        top_left = (TextView) findViewById(R.id.left_tv);
        top_title = (TextView) findViewById(R.id.title_tv);
        top_right = (TextView) findViewById(R.id.right_tv);
        et_search = (EditText)findViewById(R.id.et_search);
        et_search.addTextChangedListener(new EditChangedListener());

        listview_search = (ListView)findViewById(R.id.listview_search);
        listview_search.setVisibility(View.GONE);
        adapter = new MySearchAdapter();
        listview_search.setAdapter(adapter);
        listview_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchStoreActivity.this, ShopDetailActivity.class);
                intent.putExtra("passData", searchDatas.get(position).getId());
                startActivity(intent);

            }
        });

        top_left.setOnClickListener(this);
        top_title.setText("搜索");
    }

    // 完整的判断中文汉字和符号
    public boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    class MySearchAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if(searchDatas.size() != 0 && searchDatas != null){
                return searchDatas.size();
            }else{
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.item_search_list, null);
            TextView tv_search_result = (TextView)view.findViewById(R.id.tv_search_result);
            tv_search_result.setText(searchDatas.get(position).getName());
            return view;
        }
    }

    class EditChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            search = et_search.getText().toString().trim();
            if("".equals(search)){
                listview_search.setVisibility(View.GONE);
            }else{
                if(isChinese(search)){
                    post_search(search);
                    listview_search.setVisibility(View.VISIBLE);
//                    Toast.makeText(SearchActivity.this, search, Toast.LENGTH_LONG).show();
                }
            }
//            Log.e("search", "" + isChinese(search));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_tv:
                finish();
                break;
        }
    }

   /* keyword    *检索的关键词
    lng        *经纬度    极光请求里的经纬度
    lat        *经纬度    极光请求里的经纬度*/

    private void post_search(String str) {
        JSONObject json = new JSONObject();
        try {
            json.put("keyword", str);
            json.put("lng", ShpfUtil.getStringValue("lng"));
            json.put("lat", ShpfUtil.getStringValue("lat"));
        }catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        OkHttpUtils
                .postString()
                .url(MyURL.URL_SEARCH_STORE)
                .content(json.toString())
                .build()
                .execute(new Callback<SearchWrapper>() {
                    @Override
                    public SearchWrapper parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
//                        Log.e("json","" + json);
                        Log.e("parseNetworkResponse", "parseNetworkResponse");
                        return new Gson().fromJson(json, SearchWrapper.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e("onError", "onError");
                    }

                    @Override
                    public void onResponse(SearchWrapper response) {
                        Log.e("onResponse", "onResponse");
                        if (response.getStatus() == 1) {
                            searchDatas.clear();
                            searchDatas = (ArrayList<SearchWrapper.Data>)response.getData();
                            adapter.notifyDataSetChanged();
//                            Log.e("searchDatas1","" + searchDatas1.size());
                        }
                    }
                });
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
