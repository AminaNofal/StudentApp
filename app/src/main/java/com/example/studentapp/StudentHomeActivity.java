package com.example.studentapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StudentHomeActivity extends AppCompatActivity {

    TextView tvWelcome;
    Button btnLogout, btnViewSchedule, btnViewMarks, btnUploadAssignment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        tvWelcome = findViewById(R.id.tvWelcome);
        btnLogout = findViewById(R.id.btnLogout);
        btnViewSchedule = findViewById(R.id.btnViewSchedule);
        btnViewMarks = findViewById(R.id.btnViewMarks);
        btnUploadAssignment = findViewById(R.id.btnUploadAssignment); // ✅ حطيتها هون بعد setContentView

        // جلب اسم الطالب من الجلسة
        SharedPreferences sp = getSharedPreferences("student_session", MODE_PRIVATE);
        String name = sp.getString("name", "Student");
        tvWelcome.setText("Welcome " + name);

        // زر عرض الجدول
        btnViewSchedule.setOnClickListener(view -> {
            startActivity(new Intent(this, ScheduleActivity.class));
        });

        // زر عرض العلامات
        btnViewMarks.setOnClickListener(view -> {
            startActivity(new Intent(this, MarksActivity.class));
        });

        // زر رفع الواجب
        btnUploadAssignment.setOnClickListener(view -> {
            startActivity(new Intent(this, UploadAssignmentActivity.class));
        });

        // زر تسجيل الخروج
        btnLogout.setOnClickListener(view -> {
            sp.edit().clear().apply();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}
