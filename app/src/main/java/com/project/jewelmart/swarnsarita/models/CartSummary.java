package com.project.jewelmart.swarnsarita.models;

/**
 * Created by javedkhan on 9/16/18.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CartSummary {

    @SerializedName("ack")
    @Expose
    private String ack;
    @SerializedName("cart_summary")
    @Expose
    private CartSummary_ cartSummary;

    public String getAck() {
        return ack;
    }

    public void setAck(String ack) {
        this.ack = ack;
    }

    public CartSummary_ getCartSummary() {
        return cartSummary;
    }

    public void setCartSummary(CartSummary_ cartSummary) {
        this.cartSummary = cartSummary;
    }

    public class CartSummary_ {

        @SerializedName("product_master")
        @Expose
        private ProductMaster productMaster;
        @SerializedName("inventory_master")
        @Expose
        private InventoryMaster inventoryMaster;

        public ProductMaster getProductMaster() {
            return productMaster;
        }

        public void setProductMaster(ProductMaster productMaster) {
            this.productMaster = productMaster;
        }

        public InventoryMaster getInventoryMaster() {
            return inventoryMaster;
        }

        public void setInventoryMaster(InventoryMaster inventoryMaster) {
            this.inventoryMaster = inventoryMaster;
        }

        public class InventoryMaster {

            @SerializedName("gross_wt")
            @Expose
            private String grossWt;
            @SerializedName("quantity")
            @Expose
            private Integer quantity;

            public String getGrossWt() {
                return grossWt;
            }

            public void setGrossWt(String grossWt) {
                this.grossWt = grossWt;
            }

            public Integer getQuantity() {
                return quantity;
            }

            public void setQuantity(Integer quantity) {
                this.quantity = quantity;
            }

        }

        public class ProductMaster {

            @SerializedName("gross_wt")
            @Expose
            private String grossWt;
            @SerializedName("quantity")
            @Expose
            private Integer quantity;

            public String getGrossWt() {
                return grossWt;
            }

            public void setGrossWt(String grossWt) {
                this.grossWt = grossWt;
            }

            public Integer getQuantity() {
                return quantity;
            }

            public void setQuantity(Integer quantity) {
                this.quantity = quantity;
            }

        }

    }

}