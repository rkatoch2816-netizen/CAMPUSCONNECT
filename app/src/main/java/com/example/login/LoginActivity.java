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

        // 1. Auto-fill logic for "Remember Me"
        if (sharedPreferences.getBoolean(KEY_REMEMBER, false)) {
            etEmail.setText(sharedPreferences.getString(KEY_EMAIL, ""));
            etPassword.setText(sharedPreferences.getString(KEY_PASS, ""));
            cbRememberMe.setChecked(true);
        }

        // 2. Suggestion logic: Provide list of previously used emails
        setupSuggestions();

        db = dbHelper.getReadableDatabase();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String pass = etPassword.getText().toString();

                Cursor cursor = db.rawQuery(
                        "SELECT role FROM users WHERE email=? AND password=?",
                        new String[]{email, pass});

                if (cursor.moveToFirst()) {
                    String role = cursor.getString(0);

                    // Save this email to the list of suggestions for next time
                    saveEmailToHistory(email);

                    // Handle Remember Me logic
                    if (cbRememberMe.isChecked()) {
                        editor.putString(KEY_EMAIL, email);
                        editor.putString(KEY_PASS, pass);
                        editor.putBoolean(KEY_REMEMBER, true);
                    } else {
                        // Clear specific "Remember Me" info but keep suggestion list
                        editor.remove(KEY_EMAIL);
                        editor.remove(KEY_PASS);
                        editor.putBoolean(KEY_REMEMBER, false);
                    }
                    editor.apply();

                    if (role.equals("student")) {
                        startActivity(new Intent(LoginActivity.this, StudentDashboard.class));
                    } else {
                        startActivity(new Intent(LoginActivity.this, TeacherDashboard.class));
                    }
                    finish(); 
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Login", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
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
        // Create a copy to modify
        Set<String> newEmailSet = new HashSet<>(emailSet);
        newEmailSet.add(email);
        editor.putStringSet(KEY_ALL_EMAILS, newEmailSet);
        editor.apply();
    }
}
