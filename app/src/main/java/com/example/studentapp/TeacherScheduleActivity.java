package com.example.studentapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeacherScheduleActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Schedule> scheduleList;
    ScheduleAdapter adapter;
    String URL = "http://10.0.2.2/StudentApp/get_teacher_schedule.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_schedule);
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            finish(); // أو startActivity(new Intent(this, TeacherHomeActivity.class));
        });

        recyclerView = findViewById(R.id.recyclerViewSchedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        scheduleList = new ArrayList<>();

        // استرجاع id المعلم من SharedPreferences
        SharedPreferences sp = getSharedPreferences("teacher", MODE_PRIVATE);
        int teacherId = sp.getInt("id", -1);

        if (teacherId == -1) {
            Toast.makeText(this, "Teacher not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // الطلب من السيرفر
        StringRequest request = new StringRequest(Request.Method.POST, URL, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);

                if (jsonObject.getString("status").equals("success")) {
                    JSONArray arr = jsonObject.getJSONArray("data");

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);

                        String subject = obj.getString("subject_name");
                        String day = obj.getString("day");
                        String time = obj.getString("time");
                        String location = obj.getString("location");

                        scheduleList.add(new Schedule(subject, day, time, location));
                    }

                    adapter = new ScheduleAdapter(scheduleList);
                    recyclerView.setAdapter(adapter);
                } else {
                    String message = jsonObject.getString("message");
                    Toast.makeText(this, "Server error: " + message, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error parsing data", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            error.printStackTrace();
            Toast.makeText(this, "Volley Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new java.util.HashMap<>();
                params.put("teacher_id", String.valueOf(teacherId));
                return params;
            }
        };


        Volley.newRequestQueue(this).add(request);
    }
}
