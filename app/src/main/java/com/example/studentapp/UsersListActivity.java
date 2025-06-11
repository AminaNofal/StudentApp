package com.example.studentapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UsersListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter  adapter;
    private List<User> userList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_list_activity);

        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // افتراضيًا تحميل بيانات وهمية أو من قاعدة البيانات
        userList = loadUsers();

        adapter = new UserAdapter (this ,userList);
        recyclerView.setAdapter(adapter);
    }

    private List<User> loadUsers() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        return dbHelper.getAllUsers();
    }

}

