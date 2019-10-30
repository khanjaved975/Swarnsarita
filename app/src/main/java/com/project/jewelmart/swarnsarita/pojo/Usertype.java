package com.project.jewelmart.swarnsarita.pojo;

/**
 * Created by javedkhan on 11/28/17.
 */
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Usertype {

    @SerializedName("usertype")
    @Expose
    private List<Usertype_> usertype = null;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("ack")
    @Expose
    private Integer ack;

    public List<Usertype_> getUsertype() {
        return usertype;
    }

    public void setUsertype(List<Usertype_> usertype) {
        this.usertype = usertype;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getAck() {
        return ack;
    }

    public void setAck(Integer ack) {
        this.ack = ack;
    }

    public class Usertype_ {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

}
