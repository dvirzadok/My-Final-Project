package com.example.myfinalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TraineeAdapter extends RecyclerView.Adapter<TraineeAdapter.TraineeViewHolder> {

    private List<Trainee> trainees;
    private Context context;

    public TraineeAdapter(List<Trainee> trainees, Context context) {
        this.trainees = trainees;
        this.context = context;
    }

    // ViewHolder
    public static class TraineeViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvGoal;

        public TraineeViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvGoal = itemView.findViewById(R.id.tvGoal);
        }
    }

    // יצירת כרטיס
    @NonNull
    @Override
    public TraineeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trainee_card, parent, false);
        return new TraineeViewHolder(view);
    }

    // הכנסת נתונים לכרטיס
    @Override
    public void onBindViewHolder(@NonNull TraineeViewHolder holder, int position) {
        Trainee trainee = trainees.get(position);

        holder.tvName.setText(trainee.getName());

        // אם אין מטרה - שלא יקרוס
        if (trainee.getGoal() != null && !trainee.getGoal().isEmpty()) {
            holder.tvGoal.setText("מטרה: " + trainee.getGoal());
        } else {
            holder.tvGoal.setText("מטרה לא הוגדרה");
        }
    }

    // כמות פריטים
    @Override
    public int getItemCount() {
        return trainees.size();
    }
}