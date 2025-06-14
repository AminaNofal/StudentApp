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

public class RegistrarLoginActivity extends AppCompatActivity {
    EditText etUsername, etPassword;
    Button btnLogin;

    private static final String LOGIN_URL = "http://10.0.2.2/studentapp/login_register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {

       String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            Toast.makeText(this, username, Toast.LENGTH_SHORT).show();


            if (!username.isEmpty() && !password.isEmpty()) {
                Toast.makeText(this, "123", Toast.LENGTH_SHORT).show();
                new LoginTask().execute(username, password);
            } else {
                Toast.makeText(this, "Please # fill all fields", Toast.LENGTH_SHORT).show();
            }
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
                //return e.toString();
                return "{\"status\":\"error\",\"message\":\"Connection error11\"}";

            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json = new JSONObject(result);
                String status = json.getString("status");

                if (status.equals("success")) {
                    // إذا نجح الدخول، انتقل إلى RegisterOptionsActivity
                    Intent intent = new Intent(RegistrarLoginActivity.this, RegisterOptionsActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    String message = json.getString("message");
                    Toast.makeText(RegistrarLoginActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(RegistrarLoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}