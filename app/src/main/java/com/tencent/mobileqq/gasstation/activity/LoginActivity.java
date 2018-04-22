package com.tencent.mobileqq.gasstation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.tencent.mobileqq.gasstation.R;
import com.tencent.mobileqq.gasstation.bean.LoginParams;
import com.tencent.mobileqq.gasstation.bean.ResetPassParams;
import com.tencent.mobileqq.gasstation.bean.UserInfoBean;
import com.tencent.mobileqq.gasstation.db.UserInfoDb;
import com.tencent.mobileqq.gasstation.http.KJHttpUtil;

import org.kymjs.kjframe.http.HttpCallBack;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Zhangchen on 2018/2/27.
 */

public class LoginActivity extends BaseActivity {

    private static String TAG = "LoginActivity";

    @BindView(R.id.rela_titlemain)
    RelativeLayout relaTitlemain;
    @BindView(R.id.img_exit)
    ImageView imgExit;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    //登陆主体
    @BindView(R.id.lin_loginmain)
    LinearLayout linLoginmain;
    @BindView(R.id.edit_phone)
    EditText editPhone; //企业账号
    @BindView(R.id.edit_pass)
    EditText editPass; //账号密码
    @BindView(R.id.login_textregist)
    TextView loginTextregist; //注册
    @BindView(R.id.login_textforget)
    TextView loginTextforget; //忘记密码
    @BindView(R.id.login_but)
    Button loginBut; //确定按钮


    //忘记密码主体
    @BindView(R.id.lin_forgetmain)
    LinearLayout linForgetmain;
    @BindView(R.id.edit_forgetphone)
    EditText editForgetphone; //忘记的账号
    @BindView(R.id.edit_forgetcode)
    EditText editForgetcode; //验证码
    @BindView(R.id.forget_butcode)
    Button forgetButcode;  //获取验证码按钮
    @BindView(R.id.edit_forgetidentitycode)
    EditText editForgetidentitycode; //身份证号
    @BindView(R.id.edit_forgetnewpass)
    EditText editForgetnewpass; //新密码
    @BindView(R.id.edit_forgetsurepass)
    EditText editForgetsurepass; //确认新密码
    @BindView(R.id.forget_but)
    Button forgetBut;  //确认按钮

    private UserInfoDb db;
    private LocationClient mLocationClient;
    private BDLocationListener mBDLocaListener;
    private Context context;


    @Override
    protected int setLayout() {
        context = this;
        return R.layout.activity_login;
    }

    @Override
    protected void setView() {
        loginModel();
    }


    @Override
    protected void setDeal() {
        autoLogin(); //自动登录
        getLocation(); //获得定位信息
    }

    /**
     * 获得定位信息
     */
    private void getLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        mLocationClient = new LocationClient(this);
        mBDLocaListener = new MyBDLocationListener();
        mLocationClient.registerLocationListener(mBDLocaListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("db0911");
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        option.setNeedDeviceDirect(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    private class MyBDLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null){
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String address = location.getAddrStr();
                Log.e(TAG, "address:" + address + " latitude:" + latitude
                        + " longitude:" + longitude + "---");
                String jingdu = String.valueOf(location.getLongitude());
                String weidu = String.valueOf(location.getLatitude());
                if (mLocationClient.isStarted()) {
                    // 获得位置之后停止定位
                    mLocationClient.stop();
                }
            }
        }
    }

    /**
     * 自动登录
     */
    private void autoLogin() {
        db = new UserInfoDb(this);
        List<UserInfoBean> dao = db.getData(UserInfoBean.class);
        Log.d("LoginActivityc",dao.toString());
        if (dao != null && dao.size() > 0) {
            UserInfoBean userInfoBean = dao.get(0);
            if (userInfoBean != null) {
                Log.e(TAG, userInfoBean.toString());
            }
        }
    }

    @OnClick({R.id.img_exit, R.id.login_but, R.id.forget_butcode, R.id.forget_but
            , R.id.login_textforget, R.id.login_textregist})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_exit:
                //退回操作
                loginModel();
                break;
            case R.id.login_textforget:
                //忘记密码
                forgetModel();
                break;
            case R.id.login_textregist:
                //注册模块
                Intent it = new Intent(LoginActivity.this,RegistActivity.class);
                startActivity(it);
                break;
            case R.id.login_but:
                //登录确定
                login();
                //支付宝支付
//                Intent its = new Intent(LoginActivity.this,AliPayAcitvity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("result", "ss");
//                its.putExtras(bundle);
//                startActivity(its);

