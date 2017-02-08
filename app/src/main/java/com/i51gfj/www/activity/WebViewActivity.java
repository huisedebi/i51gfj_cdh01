package com.i51gfj.www.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.i51gfj.www.R;
import com.umeng.analytics.MobclickAgent;


public class WebViewActivity extends FragmentActivity implements View.OnClickListener {
    WebView webview;
    String url,store_name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initData();
        initView();
    }

    private void initData() {
        url = getIntent().getStringExtra("url");
        store_name = getIntent().getStringExtra("title");
        Log.i("tag", "initData: "+url);
    }

    private void initView() {
        webview = (WebView) findViewById(R.id.webview);
        TextView title = (TextView) findViewById(R.id.title_tv);
        findViewById(R.id.left_tv).setOnClickListener(this);
        webview.setWebViewClient(new WebViewClient());
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        title.setText(store_name==null?"广告":store_name);
        if(url!=null){
            webview.loadUrl(url);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_tv:
                final ViewGroup viewGroup = (ViewGroup) webview.getParent();
                if (viewGroup != null)
                {
                    viewGroup.removeView(webview);
                }
                webview.removeAllViews();
                webview.destroy();
                finish();
                break;
        }
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
