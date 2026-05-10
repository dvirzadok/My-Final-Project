package com.example.myfinalproject;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewTraineeExercise extends Fragment {

    private RecyclerView recyclerView;
    private ExerciseAdapter adapter;
    private Button btnCreate, btnBack;
    private List<Exercise> allExercises;

    public ViewTraineeExercise() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_trainee_exercise, container, false);

        recyclerView = view.findViewById(R.id.recyclerExercises);
        btnCreate = view.findViewById(R.id.btnCreateExercise);
        btnBack = view.findViewById(R.id.btnBackToPrograms);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // טעינת הרשימה הגלובלית מה-Splash
        allExercises = SplashActivity.exercisesList;

        adapter = new ExerciseAdapter(allExercises);
        recyclerView.setAdapter(adapter);

        // 1. הוספת תרגיל למתאמן הנוכחי בלחיצה רגילה על הכרטיס
        adapter.setOnItemClickListener(exercise -> {
            addExerciseToCurrentTrainee(exercise);
        });

        // 2. מחיקה גלובלית בלחיצה על האיקס (X)
        adapter.setOnDeleteClickListener(exercise -> {
            showGlobalDeleteConfirmation(exercise);
        });

        // כפתור חזור
        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        // כפתור יצירת תרגיל חדש
        btnCreate.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, new AddExerciseFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    // --- לוגיקה להוספה למתאמן הספציפי ---
    private void addExerciseToCurrentTrainee(Exercise exercise) {
        MiniTrainee trainee = DataHolder.getSelectedMiniTrainee();
        if (trainee == null) {
            Toast.makeText(getContext(), "אנא בחר מתאמן תחילה", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<String> exerciseIds = trainee.getExerciseIds();
        if (exerciseIds == null) exerciseIds = new ArrayList<>();

        if (!exerciseIds.contains(exercise.getId())) {
            exerciseIds.add(exercise.getId());
            trainee.setExerciseIds(exerciseIds);

            DBref.TraineesRef.child(trainee.getId()).child("exerciseIds")
                    .setValue(exerciseIds)
                    .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "התרגיל נוסף לתוכנית! ✅", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(getContext(), "התרגיל כבר קיים אצל המתאמן", Toast.LENGTH_SHORT).show();
        }
    }

    // --- לוגיקה למחיקה גלובלית ---
    private void showGlobalDeleteConfirmation(Exercise exercise) {
        new AlertDialog.Builder(getContext())
                .setTitle("מחיקה גלובלית")
                .setMessage("האם אתה בטוח שברצונך למחוק את '" + exercise.getName() + "' לצמיתות מכל המתאמנים ומהמאגר?")
                .setPositiveButton("מחק הכל", (dialog, which) -> deleteExerciseFromEverywhere(exercise))
                .setNegativeButton("ביטול", null)
                .setIcon(android.R.drawable.ic_delete)
                .show();
    }

    private void deleteExerciseFromEverywhere(Exercise exercise) {
        String exerciseId = exercise.getId();

        // א. מחיקה מהרשימה הכללית ב-Firebase
        DBref.ExercisesRef.child(exerciseId).removeValue();

        // ב. מחיקה מהרשימה הלוקאלית (כדי שייעלם מהמסך מיד)
        allExercises.remove(exercise);
        adapter.notifyDataSetChanged();

        // ג. מעבר על כל המתאמנים וניקוי ה-ID שלהם
        // אנחנו משתמשים ברשימה שנמצאת ב-SplashActivity.traineesList
        for (MiniTrainee trainee : SplashActivity.traineesList) {
            if (trainee.getExerciseIds() != null && trainee.getExerciseIds().contains(exerciseId)) {

                // הסרה מקומית מהאובייקט
                trainee.getExerciseIds().remove(exerciseId);

                // עדכון ב-Firebase עבור כל מתאמן שנמצא עם התרגיל הזה
                DBref.TraineesRef.child(trainee.getId()).child("exerciseIds")
                        .setValue(trainee.getExerciseIds());
            }
        }

        Toast.makeText(getContext(), "התרגיל נמחק מכל המערכת", Toast.LENGTH_SHORT).show();
    }
}