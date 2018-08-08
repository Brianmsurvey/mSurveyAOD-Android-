package com.msurvey.projectm.msurveyaod;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class User {

    private String name;

    private String userAge;

    private String userDob;

    private String userGender;

    private String location;

    private String phoneNumber;

    private String avatorImage;

    private Map<String, mpesaSMS> mPesaData;

    public User(){

        this.mPesaData = new HashMap<>();
    }

    public User(String name, String age, String dob, String location, String phoneNumber, Map<String, mpesaSMS> mpesadata, String avatorImage){

        this.name = name;

        this.userAge = age;

        this.userDob = dob;

        this.location = location;

        this.phoneNumber = phoneNumber;

        this.mPesaData = mpesadata;

        this.avatorImage = avatorImage;

    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public void setUserDob(String userDob) {
        this.userDob = userDob;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAvatorImage(String avatorImage) {
        this.avatorImage = avatorImage;
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

    public String getUserAge() {
        return userAge;
    }

    public String getUserGender() {
        return userGender;
    }

    public String getUserDob() {
        return userDob;
    }

    public String getLocation() {
        return location;
    }

    public String getAvatorImage() {
        return avatorImage;
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
