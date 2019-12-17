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
        @SerializedName("manufacturing_code")
        @Expose
        private String manufacturingCode;
        @SerializedName("gross_wt")
        @Expose
        private String grossWt;
        @SerializedName("net_wt")
        @Expose
        private String netWt;
        @SerializedName("quantity")
        @Expose
        private String quantity;
        @SerializedName("remarks")
        @Expose
        private String remarks;
        @SerializedName("kundan_weight")
        @Expose
        private String kundanWeight;
        @SerializedName("stone_weight")
        @Expose
        private String stoneWeight;
        @SerializedName("black_beads_weight")
        @Expose
        private String blackBeadsWeight;
        @SerializedName("extra_less")
        @Expose
        private String extraLess;
        @SerializedName("kundan_pcs")
        @Expose
        private String kundanPcs;
        @SerializedName("images")
        @Expose
        private String images;
        @SerializedName("large_image")
        @Expose
        private String largeImage;
        @SerializedName("zoom_image")
        @Expose
        private String zoomImage;
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

        public String getManufacturingCode() {
            return manufacturingCode;
        }

        public void setManufacturingCode(String manufacturingCode) {
            this.manufacturingCode = manufacturingCode;
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

        public String getKundanWeight() {
            return kundanWeight;
        }

        public void setKundanWeight(String kundanWeight) {
            this.kundanWeight = kundanWeight;
        }

        public String getStoneWeight() {
            return stoneWeight;
        }

        public void setStoneWeight(String stoneWeight) {
            this.stoneWeight = stoneWeight;
        }

        public String getBlackBeadsWeight() {
            return blackBeadsWeight;
        }

        public void setBlackBeadsWeight(String blackBeadsWeight) {
            this.blackBeadsWeight = blackBeadsWeight;
        }

        public String getExtraLess() {
            return extraLess;
        }

        public void setExtraLess(String extraLess) {
            this.extraLess = extraLess;
        }

        public String getKundanPcs() {
            return kundanPcs;
        }

        public void setKundanPcs(String kundanPcs) {
            this.kundanPcs = kundanPcs;
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

    }


}
