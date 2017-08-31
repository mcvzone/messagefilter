package com.devman.neo.messagefilter.common;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by neo on 2017-08-22.
 */
public class DDL {

    SQLiteDatabase database;

    public DDL(SQLiteDatabase database) {
        this.database = database;
    }

    public void SMS_LIST(){
        StringBuffer sql = new StringBuffer();
        sql.append("CREATE TABLE IF NOT EXISTS SMS_LIST(\n");
        sql.append("SEQ integer primary key autoincrement, \n");
        sql.append("SENDER TEXT, \n");
        sql.append("MESSAGE TEXT, \n");
        sql.append("RECEIVED_DT TEXT )");
        database.execSQL(sql.toString());
    }

    public void FILTER_LIST(){
        database.execSQL("CREATE TABLE IF NOT EXISTS FILTER_LIST ( SENDER TEXT, PRIMARY KEY(SENDER) )");
    }
}
