package com.example.phonecatcher.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.phonecatcher.R;

public class PopupView extends LinearLayout {
    private TextView phone;
    public PopupView(Context context) {
        super(context);

        setView(context);
    }

    public void setPhone(String p){
        phone.setText(p);
    }

    private void setView(Context context) {
        final View view = LayoutInflater.from(context).inflate(R.layout.card_popup, this, false);
        phone = view.findViewById(R.id.tv_call_number);
        phone.setText("초기화되지 않았습니다.");
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        addView(view);
    }
}
