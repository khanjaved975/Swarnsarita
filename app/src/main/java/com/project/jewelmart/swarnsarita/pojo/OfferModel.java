package com.project.jewelmart.swarnsarita.pojo;

/**
 * Created by javedkhan on 2/13/19.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfferModel {

    @SerializedName("ack")
    @Expose
    private Integer ack;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private Data data;

    public Integer getAck() {
        return ack;
    }

    public void setAck(Integer ack) {
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

    public class GiftCoupan {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("coupan_name")
        @Expose
        private String coupanName;
        @SerializedName("weight_from")
        @Expose
        private String weightFrom;
        @SerializedName("weight_to")
        @Expose
        private String weightTo;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("image")
        @Expose
        private String image;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCoupanName() {
            return coupanName;
        }

        public void setCoupanName(String coupanName) {
            this.coupanName = coupanName;
        }

        public String getWeightFrom() {
            return weightFrom;
        }

        public void setWeightFrom(String weightFrom) {
            this.weightFrom = weightFrom;
        }

        public String getWeightTo() {
            return weightTo;
        }

        public void setWeightTo(String weightTo) {
            this.weightTo = weightTo;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

    }


    public class Data {

        @SerializedName("gift_coupans")
        @Expose
        private List<GiftCoupan> giftCoupans = null;
        @SerializedName("path")
        @Expose
        private String path;

        public List<GiftCoupan> getGiftCoupans() {
            return giftCoupans;
        }

        public void setGiftCoupans(List<GiftCoupan> giftCoupans) {
            this.giftCoupans = giftCoupans;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

    }

}
