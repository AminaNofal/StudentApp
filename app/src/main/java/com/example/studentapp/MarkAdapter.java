package com.example.studentapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MarkAdapter extends RecyclerView.Adapter<MarkAdapter.MarkViewHolder> {

    List<Mark> markList;

    public MarkAdapter(List<Mark> markList) {
        this.markList = markList;
    }

    @NonNull
    @Override
    public MarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mark, parent, false);
        return new MarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarkViewHolder holder, int position) {
        Mark mark = markList.get(position);
        holder.tvSubject.setText(mark.subjectName);
        holder.tvExamType.setText(mark.examType);
        holder.tvMark.setText(String.valueOf(mark.mark));
    }

    @Override
    public int getItemCount() {
        return markList.size();
    }

    static class MarkViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubject, tvExamType, tvMark;

        public MarkViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvExamType = itemView.findViewById(R.id.tvExamType);
            tvMark = itemView.findViewById(R.id.tvMark);
        }
    }
}
