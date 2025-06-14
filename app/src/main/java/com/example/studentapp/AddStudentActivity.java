package com.example.studentapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AddStudentActivity extends AppCompatActivity {

    EditText etStudentName, etStudentID, etStudentEmail;
    Spinner spMajor;
    Button btnAddStudent, btnBack;

    private static final String ADD_STUDENT_URL = "http://10.0.2.2/StudentApp/add_student.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        etStudentName = findViewById(R.id.etStudentName);
        etStudentID = findViewById(R.id.etStudentID);
        etStudentEmail = findViewById(R.id.etStudentEmail);
        spMajor = findViewById(R.id.spMajor);
        btnAddStudent = findViewById(R.id.btnAddStudent);
        btnBack = findViewById(R.id.btnBack);

        String[] majors = {"Computer Science", "Engineering", "Business", "Medicine", "Law"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, majors);
        spMajor.setAdapter(adapter);

        btnAddStudent.setOnClickListener(v -> {
            String name = etStudentName.getText().toString().trim();
            String id = etStudentID.getText().toString().trim();
            String email = etStudentEmail.getText().toString().trim();
            String major = spMajor.getSelectedItem().toString();

            if (name.isEmpty() || id.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            StringRequest request = new StringRequest(Request.Method.POST, ADD_STUDENT_URL,
                    response -> {
                        Toast.makeText(this, "Response: " + response, Toast.LENGTH_LONG).show();
                        etStudentName.setText("");
                        etStudentID.setText("");
                        etStudentEmail.setText("");
                        spMajor.setSelection(0);
                    },
                    error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show()
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", name);
                    params.put("student_id", id);
                    params.put("email", email);
                    params.put("major", major);
                    return params;
                }
            };

            Volley.newRequestQueue(this).add(request);
        });

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(AddStudentActivity.this, RegisterOptionsActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
