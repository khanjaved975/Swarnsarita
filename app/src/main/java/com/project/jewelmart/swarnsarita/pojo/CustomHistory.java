package com.project.jewelmart.swarnsarita.pojo;

/**
 * Created by javedkhan on 12/16/18.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomHistory {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("ack")
    @Expose
    private String ack;

    @SerializedName("path")
    @Expose
    private String path;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getAck() {
        return ack;
    }

    public void setAck(String ack) {
        this.ack = ack;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public class Data {

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

            @SerializedName("label")
            @Expose
            private List<String> label = null;
            @SerializedName("value")
            @Expose
            private List<String> value = null;
            @SerializedName("image_name")
            @Expose
            private String imageName;

            public List<String> getLabel() {
                return label;
            }

            public void setLabel(List<String> label) {
                this.label = label;
            }

            public List<String> getValue() {
                return value;
            }

            public void setValue(List<String> value) {
                this.value = value;
            }

            public String getImageName() {
                return imageName;
            }

            public void setImageName(String imageName) {
                this.imageName = imageName;
            }

        }

    }

}
