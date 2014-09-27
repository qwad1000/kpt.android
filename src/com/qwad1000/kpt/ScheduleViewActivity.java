package com.qwad1000.kpt;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

/**
 * Created by Сергій on 27.09.2014.
 */
public class ScheduleViewActivity extends Activity {
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.web_view);

        mWebView = (WebView) findViewById(R.id.webView);
        Bundle extras = getIntent().getExtras();
        String url = extras.getString("transport_schedule_website");
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setBuiltInZoomControls(true);


        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.loadUrl(url);
    }


}
