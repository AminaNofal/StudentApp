package com.example.studentapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;

public class TeacherMarksActivity extends AppCompatActivity {

    Spinner spinnerSubjects;
    EditText etStudentId, etMark;
    Button btnSubmitMark, btnBack;
    int teacherId;
    List<Subject> subjectList = new ArrayList<>();

    String GET_SUBJECTS_URL = "http://10.0.2.2/StudentApp/get_teacher_subjects.php";
    String SUBMIT_MARK_URL = "http://10.0.2.2/StudentApp/submit_mark.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_marks);

        spinnerSubjects = findViewById(R.id.spinnerSubjects);
        etStudentId = findViewById(R.id.etStudentId);
        etMark = findViewById(R.id.etMark);
        btnSubmitMark = findViewById(R.id.btnSubmitMark);
        btnBack = findViewById(R.id.btnBack);

        SharedPreferences prefs = getSharedPreferences("teacher", MODE_PRIVATE);
        teacherId = prefs.getInt("id", -1);

        if (teacherId == -1) {
            Toast.makeText(this, "Teacher not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadSubjects();

        btnSubmitMark.setOnClickListener(v -> submitMark());

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, TeacherHomeActivity.class));
            finish();
        });
    }

    private void loadSubjects() {
        StringRequest request = new StringRequest(Request.Method.POST, GET_SUBJECTS_URL, response -> {
            try {
                JSONArray arr = new JSONArray(response);
                List<String> subjectNames = new ArrayList<>();

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    Subject subject = new Subject(obj.getInt("id"), obj.getString("name"));
                    subjectList.add(subject);
                    subjectNames.add(subject.getName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item, subjectNames);
                spinnerSubjects.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error loading subjects", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("teacher_id", String.valueOf(teacherId));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void submitMark() {
        String studentId = etStudentId.getText().toString().trim();
        String mark = etMark.getText().toString().trim();

        if (studentId.isEmpty() || mark.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedIndex = spinnerSubjects.getSelectedItemPosition();
        int subjectId = subjectList.get(selectedIndex).getId();

        StringRequest request = new StringRequest(Request.Method.POST, SUBMIT_MARK_URL, response -> {
            Toast.makeText(this, "Mark submitted", Toast.LENGTH_SHORT).show();
            etStudentId.setText("");
            etMark.setText("");
        }, error -> {
            Toast.makeText(this, "Error submitting mark", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", studentId);
                params.put("subject_id", String.valueOf(subjectId));
                params.put("mark", mark);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    static class Subject {
        private int id;
        private String name;

        public Subject(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() { return id; }
        public String getName() { return name; }
    }
}
