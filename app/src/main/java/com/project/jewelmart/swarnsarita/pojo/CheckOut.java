package com.project.jewelmart.swarnsarita.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckOut {

    @SerializedName("field_name")
    @Expose
    private String fieldName;
    @SerializedName("field_code")
    @Expose
    private String fieldCode;
    @SerializedName("required")
    @Expose
    private String required;
    @SerializedName("field_type")
    @Expose
    private String fieldType;
    @SerializedName("visible")
    @Expose
    private String visible;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

}
