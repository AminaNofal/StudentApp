package com.example.studentapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin, btnLogout, btnTeacherLogin, btnRegistrarLogin;
    TextView tvUserInfo;

    // Server URL
    String URL = "http://10.0.2.2/StudentApp/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind views
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvUserInfo = findViewById(R.id.tvUserInfo);
//        btnLogout = findViewById(R.id.btnLogout);
        btnTeacherLogin = findViewById(R.id.btnTeacherLogin);
        btnRegistrarLogin = findViewById(R.id.btnRegistrarLogin);

        // Retrieve username from session
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        String sessionUsername = prefs.getString("username", "User unknown");
        tvUserInfo.setText("Hello " + sessionUsername);

        // Logout
//        btnLogout.setOnClickListener(v -> {
//            prefs.edit().clear().apply();
//            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
//        });

        // Teacher login
        btnTeacherLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, TeacherLoginActivity.class));
        });

        // Register new role
        btnRegistrarLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, RegistrarLoginActivity.class));
        });
        // Student login
        btnLogin.setOnClickListener(view -> {
            String usernameInput = etUsername.getText().toString().trim();
            String passwordInput = etPassword.getText().toString().trim();

            if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    response -> {
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.getString("status").equals("success")) {

                                // Save student data to session
                                SharedPreferences sp = getSharedPreferences("student_session", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putInt("id", json.getInt("id"));
                                editor.putString("name", json.getString("full_name"));
                                editor.putString("major", json.getString("major"));
                                editor.apply();

                                Toast.makeText(this, "Welcome " + json.getString("full_name"), Toast.LENGTH_LONG).show();
                                startActivity(new Intent(this, StudentHomeActivity.class));
                                finish();

                            } else {
                                Toast.makeText(this, json.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(this, "JSON parsing error", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        Toast.makeText(this, "Network error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", usernameInput);
                    params.put("password", passwordInput);
                    return params;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(stringRequest);
        });
    }
}
