package com.example.myfinalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private List<Exercise> exerciseList;

    // קונסטרקטור שמקבל את רשימת התרגילים להצגה
    public ExerciseAdapter(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // קישור לקובץ ה-XML של הכרטיס שיצרת (exercise_card)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_card, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        // קבלת האובייקט הנוכחי מהרשימה
        Exercise exercise = exerciseList.get(position);

        // מילוי הנתונים בתוך ה-View (דילוג על ה-ID כפי שביקשת)
        holder.tvName.setText(exercise.getName());
        holder.tvSets.setText("סטים: " + exercise.getSets());
        holder.tvReps.setText("חזרות: " + exercise.getReps());
        holder.tvWeight.setText("משקל: " + exercise.getWeight() + " ק\"ג");
        holder.tvNotes.setText("הערות: " + exercise.getNotes());
    }

    @Override
    public int getItemCount() {
        if (exerciseList == null) {
            return 0;
        }
        return exerciseList.size();
    }

    // מחלקת ה-ViewHolder שתחזיק את הרפרנסים לכל הרכיבים בכרטיס
    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvSets, tvReps, tvWeight, tvNotes;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);

            // קישור המשתנים ל-ID שנתת ב-XML (exercise_card.xml)
            tvName = itemView.findViewById(R.id.tvName);
            tvSets = itemView.findViewById(R.id.tvSets);
            tvReps = itemView.findViewById(R.id.tvReps);
            tvWeight = itemView.findViewById(R.id.tvWeight);
            tvNotes = itemView.findViewById(R.id.tvNotes);
        }
    }
}