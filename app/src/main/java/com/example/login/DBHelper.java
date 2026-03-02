package com.example.login;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "CampusConnect.db";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
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

        // Dummy users
        db.execSQL("INSERT INTO users(name,email,password,role) VALUES('Ritika','student@gmail.com','1234','student')");
        db.execSQL("INSERT INTO users(name,email,password,role) VALUES('Riya','teacher@gmail.com','1234','teacher')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}
