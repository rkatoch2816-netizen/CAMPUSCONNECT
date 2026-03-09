package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- Populate Notice Board from Synced Data ---
        LinearLayout noticeContainer = findViewById(R.id.noticeContainer);
        LayoutInflater inflater = LayoutInflater.from(this);
        
        SharedPreferences noticePrefs = getSharedPreferences("NoticePrefs", Context.MODE_PRIVATE);
        String noticesJson = noticePrefs.getString("notices_json", "[]");

        try {
            JSONArray jsonArray = new JSONArray(noticesJson);
            
            // If no notices are added by teachers yet, show a default one
            if (jsonArray.length() == 0) {
                addDefaultNotice(noticeContainer, inflater);
            }

            // Display notices in reverse order (newest first)
            for (int i = jsonArray.length() - 1; i >= 0; i--) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String titleStr = obj.getString("title");
                String dateStr = obj.getString("date");
                String descStr = obj.getString("description");

                View card = inflater.inflate(R.layout.item_notice_card, noticeContainer, false);
                
                TextView title = card.findViewById(R.id.tvNoticeTitle);
                TextView date = card.findViewById(R.id.tvNoticeDate);
                TextView desc = card.findViewById(R.id.tvNoticeShortDesc);

                title.setText(titleStr);
                date.setText(dateStr);
                desc.setText(descStr);

                card.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, NoticeDetailActivity.class);
                    intent.putExtra("title", titleStr);
                    intent.putExtra("date", dateStr);
                    intent.putExtra("desc", descStr);
                    startActivity(intent);
                });

                noticeContainer.addView(card);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            addDefaultNotice(noticeContainer, inflater);
        }
    }

    private void addDefaultNotice(LinearLayout container, LayoutInflater inflater) {
        View card = inflater.inflate(R.layout.item_notice_card, container, false);
        ((TextView) card.findViewById(R.id.tvNoticeTitle)).setText("Welcome to Campus Connect");
        ((TextView) card.findViewById(R.id.tvNoticeDate)).setText("Today");
        ((TextView) card.findViewById(R.id.tvNoticeShortDesc)).setText("Check here for latest updates from your teachers.");
        container.addView(card);
    }
}
