package com.tconan.android.only;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class X5WebViewActivity extends AppCompatActivity {

    public final static String PARAM_URL = "URL";

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        setContentView(R.layout.activity_x5_web_view);

        webView = findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);

        // 添加本地存储支持
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        webView.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);

        // 设置chromeClient
        webView.setWebChromeClient(new WebChromeClient());

        // 支持地址拦截
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                webView.loadUrl(url);
                return super.shouldOverrideUrlLoading(webView, url);
            }
        });

        String path = getIntent().getStringExtra(X5WebViewActivity.PARAM_URL);
        if (!TextUtils.isEmpty(path)) {
            webView.loadUrl(path);
        } else {
            Utils.toast("参数 url 无效");
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.webview_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.close == item.getItemId()) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            } else {
                // moveTaskToBack(false);
                // finish();
                Utils.toast("已回到首页");
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
