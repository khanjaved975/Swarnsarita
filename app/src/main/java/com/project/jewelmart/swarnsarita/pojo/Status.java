package com.project.jewelmart.swarnsarita.pojo;

/**
 * Created by javedkhan on 11/27/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("ack")
    @Expose
    private Integer ack;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getAck() {
        return ack;
    }

    public void setAck(Integer ack) {
        this.ack = ack;
    }

}
