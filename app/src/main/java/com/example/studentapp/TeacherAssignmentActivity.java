package com.example.studentapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import android.app.DatePickerDialog;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;

public class TeacherAssignmentActivity extends AppCompatActivity {

    Spinner spinnerSubjects;
    EditText etTitle, etDescription;
    Button btnSend, btnBack;
    int teacherId;
    EditText etDueDate;

    List<Subject> subjectList = new ArrayList<>();

    String GET_SUBJECTS_URL = "http://10.0.2.2/StudentApp/get_teacher_subjects.php";
    String SUBMIT_ASSIGNMENT_URL = "http://10.0.2.2/StudentApp/submit_assignment.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_assignment);

        spinnerSubjects = findViewById(R.id.spinnerSubjects);
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        btnSend = findViewById(R.id.btnSend);
        btnBack = findViewById(R.id.btnBack);
        etDueDate = findViewById(R.id.etDueDate);

        etDueDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String date = selectedYear + "-" + String.format("%02d", (selectedMonth + 1)) + "-" + String.format("%02d", selectedDay);
                        etDueDate.setText(date);
                    }, year, month, day);
            datePickerDialog.show();
        });

        SharedPreferences prefs = getSharedPreferences("teacher", MODE_PRIVATE);
        teacherId = prefs.getInt("id", -1);

        if (teacherId == -1) {
            Toast.makeText(this, "Teacher not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadSubjects();

        btnSend.setOnClickListener(v -> sendAssignment());

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

    private void sendAssignment() {
        String title = etTitle.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();
        String dueDate = etDueDate.getText().toString().trim();

        if (title.isEmpty() || desc.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedIndex = spinnerSubjects.getSelectedItemPosition();
        int subjectId = subjectList.get(selectedIndex).getId();

        StringRequest request = new StringRequest(Request.Method.POST, SUBMIT_ASSIGNMENT_URL, response -> {
            Toast.makeText(this, "Assignment sent!", Toast.LENGTH_SHORT).show();
            etTitle.setText("");
            etDescription.setText("");
            etDueDate.setText(""); // Clear due date
        }, error -> {
            Toast.makeText(this, "Error submitting assignment", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("subject_id", String.valueOf(subjectId));
                params.put("teacher_id", String.valueOf(teacherId));
                params.put("title", title);
                params.put("description", desc);
                params.put("file_name", "");
                params.put("due_date", dueDate);
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
