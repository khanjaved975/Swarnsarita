package com.project.jewelmart.swarnsarita.pojo;

/**
 * Created by javedkhan on 12/3/17.
 */

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Collection implements Serializable{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("col_name")
    @Expose
    private String colName;
    @SerializedName("short_code")
    @Expose
    private String shortCode;

    @SerializedName("image_name")
    @Expose
    private String image_name;

    @SerializedName("position")
    @Expose
    private String position;
    @SerializedName("subcategory")
    @Expose
    private List<Subcategory> subcategory = null;

    @SerializedName("created_date")
    @Expose
    private String created;

    @SerializedName("latest")
    @Expose
    private String latest;

    public void setLatest(String In_wishlist) {
        this.latest = In_wishlist;
    }

    public String getLatest() {
        return this.latest;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public List<Subcategory> getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(List<Subcategory> subcategory) {
        this.subcategory = subcategory;
    }

    public class Subcategory implements Serializable {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("col_name")
        @Expose
        private String colName;
        @SerializedName("short_code")
        @Expose
        private String shortCode;
        @SerializedName("position")
        @Expose
        private String position;
        @SerializedName("subcategory")
        @Expose
        private List<Subcategory> subcategory = null;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getColName() {
            return colName;
        }

        public void setColName(String colName) {
            this.colName = colName;
        }

        public String getShortCode() {
            return shortCode;
        }

        public void setShortCode(String shortCode) {
            this.shortCode = shortCode;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public List<Subcategory> getSubcategory() {
            return subcategory;
        }

        public void setSubcategory(List<Subcategory> subcategory) {
            this.subcategory = subcategory;
        }


        public class Subcategory_  implements Serializable{

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("col_name")
            @Expose
            private String colName;
            @SerializedName("short_code")
            @Expose
            private String shortCode;
            @SerializedName("position")
            @Expose
            private String position;
            @SerializedName("subcategory")
            @Expose
            private List<Object> subcategory = null;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getColName() {
                return colName;
            }

            public void setColName(String colName) {
                this.colName = colName;
            }

            public String getShortCode() {
                return shortCode;
            }

            public void setShortCode(String shortCode) {
                this.shortCode = shortCode;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public List<Object> getSubcategory() {
                return subcategory;
            }

            public void setSubcategory(List<Object> subcategory) {
                this.subcategory = subcategory;
            }

        }


    }
}
