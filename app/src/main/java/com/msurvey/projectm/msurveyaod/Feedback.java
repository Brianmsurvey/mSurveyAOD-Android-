package com.msurvey.projectm.msurveyaod;

public class Feedback {


    private String ovrResponse;

    private String feedbackInput;

    private String transactionDate;

    private String transactionTime;

    private String timeStamp;

    private String merchantName;

    private String userNumber;


    public Feedback(){};

    public Feedback(String ovrResponse, String feedbackInput, String transactionDate, String transactionTime){

        this.ovrResponse = ovrResponse;

        this.feedbackInput = feedbackInput;

        this.transactionDate = transactionDate;

        this.transactionTime = transactionTime;
    }

    public Feedback(String ovrResponse, String feedbackInput, String transactionDate, String transactionTime, String merchantName,
                    String userNumber){

        this.ovrResponse = ovrResponse;

        this.feedbackInput = feedbackInput;

        this.transactionDate = transactionDate;

        this.transactionTime = transactionTime;

        this.merchantName = merchantName;

        this.userNumber = userNumber;
    }


    public Feedback(String ovrResponse, String transactionDate, String transactionTime, String merchantName, String userNumber){

        this.ovrResponse = ovrResponse;

        this.transactionDate = transactionDate;

        this.transactionTime = transactionTime;

        this.merchantName = merchantName;

        this.userNumber = userNumber;
    }


    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setFeedbackInput(String feedbackInput) {
        this.feedbackInput = feedbackInput;
    }

    public void setOvrResponse(String ovrResponse) {
        this.ovrResponse = ovrResponse;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getmerchantName() {
        return merchantName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getFeedbackInput() {
        return feedbackInput;
    }

    public String getOvrResponse() {
        return ovrResponse;
    }
}
