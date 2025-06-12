package com.example.studentapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    Button btnAddStudent, btnAddCourse, btnLogout, btnBack, btnEditUsers, btnViewGrades, btnViewUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        btnAddStudent = findViewById(R.id.btnAddStudent);
        btnAddCourse = findViewById(R.id.btnAddCourse);
        btnLogout = findViewById(R.id.btnLogout);
        btnBack = findViewById(R.id.btnBack);
        btnEditUsers = findViewById(R.id.btnEditUsers);
        btnViewGrades = findViewById(R.id.btnViewGrades);
        btnViewUsers = findViewById(R.id.btnViewUsers);

        btnAddStudent.setOnClickListener(v ->
                startActivity(new Intent(this, AddStudentActivity.class)));

        btnAddCourse.setOnClickListener(v ->
                startActivity(new Intent(this, AddCourseActivity.class)));

        btnEditUsers.setOnClickListener(v ->
                startActivity(new Intent(this, EditUserActivity.class)));

        btnViewGrades.setOnClickListener(v ->
                startActivity(new Intent(this, GradesListActivity.class)));

        btnViewUsers.setOnClickListener(v ->
                startActivity(new Intent(this, UsersListActivity.class)));

        btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("admin_login", MODE_PRIVATE);
            prefs.edit().clear().apply();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
