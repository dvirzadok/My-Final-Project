package com.example.myfinalproject;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class TrainingPrograms extends Fragment {

    // הגדרת המשתנים שלנו
    private RecyclerView recyclerView;
    private com.example.myfinalproject.ExerciseAdapter adapter;
    private List<Exercise> exerciseList;

    public TrainingPrograms() {
        // חייב להישאר ריק
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 1. ניפוח ה-Layout (מחברים את הקובץ XML של הפרגמנט)
        View view = inflater.inflate(R.layout.fragment_training_programs, container, false);

        // 2. מציאת ה-RecyclerView מתוך ה-XML (וודא שיש לו ID בשם rvExercises)
        recyclerView = view.findViewById(R.id.recyclerExercises);

        // 3. הגדרת מנהל תצוגה (LayoutManager) - מסדר את הכרטיסים בטור
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 4. שימוש ברשימה שהגיעה מה-Splash
        exerciseList = SplashActivity.exercisesList;

        // 5. אתחול האדפטר וחיבורו ל-RecyclerView
        adapter = new com.example.myfinalproject.ExerciseAdapter(exerciseList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}