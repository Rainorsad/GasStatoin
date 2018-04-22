package com.tencent.mobileqq.gasstation.http;

import org.kymjs.kjframe.http.HttpCallBack;

/**
 * Created by Zhangchen on 2018/3/7.
 */

public abstract class NetHttpCallBack extends HttpCallBack {
    public abstract void noNet();
}
