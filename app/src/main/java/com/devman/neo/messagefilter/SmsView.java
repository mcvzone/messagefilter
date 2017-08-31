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

public class SmsView extends LinearLayout {

    private TextView smsMessage;
    private TextView smsCount;

    public SmsView(Context context) {
        super(context);
        init(context);
    }

    public SmsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_sms, this, true);

        smsMessage = findViewById(R.id.smsMessage);
        smsCount = findViewById(R.id.smsCount);
    }

    public void setSmsMessage(String sMessage) {
        this.smsMessage.setText(sMessage);
    }

    public void setSmsCount(String sCount) {
        this.smsCount.setText(sCount);
    }
}
