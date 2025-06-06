package com.example.studentapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TeacherLoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin;
    String URL = "http://10.0.2.2/StudentApp/login_teacher.php"; // تأكدي من اسم المسار

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        Button btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(view -> {
            // رجوع إلى MainActivity (شاشة الطالب أو شاشة الاختيار)
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // يمسح الطبقات فوق MainActivity
            startActivity(intent);
            finish();
        });



        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            StringRequest request = new StringRequest(Request.Method.POST, URL, response -> {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("status").equals("success")) {
                        SharedPreferences prefs = getSharedPreferences("teacher", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt("id", obj.getInt("id"));
                        editor.putString("name", obj.getString("full_name"));
                        editor.apply();

                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, TeacherHomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
                }
            }, error -> {
                error.printStackTrace();
                Toast.makeText(this, "Volley error: " + error.toString(), Toast.LENGTH_LONG).show();
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("password", password);
                    return params;
                }
            };

            Volley.newRequestQueue(this).add(request);
        });
    }
}

