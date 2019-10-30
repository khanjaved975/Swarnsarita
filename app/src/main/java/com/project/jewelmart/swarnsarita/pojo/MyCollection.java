package com.project.jewelmart.swarnsarita.pojo;

/**
 * Created by javedkhan on 1/7/18.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyCollection {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("collection_name")
    @Expose
    private String collectionName;
    @SerializedName("send_to")
    @Expose
    private String sendTo;
    @SerializedName("product_inventory_type")
    @Expose
    private String productInventoryType;
    @SerializedName("product_count")
    @Expose
    private String productCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getProductInventoryType() {
        return productInventoryType;
    }

    public void setProductInventoryType(String productInventoryType) {
        this.productInventoryType = productInventoryType;
    }

    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

}
