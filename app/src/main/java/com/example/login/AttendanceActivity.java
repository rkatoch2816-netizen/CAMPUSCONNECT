package com.example.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AttendanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        String studentName = getIntent().getStringExtra("STUDENT_NAME");
        if (studentName == null) studentName = "Ritika";

        SharedPreferences prefs = getSharedPreferences("AttendancePrefs", Context.MODE_PRIVATE);
        String studentAttendance = prefs.getString("attendance_" + studentName, "0");
        int attended = Integer.parseInt(studentAttendance);
        int total = 65;

        // --- Subject Names based on Teachers in Dashboard ---
        // Neha teaches BCA III: VB.NET, BCA II: Data Structure, BCA I: Computer Networks
        // Shivani teaches BCA III: Project Lab, BCA II: Discrete Maths, BCA I: Numerical Methods
        // Parminder teaches BCA III: Graphics, BCA II: SPM, BCA I: Operating System

        String s1 = "", s2 = "", s3 = "";
        
        // Dynamic subject assignment based on student's class (simplified logic)
        // Since you wanted them to match the teacher dashboard subjects:
        if (studentName.equals("Riya")) { // BCA I
            s1 = "Computer Networks (Prof. Neha)";
            s2 = "Numerical Methods (Prof. Shivani)";
            s3 = "Operating System (Prof. Parminder)";
        } else if (studentName.equals("Minakshi")) { // BCA II
            s1 = "Data Structure (Prof. Neha)";
            s2 = "Discrete Mathematics (Prof. Shivani)";
            s3 = "Software Project Mgmt (Prof. Parminder)";
        } else { // BCA III (Ritika)
            s1 = "VB.NET (Prof. Neha)";
            s2 = "Project Lab (Prof. Shivani)";
            s3 = "Computer Graphics (Prof. Parminder)";
        }

        updateUI(R.id.tvSubject1, R.id.txtPercentage1, R.id.tvStats1, s1, total, attended);
        updateUI(R.id.tvSubject2, R.id.txtPercentage2, R.id.tvStats2, s2, total, Math.max(0, attended - 2));
        updateUI(R.id.tvSubject3, R.id.txtPercentage3, R.id.tvStats3, s3, total, Math.max(0, attended - 5));
    }

    private void updateUI(int subId, int percentId, int statsId, String name, int total, int present) {
        TextView tvSub = findViewById(subId);
        TextView tvPercent = findViewById(percentId);
        TextView tvStats = findViewById(statsId);

        if (tvSub != null) tvSub.setText(name);
        
        int percent = (present * 100) / total;
        if (tvPercent != null) {
            tvPercent.setText(percent + "%");
            tvPercent.setTextColor(percent >= 75 ? Color.parseColor("#007bff") : Color.parseColor("#FF5252"));
        }
        
        if (tvStats != null) {
            tvStats.setText("Present: " + present + " | Absent: " + (total - present));
        }
    }
}
