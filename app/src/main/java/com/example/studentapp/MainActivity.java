package com.example.studentapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin;
    String URL = "http://10.0.2.2/StudentApp/login.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin   = findViewById(R.id.btnLogin);
        Button btnTeacherLogin = findViewById(R.id.btnTeacherLogin);

        btnTeacherLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, TeacherLoginActivity.class)); // ✅ الصح
        });




        btnLogin.setOnClickListener(view -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    response -> {
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.getString("status").equals("success")) {

                                SharedPreferences sp = getSharedPreferences("student_session", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putInt("id", json.getInt("id"));
                                editor.putString("name", json.getString("full_name"));
                                editor.putString("major", json.getString("major"));
                                editor.apply(); // احفظ التغييرات

                                Toast.makeText(this, "Welcome " + json.getString("full_name"), Toast.LENGTH_LONG).show();

                                String name = json.getString("full_name");

                                startActivity(new Intent(this, StudentHomeActivity.class));
                                finish(); // حتى ما يرجع لواجهة تسجيل الدخول لما يضغط back

                            } else {
                                Toast.makeText(this, json.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(this, "JSON Error", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        Toast.makeText(this, "Volley error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("password", password);
                    return params;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(stringRequest);
  });
}
}