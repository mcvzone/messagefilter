package com.devman.neo.messagefilter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by neo on 2017-08-27.
 */

public class FilterView extends LinearLayout {
    private TextView filterNumber;
    private TextView filterCount;

    public FilterView(Context context) {
        super(context);
        this.init(context);
    }

    public FilterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_filter, this, true);

        filterNumber = findViewById(R.id.filterNumber);
        filterCount = findViewById(R.id.filterCount);
    }

    public void setFilterNumber(String sFilterNumber) {
        this.filterNumber.setText(sFilterNumber);
    }

    public void setFilterCount(String sFilterCount) {
        this.filterCount.setText(sFilterCount);
    }
}
