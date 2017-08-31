package com.devman.neo.messagefilter.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.devman.neo.messagefilter.common.DML;
import com.devman.neo.messagefilter.common.DataBaseManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SmsAnalyzeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] messages = getMessages(intent.getExtras());

        String sSender = messages[0].getOriginatingAddress();
        String sMessage = messages[0].getMessageBody();

        Date received = new Date(messages[0].getTimestampMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.mm.dd HH:mm:ss", Locale.KOREA);
        String sReceived = sdf.format(received);

        Log.d("MYLOG", "sSender : " + sSender);
        Log.d("MYLOG", "sMessage : " + sMessage);
        Log.d("MYLOG", "sReceived dt : " + sReceived);

        DataBaseManager dataBaseManager = DataBaseManager.getInstance(context);
        SQLiteDatabase database = dataBaseManager.getWritableDatabase();
        DML dml = new DML(database);
        Cursor cursor = dml.selectSender(new String[]{sSender});
        if (cursor != null && cursor.moveToNext()) {
            dml.insertSmsMessage(new Object[]{sSender, sMessage, sReceived});
        } else {
            Log.d("MYLOG", "필터로 추가되지 않은 번호.");
        }
    }


    /**
     * get sms
     * sender, message, received date
     * @param bundle
     * @return
     */
    private SmsMessage[] getMessages(Bundle bundle) {
        Object[] objects = (Object[])bundle.get("pdus");

        SmsMessage[] messages = new SmsMessage[objects.length];

        if (objects != null && objects.length > 0) {
            String sFormat = bundle.getString("format");
            for( int i=0; i<objects.length; i++ ){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    messages[i] = SmsMessage.createFromPdu((byte[])objects[i], sFormat);
                } else {
                    messages[i] = SmsMessage.createFromPdu((byte[])objects[i]);
                }
            }
        }

        return messages;
    }
}
