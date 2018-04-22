package com.tencent.mobileqq.gasstation.bean;

/**
 * Created by Zhangchen on 2018/3/8.
 */

public class LoginParams {
    public LoginParams(BodyBean body) {
        this.body = body;
    }

    /**
     * body : {"loginName":"13001168433","loginPwd":"123456&@"}
     */

    private BodyBean body;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean {
        public BodyBean(String loginName, String loginPwd) {
            this.loginName = loginName;
            this.loginPwd = loginPwd;
        }

        /**
         * loginName : 13001168433
         * loginPwd : 123456&@
         */

        private String loginName;
        private String loginPwd;

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
    }
}
