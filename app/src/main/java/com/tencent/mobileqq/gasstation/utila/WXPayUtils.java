package com.tencent.mobileqq.gasstation.utila;

import android.content.Context;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.mobileqq.gasstation.configer.Configer;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by WB on 2016/12/23.
 */

public class WXPayUtils {

    private static final String TAG = "WXPayUtils";
    private static WXPayUtils wxPayUtils = null;
    private IWXAPI api;

    public WXPayUtils(){
        super();
    }

    public static WXPayUtils getWxPayUtils(){
        if (wxPayUtils == null){
            synchronized (WXPayUtils.class){
                if (wxPayUtils == null){
                    return wxPayUtils = new WXPayUtils();
                }
            }
        }
        return wxPayUtils;
    }

    public boolean wxPay(Context context, String s){
        //genPayReq();//生成签名参数
        api = WXAPIFactory.createWXAPI(context, null);
        // 将该app注册到微信
        api.registerApp(Configer.APP_ID);
        boolean isLift = false;
        if(s != null) {
            isLift = sendPayReq(s);//调起支付
        }
        return isLift;
    }

    /**
     * 调起支付
     * @param wxPayJson
     */
    private boolean sendPayReq(String wxPayJson) {
        try {
            JSONObject json = new JSONObject(wxPayJson);
            PayReq req = new PayReq();
            req.appId = Configer.APP_ID;//1
            req.partnerId = json.getString("partnerid"); //1
            req.prepayId = json.getString("prepayid"); //1
            req.packageValue = json.getString("package");//1
            req.nonceStr= json.getString("noncestr");//1
            req.timeStamp = json.getString("timestamp");
            req.sign = json.getString("sign");
            req.extData = "app data"; // optional
            api.registerApp(Configer.APP_ID);
            boolean isSendSuccess = api.sendReq(req);
            return isSendSuccess;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

}
