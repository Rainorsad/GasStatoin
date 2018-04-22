package com.tencent.mobileqq.gasstation.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.tencent.mobileqq.gasstation.bean.PayResult;

import org.kymjs.kjframe.ui.ViewInject;

import java.util.Map;

/**
 * Created by Zhangchen on 2018/3/1.
 */

public class AliPayAcitvity extends AppCompatActivity {

    private static final int SDK_PAY_FLAG = 1;
    private String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        info = extras.getString("result");
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e("支付宝调试",msg.toString());
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((String) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        ViewInject.toast("支付宝支付成功");
                        finish();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
//                        if (TextUtils.equals(resultStatus, "8000")) {
//                            Toast.makeText(AliPayAcitvity.this, "支付结果确认中",
//                                    Toast.LENGTH_SHORT).show();
//                            finish();
//                        } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//                            Toast.makeText(AliPayAcitvity.this,
//                                    "支付失败" + resultStatus, Toast.LENGTH_SHORT)
//                                    .show();
                        ViewInject.toast("支付宝支付失败");
                        finish();
                    }
                    break;
            }
        }

        ;
    };
    /**
     * 支付宝支付异步任务
     *
     * @author Simon
     */
    Runnable payRunnable = new Runnable() {

        @Override
        public void run() {
            PayTask alipay = new PayTask(AliPayAcitvity.this);
            Map<String, String> result = alipay.payV2(info, true);
            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        }
    };
}
