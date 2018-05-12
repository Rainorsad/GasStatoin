package com.tencent.mobileqq.gasstation.bean;

import org.kymjs.kjframe.database.annotate.Table;

import java.io.Serializable;

/**
 * Created by Zhangchen on 2018/3/5.
 */

@Table(name="user_info_sql")
public class UserInfoBean implements Serializable{
    String loginName;
    String loginPwd;
    String id;
    String token;

    @Override
    public String toString() {
        return "UserInfoBean{" + "loginName='" + loginName + '\'' + ", loginPwd='" + loginPwd + '\'' + ", id='" + id + '\'' + ", token='" + token + '\'' + '}';
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
