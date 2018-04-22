package com.tencent.mobileqq.gasstation.bean;

public class Location {
    String jingdu;
    String weidu;

    public Location(String jingdu, String weidu) {
        this.jingdu = jingdu;
        this.weidu = weidu;
    }

    public String getJingdu() {
        return jingdu;
    }

    public void setJingdu(String jingdu) {
        this.jingdu = jingdu;
    }

    public String getWeidu() {
        return weidu;
    }

    public void setWeidu(String weidu) {
        this.weidu = weidu;
    }
}
