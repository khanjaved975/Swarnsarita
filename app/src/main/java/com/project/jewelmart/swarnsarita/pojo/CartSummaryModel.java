package com.project.jewelmart.swarnsarita.pojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartSummaryModel {

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("gross_wt")
    @Expose
    private String grossWt;
    @SerializedName("net_wt")
    @Expose
    private String netWt;
    @SerializedName("length")
    @Expose
    private String length;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("cus_weight")
    @Expose
    private String cusWeight;
    @SerializedName("quantity")
    @Expose
    private String quantity;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCusWeight() {
        return cusWeight;
    }

    public void setCusWeight(String cusWeight) {
        this.cusWeight = cusWeight;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

}