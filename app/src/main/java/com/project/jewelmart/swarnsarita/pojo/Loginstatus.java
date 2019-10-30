package com.project.jewelmart.swarnsarita.pojo;

/**
 * Created by javedkhan on 2/17/18.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Loginstatus {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("ack")
    @Expose
    private Integer ack;
    @SerializedName("user_data")
    @Expose
    private UserData userData;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getAck() {
        return ack;
    }

    public void setAck(Integer ack) {
        this.ack = ack;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }


    public class UserData {

        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("mobile_number")
        @Expose
        private String mobileNumber;
        @SerializedName("email_id")
        @Expose
        private String emailId;
        @SerializedName("full_name")
        @Expose
        private String fullName;
        @SerializedName("user_status")
        @Expose
        private String userStatus;

        @SerializedName("default_melting")
        @Expose
        private String default_melting;

        public String getUserId() {
            return userId;
        }

        public void setDefault_melting(String default_melting) {
            this.default_melting = default_melting;
        }

        public String getDefault_melting() {
            return default_melting;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getEmailId() {
            return emailId;
        }

        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getUserStatus() {
            return userStatus;
        }

        public void setUserStatus(String userStatus) {
            this.userStatus = userStatus;
        }

    }

}
