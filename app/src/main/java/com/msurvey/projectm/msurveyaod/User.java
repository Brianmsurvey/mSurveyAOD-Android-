package com.msurvey.projectm.msurveyaod;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class User {

    private String name;

    private String phoneNumber;

    private Map<String, mpesaSMS> mPesaData;

    public User(){

        this.mPesaData = new HashMap<>();
    }

    public User(String phoneNumber, Map<String, mpesaSMS> mpesadata){

        this.phoneNumber = phoneNumber;

        this.mPesaData = mpesadata;

    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

    public Map<String, mpesaSMS> getmPesaData() {
        return mPesaData;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setmPesaData(Map<String, mpesaSMS> mPesaData) {
        this.mPesaData = mPesaData;
    }

    public void addMpesaSmsTomPesaData(mpesaSMS mpesaSMS){

        this.mPesaData.put(mpesaSMS.getTransactionId(), mpesaSMS);
    }
}
