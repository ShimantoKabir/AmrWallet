package com.example.maask.amrwallet.Database;

/**
 * Created by Maask on 1/5/2018.
 */

public class IncomingMoney {

    private int transactionId;
    private String transactionName;
    private String transactionDate;
    private float transactionAmount;
    private String transactionTime;
    private String chkDateSession;

    public IncomingMoney() {}


    public IncomingMoney(int transactionId, String transactionName, String transactionDate, float transactionAmount, String transactionTime, String chkDateSession) {
        this.transactionId = transactionId;
        this.transactionName = transactionName;
        this.transactionDate = transactionDate;
        this.transactionAmount = transactionAmount;
        this.transactionTime = transactionTime;
        this.chkDateSession = chkDateSession;
    }

    public IncomingMoney(String transactionName, String transactionDate, float transactionAmount, String transactionTime, String chkDateSession) {
        this.transactionName = transactionName;
        this.transactionDate = transactionDate;
        this.transactionAmount = transactionAmount;
        this.transactionTime = transactionTime;
        this.chkDateSession = chkDateSession;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public float getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(float transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getChkDateSession() {
        return chkDateSession;
    }

    public void setChkDateSession(String chkDateSession) {
        this.chkDateSession = chkDateSession;
    }
}
