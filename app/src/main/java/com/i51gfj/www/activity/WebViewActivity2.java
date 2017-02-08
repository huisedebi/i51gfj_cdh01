package com.i51gfj.www.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.i51gfj.www.R;


public class WebViewActivity2 extends FragmentActivity implements View.OnClickListener {
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
        webview.setWebViewClient(new WebViewClient());
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        title.setText(store_name==null?"广告":store_name);
        if(url!=null){
            webview.loadUrl(url);
            findViewById(R.id.left_tv).setOnClickListener(this);
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
