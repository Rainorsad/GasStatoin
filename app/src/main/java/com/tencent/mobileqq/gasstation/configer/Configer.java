package com.tencent.mobileqq.gasstation.configer;

import android.os.Environment;

/**
 * Created by Zhangchen on 2018/2/28.
 */

public class Configer {
    /**
     * sd卡文件路径 -返回扩展存储区目录(SDCard)
     */
    public static final String FILE_MAIN_PATH = Environment.getExternalStorageDirectory().toString() + "/GasStation";
    /**
     * 图片地址
     */
    public static final String FILE_PIC_PATH = FILE_MAIN_PATH + "/image/";

    //登录h5链接
    public static final String LOGINHTTP = "http://118.190.152.119/app/html/driver/login.html";
    //司机端主界面链接
    public static final String DIVERMAINHTTP = "http://118.190.152.119/app/html/driver/index.html";

    /**
     微信支付id
     */
    public static final String APP_ID = "";
    public static final String WX_APP_KEY = "584043c98cf26598b76307f57441d41e"; //微信秘钥

    public static final String HTTP_MAIN = "";
    public static final String HTTP_LOGIN = "/driver/login/";
    public static final String HTTP_GETCODE = "/common/sms/sendValidateCode/";
    public static final String HTTP_PHONEALI = "/driver/checkMobile/";
    public static final String HTTP_CHECKIDENTITY = "/vender/checkIdCard/";
    public static final String HTTP_FORGET = "/driver/resetPwd/";
    public static final String HTTP_FILE = "/common/upload/uploadFile/";
}
