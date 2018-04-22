package com.tencent.mobileqq.gasstation.bean;

/**
 * Created by Zhangchen on 2018/3/8.
 */

public class ResetPassParams {
    public ResetPassParams(String mobile, String validateCode, String dCard, String loginPwd) {
        this.mobile = mobile;
        this.validateCode = validateCode;
        this.dCard = dCard;
        this.loginPwd = loginPwd;
    }

    /**
     * mobile : 13001168433
     * validateCode : 123456&@
     * dCard : 123
     * loginPwd : 123
     */

    private String mobile;
    private String validateCode;
    private String dCard;
    private String loginPwd;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public String getDCard() {
        return dCard;
    }

    public void setDCard(String dCard) {
        this.dCard = dCard;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }
}
