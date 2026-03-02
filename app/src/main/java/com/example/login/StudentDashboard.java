package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StudentDashboard extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        // --- Notice Board ---
        LinearLayout btnNotice = findViewById(R.id.btnNotice);
        if (btnNotice != null) {
            btnNotice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StudentDashboard.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }

        // --- Attendance Tracker ---
        Button btnAttendanceView = findViewById(R.id.btnAttendanceView);
        if (btnAttendanceView != null) {
            btnAttendanceView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StudentDashboard.this, AttendanceActivity.class);
                    startActivity(intent);
                }
            });
        }

        // --- Fees Tracker ---
        Button btnFeesView = findViewById(R.id.btnFeesView);
        if (btnFeesView != null) {
            btnFeesView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StudentDashboard.this, FeesActivity.class);
                    startActivity(intent);
                }
            });
        }

        // --- Logout Logic (Using Power Icon ID: btnStudentLogout) ---
        View btnLogout = findViewById(R.id.btnStudentLogout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(StudentDashboard.this, "Logging out...", Toast.LENGTH_SHORT).show();
                    
                    Intent intent = new Intent(StudentDashboard.this, LoginActivity.class);
                    // Clear the entire activity history
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}
