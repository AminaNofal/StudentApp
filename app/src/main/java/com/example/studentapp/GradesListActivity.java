package com.example.studentapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class GradesListActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> gradesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades_list);

        listView = findViewById(R.id.listViewGrades);

        String url = "http://10.0.2.2/StudentApp/get_grades.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        Log.d("GRADES_RESPONSE", response);
                        JSONArray grades = new JSONArray(response);
                        for (int i = 0; i < grades.length(); i++) {
                            JSONObject grade = grades.getJSONObject(i);
                            String info = "Student: " + grade.getString("student_name") + ", Grade: " + grade.getString("grade");
                            gradesList.add(info);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gradesList);
                        listView.setAdapter(adapter);
                    } catch (Exception e) {
                        Toast.makeText(this, "Error parsing grades", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show()
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}