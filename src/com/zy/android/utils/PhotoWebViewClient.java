package com.zy.android.utils;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PhotoWebViewClient extends WebViewClient {

	@Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return true;
	}

}
