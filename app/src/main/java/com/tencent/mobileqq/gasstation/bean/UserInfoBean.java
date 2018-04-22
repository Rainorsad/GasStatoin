package com.tencent.mobileqq.gasstation.bean;

import org.kymjs.kjframe.database.annotate.Table;

import java.io.Serializable;

/**
 * Created by Zhangchen on 2018/3/5.
 */

@Table(name="user_info_sql")
public class UserInfoBean implements Serializable{
    @Override
    public String toString() {
        return "UserInfoBean{" +
                "id=" + id +
                "pass=" + password +
                "userid=" + userid +
                '}';
    }

    private int id; //用来区别用户id
    private String password;
    private String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
