package com.project.jewelmart.swarnsarita.models;

/**
 * Created by javedkhan on 9/19/18.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderHistory {

    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("delivery_date")
    @Expose
    private String deliveryDate;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("gross_wt")
    @Expose
    private String grossWt;
    @SerializedName("net_wt")
    @Expose
    private String netWt;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("collection_id")
    @Expose
    private String collectionId;
    @SerializedName("order_stage")
    @Expose
    private String order_stage;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("tone")
    @Expose
    private String tone;
    @SerializedName("polish")
    @Expose
    private String polish;
    @SerializedName("image_name")
    @Expose
    private List<ImageName> imageName = null;

    @SerializedName("gift_data")
    @Expose
    private GiftData giftData;

    @SerializedName("product_inventory_type")
    @Expose
    private String product_inventory_type;

    public GiftData getGiftData() {
        return giftData;
    }

    public void setGiftData(GiftData giftData) {
        this.giftData = giftData;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getGrossWt() {
        return grossWt;
    }

    public void setGrossWt(String grossWt) {
        this.grossWt = grossWt;
    }

    public String getNetWt() {
        return netWt;
    }

    public void setNetWt(String netWt) {
        this.netWt = netWt;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public List<ImageName> getImageName() {
        return imageName;
    }

    public void setImageName(List<ImageName> imageName) {
        this.imageName = imageName;
    }

    public String getOrder_stage() {
        return order_stage;
    }

    public void setOrder_stage(String order_stage) {
        this.order_stage = order_stage;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    public String getPolish() {
        return polish;
    }

    public void setPolish(String polish) {
        this.polish = polish;
    }

    public String getProduct_inventory_type() {
        return product_inventory_type;
    }

    public void setProduct_inventory_type(String product_inventory_type) {
        this.product_inventory_type = product_inventory_type;
    }

    public class GiftData {

        @SerializedName("coupan_name")
        @Expose
        private String coupanName;
        @SerializedName("description")
        @Expose
        private String description;

        public String getCoupanName() {
            return coupanName;
        }

        public void setCoupanName(String coupanName) {
            this.coupanName = coupanName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }

    public class ImageName {

        @SerializedName("image_name")
        @Expose
        private String imageName;

        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }

    }

}
