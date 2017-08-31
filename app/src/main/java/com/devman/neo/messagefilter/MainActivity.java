package com.devman.neo.messagefilter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.devman.neo.messagefilter.common.DML;
import com.devman.neo.messagefilter.common.DataBaseManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase database;
    EditText mainSender;
    DML dml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.confirmPermission();//confirm permission

        //database
        DataBaseManager dataBaseManager = DataBaseManager.getInstance(this);
        database = dataBaseManager.getWritableDatabase();
        dml = new DML(database);

        mainSender = (EditText)findViewById(R.id.mainSender);

        /**
         * 필터링 목록 조회
         */
        findViewById(R.id.mainListBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FilterListActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 입력한 발신번호 필터링으로 추가
         */
        findViewById(R.id.mainAddBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sSender = mainSender.getText().toString();
                if ("".equals(sSender)) {
                    Toast.makeText(getApplicationContext(), "발신번호를 입력 하세요.", Toast.LENGTH_SHORT).show();
                    mainSender.setFocusable(true);
                } else {
                    Cursor cursor = dml.selectSender(new String[]{sSender});

                    if( cursor != null && cursor.moveToNext() ){
                        Toast.makeText(getApplicationContext(), sSender + " 이미 추가된 번호 입니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        int result = getSmsList(sSender);

                        if (result > 0) {
                            dml.insertFilterNumber(new Object[]{sSender});
                            Toast.makeText(getApplicationContext(), sSender + "의 " + result + "건이 추가 되었습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), sSender + "로 수신된 메시지가 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    /**
     * Permission
     */
    private void confirmPermission(){
        int permission;

        //SMS Confirm
        permission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);

        if (!(permission == PackageManager.PERMISSION_GRANTED)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {
                //Toast.makeText(this, "SMS 수신권한 설명 필요함.", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
            }
        }

        //SMS Confirm
        permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);

        if (!(permission == PackageManager.PERMISSION_GRANTED)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
                //Toast.makeText(this, "SMS 수신권한 설명 필요함.", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 1);
            }
        }
    }

    public int getSmsList(String sSender) {
        Uri inbox = Uri.parse("content://sms/inbox");
        Cursor cur = this.getContentResolver().query(inbox, null, null, null, null);

        String sNumber, sMessage, sReceivedDate;
        Date received;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.mm.dd HH:mm:ss", Locale.KOREA);
        long timestamp;
        int count=0;

        while (cur.moveToNext()) {
            sNumber = cur.getString(cur.getColumnIndex("address"));

            if (sNumber.equals(sSender)) {
                sMessage = cur.getString(cur.getColumnIndex("body"));
                timestamp = cur.getLong(cur.getColumnIndex("date"));

                received = new Date(timestamp);
                sReceivedDate = sdf.format(received);

                Log.d("MYLOG", "sNumber, sMessage, sReceivedDate : " + sNumber+", "+sMessage+", "+sReceivedDate);

                dml.insertSmsMessage(new Object[]{sNumber, sMessage, sReceivedDate});
                count++;
            }
        }
        return count;
    }

    public void SMSDelete() {
        Uri deleteUri = Uri.parse("content://sms");
        int count = 0;
        Cursor c = this.getContentResolver().query(deleteUri, null, null, null, null);
        while (c.moveToNext()) {
            try {
                // Delete the SMS
                String pid = c.getString(0);
                // Get id;
                String uri = "content://sms/" + pid;
                // count = this.getContentResolver().delete(Uri.parse(uri),null, null);
            } catch (Exception e) {
            }
        }
    }
}
