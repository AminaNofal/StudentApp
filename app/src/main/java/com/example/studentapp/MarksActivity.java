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

public class MarksActivity extends AppCompatActivity {

    RecyclerView recyclerMarks;
    List<Mark> markList;
    MarkAdapter adapter;

    String URL = "http://10.0.2.2/StudentApp/get_marks.php"; // رابط PHP داخل XAMPP

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks);

        recyclerMarks = findViewById(R.id.recyclerMarks);
        recyclerMarks.setLayoutManager(new LinearLayoutManager(this));

        markList = new ArrayList<>();
        adapter = new MarkAdapter(markList);
        recyclerMarks.setAdapter(adapter);

        // جلب id الطالب من SharedPreferences
        SharedPreferences sp = getSharedPreferences("student_session", MODE_PRIVATE);
        int studentId = sp.getInt("id", -1);

        if (studentId == -1) {
            Toast.makeText(this, "Session not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // جلب البيانات من السيرفر
        fetchMarksFromServer(studentId);
    }

    private void fetchMarksFromServer(int studentId) {
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getString("status").equals("success")) {
                            JSONArray data = json.getJSONArray("data");

                            markList.clear(); // حذف القديم
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject obj = data.getJSONObject(i);
                                String subject = obj.getString("subject");
                                String exam = obj.getString("exam");
                                double mark = obj.getDouble("mark");

                                markList.add(new Mark(subject, exam, mark));
                            }

                            adapter.notifyDataSetChanged(); // تحديث الشاشة
                        } else {
                            Toast.makeText(this, "No marks found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Parsing error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Volley error: " + error.getMessage(), Toast.LENGTH_LONG).show()
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
