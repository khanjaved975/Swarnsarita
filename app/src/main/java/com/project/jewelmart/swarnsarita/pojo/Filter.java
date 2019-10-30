package com.project.jewelmart.swarnsarita.pojo;

/**
 * Created by javedkhan on 2/5/18.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Filter {

    @SerializedName("gross_wt")
    @Expose
    private List<GrossWt> grossWt = null;
    @SerializedName("net_wt")
    @Expose
    private List<NetWt> netWt = null;
    @SerializedName("product_status")
    @Expose
    private List<ProductStatus> productStatus = null;

    public HashMap<String,ArrayList<Filter>> fullfilter;

    public List<GrossWt> getGrossWt() {
        return grossWt;
    }

    public void setGrossWt(List<GrossWt> grossWt) {
        this.grossWt = grossWt;
    }

    public List<NetWt> getNetWt() {
        return netWt;
    }

    public void setNetWt(List<NetWt> netWt) {
        this.netWt = netWt;
    }

    public List<ProductStatus> getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(List<ProductStatus> productStatus) {
        this.productStatus = productStatus;
    }

    public class GrossWt {

        @SerializedName("min_gross_wt")
        @Expose
        private String minGrossWt;
        @SerializedName("max_gross_wt")
        @Expose
        private String maxGrossWt;
        @SerializedName("type")
        @Expose
        private String type;

        public String getMinGrossWt() {
            return minGrossWt;
        }

        public void setMinGrossWt(String minGrossWt) {
            this.minGrossWt = minGrossWt;
        }

        public String getMaxGrossWt() {
            return maxGrossWt;
        }

        public void setMaxGrossWt(String maxGrossWt) {
            this.maxGrossWt = maxGrossWt;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }

    public class NetWt {

        @SerializedName("min_net_wt")
        @Expose
        private String minNetWt;
        @SerializedName("max_net_wt")
        @Expose
        private String maxNetWt;
        @SerializedName("type")
        @Expose
        private String type;

        public String getMinNetWt() {
            return minNetWt;
        }

        public void setMinNetWt(String minNetWt) {
            this.minNetWt = minNetWt;
        }

        public String getMaxNetWt() {
            return maxNetWt;
        }

        public void setMaxNetWt(String maxNetWt) {
            this.maxNetWt = maxNetWt;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }

    public class ProductStatus {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("status")
        @Expose
        private String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }

}
