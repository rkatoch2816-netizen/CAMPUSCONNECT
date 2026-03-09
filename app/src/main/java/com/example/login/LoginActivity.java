package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.campusconnect.officeDashboard.OfficeDashboardActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {

    AutoCompleteTextView etEmail;
    EditText etPassword;
    CheckBox cbRememberMe;
    Button btnLogin;
    DBHelper dbHelper;
    SQLiteDatabase db;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASS = "password";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_ALL_EMAILS = "all_emails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new DBHelper(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        btnLogin = findViewById(R.id.btnLogin);

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.getBoolean(KEY_REMEMBER, false)) {
            etEmail.setText(sharedPreferences.getString(KEY_EMAIL, ""));
            etPassword.setText(sharedPreferences.getString(KEY_PASS, ""));
            cbRememberMe.setChecked(true);
        }

        setupSuggestions();
        db = dbHelper.getReadableDatabase();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim().toLowerCase();
                String pass = etPassword.getText().toString().trim();

                if (email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter credentials", Toast.LENGTH_SHORT).show();
                    return;
                }

                String roleFound = null;
                String userName = "";

                // 1. Direct Hardcoded Check (Updated as per request)
                if (email.equals("ritika@gmail.com") && pass.equals("1234")) {
                    roleFound = "student";
                    userName = "Ritika";
                } else if (email.equals("riya@gmail.com") && pass.equals("1234")) {
                    roleFound = "student";
                    userName = "Riya";
                } else if (email.equals("minakshi@gmail.com") && pass.equals("1234")) {
                    roleFound = "student";
                    userName = "Minakshi";
                } else if (email.equals("neha@gmail.com") && pass.equals("1234")) {
                    roleFound = "teacher";
                    userName = "Neha";
                } else if (email.equals("shivani@gmail.com") && pass.equals("1234")) {
                    roleFound = "teacher";
                    userName = "Shivani";
                } else if (email.equals("parminder@gmail.com") && pass.equals("1234")) {
                    roleFound = "teacher";
                    userName = "Parminder";
                } else if (email.equals("office@gmail.com") && pass.equals("1234")) {
                    roleFound = "office";
                    userName = "Office Admin";
                } else {
                    // 2. Database Check
                    Cursor cursor = db.rawQuery(
                            "SELECT role, name FROM users WHERE LOWER(email)=? AND password=?",
                            new String[]{email, pass});
                    if (cursor.moveToFirst()) {
                        roleFound = cursor.getString(0);
                        userName = cursor.getString(1);
                    }
                    cursor.close();
                }

                if (roleFound != null) {
                    saveEmailToHistory(email);
                    if (cbRememberMe.isChecked()) {
                        editor.putString(KEY_EMAIL, email);
                        editor.putString(KEY_PASS, pass);
                        editor.putBoolean(KEY_REMEMBER, true);
                    } else {
                        editor.remove(KEY_EMAIL);
                        editor.remove(KEY_PASS);
                        editor.putBoolean(KEY_REMEMBER, false);
                    }
                    editor.apply();

                    if (roleFound.equalsIgnoreCase("student")) {
                        Intent intent = new Intent(LoginActivity.this, StudentDashboard.class);
                        intent.putExtra("STUDENT_NAME", userName);
                        startActivity(intent);
                    } else if (roleFound.equalsIgnoreCase("office")) {
                        startActivity(new Intent(LoginActivity.this, OfficeDashboardActivity.class));
                    } else {
                        Intent intent = new Intent(LoginActivity.this, TeacherDashboard.class);
                        intent.putExtra("TEACHER_NAME", userName);
                        intent.putExtra("TEACHER_EMAIL", email);
                        startActivity(intent);
                    }
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupSuggestions() {
        Set<String> emailSet = sharedPreferences.getStringSet(KEY_ALL_EMAILS, new HashSet<>());
        if (emailSet != null && !emailSet.isEmpty()) {
            ArrayList<String> emailList = new ArrayList<>(emailSet);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_dropdown_item_1line, emailList);
            etEmail.setAdapter(adapter);
        }
    }

    private void saveEmailToHistory(String email) {
        Set<String> emailSet = sharedPreferences.getStringSet(KEY_ALL_EMAILS, new HashSet<>());
        Set<String> newEmailSet = new HashSet<>(emailSet);
        newEmailSet.add(email);
        editor.putStringSet(KEY_ALL_EMAILS, newEmailSet);
        editor.apply();
    }
}
