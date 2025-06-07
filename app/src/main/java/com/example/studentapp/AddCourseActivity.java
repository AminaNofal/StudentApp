package com.example.studentapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class AddCourseActivity extends AppCompatActivity {

    EditText etCourseName, etCourseCode;
    Spinner spDepartment;
    Button btnAddCourse, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        etCourseName = findViewById(R.id.etCourseName);
        etCourseCode = findViewById(R.id.etCourseCode);
        spDepartment = findViewById(R.id.spDepartment);
        btnAddCourse = findViewById(R.id.btnAddCourse);
        btnBack = findViewById(R.id.btnBack);
        String[] departments = {
                "Bemiheh206", "Bemieh308", "Masrye109", "Masrye306", "Masry107",
                "Shaheen105", "Shaheen116", "Shwqqy Shahen 109", "Shwqqy Shahen 209"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, departments);
        spDepartment.setAdapter(adapter);

        btnAddCourse.setOnClickListener(v -> {
            String courseName = etCourseName.getText().toString().trim();
            String courseCode = etCourseCode.getText().toString().trim();
            String department = spDepartment.getSelectedItem() != null ? spDepartment.getSelectedItem().toString() : "";

            if (courseName.isEmpty() || courseCode.isEmpty() || department.isEmpty()) {
                Toast.makeText(AddCourseActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                new AddCourseTask().execute(courseName, courseCode, department);
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private class AddCourseTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String courseName = params[0];
            String courseCode = params[1];
            String department = params[2];

            try {
                URL url = new URL("http://10.0.2.2/StudentApp/add_course.php\n");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                String data =
                        URLEncoder.encode("course_name", "UTF-8") + "=" + URLEncoder.encode(courseName, "UTF-8") + "&" +
                                URLEncoder.encode("course_code", "UTF-8") + "=" + URLEncoder.encode(courseCode, "UTF-8") + "&" +
                                URLEncoder.encode("department", "UTF-8") + "=" + URLEncoder.encode(department, "UTF-8");

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(data);
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    return sb.toString();
                } else {
                    return "Server returned response code: " + responseCode;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.toLowerCase().contains("success")) {
                Toast.makeText(AddCourseActivity.this, "Course added successfully!", Toast.LENGTH_SHORT).show();
                etCourseName.setText("");
                etCourseCode.setText("");
                spDepartment.setSelection(0);
            } else {
                Toast.makeText(AddCourseActivity.this, "Failed to add course: " + result, Toast.LENGTH_LONG).show();
            }
        }
    }
}
