package com.project.jewelmart.swarnsarita.models;

/**
 * Created by javedkhan on 11/10/18.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetailModel {

    @SerializedName("product_data")
    @Expose
    private List<ProductDatum> productData = null;

    public List<ProductDatum> getProductData() {
        return productData;
    }

    public void setProductData(List<ProductDatum> productData) {
        this.productData = productData;
    }


    public class ProductDatum {

        @SerializedName("data_key")
        @Expose
        private List<String> dataKey = null;
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
            @SerializedName("featured_image")
            @Expose
            private String featuredImage;

            public String getImageName() {
                return imageName;
            }

            public void setImageName(String imageName) {
                this.imageName = imageName;
            }

            public String getFeaturedImage() {
                return featuredImage;
            }

            public void setFeaturedImage(String featuredImage) {
                this.featuredImage = featuredImage;
            }

        }

    }

}
