package com.project.jewelmart.swarnsarita.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by javedkhan on 3/1/18.
 */

public class ProductDetail2 {

    @SerializedName("product_master_id")
    @Expose
    private String productMasterId;

    @SerializedName("default_melting_id")
    @Expose
    private String default_melting_id;

    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("collection_name")
    @Expose
    private String collectionName;
    @SerializedName("collection_sku_code")
    @Expose
    private String collectionSkuCode;
    @SerializedName("key_label")
    @Expose
    private List<String> keyLabel = null;
    @SerializedName("key_value")
    @Expose
    private List<String> keyValue = null;
    @SerializedName("karat_selection")
    @Expose
    private List<String> karatSelection = null;

    @SerializedName("size_selection")
    @Expose
    private List<String> sizeSelection = null;

    @SerializedName("tone_selection")
    @Expose
    private List<String> toneSelection = null;

    @SerializedName("piece_selection")
    @Expose
    private List<String> pieceSelection = null;
    @SerializedName("tone")
    @Expose
    private String tone;
    @SerializedName("polish")
    @Expose
    private String polish;

    @SerializedName("color")
    @Expose
    private String color;

    @SerializedName("karat_rates")
    @Expose
    private List<KaratRate> karatRates = null;
    @SerializedName("karat_change_rate")
    @Expose
    private List<KaratChangeRate> karatChangeRate = null;
    @SerializedName("product_default_data")
    @Expose
    private ProductDefaultData productDefaultData;
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
    @SerializedName("product_default")
    @Expose
    private List<Object> productDefault = null;
    @SerializedName("in_cart")
    @Expose
    private Integer inCart;
    @SerializedName("in_wishlist")
    @Expose
    private Integer inWishlist;

    @SerializedName("hallmarking_rates")
    @Expose
    private List<HallmarkingRate> hallmarkingRates = null;
    @SerializedName("certification_rates")
    @Expose
    private List<CertificationRate> certificationRates = null;

    @SerializedName("labour_details")
    @Expose
    private List<LabourDetail> labourDetails = null;

    public List<LabourDetail> getLabourDetails() {
        return labourDetails;
    }

    public void setLabourDetails(List<LabourDetail> labourDetails) {
        this.labourDetails = labourDetails;
    }


    public List<HallmarkingRate> getHallmarkingRates() {
        return hallmarkingRates;
    }

    public void setHallmarkingRates(List<HallmarkingRate> hallmarkingRates) {
        this.hallmarkingRates = hallmarkingRates;
    }

    public List<CertificationRate> getCertificationRates() {
        return certificationRates;
    }

    public void setCertificationRates(List<CertificationRate> certificationRates) {
        this.certificationRates = certificationRates;
    }

    public String getProductMasterId() {
        return productMasterId;
    }

    public void setProductMasterId(String productMasterId) {
        this.productMasterId = productMasterId;
    }

    public String getDefaulMeltingId() {
        return default_melting_id;
    }

