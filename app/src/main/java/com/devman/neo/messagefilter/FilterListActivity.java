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

public class FilterListActivity extends AppCompatActivity {

    private FilterAdapter filterAdapter;
    private SQLiteDatabase database;
    private DML dml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_list);

        /** database */
        DataBaseManager dataBaseManager = DataBaseManager.getInstance(this);
        database = dataBaseManager.getWritableDatabase();
        dml = new DML(database);

        /** list */
        ArrayList<Map<String, String>> filters;
        ListView filterView = (ListView)findViewById(R.id.filterList);

        Cursor cursor = dml.selectFilterList();
        if( cursor!=null ){
            filters = new ArrayList();
            filterAdapter = new FilterAdapter(filters);

            Map<String, String> map;
            while(cursor.moveToNext()){
                map = new HashMap();
                map.put("number", cursor.getString(cursor.getColumnIndex("SENDER")));
                map.put("count", cursor.getString(cursor.getColumnIndex("CNT")));
                filters.add(map);
            }
            filterView.setAdapter(filterAdapter);
        }

        filterView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, String> filter = filterAdapter.getItem(i);
                String sNumber = filter.get("number");
                Intent intent = new Intent(getApplicationContext(), NumberActivity.class);
                intent.putExtra("number", sNumber);
                startActivity(intent);
            }
        });
    }

    class FilterAdapter extends BaseAdapter{
        private ArrayList<Map<String, String>> filters = null;

        public FilterAdapter(ArrayList<Map<String, String>> filters) {
            this.filters = filters;
        }

        @Override
        public int getCount() {
            return filters.size();
        }

        @Override
        public Map<String, String> getItem(int i) {
            return filters.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            FilterView filterView;
            if (view == null) {
                filterView = new FilterView(getApplicationContext());
            } else {
                filterView = (FilterView)view;
            }

            Map<String, String> filter = filters.get(i);
            filterView.setFilterNumber(filter.get("number"));
            filterView.setFilterCount(filter.get("count"));
            return filterView;
        }
    }
}
