package com.devman.neo.messagefilter.common;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by neo on 2017-08-22.
 */

public class DML {
    SQLiteDatabase database;

    public DML(SQLiteDatabase database) {
        this.database = database;
    }

    public Cursor selectSender(String [] param){
        return database.rawQuery("SELECT SENDER FROM FILTER_LIST WHERE SENDER = ?", param);
    }

    public Cursor selectFilterList(){
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT SENDER, COUNT(*) CNT FROM SMS_LIST GROUP BY SENDER");

        return database.rawQuery(sql.toString(), null);
    }

    public Cursor selectSmsList(String [] param){
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT MESSAGE, COUNT(*) CNT FROM SMS_LIST WHERE SENDER = ? GROUP BY MESSAGE");

        return database.rawQuery(sql.toString(), param);
    }

    public void insertSmsMessage(Object [] param){
        database.execSQL("INSERT INTO SMS_LIST(SENDER, MESSAGE, RECEIVED_DT) values (?, ?, ?)", param);
    }

    public void insertFilterNumber(Object [] param){
        database.execSQL("INSERT INTO FILTER_LIST (SENDER) values (?)", param);
    }
}
