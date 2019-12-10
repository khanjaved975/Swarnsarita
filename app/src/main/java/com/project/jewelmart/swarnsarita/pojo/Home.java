package com.project.jewelmart.swarnsarita.pojo;

/**
 * Created by javedkhan on 12/2/17.
 */

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Home {

    @SerializedName("base_path")
    @Expose
    private String basePath;
    @SerializedName("brand_banner")
    @Expose
    private List<BrandBanner> brandBanner = null;
    @SerializedName("final_collection")
    @Expose
    private List<FinalCollection> finalCollection = null;
    @SerializedName("image_path")
    @Expose
    private ImagePath imagePath;

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public List<BrandBanner> getBrandBanner() {
        return brandBanner;
    }

    public void setBrandBanner(List<BrandBanner> brandBanner) {
        this.brandBanner = brandBanner;
    }

    public List<FinalCollection> getFinalCollection() {
        return finalCollection;
    }

    public void setFinalCollection(List<FinalCollection> finalCollection) {
        this.finalCollection = finalCollection;
    }

    public ImagePath getImagePath() {
        return imagePath;
    }

    public void setImagePath(ImagePath imagePath) {
        this.imagePath = imagePath;
    }

    public class BrandBanner {

        @SerializedName("brand_image")
        @Expose
        private String brandImage;
        @SerializedName("position")
        @Expose
        private String position;
        @SerializedName("description")
        @Expose
        private String description;

        public String getBrandImage() {
            return brandImage;
        }

        public void setBrandImage(String brandImage) {
            this.brandImage = brandImage;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public class FinalCollection {
        @SerializedName("key")
        @Expose
        private String key;
        @SerializedName("product_assign")
        @Expose
        private List<ProductAssign> productAssign = null;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<ProductAssign> getProductAssign() {
            return productAssign;
        }

        public void setProductAssign(List<ProductAssign> productAssign) {
            this.productAssign = productAssign;
        }

        public class ProductAssign {

            @SerializedName("collection_sku_code")
            @Expose
            private String collectionSkuCode;
            @SerializedName("product_id")
            @Expose
            private String product_id;
            @SerializedName("gross_wt")
            @Expose
            private String grossWt;
            @SerializedName("net_wt")
            @Expose
            private String netWt;

            @SerializedName("in_cart")
            @Expose
            private String incart;

            @SerializedName("in_wish")
            @Expose
            private String inwishlist;

            @SerializedName("quantity")
            @Expose
            private String quantity;
            @SerializedName("product_status")
            @Expose
            private String product_status;

            @SerializedName("images")
            @Expose
            private List<Image> images = null;

            public String getCollectionSkuCode() {
                return collectionSkuCode;
            }

            public void setCollectionSkuCode(String collectionSkuCode) {
                this.collectionSkuCode = collectionSkuCode;
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

            public List<Image> getImages() {
                return images;
            }

            public void setImages(List<Image> images) {
                this.images = images;
            }

            public String getIncart() {
                return incart;
            }

            public void setIncart(String incart) {
                this.incart = incart;
            }

            public String getInwishlist() {
                return inwishlist;
            }

            public void setInwishlist(String inwishlist) {
                this.inwishlist = inwishlist;
            }

            public String getProduct_id() {
                return product_id;
            }

            public void setProduct_id(String product_id) {
                this.product_id = product_id;
            }

            public String getQuantity() {
                return quantity;
            }

            public void setQuantity(String quantity) {
                this.quantity = quantity;
            }

            public void setProduct_status(String product_status) {
                this.product_status = product_status;
            }

            public String getProduct_status() {
                return product_status;
            }

            public class Image {

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



    public class ImagePath {

        @SerializedName("large_image")
        @Expose
        private String largeImage;
        @SerializedName("small_image")
        @Expose
        private String smallImage;
        @SerializedName("zoom_image")
        @Expose
        private String zoomImage;
        @SerializedName("thumb_image")
        @Expose
        private String thumbImage;

        public String getLargeImage() {
            return largeImage;
        }

        public void setLargeImage(String largeImage) {
            this.largeImage = largeImage;
        }

        public String getSmallImage() {
            return smallImage;
        }

        public void setSmallImage(String smallImage) {
            this.smallImage = smallImage;
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

    }




}
