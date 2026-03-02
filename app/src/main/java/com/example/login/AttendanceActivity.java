package com.example.login;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AttendanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        // VB.Net Calculation (80%)
        updateAttendance(R.id.txtPercentageVB, 20, 16);

        // Computer Graphics Calculation (75%)
        updateAttendance(R.id.txtPercentageCG, 20, 15);

        // E-commerce Calculation (45%)
        updateAttendance(R.id.txtPercentageEC, 20, 9);
    }

    private void updateAttendance(int textViewId, int total, int present) {
        TextView percentageTv = findViewById(textViewId);
        if (percentageTv != null) {
            int percent = (present * 100) / total;
            percentageTv.setText(percent + "%");

            if (percent >= 75) {
                percentageTv.setTextColor(Color.parseColor("#2E7D32")); // Green
            } else {
                percentageTv.setTextColor(Color.parseColor("#D32F2F")); // Red
            }
        }
    }
}
