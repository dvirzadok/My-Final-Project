package com.example.myfinalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private List<Exercise> exerciseList;
    private OnItemClickListener listener;
    private OnDeleteClickListener deleteListener;

    public interface OnItemClickListener {
        void onItemClick(Exercise exercise);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Exercise exercise);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public ExerciseAdapter(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exercise_card, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);

        holder.tvName.setText(exercise.getName());
        holder.tvSets.setText("סטים: " + exercise.getSets());
        holder.tvReps.setText("חזרות: " + exercise.getReps());
        holder.tvWeight.setText(exercise.getWeight() + " ק\"ג");
        holder.tvNotes.setText(exercise.getNotes());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(exercise);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) deleteListener.onDeleteClick(exercise);
        });
    }

    @Override
    public int getItemCount() {
        return exerciseList == null ? 0 : exerciseList.size();
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvSets, tvReps, tvWeight, tvNotes;
        ImageButton btnDelete;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvSets = itemView.findViewById(R.id.tvSets);
            tvReps = itemView.findViewById(R.id.tvReps);
            tvWeight = itemView.findViewById(R.id.tvWeight);
            tvNotes = itemView.findViewById(R.id.tvNotes);
            btnDelete = itemView.findViewById(R.id.btnDeleteFromTrainee);
        }
    }
}