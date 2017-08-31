package com.devman.neo.messagefilter.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by neo on 2017-08-22.
 */

public class DataBaseManager extends SQLiteOpenHelper {

    private static DataBaseManager instance = null;

    private DataBaseManager(Context context) {
        super(context, Constant.DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("MYLOG", "DB onCreate call.");
        DDL ddl = new DDL(sqLiteDatabase);
        ddl.SMS_LIST();
        ddl.FILTER_LIST();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if( newVersion > 1 ){
            //최초버전이 1이라는 가정하에 1보다 높으면, 버전업 되었다고 판단.
        }
    }

    public static DataBaseManager getInstance(Context context){
        if( instance == null ){
            instance = new DataBaseManager(context);
        }

        return instance;
    }
}
