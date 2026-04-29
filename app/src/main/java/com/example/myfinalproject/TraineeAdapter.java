package com.example.myfinalproject;

import android.view.LayoutInflater;
import android.view.View;                     // בשביל View
import android.view.ViewGroup;               // בשביל ה-parent
import android.widget.TextView;              // טקסטים בכרטיס

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;                       // רשימה של אובייקטים

public class TraineeAdapter extends RecyclerView.Adapter<TraineeAdapter.TraineeViewHolder> {

    private List<MiniTrainee> trainees;      // רשימת מתאמנים (אובייקטים ולא String)
    private OnItemClickListener listener;    // מאזין ללחיצה על כרטיס

    public interface OnItemClickListener {
        void onItemClick(MiniTrainee trainee);   // מחזיר את המתאמן שנלחץ
    }

    // קונסטרקטור שמקבל רשימה + מאזין
    public TraineeAdapter(List<MiniTrainee> trainees, OnItemClickListener listener) {
        this.trainees = trainees;
        this.listener = listener;
    }

    // מחלקה פנימית שמייצגת כרטיס אחד
    public class TraineeViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;   // שם המתאמן
        TextView tvGoal;   // המטרה שלו

        public TraineeViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);   // קישור ל-XML
            tvGoal = itemView.findViewById(R.id.tvGoal);   // קישור ל-XML

            // לחיצה על כל הכרטיס
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // בדיקה שהמיקום תקין
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {

                        // שליחת המתאמן שנלחץ לפרגמנט
                        listener.onItemClick(trainees.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    // יוצר כרטיס חדש (מנפח את ה-XML)
    @NonNull
    @Override
    public TraineeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trainee_card, parent, false); // קובץ העיצוב של הכרטיס

        return new TraineeViewHolder(view);
    }

    // ממלא את הנתונים בתוך הכרטיס
    @Override
    public void onBindViewHolder(@NonNull TraineeViewHolder holder, int position) {

        MiniTrainee trainee = trainees.get(position); // לוקח מתאמן לפי מיקום

        holder.tvName.setText(trainee.getName());          // מציג שם
        holder.tvGoal.setText("מטרה: " + trainee.getGoal()); // מציג מטרה
    }

    // כמה פריטים יש ברשימה
    @Override
    public int getItemCount() {
        return (trainees != null) ? trainees.size() : 0;
    }
}