package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StudentDashboard extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        // --- Get Student Name from Intent ---
        String nameFromIntent = getIntent().getStringExtra("STUDENT_NAME");
        final String studentName = (nameFromIntent != null) ? nameFromIntent : "Ritika";

        // --- View Initialization ---
        View cardAttendance = findViewById(R.id.btnAttendanceView);
        View cardFees = findViewById(R.id.btnFeesView);
        View cardNotice = findViewById(R.id.btnNotice);
        ImageView btnLogout = findViewById(R.id.btnStudentLogout);
        TextView tvStudentWelcome = findViewById(R.id.tvStudentName);
        
        if (tvStudentWelcome != null) {
            tvStudentWelcome.setText(studentName + "!");
        }
        
        TextView tvAttendancePercent = findViewById(R.id.tvAttendancePercent);
        TextView tvPresentAbsent = findViewById(R.id.tvPresentAbsent);

        // --- Sync Attendance for the logged-in student ---
        SharedPreferences prefs = getSharedPreferences("AttendancePrefs", Context.MODE_PRIVATE);
        
        // Fetch attendance specifically for the logged-in student name
        String studentAttendance = prefs.getString("attendance_" + studentName, "0");
        
        // Update Attendance UI
        if (tvAttendancePercent != null) {
            int attended = Integer.parseInt(studentAttendance);
            int percent = (attended * 100) / 65;
            tvAttendancePercent.setText(percent + "%");
        }
        if (tvPresentAbsent != null) {
            int attended = Integer.parseInt(studentAttendance);
            int absent = 65 - attended;
            tvPresentAbsent.setText("Present: " + attended + " Days | Absent: " + absent + " Days");
        }

        // --- Click Listeners ---
        if (cardAttendance != null) {
            cardAttendance.setOnClickListener(v -> {
                Intent intent = new Intent(StudentDashboard.this, AttendanceActivity.class);
                intent.putExtra("STUDENT_NAME", studentName);
                startActivity(intent);
            });
        }
        if (cardFees != null) {
            cardFees.setOnClickListener(v -> startActivity(new Intent(StudentDashboard.this, FeesActivity.class)));
        }
        if (cardNotice != null) {
            cardNotice.setOnClickListener(v -> startActivity(new Intent(StudentDashboard.this, MainActivity.class)));
        }
        
        // Final Fix for Student Logout
        if (btnLogout != null) {
            btnLogout.bringToFront(); // Ensure it's not covered by other views
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(StudentDashboard.this, "Logging out...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(StudentDashboard.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}
