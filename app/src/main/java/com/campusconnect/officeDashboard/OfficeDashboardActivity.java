package com.campusconnect.officeDashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.login.LoginActivity;
import com.example.login.MainActivity;
import com.example.login.R;
import com.google.android.material.card.MaterialCardView;

public class OfficeDashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_dashboard);

        ImageView btnLogout = findViewById(R.id.btnOfficeLogout);
        MaterialCardView btnManageFees = findViewById(R.id.btnManageFees);
        MaterialCardView btnViewNotices = findViewById(R.id.btnViewNotices);

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                Toast.makeText(this, "Logging out from Office...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }

        if (btnManageFees != null) {
            btnManageFees.setOnClickListener(v -> {
                // For now, staying on same dashboard or opening fragment
                Toast.makeText(this, "Opening Fee Management...", Toast.LENGTH_SHORT).show();
            });
        }

        if (btnViewNotices != null) {
            btnViewNotices.setOnClickListener(v -> {
                // Assuming MainActivity shows notices
                startActivity(new Intent(this, MainActivity.class));
            });
        }
    }
}
