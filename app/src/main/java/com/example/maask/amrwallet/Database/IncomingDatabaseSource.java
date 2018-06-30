package com.example.maask.amrwallet.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Maask on 12/26/2017.
 */

public class IncomingDatabaseSource {

    private DatabaseHelper dbhelper;
    private SQLiteDatabase db;
    Context context;

    public IncomingDatabaseSource(Context context) {

        dbhelper = new DatabaseHelper(context);
        this.context = context;

    }

    public void open(){ db = dbhelper.getWritableDatabase();}

    public void close(){db.close();}

    public boolean insertIncomingTransactions(IncomingMoney incomingMoney){

        this.open();
        ContentValues keyValues = new ContentValues();

        keyValues.put(DatabaseHelper.INCOMING_MONEY_COL_TSN_NAME,incomingMoney.getTransactionName());
        keyValues.put(DatabaseHelper.INCOMING_MONEY_COL_TNS_DATE,incomingMoney.getTransactionDate().toLowerCase());
        keyValues.put(DatabaseHelper.INCOMING_MONEY_COL_TNS_AMOUNT,incomingMoney.getTransactionAmount());
        keyValues.put(DatabaseHelper.INCOMING_MONEY_COL_TNS_TIME,incomingMoney.getTransactionTime());
        keyValues.put(DatabaseHelper.INCOMING_MONEY_COL_TNS_CHK_MONTH,incomingMoney.getChkDateSession());

        long insertedRowNotification = db.insert(DatabaseHelper.INCOMING_MONEY_TABLE_NAME,null,keyValues);
        this.close();

        if (insertedRowNotification > 0){
            return true;
        }else {
            return false;
        }

    }

