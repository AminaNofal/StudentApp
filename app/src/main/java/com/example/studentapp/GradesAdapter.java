package com.example.studentapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GradesAdapter extends RecyclerView.Adapter<GradesAdapter.GradeViewHolder> {

    private List<Grade> gradesList;

    public GradesAdapter(List<Grade> gradesList) {
        this.gradesList = gradesList;
    }

    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grade, parent, false);
        return new GradeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeViewHolder holder, int position) {
        Grade grade = gradesList.get(position);
        holder.textSubject.setText(grade.getSubject());
        holder.textGrade.setText(String.valueOf(grade.getGradeValue()));
        holder.textStudentId.setText("Student ID: " + grade.getStudentId());
    }

    @Override
    public int getItemCount() {
        return gradesList.size();
    }

    static class GradeViewHolder extends RecyclerView.ViewHolder {
        TextView textSubject, textGrade, textStudentId;

        public GradeViewHolder(@NonNull View itemView) {
            super(itemView);
            textSubject = itemView.findViewById(R.id.textSubject);
            textGrade = itemView.findViewById(R.id.textGrade);
            textStudentId = itemView.findViewById(R.id.textStudentId);
        }
    }
}

