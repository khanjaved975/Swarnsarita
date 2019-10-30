package com.project.jewelmart.swarnsarita.pojo;

/**
 * Created by javedkhan on 11/25/18.
 */
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CatalogueModel {

    @SerializedName("ack")
    @Expose
    private Integer ack;
    @SerializedName("catalogue_slider")
    @Expose
    private List<CatalogueSlider> catalogueSlider = null;
    @SerializedName("image_path")
    @Expose
    private String imagePath;

    public Integer getAck() {
        return ack;
    }

    public void setAck(Integer ack) {
        this.ack = ack;
    }

    public List<CatalogueSlider> getCatalogueSlider() {
        return catalogueSlider;
    }

    public void setCatalogueSlider(List<CatalogueSlider> catalogueSlider) {
        this.catalogueSlider = catalogueSlider;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public class CatalogueSlider {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("image_name")
        @Expose
        private String imageName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }

    }

}