    public ArrayList<IncomingMoney> getAllIncomingMoney(String chk_month_session){

        this.open();
        ArrayList<IncomingMoney> monies = new ArrayList<>();

        Cursor cursor = db.query(DatabaseHelper.INCOMING_MONEY_TABLE_NAME,null,DatabaseHelper.INCOMING_MONEY_COL_TNS_CHK_MONTH +" = '"+ chk_month_session + "' ",null,null,null,
                DatabaseHelper.INCOMING_MONEY_COL_ID +" DESC");


        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.INCOMING_MONEY_COL_ID));
                String tns_name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.INCOMING_MONEY_COL_TSN_NAME));
                String tns_date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.INCOMING_MONEY_COL_TNS_DATE));
                Float tns_amount = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.INCOMING_MONEY_COL_TNS_AMOUNT));
                String tns_time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.INCOMING_MONEY_COL_TNS_TIME));
                String chk_date_session = cursor.getString(cursor.getColumnIndex(DatabaseHelper.INCOMING_MONEY_COL_TNS_CHK_MONTH));
                monies.add(new IncomingMoney(tns_name,tns_date,tns_amount,tns_time,chk_date_session));
            }while (cursor.moveToNext());
        }

        cursor.close();
        this.close();
        return monies;
    }

    public float getAllIncomingMoneyOnlyAmount(String chk_month_session){

        this.open();
        float totalIncomingAmount = 0;
        String[] columns = {DatabaseHelper.INCOMING_MONEY_COL_TNS_AMOUNT};

        Cursor cursor = db.query(DatabaseHelper.INCOMING_MONEY_TABLE_NAME,columns,DatabaseHelper.INCOMING_MONEY_COL_TNS_CHK_MONTH +" = '"+ chk_month_session + "' ",null,null,null,null);


        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                totalIncomingAmount = totalIncomingAmount + cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.INCOMING_MONEY_COL_TNS_AMOUNT));


            }while (cursor.moveToNext());
        }

        cursor.close();
        this.close();
        return totalIncomingAmount;
    }

    public ArrayList<String> getDistinctMonth(){

        this.open();
        ArrayList<String> distinctMonths = new ArrayList<>();

        String[] columns = {DatabaseHelper.INCOMING_MONEY_COL_TNS_CHK_MONTH};

        Cursor cursor = db.query(true,DatabaseHelper.INCOMING_MONEY_TABLE_NAME,columns,null,null,null,null,null,null);

        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            do {

                distinctMonths.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.INCOMING_MONEY_COL_TNS_CHK_MONTH)));

            }while (cursor.moveToNext());
        }

        cursor.close();
        this.close();

        return distinctMonths;
    }

    public boolean deleteIncomingTransaction(int rowId){

        this.open();
        int deleteNotification = db.delete(DatabaseHelper.INCOMING_MONEY_TABLE_NAME, DatabaseHelper.INCOMING_MONEY_COL_ID + "=" + rowId, null);
        this.close();

        if (deleteNotification > 0){
            return true;
        }else {
            return false;
        }

    }

    public ArrayList<Integer> getAllColumnId(String chk_month_session){

        this.open();
        ArrayList<Integer> colId = new ArrayList<>();

        String[] columns = {DatabaseHelper.INCOMING_MONEY_COL_ID};

        Cursor cursor = db.query(DatabaseHelper.INCOMING_MONEY_TABLE_NAME,columns,DatabaseHelper.INCOMING_MONEY_COL_TNS_CHK_MONTH +" = '"+ chk_month_session + "' ",null,null,null,null);


        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                int cid = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.INCOMING_MONEY_COL_ID));
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

        String[] columns = {DatabaseHelper.INCOMING_MONEY_COL_TSN_NAME,
                            DatabaseHelper.INCOMING_MONEY_COL_TNS_DATE,
                            DatabaseHelper.INCOMING_MONEY_COL_TNS_TIME,
                            DatabaseHelper.INCOMING_MONEY_COL_TNS_AMOUNT};

        Cursor cursor = db.query(DatabaseHelper.INCOMING_MONEY_TABLE_NAME,columns,DatabaseHelper.INCOMING_MONEY_COL_TNS_CHK_MONTH +" = '"+ chk_month_session + "' AND " + DatabaseHelper.INCOMING_MONEY_COL_ID +" = '"+ colid + "' " ,null,null,null,null);

        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            do {

                String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.INCOMING_MONEY_COL_TSN_NAME));
                String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.INCOMING_MONEY_COL_TNS_DATE));
                String time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.INCOMING_MONEY_COL_TNS_TIME));
                String amount = cursor.getString(cursor.getColumnIndex(DatabaseHelper.INCOMING_MONEY_COL_TNS_AMOUNT));

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

    public boolean updateSingleRow(int colid,String name,String date,String time,float Amount){

        this.open();

        ContentValues keyValues = new ContentValues();

        keyValues.put(DatabaseHelper.INCOMING_MONEY_COL_TSN_NAME,name);
        keyValues.put(DatabaseHelper.INCOMING_MONEY_COL_TNS_DATE,date);
        keyValues.put(DatabaseHelper.INCOMING_MONEY_COL_TNS_TIME,time);
        keyValues.put(DatabaseHelper.INCOMING_MONEY_COL_TNS_AMOUNT,Amount);

        int updateNotification = db.update(DatabaseHelper.INCOMING_MONEY_TABLE_NAME,
                keyValues,
                DatabaseHelper.INCOMING_MONEY_COL_ID +" = '"+ colid + "' ",
                null);

        this.close();

        if (updateNotification > 0 ){
            return true;
        }else {
            return false;
        }

    }

    public float getEntireIncomingMoneyOnlyAmount(){

        this.open();
        float totalIncomingAmount = 0;
        String[] columns = {DatabaseHelper.INCOMING_MONEY_COL_TNS_AMOUNT};

        Cursor cursor = db.query(DatabaseHelper.INCOMING_MONEY_TABLE_NAME,columns,null,null,null,null,null);


        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                totalIncomingAmount = totalIncomingAmount + cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.INCOMING_MONEY_COL_TNS_AMOUNT));


            }while (cursor.moveToNext());
        }

        cursor.close();
        this.close();
        return totalIncomingAmount;
    }

}
