package com.tconan.android.only;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 当前年月的地址 http://www.ruanyifeng.com/blog/2018/05/
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                String url = String.format(Locale.US, "http://www.ruanyifeng.com/blog/%d/%02d/", year, month);

                Intent intent = new Intent(MainActivity.this, X5WebViewActivity.class);
                intent.putExtra(X5WebViewActivity.PARAM_URL, url);
                MainActivity.this.startActivity(intent);
            }
        });

        findViewById(R.id.btn_weibo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSsoHandler = new SsoHandler(MainActivity.this);
                mSsoHandler.authorize(new SelfWbAuthListener());
            }
        });

        AuthInfo mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        WbSdk.install(this, mAuthInfo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    public SsoHandler mSsoHandler;
    public Oauth2AccessToken mAccessToken;
    private class SelfWbAuthListener implements com.sina.weibo.sdk.auth.WbAuthListener{
        @Override
        public void onSuccess(final Oauth2AccessToken token) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAccessToken = token;
                    if (mAccessToken.isSessionValid()) {
                        // 显示 Token
                        // updateTokenView(false);
                        // 保存 Token 到 SharedPreferences
                        AccessTokenKeeper.writeAccessToken(MainActivity.this, mAccessToken);
                        Utils.toast("登录成功");
                    }
                }
            });
        }

        @Override
        public void cancel() {
            Utils.toast("取消登录");
        }

        @Override
        public void onFailure(WbConnectErrorMessage errorMessage) {
            Utils.toast(errorMessage.getErrorMessage());
        }
    }
}
