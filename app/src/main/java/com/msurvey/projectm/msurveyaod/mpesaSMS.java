package com.msurvey.projectm.msurveyaod;

public class mpesaSMS {

    private String transactionId;

    private String amountTransacted;

    private String mpesaBalance;

    private String cashReceiver;

    private String transactionTime;

    private String transactionDate;

    private String transactionCost;

    private String transactionType;


    public mpesaSMS(){};

    public mpesaSMS(String transactionId, String amountTransacted, String mpesaBalance, String cashReceiver, String transactionTime,
                    String transactionDate, String transactionCost, String transactionType){

        this.transactionId = transactionId;

        this.amountTransacted = amountTransacted;

        this.mpesaBalance = mpesaBalance;

        this.cashReceiver = cashReceiver;

        this.transactionTime = transactionTime;

        this.transactionDate = transactionDate;

        this.transactionCost = transactionCost;

        this.transactionType = transactionType;

    }


    public  String getTransactionId() {
        return this.transactionId;
    }

    public  String getAmountTransacted() {
        return amountTransacted;
    }

    public  String getMpesaBalance() {
        return mpesaBalance;
    }

    public  String getCashReceiver() {
        return cashReceiver;
    }

    public  String getTransactionTime() {
        return transactionTime;
    }

    public  String getTransactionDate() {
        return transactionDate;
    }

    public  String getTransactionCost() {
        return transactionCost;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public  void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public  void setAmountTransacted(String amountTransacted) {
        this.amountTransacted = amountTransacted;
    }

    public  void setCashReceiver(String cashReceiver) {
        this.cashReceiver = cashReceiver;
    }

    public  void setMpesaBalance(String mpesaBalance) {
        this.mpesaBalance = mpesaBalance;
    }

    public  void setTransactionCost(String transactionCost) {
        this.transactionCost = transactionCost;
    }

    public  void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public  void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }


}
