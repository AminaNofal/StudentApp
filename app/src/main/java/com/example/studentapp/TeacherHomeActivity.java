package com.example.studentapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherHomeActivity extends AppCompatActivity {

    TextView tvWelcome;
    Button btnSchedule, btnMarks, btnAssignments, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);

        tvWelcome = findViewById(R.id.tvWelcome);
        btnSchedule = findViewById(R.id.btnSchedule);
        btnMarks = findViewById(R.id.btnMarks);
        btnAssignments = findViewById(R.id.btnAssignments);
        btnLogout = findViewById(R.id.btnLogout);

        // احضار اسم المعلم من SharedPreferences
        SharedPreferences prefs = getSharedPreferences("teacher", MODE_PRIVATE);
        String teacherName = prefs.getString("name", "Teacher");
        tvWelcome.setText("Welcome, " + teacherName + "!");

        // فتح شاشة الجدول
        btnSchedule.setOnClickListener(v ->
                startActivity(new Intent(this, TeacherScheduleActivity.class))
        );

        // فتح شاشة العلامات
        btnMarks.setOnClickListener(v -> {
            startActivity(new Intent(this, TeacherMarksActivity.class));
        });

        btnAssignments.setOnClickListener(v -> {
            startActivity(new Intent(this, TeacherAssignmentActivity.class));
        });

        // تسجيل الخروج
        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(this, TeacherLoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
