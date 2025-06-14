package com.example.studentapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class EditUserActivity extends AppCompatActivity {
    EditText etUserId, etUserName;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user);

        etUserId = findViewById(R.id.etUserId);
        etUserName = findViewById(R.id.etUsername);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            String userId = etUserId.getText().toString();
            String userName = etUserName.getText().toString();

            String url = "http://10.0.2.2/StudentApp/update_user.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> Toast.makeText(this, "User updated!", Toast.LENGTH_SHORT).show(),
                    error -> Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show()
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", userId);
                    params.put("name", userName);
                    return params;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(stringRequest);
        });
    }
}