package com.project.jewelmart.swarnsarita.pojo;

/**
 * Created by javedkhan on 12/6/17.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Productgridpojo {

    @SerializedName("products")
    @Expose
    private List<Result> products = null;

    public List<Result> getResult() {
        return products;
    }

    public void setResult(List<Result> products) {
        this.products = products;
    }

/*
    public class Image {
        @SerializedName("image_name")
        @Expose
        private String imageName;
        public String getImageName() {  return imageName;}
        public void setImageName(String imageName) {
            this.imageName = imageName;
        }

    }
*/

    public static class Result {

        public boolean progress = false;
        public Result(boolean progress) {
            this.progress = progress;
        }

        @SerializedName("collection_sku_code")
        @Expose
        private String collectionSkuCode;

        @SerializedName("product_inventory_id")
        @Expose
        private String product_inventory_id;

        @SerializedName("product_inventory_type")
        @Expose
        private String product_inventory_type;

        @SerializedName("in_cart")
        @Expose
        private String in_cart;

        @SerializedName("in_wishlist")
        @Expose
        private String in_wishlist;

        @SerializedName("gross_wt")
        @Expose
        private String grossWt;

        @SerializedName("net_wt")
        @Expose
        private String netWt;

        @SerializedName("design_number")
        @Expose
        private String design_number;

        @SerializedName("image_name")
        @Expose
        private String imageName;


        @SerializedName("quantity")
        @Expose
        private String quantity;

        @SerializedName("product_status")
        @Expose
        private String product_status;

        public String getCollectionSkuCode() {
            return collectionSkuCode;
        }

        public String getProduct_inventory_id() {
            return product_inventory_id;
        }

        public void setCollectionSkuCode(String collectionSkuCode) {
            this.collectionSkuCode = collectionSkuCode;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public void setProduct_inventory_id(String product_inventory_id) {
            this.product_inventory_id = product_inventory_id;
        }

        public void setIn_cart(String In_cart) {
            this.in_cart = In_cart;
        }

        public String getIn_cart() {
            return this.in_cart;
        }


        public void setDesign_number(String design_number) {
            this.design_number = design_number;
        }

        public String getDesign_number() {
            return this.design_number;
        }

        public void setIn_wishlist(String In_wishlist) {
            this.in_wishlist = In_wishlist;
        }

        public String getIn_wishlist() {
            return this.in_wishlist;
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

        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }

        public void setProduct_status(String product_status) {
            this.product_status = product_status;
        }

        public String getProduct_status() {
            return product_status;
        }
    }

}
