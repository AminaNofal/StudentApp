package com.example.studentapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GradesListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GradesAdapter adapter;
    private List<Grade> gradesList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades_list);

        recyclerView = findViewById(R.id.recyclerViewGrades);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        gradesList = new ArrayList<>();
        adapter = new GradesAdapter(gradesList);
        recyclerView.setAdapter(adapter);

        dbHelper = new DatabaseHelper(this);

        loadGradesAsync();
    }

    private void loadGradesAsync() {
        new Thread(() -> {
            List<Grade> grades = dbHelper.getAllGrades();

            runOnUiThread(() -> {
                gradesList.clear();
                gradesList.addAll(grades);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }
}
