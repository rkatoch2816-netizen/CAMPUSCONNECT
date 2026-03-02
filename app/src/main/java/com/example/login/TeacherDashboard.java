package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import java.util.ArrayList;

public class TeacherDashboard extends AppCompatActivity {

    ArrayList<NoticeModel> noticeData = new ArrayList<>();
    DrawerLayout drawerLayout;
    ImageView btnMenu, btnTeacherLogout;
    Spinner spinnerClass;

    String[][] students = {
            {"104", "Ritika", "9th", "64/70", "Present"},
            {"105", "Suhani", "9th", "54/95", "Present"},
            {"106", "Palak", "8th", "48/70", "Absent"},
            {"107", "Mansi", "10th", "56/75", "Present"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        drawerLayout = findViewById(R.id.drawer_layout);
        btnMenu = findViewById(R.id.btnMenu);
        btnTeacherLogout = findViewById(R.id.btnTeacherLogout);
        spinnerClass = findViewById(R.id.spinnerClass);

        // Setup Spinner for BCA I, II, III
        String[] classes = {"BCA I", "BCA II", "BCA III"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(adapter);

        btnMenu.setOnClickListener(v -> {
            if (drawerLayout != null) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Logout logic
        btnTeacherLogout.setOnClickListener(v -> {
            Intent intent = new Intent(TeacherDashboard.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

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
                Intent intent = new Intent(TeacherDashboard.this, NoticeDetailActivity.class);
                intent.putExtra("title", notice.getTitle());
                intent.putExtra("date", notice.getDate());
                intent.putExtra("desc", notice.getDescription());
                startActivity(intent);
            });

            noticeContainer.addView(card);
        }

        // Sidebar Navigation click listeners
        setupSidebarNavigation();
    }

    private void setupSidebarNavigation() {
        findViewById(R.id.nav_dashboard).setOnClickListener(v -> drawerLayout.closeDrawer(GravityCompat.START));
        findViewById(R.id.nav_attendance).setOnClickListener(v -> drawerLayout.closeDrawer(GravityCompat.START));
        findViewById(R.id.nav_notice).setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
        });
        findViewById(R.id.nav_assignments).setOnClickListener(v -> drawerLayout.closeDrawer(GravityCompat.START));
        findViewById(R.id.nav_reports).setOnClickListener(v -> drawerLayout.closeDrawer(GravityCompat.START));
        findViewById(R.id.nav_settings).setOnClickListener(v -> drawerLayout.closeDrawer(GravityCompat.START));
    }
}
