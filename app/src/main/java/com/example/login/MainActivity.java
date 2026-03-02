package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<NoticeModel> noticeData = new ArrayList<>();

    String[][] students = {
            {"104", "Faizan Ali", "9th", "64/70", "Present"},
            {"105", "Rahim Khan", "9th", "54/95", "Present"},
            {"106", "Farhan Shah", "8th", "48/70", "Absent"},
            {"107", "Sana Patel", "10th", "56/75", "Present"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- Populate Student Attendance ---
        LinearLayout studentContainer = findViewById(R.id.studentContainer);
        LayoutInflater inflater = LayoutInflater.from(this);

        for (String[] s : students) {
            View item = inflater.inflate(R.layout.item_student, studentContainer, false);

            TextView tvId = item.findViewById(R.id.tvId);
            TextView tvName = item.findViewById(R.id.tvName);
            TextView tvClass = item.findViewById(R.id.tvClass);
            TextView tvAttend = item.findViewById(R.id.tvAttend);
            TextView tvStatus = item.findViewById(R.id.tvStatus);

            tvId.setText(s[0]);
            tvName.setText(s[1]);
            tvClass.setText(s[2]);
            tvAttend.setText(s[3]);
            tvStatus.setText(s[4]);

            if (s[4].equalsIgnoreCase("Absent")) {
                tvStatus.setBackgroundResource(R.drawable.bg_status_absent);
            } else {
                tvStatus.setBackgroundResource(R.drawable.bg_status_present);
            }

            studentContainer.addView(item);
        }

        // --- Populate Notice Board ---
        LinearLayout noticeContainer = findViewById(R.id.noticeContainer);
        
        noticeData.add(new NoticeModel(
                "Exam Schedule Updated",
                "Today - Macanago",
                "New exam dates for the upcoming term have been posted."
        ));

        noticeData.add(new NoticeModel(
                "School Reopening Reminder",
                "2 Days Ago - Geshnago",
                "School will reopen from May 1st. All students are advised to be prepared."
        ));

        noticeData.add(new NoticeModel(
                "Holiday Notice",
                "6 Days Ago",
                "The college will remain closed for the upcoming holidays."
        ));

        for (NoticeModel notice : noticeData) {
            View card = inflater.inflate(R.layout.item_notice_card, noticeContainer, false);
            
            TextView title = card.findViewById(R.id.tvNoticeTitle);
            TextView date = card.findViewById(R.id.tvNoticeDate);
            TextView desc = card.findViewById(R.id.tvNoticeShortDesc);

            title.setText(notice.getTitle());
            date.setText(notice.getDate());
            desc.setText(notice.getDescription());

            card.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, NoticeDetailActivity.class);
                intent.putExtra("title", notice.getTitle());
                intent.putExtra("date", notice.getDate());
                intent.putExtra("desc", notice.getDescription());
                startActivity(intent);
            });

            noticeContainer.addView(card);
        }
    }
}
