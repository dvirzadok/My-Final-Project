package com.example.myfinalproject;

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
    private Button btnCreate, btnDelete, btnBack; // הוספנו btnBack
    private List<Exercise> allExercises;

    public ViewTraineeExercise() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_trainee_exercise, container, false);

        recyclerView = view.findViewById(R.id.recyclerExercises);
        btnCreate = view.findViewById(R.id.btnCreateExercise);
        btnDelete = view.findViewById(R.id.btnDeleteExercise);
        btnBack = view.findViewById(R.id.btnBackToPrograms); // קישור הכפתור

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        allExercises = SplashActivity.exercisesList;

        adapter = new ExerciseAdapter(allExercises);
        recyclerView.setAdapter(adapter);

        // לחיצה על כפתור חזור
        btnBack.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack(); // חוזר לפרגמנט הקודם (TrainingPrograms)
        });

        adapter.setOnItemClickListener(exercise -> {
            MiniTrainee trainee = DataHolder.getSelectedMiniTrainee();

            if (trainee != null) {
                ArrayList<String> exerciseIds = trainee.getExerciseIds();
                if (exerciseIds == null) exerciseIds = new ArrayList<>();

                if (!exerciseIds.contains(exercise.getId())) {
                    exerciseIds.add(exercise.getId());
                    trainee.setExerciseIds(exerciseIds);

                    DBref.TraineesRef.child(trainee.getId())
                            .child("exerciseIds")
                            .setValue(exerciseIds)
                            .addOnSuccessListener(aVoid -> {
                                if (getContext() != null) {
                                    Toast.makeText(getContext(), "נוסף: " + exercise.getName() + " ✅", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(getContext(), "התרגיל כבר קיים", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCreate.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, new AddExerciseFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}