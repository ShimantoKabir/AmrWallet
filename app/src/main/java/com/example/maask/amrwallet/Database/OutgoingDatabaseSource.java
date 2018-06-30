package com.example.maask.amrwallet.Database;

/**
 * Created by Maask on 1/5/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Maask on 12/28/2017.
 */

public class OutgoingDatabaseSource {

    private DatabaseHelper dbhelper;
    private SQLiteDatabase db;


    public OutgoingDatabaseSource(Context context) {

        dbhelper = new DatabaseHelper(context);

    }

    public void open(){ db = dbhelper.getWritableDatabase();}

    public void close(){db.close();}

    public boolean insertOutgoingTransactions(OutgoingMoney outgoingMoney){

        this.open();
        ContentValues keyValues = new ContentValues();

        keyValues.put(DatabaseHelper.OUTGOING_MONEY_COL_TSN_NAME,outgoingMoney.getTransactionName());
        keyValues.put(DatabaseHelper.OUTGOING_MONEY_COL_TSN_DATE,outgoingMoney.getTransactionDate());
        keyValues.put(DatabaseHelper.OUTGOING_MONEY_COL_TSN_AMOUNT,outgoingMoney.getTransactionAmount());
        keyValues.put(DatabaseHelper.OUTGOING_MONEY_COL_TSN_TIME,outgoingMoney.getTransactionTime());
        keyValues.put(DatabaseHelper.OUTGOING_MONEY_COL_TNS_CHK_MONTH,outgoingMoney.getChkDateSession());

        long insertedRowNotification = db.insert(DatabaseHelper.OUTGOING_MONEY_TABLE_NAME,null,keyValues);
        this.close();

        if (insertedRowNotification > 0){
            return true;
        }else {
            return false;
        }

    }

