package com.example.studentapp;


import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// تأكد أنك تملك كلاس User يحتوي على getId() و getUsername()
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<User> userList;
    private Context context;

    public UserAdapter(Context ctx, List<User> users) {
        this.context = ctx;
        this.userList = users;
    }


    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername;
        Button btnEdit, btnDelete;

        public UserViewHolder(View view) {
            super(view);
            tvUsername = view.findViewById(R.id.tvUsername);
            btnEdit = view.findViewById(R.id.btnEdit);
            btnDelete = view.findViewById(R.id.btnDelete);
        }
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.tvUsername.setText(user.getUsername());

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("تأكيد الحذف")
                    .setMessage("هل أنت متأكد من حذف المستخدم؟")
                    .setPositiveButton("نعم", (d, i) -> {
                        deleteUserFromServer(user.getId());
                    })
                    .setNegativeButton("إلغاء", null)
                    .show();
        });

        holder.btnEdit.setOnClickListener(v -> {
            // تنفيذ التعديل
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    private void deleteUserFromServer(int userId) {
        // هنا تضع كود حذف المستخدم باستخدام Volley
    }
}

