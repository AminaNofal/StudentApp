package com.example.studentapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UploadAssignmentActivity extends AppCompatActivity {

    Spinner spinnerSubjects;
    EditText etNote;
    Button btnChooseFile, btnUpload;
    TextView tvFileName;

    Uri selectedFileUri = null;
    String selectedSubjectId = "";
    int studentId;

    List<Subject> subjectList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_assignment);

        spinnerSubjects = findViewById(R.id.spinnerSubjects);
        etNote = findViewById(R.id.etNote);
        btnChooseFile = findViewById(R.id.btnChooseFile);
        btnUpload = findViewById(R.id.btnUpload);
        tvFileName = findViewById(R.id.tvFileName);

        SharedPreferences sp = getSharedPreferences("student_session", MODE_PRIVATE);
        studentId = sp.getInt("id", -1);

        // بيانات مؤقتة للمادة
        subjectList.add(new Subject("1", "Data Structures"));
        subjectList.add(new Subject("2", "Database Systems"));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                getSubjectNames(subjectList)
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubjects.setAdapter(adapter);

        spinnerSubjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSubjectId = subjectList.get(position).id;
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnChooseFile.setOnClickListener(v -> pickFile());

        btnUpload.setOnClickListener(v -> {
            if (selectedFileUri == null) {
                Toast.makeText(this, "Please choose a file first", Toast.LENGTH_SHORT).show();
                return;
            }

            String note = etNote.getText().toString().trim();
            uploadFileToServer(studentId, selectedSubjectId, note, selectedFileUri);
        });
    }

    private void pickFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // أو "application/pdf" لو بدك تقصرها
        startActivityForResult(Intent.createChooser(intent, "Select File"), 1001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            selectedFileUri = data.getData();

            String fileName = getFileName(selectedFileUri);
            tvFileName.setText(fileName);
        }
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = "unknown";
        try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
        }
        return result;
    }

    private List<String> getSubjectNames(List<Subject> list) {
        List<String> names = new ArrayList<>();
        for (Subject s : list) names.add(s.name);
        return names;
    }

    private void uploadFileToServer(int studentId, String subjectId, String note, Uri fileUri) {
        String uploadUrl = "http://10.0.2.2/student_api/upload_assignment.php";
        try {
            InputStream iStream = getContentResolver().openInputStream(fileUri);
            byte[] inputData = new byte[iStream.available()];
            iStream.read(inputData);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(
                    Request.Method.POST,
                    uploadUrl,
                    response -> Toast.makeText(this, "Uploaded successfully!", Toast.LENGTH_SHORT).show(),
                    error -> Toast.makeText(this, "Upload error: " + error.getMessage(), Toast.LENGTH_LONG).show(),
                    new HashMap<String, String>() {{
                        put("student_id", String.valueOf(studentId));
                        put("subject_id", subjectId);
                        put("note", note);
                    }},
                    new HashMap<String, VolleyMultipartRequest.DataPart>() {{
                        put("file", new VolleyMultipartRequest.DataPart(getFileName(fileUri), inputData, "application/octet-stream"));
                    }}
            );

            Volley.newRequestQueue(this).add(volleyMultipartRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error reading file", Toast.LENGTH_SHORT).show();
        }


    }

    static class Subject {
        String id, name;
        Subject(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
