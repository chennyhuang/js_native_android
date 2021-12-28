package com.example.application1;

import androidx.appcompat.app.AlertDialog;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.application1.utils.StatusBarUtil;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button btn1;
    private Button btn2;
    private WebView webview;
    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置透明状态栏
        StatusBarUtil.transparencyBar(this);
        //设置状态栏字体黑色
        StatusBarUtil.setLightStatusBar(this,true,true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        webview = findViewById(R.id.web_view);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        //加载本地html
        webview.loadUrl("file:///android_asset/web/index.html");
        //webview注入 android 对象
        webview.addJavascriptInterface(MainActivity.this,"android");
        webview.setWebViewClient(new WebViewClient() {
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button1) {
            webview.evaluateJavascript("javascript:nativeCalljs1(" + "'我是原生参数'" + ")", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String s) {
                    Log.i("webCallBack","nativeCalljs1 callback : " + s);
                }
            });
        } else if (v.getId() == R.id.button2) {
            webview.evaluateJavascript("javascript:nativeCalljs0()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String s) {
                    //js 方法的返回值
                    Log.i("webCallBack","nativeCalljs0 callback : " + s);
                }
            });
        }
    }

    @JavascriptInterface
    public void nativeMethod() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("js调起的原生弹出框")
                .setMessage("js传过来的参数：")
                .setPositiveButton("确定", null)
                .show();

    }

    @JavascriptInterface
    public void nativeMethod(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("js调起的原生弹出框")
                .setMessage("js传过来的参数：" + str)
                .setPositiveButton("确定", null)
                .show();
    }
}