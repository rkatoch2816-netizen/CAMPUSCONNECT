package com.example.login;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "CampusConnect.db";
    public static final int DB_VERSION = 4;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT, password TEXT, role TEXT)");
        db.execSQL("CREATE TABLE students(student_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, class TEXT, roll_no TEXT, phone TEXT)");
        db.execSQL("CREATE TABLE attendance(id INTEGER PRIMARY KEY AUTOINCREMENT, student_id INTEGER, date TEXT, status TEXT)");
        db.execSQL("CREATE TABLE fees(id INTEGER PRIMARY KEY AUTOINCREMENT, student_id INTEGER, total_fees INTEGER, paid INTEGER, due INTEGER)");
        db.execSQL("CREATE TABLE notice(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT, date TEXT)");
        db.execSQL("CREATE TABLE assignment(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT, last_date TEXT)");
        db.execSQL("CREATE TABLE results(id INTEGER PRIMARY KEY AUTOINCREMENT, student_id INTEGER, subject TEXT, marks INTEGER)");

        // Default Users
        db.execSQL("INSERT INTO users(name,email,password,role) VALUES('Ritika','student@gmail.com','1234','student')");
        db.execSQL("INSERT INTO users(name,email,password,role) VALUES('Riya','teacher@gmail.com','1234','teacher')");
        
        // New Teachers
        db.execSQL("INSERT INTO users(name,email,password,role) VALUES('Neha','neha@gmail.com','1234','teacher')");
        db.execSQL("INSERT INTO users(name,email,password,role) VALUES('Shivani','shivani@gmail.com','1234','teacher')");
        db.execSQL("INSERT INTO users(name,email,password,role) VALUES('Parminder','parminder@gmail.com','1234','teacher')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS students");
        db.execSQL("DROP TABLE IF EXISTS attendance");
        db.execSQL("DROP TABLE IF EXISTS fees");
        db.execSQL("DROP TABLE IF EXISTS notice");
        db.execSQL("DROP TABLE IF EXISTS assignment");
        db.execSQL("DROP TABLE IF EXISTS results");
        onCreate(db);
    }
}
