package com.project.jewelmart.swarnsarita.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by AJITJOSHI on 2/12/2016.
 */
public class UserSessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREFER_NAME = "BitcoinPref";
    public static final String KEY_USER_ACTIVE = "NewUser";
    public static final String USER_EMAIL = "UserEmail";
    public static final String USER_CONTACT = "UserContact";
    public static final String USER_FULL_NAME = "fullname";
    public static final String USER_PASS = "fullname";
    public static final String USER_ID = "USER_ID";
    private static final String PREF_USER_TYPE = "pref_USER_TYPE";
    private static final String PREF_USER_Melting = "pref_USER_Melting";
    private static final String PREF_Notifiacation = "pref_Notifiacation";
    private static final String PREF_IS_Notifiacation = "pref_IS_Notifiacation";

    public UserSessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setIsUserActive(String newUser) {
        editor.putString(KEY_USER_ACTIVE, newUser);
        // commit changes
        editor.commit();
    }

    public void setNotifiacation(String newUser) {
        editor.putString(PREF_Notifiacation, newUser);
        // commit changes
        editor.commit();
    }
    public void setISNotifiacation(Boolean newUser) {
        editor.putBoolean(PREF_IS_Notifiacation, newUser);
        editor.commit();
    }

    public void setUserID(String newUser) {
        editor.putString(USER_ID, newUser);
        // commit changes
        editor.commit();
    }

    public void setUserPass(String newUser) {
        editor.putString(USER_PASS, newUser);
        // commit changes
        editor.commit();
    }


    public void Logout(Context context) {
        /*editor.remove(KEY_USER_ACTIVE);
        editor.remove(USER_EMAIL);
        editor.remove(USER_CONTACT);
        editor.remove(USER_FULL_NAME);
        editor.remove(USER_PASS);
        editor.remove(USER_ID);*/
        editor.clear();
        editor.commit();
    }

    public String getUSERPASS() {
        return pref.getString(USER_PASS, "");
    }

    public String getUserID() {
        return pref.getString(USER_ID, "");
    }

    public String getIsUserActive() {
        return pref.getString(KEY_USER_ACTIVE, "");
    }


    public String getNotification() {
        return pref.getString(PREF_Notifiacation, "no");
    }

    public boolean getISNotification() {
        return pref.getBoolean(PREF_IS_Notifiacation, false);
    }

    public void setUserEmail(String userEmail) {
        editor.putString(USER_EMAIL, userEmail);
        // commit changes
        editor.commit();
    }


    public String getUserFullName() {
        return pref.getString(USER_FULL_NAME, null);
    }


    public void setUserFullName(String userEmail) {
        editor.putString(USER_FULL_NAME, userEmail);
        // commit changes
        editor.commit();
    }

    public void setUserType(String userType) {
        editor.putString(PREF_USER_TYPE, userType);
        // commit changes
        editor.commit();
    }

    public String getUserType() {
        return pref.getString(PREF_USER_TYPE, "");
    }

    public void setMeting(String meting) {
        editor.putString(PREF_USER_Melting, meting);
        // commit changes
        editor.commit();
    }

    public String getMelting() {
        return pref.getString(PREF_USER_Melting, "");
    }


    public String getUserEmail() {
        return pref.getString(USER_EMAIL, "");
    }

    public void setUserContact(String userEmail) {
        editor.putString(USER_CONTACT, userEmail);
        // commit changes
        editor.commit();
    }


    public String getUserContact() {
        return pref.getString(USER_CONTACT, "00000000");
    }

   /* public void createUserLoginSession(){
        // Storing login value as TRUE
        editor.putBoolean(KEY_NEW_USER, true);
        *//*editor.putString(KEY_USER_FIRST_NAME, firstName);*//*
        // commit changes
        editor.commit();
    }*/

}
