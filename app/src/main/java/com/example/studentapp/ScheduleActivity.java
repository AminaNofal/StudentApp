package com.example.studentapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleActivity extends AppCompatActivity {

    RecyclerView recyclerSchedule;
    List<Schedule> scheduleList;
    String URL = "http://10.0.2.2/student_api/get_schedule.php"; // تأكد من المسار

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        recyclerSchedule = findViewById(R.id.recyclerSchedule);
        recyclerSchedule.setLayoutManager(new LinearLayoutManager(this));

        scheduleList = new ArrayList<>();

        SharedPreferences sp = getSharedPreferences("student_session", MODE_PRIVATE);
        int studentId = sp.getInt("id", -1);

        if (studentId == -1) {
            Toast.makeText(this, "Missing student session", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getString("status").equals("success")) {
                            JSONArray data = json.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject obj = data.getJSONObject(i);
                                scheduleList.add(new Schedule(
                                        obj.getString("subject_name"),
                                        obj.getString("day"),
                                        obj.getString("time"),
                                        obj.getString("location")
                                ));
                            }
                            ScheduleAdapter adapter = new ScheduleAdapter(scheduleList);
                            recyclerSchedule.setAdapter(adapter);
                        } else {
                            Toast.makeText(this, "Error loading schedule", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "JSON Error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Volley Error: " + error.getMessage(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", String.valueOf(studentId));
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
