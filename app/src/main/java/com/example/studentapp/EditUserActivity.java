package com.example.studentapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class EditUserActivity extends AppCompatActivity {

    EditText etUsername;
    Button btnSave;

    int userId;
    String originalUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user);
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        etUsername = findViewById(R.id.etUsername);
        btnSave = findViewById(R.id.btnSave);

        // استقبال البيانات من الـ Intent
        Intent intent = getIntent();
        userId = intent.getIntExtra("user_id", -1);
        originalUsername = intent.getStringExtra("username");

        etUsername.setText(originalUsername);

        btnSave.setOnClickListener(v -> updateUser());
    }

    private void updateUser() {
        String updatedUsername = etUsername.getText().toString().trim();
        if (updatedUsername.isEmpty()) {
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2/StudentApp/update_user.php"; // تأكد من وجود هذا الملف في السيرفر

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show();
                    finish(); // العودة للشاشة السابقة
                },
                error -> Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(userId));
                params.put("username", updatedUsername);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}
