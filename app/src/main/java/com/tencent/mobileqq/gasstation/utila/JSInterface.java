package com.tencent.mobileqq.gasstation.utila;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.tencent.mobileqq.gasstation.inteface.JSOnclickInterface;

/**
 * Created by Zhangchen on 2018/3/5.
 */

public class JSInterface {
    private WebView webView;
    private JSOnclickInterface jsOnclickInterface;

    public JSInterface(WebView webView) {
        this.webView = webView;
        jsOnclickInterface = (JSOnclickInterface) webView.getContext();
    }

    //调取地图app
    @JavascriptInterface
    public void mapLocation(double x,double y){
        if (jsOnclickInterface != null){
            jsOnclickInterface.onClickLocation(x,y);
        }
    }

    @JavascriptInterface
    public void zxingClick(){
        if (jsOnclickInterface != null){
            jsOnclickInterface.onCLickZXing();
        }
    }

    //存储cookie信息
    @JavascriptInterface
    //andorid4.2（包括android4.2）以上，如果不写该注解，js无法调用android方法
    public void saveCookie(String json){
        if (jsOnclickInterface != null){
            jsOnclickInterface.onClickSaveCookei(json);
        }
    }

    @JavascriptInterface
    //获取经纬度
    public String getLocation(){
        if (jsOnclickInterface != null){
            return jsOnclickInterface.onClickGetLocation();
        }
        return null;
    }

    //调用相册
    @JavascriptInterface
    public void getPhoto(){
        if (jsOnclickInterface != null){
            jsOnclickInterface.onClickCamers();
        }
    }

    //注册成功弹窗
    @JavascriptInterface
    public void registDialog(){
        if (jsOnclickInterface != null){
            jsOnclickInterface.onClickRegistDialog();
        }
    }

    @JavascriptInterface
    public void back(){
        if (jsOnclickInterface != null){
            jsOnclickInterface.onClickRegistDialog();
        }
    }

    @JavascriptInterface
    public void delete(){
        if (jsOnclickInterface != null){
            jsOnclickInterface.onClickDeleteCookei();
        }
    }
}
