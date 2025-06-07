package com.example.studentapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin, btnBack;

    private static final String LOGIN_URL = "http://10.0.2.2:80/studentapp/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnBack = findViewById(R.id.btnBack);
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                new LoginTask().execute(username, password);
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    class LoginTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String username = params[0];
                String password = params[1];

                URL url = new URL(LOGIN_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                String postData = "username=" + URLEncoder.encode(username, "UTF-8") +
                        "&password=" + URLEncoder.encode(password, "UTF-8");

                OutputStream os = new BufferedOutputStream(conn.getOutputStream());
                os.write(postData.getBytes());
                os.flush();
                os.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                conn.disconnect();

                return response.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return "{\"status\":\"error\",\"message\":\"Connection error\"}";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json = new JSONObject(result);
                String status = json.getString("status");

                if (status.equals("success")) {
                    String fullName = json.getString("full_name");
                    String major = json.getString("major");
                    String id = json.getString("id");

                    Toast.makeText(LoginActivity.this, "Welcome, " + fullName, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                    intent.putExtra("full_name", fullName);
                    intent.putExtra("major", major);
                    intent.putExtra("student_id", id);
                    startActivity(intent);
                    finish();
                } else {
                    String message = json.getString("message");
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