//                微信支付
//                WXPayUtils.getWxPayUtils().wxPay(context, "ss");

                break;
            case R.id.forget_butcode:
                //获得验证码
                shopCode();
                break;
            case R.id.forget_but:
                //忘记密码确定
                forgetSure();
                break;
        }
    }


    /**
     * 登录模块
     */
    private void loginModel() {
        linLoginmain.setVisibility(View.VISIBLE);
        linForgetmain.setVisibility(View.GONE);
        relaTitlemain.setVisibility(View.INVISIBLE);

    }

    /**
     * 忘记密码模块
     */
    private void forgetModel() {
        linLoginmain.setVisibility(View.GONE);
        linForgetmain.setVisibility(View.VISIBLE);
        relaTitlemain.setVisibility(View.VISIBLE);
        tvTitle.setText("登录密码修改");
    }

    /**
     * 登录逻辑
     */
    private void login() {
        String loginname = editPhone.getText().toString();
        String loginpass = editPass.getText().toString();
        if (TextUtils.isEmpty(loginname) || TextUtils.isEmpty(loginpass)){
            ToaS(LoginActivity.this,"账号或者密码不能为空");
        }else {
            LoginParams.BodyBean bodyBean = new LoginParams.BodyBean(loginname,loginpass);
            LoginParams params = new LoginParams(bodyBean);
            String s = JSON.toJSONString(params);
            KJHttpUtil.posthttpLogin(context,s,loginCallback);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mBDLocaListener);
        }
    }

    /**
     * 商家验证码
     */
    private void shopCode() {
        TextTimeCount time = new TextTimeCount(60000, forgetButcode);
        time.start();
        String phone = editForgetphone.getText().toString();
        if (TextUtils.isEmpty(phone)){
            ToaS(context,"请输入手机号");
        }else {
            KJHttpUtil.getPhoneAli(context,phone,getCheckPhone);
        }
    }

    /**
     * 验证码倒计时
     */
    class TextTimeCount extends CountDownTimer {
        private Button tv;

        public TextTimeCount(long millisInFuture, Button tv) {
            super(millisInFuture, 1000);
            this.tv = tv;
        }

        @Override
        public void onFinish() {// 计时完毕
            tv.setText("获取验证码");
            tv.setTextColor(getResources().getColor(R.color.A1A1A1));
            tv.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            tv.setClickable(false);//防止重复点击
            tv.setTextColor(getResources().getColor(R.color.red));
            tv.setText( millisUntilFinished / 1000 + "秒" );
        }
    }

    /**
     * 忘记密码确定
     */
    private String identitycode;
    private String phone;
    private String code;
    private String pass;
    private ResetPassParams resetPassParams;
    private void forgetSure() {
        phone = editForgetphone.getText().toString();
        code = editForgetcode.getText().toString();
        identitycode = editForgetidentitycode.getText().toString();
        pass = editForgetnewpass.getText().toString();
        String surepa = editForgetsurepass.getText().toString();
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(code) || TextUtils.isEmpty(identitycode) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(surepa)){
            ToaS(context,"请完善注册信息");
        }else if (!pass.equals(surepa)){
            ToaS(context,"两次密码不一致");
        }else {
            resetPassParams = new ResetPassParams(phone,code,identitycode,pass);
            KJHttpUtil.getCheckIdentity(context,identitycode,identityCallback);
        }
    }


    /**
     * 登录回调
     */
    HttpCallBack loginCallback = new HttpCallBack() {
        @Override
        public void onSuccess(String t) {
            String retCode = JSON.parseObject(t).getString("retCode");
            if (retCode.equals("0")){
                ToaS(context,"登陆成功");
            }else {
                ToaS(context,JSON.parseObject(t).getString("retMsg"));
            }
        }

        @Override
        public void onFailure(int errorNo, String strMsg) {
            super.onFailure(errorNo, strMsg);
        }
    };

    /**
     * 获得验证码回调
     */
    HttpCallBack getcodeCallback = new HttpCallBack() {
        @Override
        public void onSuccess(String t) {
            String code = JSON.parseObject(t).getString("retCode");
            if (code.equals("0")){
                ToaS(context,"验证码已发送，请注意查收");
            }
        }

        @Override
        public void onFailure(int errorNo, String strMsg) {
            super.onFailure(errorNo, strMsg);
        }
    };

    /**
     * 手机验证回调
     */
    HttpCallBack getCheckPhone = new HttpCallBack() {
        @Override
        public void onSuccess(String t) {
            String code = JSON.parseObject(t).getString("retCode");
            if (code.equals("0")){
                KJHttpUtil.getMissPassCode(context,phone,getcodeCallback);
            }else {
                ToaS(context,"手机号不存在");
            }
        }

        @Override
        public void onFailure(int errorNo, String strMsg) {
            super.onFailure(errorNo, strMsg);
        }
    };

    /**
     * 身份验证回调
     */
    HttpCallBack identityCallback = new HttpCallBack() {
        @Override
        public void onSuccess(String t) {
            String retCode = JSON.parseObject(t).getString("retCode");
            if (retCode.equals("0")){
                String s = JSON.toJSONString(resetPassParams);
                KJHttpUtil.postForget(context,s,forgetCallback);
            }else {
                ToaS(context,"身份证号不存在");
            }
        }

        @Override
        public void onFailure(int errorNo, String strMsg) {
            super.onFailure(errorNo, strMsg);
        }
    };

    /**
     * 修改密码回调
     */
    HttpCallBack forgetCallback = new HttpCallBack() {
        @Override
        public void onSuccess(String t) {
            String retCode = JSON.parseObject(t).getString("retCode");
            if (retCode.equals("0")){
                ToaS(context,"修改成功");
                loginModel();
            }else {
                ToaS(context,JSON.parseObject(t).getString("retMsg"));
            }
        }

        @Override
        public void onFailure(int errorNo, String strMsg) {
            super.onFailure(errorNo, strMsg);
        }
    };

}

