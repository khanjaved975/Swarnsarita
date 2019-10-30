package com.project.jewelmart.swarnsarita.pojo;

/**
 * Created by javedkhan on 11/27/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Acknowledge {

    @SerializedName("ack")
    @Expose
    private String ack;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getAck() {
        return ack;
    }

    public void setAck(String ack) {
        this.ack = ack;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