    public ArrayList<OutgoingMoney> getAllOutgoingMoney(String chk_month_session){

        this.open();
        ArrayList<OutgoingMoney> monies = new ArrayList<>();

        Cursor cursor = db.query(DatabaseHelper.OUTGOING_MONEY_TABLE_NAME,null,DatabaseHelper.OUTGOING_MONEY_COL_TNS_CHK_MONTH +" = '"+ chk_month_session + "' ",null,null,null,
                DatabaseHelper.OUTGOING_MONEY_COL_ID +" DESC");


        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.OUTGOING_MONEY_COL_ID));
                String tns_name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.OUTGOING_MONEY_COL_TSN_NAME));
                String tns_date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.OUTGOING_MONEY_COL_TSN_DATE));
                Float  tns_amount = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.OUTGOING_MONEY_COL_TSN_AMOUNT));
                String tns_time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.OUTGOING_MONEY_COL_TSN_TIME));
                String chk_date_session = cursor.getString(cursor.getColumnIndex(DatabaseHelper.OUTGOING_MONEY_COL_TNS_CHK_MONTH));
                monies.add(new OutgoingMoney(tns_name,tns_date,tns_amount,tns_time,chk_date_session));
            }while (cursor.moveToNext());
        }

        cursor.close();
        this.close();
        return monies;

    }

    public float getAllOutgoingMoneyOnlyAmount(String chk_month_session){

        this.open();
        float totalOutgoingAmount = 0;
        String[] columns = {DatabaseHelper.OUTGOING_MONEY_COL_TSN_AMOUNT};

        Cursor cursor = db.query(DatabaseHelper.OUTGOING_MONEY_TABLE_NAME,columns,DatabaseHelper.OUTGOING_MONEY_COL_TNS_CHK_MONTH +" = '"+ chk_month_session + "' ",null,null,null,null);


        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            do {

                totalOutgoingAmount = totalOutgoingAmount + cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.OUTGOING_MONEY_COL_TSN_AMOUNT));

            }while (cursor.moveToNext());
        }

        cursor.close();
        this.close();
        return totalOutgoingAmount;

    }

    public ArrayList<String> getDistinctMonth(){

        this.open();
        ArrayList<String> distinctMonths = new ArrayList<>();

        String[] columns = {DatabaseHelper.OUTGOING_MONEY_COL_TNS_CHK_MONTH};

        Cursor cursor = db.query(true,DatabaseHelper.OUTGOING_MONEY_TABLE_NAME,columns,null,null,null,null,null,null);

        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            do {


                distinctMonths.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.OUTGOING_MONEY_COL_TNS_CHK_MONTH)));

            }while (cursor.moveToNext());
        }

        cursor.close();
        this.close();

        return distinctMonths;
    }

    public ArrayList<Integer> getAllColumnId(String chk_month_session){

        this.open();
        ArrayList<Integer> colId = new ArrayList<>();

        String[] columns = {DatabaseHelper.OUTGOING_MONEY_COL_ID};

        Cursor cursor = db.query(DatabaseHelper.OUTGOING_MONEY_TABLE_NAME,columns,DatabaseHelper.OUTGOING_MONEY_COL_TNS_CHK_MONTH +" = '"+ chk_month_session + "' ",null,null,null,null);


        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                int cid = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.OUTGOING_MONEY_COL_ID));
                colId.add(cid);
            }while (cursor.moveToNext());
        }

        cursor.close();
        this.close();
        return colId;
    }

    public ArrayList<String> getSingleRowInfoById(String chk_month_session,int colid){

        this.open();
        ArrayList<String> singleRowInfo = new ArrayList<>();

        String[] columns = {
                DatabaseHelper.OUTGOING_MONEY_COL_TSN_NAME,
                DatabaseHelper.OUTGOING_MONEY_COL_TSN_DATE,
                DatabaseHelper.OUTGOING_MONEY_COL_TSN_TIME,
                DatabaseHelper.OUTGOING_MONEY_COL_TSN_AMOUNT};

        Cursor cursor = db.query(DatabaseHelper.OUTGOING_MONEY_TABLE_NAME,columns,DatabaseHelper.OUTGOING_MONEY_COL_TNS_CHK_MONTH +" = '"+ chk_month_session + "' AND " + DatabaseHelper.OUTGOING_MONEY_COL_ID +" = '"+ colid + "' " ,null,null,null,null);

        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            do {

                String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.OUTGOING_MONEY_COL_TSN_NAME));
                String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.OUTGOING_MONEY_COL_TSN_DATE));
                String time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.OUTGOING_MONEY_COL_TSN_TIME));
                String amount = cursor.getString(cursor.getColumnIndex(DatabaseHelper.OUTGOING_MONEY_COL_TSN_AMOUNT));

                singleRowInfo.add(name);
                singleRowInfo.add(date);
                singleRowInfo.add(time);
                singleRowInfo.add(amount);

            }while (cursor.moveToNext());
        }



        cursor.close();
        this.close();
        return singleRowInfo;
    }

    public boolean deleteOutgoingTransaction(int rowId){

        this.open();
        int deleteNotification = db.delete(DatabaseHelper.OUTGOING_MONEY_TABLE_NAME, DatabaseHelper.OUTGOING_MONEY_COL_ID + "=" + rowId, null);
        this.close();

        if (deleteNotification > 0){
            return true;
        }else {
            return false;
        }

    }

    public boolean updateSingleRow(int colid,String name,String date,String time,float Amount){

        this.open();

        ContentValues keyValues = new ContentValues();

        keyValues.put(DatabaseHelper.OUTGOING_MONEY_COL_TSN_NAME,name);
        keyValues.put(DatabaseHelper.OUTGOING_MONEY_COL_TSN_DATE,date);
        keyValues.put(DatabaseHelper.OUTGOING_MONEY_COL_TSN_TIME,time);
        keyValues.put(DatabaseHelper.OUTGOING_MONEY_COL_TSN_AMOUNT,Amount);

        int updateNotification = db.update(DatabaseHelper.OUTGOING_MONEY_TABLE_NAME,
                keyValues,
                DatabaseHelper.OUTGOING_MONEY_COL_ID +" = '"+ colid + "' ",
                null);

        this.close();

        if (updateNotification > 0 ){
            return true;
        }else {
            return false;
        }

    }

    public float getEntireOutgoingMoneyOnlyAmount(){

        this.open();
        float totalOutgoingAmount = 0;
        String[] columns = {DatabaseHelper.OUTGOING_MONEY_COL_TSN_AMOUNT};

        Cursor cursor = db.query(DatabaseHelper.OUTGOING_MONEY_TABLE_NAME,columns,null,null,null,null,null);


        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            do {

                totalOutgoingAmount = totalOutgoingAmount + cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.OUTGOING_MONEY_COL_TSN_AMOUNT));

            }while (cursor.moveToNext());
        }

        cursor.close();
        this.close();
        return totalOutgoingAmount;

    }


}
