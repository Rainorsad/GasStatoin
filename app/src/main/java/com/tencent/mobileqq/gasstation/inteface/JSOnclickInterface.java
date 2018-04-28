package com.tencent.mobileqq.gasstation.inteface;

/**
 * Created by Zhangchen on 2018/3/5.
 * js原生交互
 */

public interface JSOnclickInterface {

    void onClickCamers(); //打开相册

    void onClickLocation(double x,double y); //调取地图

    String onClickGetLocation(); //获取经纬度

    void onClickSaveCookei(String s); //存储注册信息

    void onClickRegistDialog(); //注册成功弹窗

    void onClickDeleteCookei(); //删除缓存

    void onCLickZXing(); //二维码扫码

}
