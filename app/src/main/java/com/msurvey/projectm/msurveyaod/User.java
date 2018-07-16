package com.msurvey.projectm.msurveyaod;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class User {

    private String name;

    private Map<String, mpesaSMS> mPesaData;

    public User(){

        this.mPesaData = new HashMap<>();
    }

    public User(String name, Map<String, mpesaSMS> mpesadata){

        this.name = name;

        this.mPesaData = mpesadata;

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
}
