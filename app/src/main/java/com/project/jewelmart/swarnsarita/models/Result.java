package com.project.jewelmart.swarnsarita.models;

/**
 * Created by javedkhan on 11/22/18.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("msg")
    @Expose
    private String msg;
//    @SerializedName("key")
//    @Expose
//    private String key;
    @SerializedName("ack")
    @Expose
    private int ack;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

//    public String getKey() {
//        return key;
//    }
//
//    public void setKey(String key) {
//        this.key = key;
//    }

    public int getAck() {
        return ack;
    }

    public void setAck(int ack) {
        this.ack = ack;
    }

}