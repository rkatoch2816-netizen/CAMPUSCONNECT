package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class TeacherDashboard extends AppCompatActivity {

    ArrayList<NoticeModel> noticeData = new ArrayList<>();
    DrawerLayout drawerLayout;
    ImageView btnMenu, btnTeacherLogout;
    Spinner spinnerClass;
    LinearLayout studentContainer, noticeContainer;
    TextView tvTodayDate, tvTeacherWelcome, tvTeacherSubject;
    Button btnSubmitAttendance, btnAddNotice;

    String currentClassName = "BCA I";
    String todayDateStr;
    String teacherEmail, teacherName;

    // Student Data
    String[][] bca1Students = {{"101", "Riya", "BCA I", "60", "Present"}, {"102", "Siya", "BCA I", "58", "Present"}, {"103", "Priya", "BCA I", "55", "Present"}, {"104", "Diya", "BCA I", "62", "Present"}, {"105", "Ruhani", "BCA I", "50", "Absent"}};
    String[][] bca2Students = {{"201", "Minakshi", "BCA II", "55", "Present"}, {"202", "Ashima", "BCA II", "59", "Present"}, {"203", "Megha", "BCA II", "52", "Present"}, {"204", "Yamini", "BCA II", "61", "Present"}, {"205", "Renu", "BCA II", "45", "Absent"}};
    String[][] bca3Students = {{"301", "Ritika", "BCA III", "64", "Present"}, {"302", "Suhani", "BCA III", "54", "Present"}, {"303", "Poonam", "BCA III", "48", "Absent"}, {"304", "Palak", "BCA III", "48", "Absent"}, {"305", "Mansi", "BCA III", "56", "Present"}};
    String[][] currentStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        // Get Teacher Info from Intent
        teacherName = getIntent().getStringExtra("TEACHER_NAME");
        teacherEmail = getIntent().getStringExtra("TEACHER_EMAIL");

        drawerLayout = findViewById(R.id.drawer_layout);
        btnMenu = findViewById(R.id.btnMenu);
        btnTeacherLogout = findViewById(R.id.btnTeacherLogout);
        spinnerClass = findViewById(R.id.spinnerClass);
        studentContainer = findViewById(R.id.studentContainer);
        noticeContainer = findViewById(R.id.noticeContainer);
        tvTodayDate = findViewById(R.id.tvTodayDate);
        tvTeacherWelcome = findViewById(R.id.tvTeacherWelcome);
        tvTeacherSubject = findViewById(R.id.tvTeacherSubject);
        btnSubmitAttendance = findViewById(R.id.btnSubmitAttendance);
        btnAddNotice = findViewById(R.id.btnAddNotice);

        if (teacherName != null) {
            tvTeacherWelcome.setText("Welcome, Prof. " + teacherName);
        }

        todayDateStr = new SimpleDateFormat("dd_MMM_yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());
        tvTodayDate.setText("Date: " + new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime()));

        loadAttendanceFromPrefs();

        // Menu button to open sidebar
        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> {
                if (drawerLayout != null) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        // Logout Button Click
        if (btnTeacherLogout != null) {
            btnTeacherLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    performLogout();
                }
            });
        }

        String[] classes = {"BCA I", "BCA II", "BCA III"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(adapter);

        spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentClassName = classes[position];
                
                // Update Subject display based on teacher and class
                updateSubjectDisplay();

                if (currentClassName.equals("BCA I")) currentStudents = bca1Students;
                else if (currentClassName.equals("BCA II")) currentStudents = bca2Students;
                else currentStudents = bca3Students;
                
                checkIfAlreadySubmitted();
                populateStudents();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnSubmitAttendance.setOnClickListener(v -> {
            SharedPreferences.Editor editor = getSharedPreferences("AttendancePrefs", Context.MODE_PRIVATE).edit();
            for (int i = 0; i < currentStudents.length; i++) {
                if (currentStudents[i][4].equals("Present")) {
                    int count = Integer.parseInt(currentStudents[i][3]);
                    if (count < 65) currentStudents[i][3] = String.valueOf(count + 1);
                }
                // Save specific student attendance
                editor.putString("attendance_" + currentStudents[i][1], currentStudents[i][3]);
            }
            // Use teacher-specific submission key
            String submissionKey = "submitted_" + (teacherEmail != null ? teacherEmail : "unknown") + "_" + currentClassName + "_" + todayDateStr;
            editor.putBoolean(submissionKey, true);
            editor.apply();
            
            checkIfAlreadySubmitted();
            populateStudents();
            Toast.makeText(this, "Attendance Submitted for your subject", Toast.LENGTH_SHORT).show();
        });

        btnAddNotice.setOnClickListener(v -> showAddNoticeDialog());
        
        populateNotices();
    }

    private void performLogout() {
        Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void updateSubjectDisplay() {
        if ("neha@gmail.com".equals(teacherEmail)) {
            if ("BCA I".equals(currentClassName)) tvTeacherSubject.setText("Subject: Computer Networks");
            else if ("BCA II".equals(currentClassName)) tvTeacherSubject.setText("Subject: Data Structure");
            else if ("BCA III".equals(currentClassName)) tvTeacherSubject.setText("Subject: VB.NET");
        } else if ("shivani@gmail.com".equals(teacherEmail)) {
            if ("BCA I".equals(currentClassName)) tvTeacherSubject.setText("Subject: Numerical Methods");
            else if ("BCA II".equals(currentClassName)) tvTeacherSubject.setText("Subject: Discrete Mathematics");
            else if ("BCA III".equals(currentClassName)) tvTeacherSubject.setText("Subject: Project Lab");
        } else if ("parminder@gmail.com".equals(teacherEmail)) {
            if ("BCA I".equals(currentClassName)) tvTeacherSubject.setText("Subject: Operating System");
            else if ("BCA II".equals(currentClassName)) tvTeacherSubject.setText("Subject: Software Project Management");
            else if ("BCA III".equals(currentClassName)) tvTeacherSubject.setText("Subject: Graphics");
        } else {
            tvTeacherSubject.setText("Subject: General Duties");
        }
    }

    private void loadAttendanceFromPrefs() {
        SharedPreferences prefs = getSharedPreferences("AttendancePrefs", Context.MODE_PRIVATE);
        updateArrayFromPrefs(bca1Students, prefs);
        updateArrayFromPrefs(bca2Students, prefs);
        updateArrayFromPrefs(bca3Students, prefs);
    }

    private void updateArrayFromPrefs(String[][] array, SharedPreferences prefs) {
        for (int i = 0; i < array.length; i++) {
            String savedValue = prefs.getString("attendance_" + array[i][1], null);
            if (savedValue != null) array[i][3] = savedValue;
        }
    }

    private void showAddNoticeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Notice");
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_notice, null);
        final EditText etTitle = dialogView.findViewById(R.id.etNoticeTitle);
        final EditText etDesc = dialogView.findViewById(R.id.etNoticeDesc);
        builder.setView(dialogView);
        builder.setPositiveButton("Add", (dialog, which) -> {
            String title = etTitle.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();
            if (!title.isEmpty() && !desc.isEmpty()) saveNotice(title, desc);
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void saveNotice(String title, String desc) {
        String date = new SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Calendar.getInstance().getTime());
        SharedPreferences prefs = getSharedPreferences("NoticePrefs", Context.MODE_PRIVATE);
        try {
            JSONArray jsonArray = new JSONArray(prefs.getString("notices_json", "[]"));
            JSONObject newNotice = new JSONObject();
            newNotice.put("title", title); newNotice.put("date", date); newNotice.put("description", desc);
            jsonArray.put(newNotice);
            prefs.edit().putString("notices_json", jsonArray.toString()).apply();
            populateNotices();
        } catch (JSONException e) { e.printStackTrace(); }
    }

    private void populateNotices() {
        noticeContainer.removeAllViews();
        try {
            JSONArray jsonArray = new JSONArray(getSharedPreferences("NoticePrefs", Context.MODE_PRIVATE).getString("notices_json", "[]"));
            for (int i = jsonArray.length() - 1; i >= 0; i--) {
                JSONObject obj = jsonArray.getJSONObject(i);
                View card = getLayoutInflater().inflate(R.layout.item_notice_card, noticeContainer, false);
                ((TextView) card.findViewById(R.id.tvNoticeTitle)).setText(obj.getString("title"));
                ((TextView) card.findViewById(R.id.tvNoticeDate)).setText(obj.getString("date"));
                ((TextView) card.findViewById(R.id.tvNoticeShortDesc)).setText(obj.getString("description"));
                noticeContainer.addView(card);
            }
        } catch (JSONException e) { e.printStackTrace(); }
    }

    private void checkIfAlreadySubmitted() {
        String submissionKey = "submitted_" + (teacherEmail != null ? teacherEmail : "unknown") + "_" + currentClassName + "_" + todayDateStr;
        boolean isSubmitted = getSharedPreferences("AttendancePrefs", Context.MODE_PRIVATE).getBoolean(submissionKey, false);
        btnSubmitAttendance.setEnabled(!isSubmitted);
        btnSubmitAttendance.setText(isSubmitted ? "Submitted" : "Submit All");
        btnSubmitAttendance.setBackgroundTintList(android.content.res.ColorStateList.valueOf(isSubmitted ? 0xFFBDBDBD : 0xFF2E7D32));
    }

    private void populateStudents() {
        studentContainer.removeAllViews();
        String submissionKey = "submitted_" + (teacherEmail != null ? teacherEmail : "unknown") + "_" + currentClassName + "_" + todayDateStr;
        boolean isLocked = getSharedPreferences("AttendancePrefs", Context.MODE_PRIVATE).getBoolean(submissionKey, false);
        for (int i = 0; i < currentStudents.length; i++) {
            final int idx = i;
            View item = getLayoutInflater().inflate(R.layout.item_student, studentContainer, false);
            ((TextView) item.findViewById(R.id.tvId)).setText(currentStudents[i][0]);
            ((TextView) item.findViewById(R.id.tvName)).setText(currentStudents[i][1]);
            ((TextView) item.findViewById(R.id.tvClass)).setText(currentStudents[i][2]);
            ((TextView) item.findViewById(R.id.tvAttend)).setText(currentStudents[i][3] + "/65");
            TextView tvStatus = item.findViewById(R.id.tvStatus);
            tvStatus.setText(currentStudents[i][4]);
            updateStatusUI(tvStatus, currentStudents[i][4]);
            if (!isLocked) {
                tvStatus.setOnClickListener(v -> {
                    currentStudents[idx][4] = currentStudents[idx][4].equals("Present") ? "Absent" : "Present";
                    tvStatus.setText(currentStudents[idx][4]);
                    updateStatusUI(tvStatus, currentStudents[idx][4]);
                });
            } else { tvStatus.setAlpha(0.6f); }
            studentContainer.addView(item);
        }
    }

    private void updateStatusUI(TextView tvStatus, String status) {
        tvStatus.setBackgroundResource(status.equalsIgnoreCase("Absent") ? R.drawable.bg_status_absent : R.drawable.bg_status_present);
    }
}
