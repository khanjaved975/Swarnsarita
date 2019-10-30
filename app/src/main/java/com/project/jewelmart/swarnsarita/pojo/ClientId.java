package com.project.jewelmart.swarnsarita.pojo;

/**
 * Created by javedkhan on 11/28/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ClientId {

    @SerializedName("clients")
    @Expose
    private List<Client> clients = new ArrayList<>();
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("ack")
    @Expose
    private Integer ack;

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
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

    public class Client {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("company_name")
        @Expose
        private String companyName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

    }


}


