package com.devman.neo.messagefilter;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.devman.neo.messagefilter.common.DML;
import com.devman.neo.messagefilter.common.DataBaseManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NumberActivity extends AppCompatActivity {

    SQLiteDatabase database;
    DML dml;

    ListView numberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);

        DataBaseManager dataBaseManager = DataBaseManager.getInstance(this);
        database = dataBaseManager.getWritableDatabase();
        dml = new DML(database);

        Intent intent = getIntent();
        String sNumber = intent.getStringExtra("number");
        Cursor cursor = dml.selectSmsList(new String[]{sNumber});

        SmsAdapter smsAdapter = new SmsAdapter();
        String sMessage, sCnt;
        while(cursor.moveToNext()) {
            sMessage = cursor.getString(cursor.getColumnIndex("MESSAGE"));
            sCnt = cursor.getString(cursor.getColumnIndex("CNT"));
            smsAdapter.addItem(sMessage, sCnt);
        }

        numberList = (ListView)findViewById(R.id.numberList);
        numberList.setAdapter(smsAdapter);

        numberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    class SmsAdapter extends BaseAdapter{
        ArrayList<Map<String, String>> smss;

        public SmsAdapter() {
            this.smss = new ArrayList();
        }

        public void addItem(String message, String cnt){
            Map<String, String> sms = new HashMap();
            sms.put("message", message);
            sms.put("cnt", cnt);
            smss.add(sms);
        }

        @Override
        public int getCount() {
            return smss.size();
        }

        @Override
        public Object getItem(int i) {
            return smss.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            SmsView smsView;

            if( view == null ){
                smsView =  new SmsView(getApplicationContext());
            } else {
                smsView = (SmsView)view;
            }

            Map<String, String> sms = smss.get(i);
            smsView.setSmsMessage(sms.get("message"));
            smsView.setSmsCount(sms.get("cnt"));

            return smsView;
        }
    }
}
