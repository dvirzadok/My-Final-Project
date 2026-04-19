package com.example.myfinalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TraineeAdapter extends RecyclerView.Adapter<TraineeAdapter.TraineeViewHolder> {

    private List<String> trainees;

    public TraineeAdapter(List<String> trainees) {
        this.trainees = trainees;
    }

    public static class TraineeViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvGoal;

        public TraineeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvGoal = itemView.findViewById(R.id.tvGoal);
        }
    }

    @NonNull
    @Override
    public TraineeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trainee_card, parent, false);
        return new TraineeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TraineeViewHolder holder, int position) {
        String fullInfo = trainees.get(position); // מקבל למשל: "ישראל - חיטוב"

        // פירוק המחרוזת לפי המקף ששמנו ב-Splash
        if (fullInfo != null && fullInfo.contains(" - ")) {
            String[] parts = fullInfo.split(" - ");
            holder.tvName.setText(parts[0]); // החלק שלפני המקף (השם)
            holder.tvGoal.setText("מטרה: " + parts[1]); // החלק שאחרי המקף (המטרה)
        } else {
            // הגנה למקרה שהמחרוזת לא בפורמט הצפוי
            holder.tvName.setText(fullInfo);
            holder.tvGoal.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return (trainees != null) ? trainees.size() : 0;
    }
}