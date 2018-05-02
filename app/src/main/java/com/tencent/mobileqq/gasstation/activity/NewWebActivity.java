package com.tencent.mobileqq.gasstation.activity;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alipay.sdk.app.H5PayCallback;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.tencent.mobileqq.gasstation.R;
import com.tencent.mobileqq.gasstation.utila.AtKeyBoardUp;
import com.tencent.mobileqq.gasstation.utila.JSInterface;

import butterknife.BindView;

public class NewWebActivity extends BaseActivity{

    @BindView(R.id.main_webview)
    WebView webView;

    private WebSettings webSettings;

    @Override
    protected int setLayout() {
        return R.layout.activity_mainweb;
    }

    @Override
    protected void setView() {
        webSettings = webView.getSettings();
        AtKeyBoardUp.assistActivity(this); //防止软件盘遮挡网页输入框内容

    }

    @Override
    protected void setDeal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        webView.setWebViewClient(webViewClient);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setBlockNetworkImage(false);//同步请求图片
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);//允许DCOM

        String url = getIntent().getExtras().getString("url");
        if (url != null){
            webView.loadUrl(url);
        }else {
            ToaL(NewWebActivity.this,"页面出现错误");
            this.finish();
        }

        /**
         * 打开js交互接口
         */
        webView.addJavascriptInterface(new JSInterface(webView), "gasstation");
        webView.setWebChromeClient(new WebChromeClient());
    }

    WebViewClient webViewClient = new WebViewClient() {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, WebResourceRequest request) {
            String url = request.getUrl()
                    .toString();
            if (!(url.startsWith("http") || url.startsWith("https"))) {
                return true;
            }
            final PayTask task = new PayTask(NewWebActivity.this);
            boolean isIntercepted = task.payInterceptorWithUrl(url, true, new H5PayCallback() {
                @Override
                public void onPayResult(final H5PayResultModel result) {
                    // 支付结果返回
                    final String url = result.getReturnUrl();
                    if (!TextUtils.isEmpty(url)) {
                        NewWebActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                view.loadUrl(url);
                            }
                        });
                    }
                }
            });

            /**
             * 判断是否成功拦截
             * 若成功拦截，则无需继续加载该URL；否则继续加载
             */
            if (!isIntercepted) {
                view.loadUrl(url);
            }
            return true;
        }
    };
}
