package com.project.jewelmart.swarnsarita.pojo;

/**
 * Created by javedkhan on 11/27/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartAcknowledge {

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

        @SerializedName("total_weight")
        @Expose
        private String totalWeight;

        @SerializedName("quantity")
        @Expose
        private String quantity;

        @SerializedName("next_offer")
        @Expose
        private NextOffer nextOffer;

        public String getTotalWeight() {
            return totalWeight;
        }

        public void setTotalWeight(String totalWeight) {
            this.totalWeight = totalWeight;
        }

        public NextOffer getNextOffer() {
            return nextOffer;
        }

        public void setNextOffer(NextOffer nextOffer) {
            this.nextOffer = nextOffer;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }


        public class NextOffer {

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
            @SerializedName("status")
            @Expose
            private String status;

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

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

        }

    }

}
