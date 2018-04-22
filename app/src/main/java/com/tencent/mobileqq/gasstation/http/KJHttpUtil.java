package com.tencent.mobileqq.gasstation.http;

import android.content.Context;

import com.tencent.mobileqq.gasstation.configer.Configer;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.SystemTool;

import java.io.File;

public class KJHttpUtil {

    private static KJHttp kjh;
    private static long time = System.currentTimeMillis();

    private static KJHttp getKjHttp() {
        if (kjh == null) {
            //超时时间
            HttpConfig.TIMEOUT = 1000 * 45;
            kjh = new KJHttp();
        }
        return kjh;
    }

    /**
     * 框架中的get请求
     *
     * @param context     上下文
     * @param url         接口
     * @param httpParams  参数
     * @param isCache     是否缓存
     * @param CallBack    回调
     */
    public static void getHttp(Context context, String url, String  httpParams, boolean isCache,
                               HttpCallBack CallBack) {
        if (SystemTool.checkNet(context)) {//检测网络
            getKjHttp().get(url + httpParams, null, isCache, CallBack);
        } else {
            long nowTime = System.currentTimeMillis();
            if (nowTime - time > 3000) {
                ViewInject.toast("网络不可用，请检查网络设置");
                time = System.currentTimeMillis();
            }
            //  没有网路
            if (CallBack != null && CallBack instanceof NetHttpCallBack) {
                ((NetHttpCallBack) CallBack).noNet();
            }

        }
    }

    public static void postHttp(Context context, String url, String httpParams, boolean isCache,
                                HttpCallBack CallBack) {
        if (SystemTool.checkNet(context)) {//检测网络
            getKjHttp().post(url + httpParams, null, isCache, CallBack);
        } else {
            long nowTime = System.currentTimeMillis();
            if (nowTime - time > 3000) {
                ViewInject.toast("网络不可用，请检查网络设置");
                time = System.currentTimeMillis();
            }
        }

    }

    /**
     * 取消所有url请求
     */
    public static void cancelAndStop() {
        kjh.cancelAll();
    }

    /**
     * 登录接口
     */
    public static void posthttpLogin(Context context, String params, HttpCallBack callBack) {
        postHttp(context, Configer.HTTP_MAIN + Configer.HTTP_LOGIN , params, false, callBack);
    }

    /**
     * 忘记密码请求验证码
     */
    public static void getMissPassCode(Context context,String params,HttpCallBack callBack){
        getHttp(context,Configer.HTTP_MAIN + Configer.HTTP_GETCODE,params,false,callBack);
    }

    /**
     * 验证手机号是否存在
     */
    public static void getPhoneAli(Context context,String params,HttpCallBack callBack){
        getHttp(context,Configer.HTTP_MAIN + Configer.HTTP_PHONEALI,params,false,callBack);
    }

    /**
     * 验证身份证号是否存在
     */
    public static void getCheckIdentity(Context context,String params,HttpCallBack callBack){
        getHttp(context,Configer.HTTP_MAIN + Configer.HTTP_CHECKIDENTITY,params,false,callBack);
    }

    /**
     * 判断密码是否修改成功
     */
    public static void postForget(Context context,String params,HttpCallBack callBack){
        postHttp(context,Configer.HTTP_MAIN + Configer.HTTP_FORGET,params,false,callBack);
    }

    /**
     * 上传文件
     */
    public static void postFile(Context context,File file,HttpCallBack callBack){
        HttpParams params = new HttpParams();
        params.put("file",file);
        getKjHttp().post(Configer.HTTP_MAIN + Configer.HTTP_FILE,params,false,callBack);

    }
}
