package com.example.myfinalproject;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TrainingPrograms extends Fragment {

    private RecyclerView recyclerView;
    private ExerciseAdapter adapter;
    private List<Exercise> exerciseList;
    private List<Exercise> filteredList;
    private Spinner spinnerTrainees;
    private Button btnAddExercise;

    public TrainingPrograms() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_training_programs, container, false);

        recyclerView = view.findViewById(R.id.recyclerExercises);
        spinnerTrainees = view.findViewById(R.id.spinnerTrainees);
        btnAddExercise = view.findViewById(R.id.btnAddExercise);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        exerciseList = SplashActivity.exercisesList;
        filteredList = new ArrayList<>();

        adapter = new ExerciseAdapter(filteredList);
        recyclerView.setAdapter(adapter);

        // הגדרת לחיצה על ה-X למחיקה מהתוכנית
        adapter.setOnDeleteClickListener(exercise -> showDeleteConfirmation(exercise));

        btnAddExercise.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, new ViewTraineeExercise())
                    .addToBackStack(null)
                    .commit();
        });

        setupSpinner();
        return view;
    }

    private void setupSpinner() {
        List<MiniTrainee> trainees = SplashActivity.traineesList;
        ArrayAdapter<MiniTrainee> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, trainees);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrainees.setAdapter(spinnerAdapter);

        spinnerTrainees.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MiniTrainee selected = trainees.get(position);
                DataHolder.setSelectedMiniTrainee(selected);
                filterExercises(selected.getExerciseIds());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void filterExercises(List<String> ids) {
        filteredList.clear();
        if (ids != null) {
            for (Exercise ex : exerciseList) {
                if (ids.contains(ex.getId())) {
                    filteredList.add(ex);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void showDeleteConfirmation(Exercise exercise) {
        MiniTrainee currentTrainee = DataHolder.getSelectedMiniTrainee();
        if (currentTrainee == null) return;

        new AlertDialog.Builder(getContext())
                .setTitle("מחיקת תרגיל")
                .setMessage("האם אתה בטוח שברצונך להסיר את '" + exercise.getName() + "' מהתוכנית של " + currentTrainee.getName() + "?")
                .setPositiveButton("כן", (dialog, which) -> deleteExerciseFromTrainee(currentTrainee, exercise))
                .setNegativeButton("ביטול", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteExerciseFromTrainee(MiniTrainee trainee, Exercise exercise) {
        ArrayList<String> ids = trainee.getExerciseIds();
        if (ids != null && ids.contains(exercise.getId())) {
            ids.remove(exercise.getId());

            DBref.TraineesRef.child(trainee.getId())
                    .child("exerciseIds")
                    .setValue(ids)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "התרגיל הוסר בהצלחה", Toast.LENGTH_SHORT).show();
                        filterExercises(ids); // רענון הרשימה במסך
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "שגיאה במחיקה", Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MiniTrainee selected = DataHolder.getSelectedMiniTrainee();
        if (selected != null) {
            filterExercises(selected.getExerciseIds());
        }
    }
}