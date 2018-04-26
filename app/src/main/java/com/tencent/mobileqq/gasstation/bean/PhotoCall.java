package com.tencent.mobileqq.gasstation.bean;

public class PhotoCall {

    /**
     * retCode : 0
     * retMsg : 成功
     * data : /upload/head_img_path/20180421231614685/2ba72ec9fc218159f8f11cfc25e27cf24790adad0f9ff67cf7c5916892ed025a.jpg
     */

    private String retCode;

    private String retMsg;

    private String data;

    @Override
    public String toString() {
        return "PhotoCall{" + "retCode='" + retCode + '\'' + ", retMsg='" + retMsg + '\'' + ", data='" + data + '\'' + '}';
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
