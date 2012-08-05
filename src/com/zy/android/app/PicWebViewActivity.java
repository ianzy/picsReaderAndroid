package com.zy.android.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItem;
import android.webkit.WebView;

import com.zy.android.R;
import com.zy.android.utils.PhotoWebViewClient;

public class PicWebViewActivity extends FragmentActivity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pic);
        
        String url = this.getIntent().getStringExtra("url");
        WebView mWebView = (WebView) findViewById(R.id.webview);
        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new PhotoWebViewClient());
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            	this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
