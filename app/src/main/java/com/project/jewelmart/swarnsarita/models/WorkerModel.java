package com.project.jewelmart.swarnsarita.models;

/**
 * Created by javedkhan on 11/9/18.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WorkerModel {

    @SerializedName("data_list")
    @Expose
    private List<DataList> dataList = null;

    public List<DataList> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataList> dataList) {
        this.dataList = dataList;
    }


    public class DataList {

        @SerializedName("data_key")
        @Expose
        private List<String> dataKey = null;

        @SerializedName("product_id")
        @Expose
        private String product_id;


        @SerializedName("data_value")
        @Expose
        private List<String> dataValue = null;
        @SerializedName("image_name")
        @Expose
        private List<ImageName> imageName = null;

        public List<String> getDataKey() {
            return dataKey;
        }

        public void setDataKey(List<String> dataKey) {
            this.dataKey = dataKey;
        }

        public List<String> getDataValue() {
            return dataValue;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setDataValue(List<String> dataValue) {
            this.dataValue = dataValue;
        }

        public List<ImageName> getImageName() {
            return imageName;
        }

        public void setImageName(List<ImageName> imageName) {
            this.imageName = imageName;
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

}
