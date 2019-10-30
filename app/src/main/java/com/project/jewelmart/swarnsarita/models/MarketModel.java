package com.project.jewelmart.swarnsarita.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarketModel {

    @SerializedName("ack")
    @Expose
    private String ack;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("purpose")
        @Expose
        private String purpose;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("image_name")
        @Expose
        private String imageName;
        @SerializedName("path")
        @Expose
        private String path;

        public String getPurpose() {
            return purpose;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

    }

}
