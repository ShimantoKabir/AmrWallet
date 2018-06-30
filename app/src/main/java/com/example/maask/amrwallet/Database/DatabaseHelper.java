package com.example.maask.amrwallet.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Maask on 12/26/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    // database name and version deceleration
    public static final String DATABASE_NAME = "moneyTransaction_db";
    public static final int DATABASE_VERSION = 1;

    // incoming money table name declaration
    public static final String INCOMING_MONEY_TABLE_NAME = "tbl_incoming_money";
    // outgoing money table name declaration
    public static final String OUTGOING_MONEY_TABLE_NAME = "tbl_outgoing_money";

    // incoming money table column name declaration
    public static final String INCOMING_MONEY_COL_ID = "im_id";
    public static final String INCOMING_MONEY_COL_TSN_NAME = "im_name";
    public static final String INCOMING_MONEY_COL_TNS_DATE = "im_date";
    public static final String INCOMING_MONEY_COL_TNS_AMOUNT = "im_amount";
    public static final String INCOMING_MONEY_COL_TNS_TIME = "im_time";
    public static final String INCOMING_MONEY_COL_TNS_CHK_MONTH = "im_chk_month";

    // outgoing money table column name declaration
    public static final String OUTGOING_MONEY_COL_ID = "ot_id";
    public static final String OUTGOING_MONEY_COL_TSN_NAME = "ot_name";
    public static final String OUTGOING_MONEY_COL_TSN_DATE = "ot_date";
    public static final String OUTGOING_MONEY_COL_TSN_AMOUNT = "ot_amount";
    public static final String OUTGOING_MONEY_COL_TSN_TIME = "ot_time";
    public static final String OUTGOING_MONEY_COL_TNS_CHK_MONTH = "ot_chk_month";

    // incoming table create query
    public static final String CREATE_TABLE_INCOMING_QRY = "CREATE TABLE " + INCOMING_MONEY_TABLE_NAME + "("
            + INCOMING_MONEY_COL_ID +" INTEGER PRIMARY KEY, "
            + INCOMING_MONEY_COL_TSN_NAME + " TEXT NOT NULL, "
            + INCOMING_MONEY_COL_TNS_DATE + " TEXT NOT NULL, "
            + INCOMING_MONEY_COL_TNS_CHK_MONTH + " TEXT NOT NULL, "
            + INCOMING_MONEY_COL_TNS_TIME + " TEXT NOT NULL, "
            + INCOMING_MONEY_COL_TNS_AMOUNT + " REAL NOT NULL);";

    // outgoing table create query
    public static final String CREATE_TABLE_OUTGOING_QRY = "CREATE TABLE " + OUTGOING_MONEY_TABLE_NAME + "("
            + OUTGOING_MONEY_COL_ID +" INTEGER PRIMARY KEY, "
            + OUTGOING_MONEY_COL_TSN_NAME + " TEXT NOT NULL, "
            + OUTGOING_MONEY_COL_TSN_DATE + " TEXT NOT NULL, "
            + OUTGOING_MONEY_COL_TNS_CHK_MONTH + " TEXT NOT NULL, "
            + OUTGOING_MONEY_COL_TSN_TIME + " TEXT NOT NULL, "
            + OUTGOING_MONEY_COL_TSN_AMOUNT + " REAL NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_INCOMING_QRY);
        db.execSQL(CREATE_TABLE_OUTGOING_QRY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + INCOMING_MONEY_TABLE_NAME);
        onCreate(db);
    }

}
