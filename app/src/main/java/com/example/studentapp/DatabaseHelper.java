package com.example.studentapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "university_system";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT"
                + ")";

        String CREATE_GRADES_TABLE = "CREATE TABLE grades ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "student_id INTEGER,"
                + "subject TEXT,"
                + "grade REAL,"
                + "FOREIGN KEY(student_id) REFERENCES users(id)"
                + ")";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_GRADES_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // ميثود لجلب جميع المستخدمين من قاعدة البيانات
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));

                userList.add(new User(id, name, email));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return userList;
    }
    public List<Grade> getAllGrades() {
        List<Grade> gradesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("grades", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int studentId = cursor.getInt(cursor.getColumnIndexOrThrow("student_id"));
                String subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
                float gradeValue = cursor.getFloat(cursor.getColumnIndexOrThrow("grade"));

                gradesList.add(new Grade(id, studentId, subject, gradeValue));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return gradesList;
    }

}
