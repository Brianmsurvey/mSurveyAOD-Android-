package com.msurvey.projectm.msurveyaod;

import java.util.ArrayList;

public class Profile {

    private String commId; //Phone number of Profile

    private ArrayList<String> surveyIncentives;

    private ArrayList<String> surveysCompleted;

    private String surveysCompletedNo;

    private String airtimeEarned;


    public Profile(){

        surveyIncentives = new ArrayList<>();
        surveysCompleted = new ArrayList<>();

    };

    public Profile(String commId, ArrayList<String> surveyIncentives, ArrayList<String> surveysCompleted){

        this.commId = commId;

        this.surveyIncentives = surveyIncentives;

        this.surveysCompleted = surveysCompleted;

    }


    //Getters

    public String getCommId() {
        return commId;
    }

    public ArrayList<String> getSurveyIncentives() {
        return surveyIncentives;
    }

    public ArrayList<String> getSurveysCompleted() {
        return surveysCompleted;
    }

    public String getAirtimeEarned() {
        return airtimeEarned;
    }

    public String getSurveysCompletedNo() {
        return surveysCompletedNo;
    }




    //Setters

    public void setCommId(String commId) {
        this.commId = commId;
    }

    public void setSurveyIncentives(ArrayList<String> surveyIncentives) {
        this.surveyIncentives = surveyIncentives;
    }

    public void setSurveyIncentives(String surveyIncentive){
        this.surveyIncentives.add(surveyIncentive);
    }

    public void setAirtimeEarned(String airtimeEarned) {
        this.airtimeEarned = airtimeEarned;
    }

    public void setSurveysCompleted(ArrayList<String> surveysCompleted) {
        this.surveysCompleted = surveysCompleted;
    }

    public void setSurveysCompleted(String surveyCompleted){
        this.surveysCompleted.add(surveyCompleted);
    }

    public void setSurveysCompletedNo(String surveysCompletedNo) {
        this.surveysCompletedNo = surveysCompletedNo;
    }
}
