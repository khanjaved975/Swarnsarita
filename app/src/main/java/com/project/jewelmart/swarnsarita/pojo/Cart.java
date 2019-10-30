package com.project.jewelmart.swarnsarita.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Cart {
    @SerializedName("ack")
    @Expose
    private String ack;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("cart_wish_id")
        @Expose
        private String cartWishId;
        @SerializedName("product_inventory_type")
        @Expose
        private String productInventoryType;
        @SerializedName("product_inventory_id")
        @Expose
        private String productInventoryId;
        @SerializedName("collection_name")
        @Expose
        private String collectionName;
        @SerializedName("collection_sku_code")
        @Expose
        private String collectionSkuCode;
        @SerializedName("gross_wt")
        @Expose
        private Double grossWt;
        @SerializedName("net_wt")
        @Expose
        private Double netWt;
        @SerializedName("quantity")
        @Expose
        private String quantity;
        @SerializedName("remarks")
        @Expose
        private String remarks;
        @SerializedName("length")
        @Expose
        private String length;
        @SerializedName("images")
        @Expose
        private String images;
        @SerializedName("large_image")
        @Expose
        private String largeImage;
        @SerializedName("zoom_image")
        @Expose
        private String zoomImage;

        @SerializedName("color")
        @Expose
        private String color;
        @SerializedName("polish")
        @Expose
        private String polish;

        @SerializedName("tone")
        @Expose
        private String tone;
        @SerializedName("thumb_image")
        @Expose
        private String thumbImage;
        @SerializedName("total_gross_wt")
        @Expose
        private String totalGrossWt;
        @SerializedName("total_net_wt")
        @Expose
        private String totalNetWt;
        @SerializedName("total_price")
        @Expose
        private String totalPrice;

        public String getCartWishId() {
            return cartWishId;
        }

        public void setCartWishId(String cartWishId) {
            this.cartWishId = cartWishId;
        }

        public String getProductInventoryType() {
            return productInventoryType;
        }

        public void setProductInventoryType(String productInventoryType) {
            this.productInventoryType = productInventoryType;
        }

        public String getProductInventoryId() {
            return productInventoryId;
        }

        public void setProductInventoryId(String productInventoryId) {
            this.productInventoryId = productInventoryId;
        }

        public String getCollectionName() {
            return collectionName;
        }

        public void setCollectionName(String collectionName) {
            this.collectionName = collectionName;
        }

        public String getCollectionSkuCode() {
            return collectionSkuCode;
        }

        public void setCollectionSkuCode(String collectionSkuCode) {
            this.collectionSkuCode = collectionSkuCode;
        }

        public Double getGrossWt() {
            return grossWt;
        }

        public void setGrossWt(Double grossWt) {
            this.grossWt = grossWt;
        }

        public Double getNetWt() {
            return netWt;
        }

        public void setNetWt(Double netWt) {
            this.netWt = netWt;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getLength() {
            return length;
        }

        public void setLength(String length) {
            this.length = length;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getLargeImage() {
            return largeImage;
        }

        public void setLargeImage(String largeImage) {
            this.largeImage = largeImage;
        }

        public String getZoomImage() {
            return zoomImage;
        }

        public void setZoomImage(String zoomImage) {
            this.zoomImage = zoomImage;
        }

        public String getThumbImage() {
            return thumbImage;
        }

        public void setThumbImage(String thumbImage) {
            this.thumbImage = thumbImage;
        }

        public String getTotalGrossWt() {
            return totalGrossWt;
        }

        public void setTotalGrossWt(String totalGrossWt) {
            this.totalGrossWt = totalGrossWt;
        }

        public String getTotalNetWt() {
            return totalNetWt;
        }

        public void setTotalNetWt(String totalNetWt) {
            this.totalNetWt = totalNetWt;
        }

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getPolish() {
            return polish;
        }

        public void setPolish(String polish) {
            this.polish = polish;
        }

        public String getTone() {
            return tone;
        }

        public void setTone(String tone) {
            this.tone = tone;
        }
    }


}
