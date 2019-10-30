package com.project.jewelmart.swarnsarita.pojo;

/**
 * Created by javedkhan on 1/5/18.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetail {

    @SerializedName("product_master_id")
    @Expose
    private String productMasterId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("collection_name")
    @Expose
    private String collectionName;
    @SerializedName("collection_sku_code")
    @Expose
    private String collectionSkuCode;
    @SerializedName("manufacturing_code")
    @Expose
    private String manufacturingCode;
    @SerializedName("key_label")
    @Expose
    private List<String> keyLabel = null;
    @SerializedName("key_value")
    @Expose
    private List<String> keyValue = null;
    @SerializedName("worker_name")
    @Expose
    private String workerName;
    @SerializedName("vendor_name")
    @Expose
    private String vendorName;
    @SerializedName("image_name")
    @Expose
    private List<String> imageName = null;
    @SerializedName("large_image")
    @Expose
    private String largeImage;
    @SerializedName("zoom_image")
    @Expose
    private String zoomImage;
    @SerializedName("thumb_image")
    @Expose
    private String thumbImage;
    @SerializedName("small_image")
    @Expose
    private String smallImage;

    @SerializedName("in_cart")
    @Expose
    private String in_cart;

    @SerializedName("in_wishlist")
    @Expose
    private String in_wishlist;


    public void setIn_cart(String In_cart) {
        this.in_cart = In_cart;
    }

    public String getIn_cart() {
        return this.in_cart;
    }

    public void setIn_wishlist(String In_wishlist) {
        this.in_wishlist = In_wishlist;
    }

    public String getIn_wishlist() {
        return this.in_wishlist;
    }


    public String getProductMasterId() {
        return productMasterId;
    }

    public void setProductMasterId(String productMasterId) {
        this.productMasterId = productMasterId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public List<String> getKeyLabel() {
        return keyLabel;
    }

    public void setKeyLabel(List<String> keyLabel) {
        this.keyLabel = keyLabel;
    }

    public List<String> getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(List<String> keyValue) {
        this.keyValue = keyValue;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public List<String> getImageName() {
        return imageName;
    }

    public void setImageName(List<String> imageName) {
        this.imageName = imageName;
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

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

}