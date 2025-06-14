package com.example.studentapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterOptionsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_options);

        Button btnEditUser = findViewById(R.id.btnEditUser);
        Button btnUsersList = findViewById(R.id.btnUsersList);
        Button btnGradesList = findViewById(R.id.btnGradesList);
        Button btnAddStudent = findViewById(R.id.btnAddStudent);
        Button btnAddCourse = findViewById(R.id.btnAddCourse);
        Button btnBack = findViewById(R.id.btnBack);

        btnEditUser.setOnClickListener(v -> startActivity(new Intent(this, EditUserActivity.class)));
        btnUsersList.setOnClickListener(v -> startActivity(new Intent(this, UsersListActivity.class)));
        btnGradesList.setOnClickListener(v -> startActivity(new Intent(this, GradesListActivity.class)));
        btnAddStudent.setOnClickListener(v -> startActivity(new Intent(this, AddStudentActivity.class)));
        btnAddCourse.setOnClickListener(v -> startActivity(new Intent(this, com.example.studentapp.ui.AddCourseActivity.class)));
        
        btnBack.setOnClickListener(v -> {
            finish(); // العودة للشاشة السابقة
        });
    }
}