    public void setgetDefaulMeltingId(String default_melting_id) {
        this.default_melting_id = default_melting_id;
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

    public List<String> getKaratSelection() {
        return karatSelection;
    }

    public List<String> getSizeSelection() {
        return sizeSelection;
    }

    public List<String> getPieceSelection() {
        return pieceSelection;
    }

    public List<String> getToneSelection() {
        return toneSelection;
    }

    public void setKaratSelection(List<String> karatSelection) {
        this.karatSelection = karatSelection;
    }

    public List<KaratRate> getKaratRates() {
        return karatRates;
    }

    public void setKaratRates(List<KaratRate> karatRates) {
        this.karatRates = karatRates;
    }

    public List<KaratChangeRate> getKaratChangeRate() {
        return karatChangeRate;
    }

    public void setKaratChangeRate(List<KaratChangeRate> karatChangeRate) {
        this.karatChangeRate = karatChangeRate;
    }

    public ProductDefaultData getProductDefaultData() {
        return productDefaultData;
    }

    public void setProductDefaultData(ProductDefaultData productDefaultData) {
        this.productDefaultData = productDefaultData;
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

    public List<Object> getProductDefault() {
        return productDefault;
    }

    public void setProductDefault(List<Object> productDefault) {
        this.productDefault = productDefault;
    }

    public Integer getInCart() {
        return inCart;
    }

    public void setInCart(Integer inCart) {
        this.inCart = inCart;
    }

    public Integer getInWishlist() {
        return inWishlist;
    }

    public void setInWishlist(Integer inWishlist) {
        this.inWishlist = inWishlist;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    public String getPolish() {
        return polish;
    }

    public void setPolish(String polish) {
        this.polish = polish;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }


    public class ProductDefaultData {

        @SerializedName("gross_wt")
        @Expose
        private String grossWt;
        @SerializedName("net_wt")
        @Expose
        private String netWt;
        @SerializedName("karat")
        @Expose
        private String karat;
        @SerializedName("piece")
        @Expose
        private String piece;
        @SerializedName("size")
        @Expose
        private String size;
        @SerializedName("tone")
        @Expose
        private String tone;
        @SerializedName("certification")
        @Expose
        private String certification;
        @SerializedName("hallmarking")
        @Expose
        private String hallmarking;

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

        public String getKarat() {
            return karat;
        }

        public void setKarat(String karat) {
            this.karat = karat;
        }

        public String getPiece() {
            return piece;
        }

        public void setPiece(String piece) {
            this.piece = piece;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getTone() {
            return tone;
        }

        public void setTone(String tone) {
            this.tone = tone;
        }

        public String getCertification() {
            return certification;
        }

        public void setCertification(String certification) {
            this.certification = certification;
        }

        public String getHallmarking() {
            return hallmarking;
        }

        public void setHallmarking(String hallmarking) {
            this.hallmarking = hallmarking;
        }

    }


    public class CertificationRate {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("rate")
        @Expose
        private String rate;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

    }


    public class HallmarkingRate {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("rate")
        @Expose
        private String rate;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

    }

    public class KaratRate {

        @SerializedName("karat_value")
        @Expose
        private String karatValue;
        @SerializedName("rate")
        @Expose
        private Integer rate;

        public String getKaratValue() {
            return karatValue;
        }

        public void setKaratValue(String karatValue) {
            this.karatValue = karatValue;
        }

        public Integer getRate() {
            return rate;
        }

        public void setRate(Integer rate) {
            this.rate = rate;
        }

    }


    public class KaratChangeRate {

        @SerializedName("18")
        @Expose
        private List<_18> _18 = null;
        @SerializedName("22")
        @Expose
        private List<_22> _22 = null;
        @SerializedName("24")
        @Expose
        private List<_24> _24 = null;

        public List<_18> get18() {
            return _18;
        }

        public void set18(List<_18> _18) {
            this._18 = _18;
        }

        public List<_22> get22() {
            return _22;
        }

        public void set22(List<_22> _22) {
            this._22 = _22;
        }

        public List<_24> get24() {
            return _24;
        }

        public void set24(List<_24> _24) {
            this._24 = _24;
        }

    }


    public class _18 {
        @SerializedName("karat_name")
        @Expose
        private String karatName;
        @SerializedName("rate")
        @Expose
        private String rate;

        public String getKaratName() {
            return karatName;
        }

        public void setKaratName(String karatName) {
            this.karatName = karatName;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

    }

    public class _22 {
        @SerializedName("karat_name")
        @Expose
        private String karatName;
        @SerializedName("rate")
        @Expose
        private String rate;

        public String getKaratName() {
            return karatName;
        }

        public void setKaratName(String karatName) {
            this.karatName = karatName;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

    }

    public class _24 {
        @SerializedName("karat_name")
        @Expose
        private String karatName;
        @SerializedName("rate")
        @Expose
        private String rate;

        public String getKaratName() {
            return karatName;
        }

        public void setKaratName(String karatName) {
            this.karatName = karatName;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

    }


    public class LabourDetail {

        @SerializedName("karat_type_rate")
        @Expose
        private String karatTypeRate;
        @SerializedName("labour_name")
        @Expose
        private String labourName;
        @SerializedName("rate")
        @Expose
        private String rate;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("amount")
        @Expose
        private Integer amount;

        public String getKaratTypeRate() {
            return karatTypeRate;
        }

        public void setKaratTypeRate(String karatTypeRate) {
            this.karatTypeRate = karatTypeRate;
        }

        public String getLabourName() {
            return labourName;
        }

        public void setLabourName(String labourName) {
            this.labourName = labourName;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }

    }



}
