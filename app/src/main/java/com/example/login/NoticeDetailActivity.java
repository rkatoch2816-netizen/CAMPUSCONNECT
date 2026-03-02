package com.example.login;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class NoticeDetailActivity extends AppCompatActivity {

    TextView titleTxt, dateTxt, descTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

        titleTxt = findViewById(R.id.titleText);
        dateTxt = findViewById(R.id.dateText);
        descTxt = findViewById(R.id.descText);

        // Receive data safely
        String title = getIntent().getStringExtra("title");
        String date = getIntent().getStringExtra("date");
        String desc = getIntent().getStringExtra("desc");

        if (title != null) titleTxt.setText(title);
        if (date != null) dateTxt.setText(date);
        if (desc != null) descTxt.setText(desc);
    }
}