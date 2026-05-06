package com.example.myfinalproject;

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

import java.util.ArrayList;
import java.util.List;

public class TrainingPrograms extends Fragment {

    private RecyclerView recyclerView;
    private ExerciseAdapter adapter;

    private List<Exercise> exerciseList;
    private List<Exercise> filteredList;

    private Spinner spinnerTrainees;
    private Button btnAddExercise; // הצהרה על הכפתור

    public TrainingPrograms() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_training_programs, container, false);

        // אתחול רכיבי ה-UI
        recyclerView = view.findViewById(R.id.recyclerExercises);
        spinnerTrainees = view.findViewById(R.id.spinnerTrainees);
        btnAddExercise = view.findViewById(R.id.btnAddExercise); // מציאת הכפתור ב-XML

        // הגדרת ה-RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        exerciseList = SplashActivity.exercisesList;
        filteredList = new ArrayList<>();
        adapter = new ExerciseAdapter(filteredList);
        recyclerView.setAdapter(adapter);

        // הגדרת לחיצה על כפתור הוספת תרגיל
        btnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // מעבר למסך בחירת תרגילים (ViewTraineeExercise)
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main, new ViewTraineeExercise())
                        .addToBackStack(null)
                        .commit();
            }
        });

        setupSpinner();

        return view;
    }

    private void setupSpinner() {
        List<MiniTrainee> trainees = SplashActivity.traineesList;

        ArrayAdapter<MiniTrainee> spinnerAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                trainees
        );

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrainees.setAdapter(spinnerAdapter);

        spinnerTrainees.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MiniTrainee selected = trainees.get(position);

                // שמירת המתאמן שנבחר ב-DataHolder לשימוש במסכים הבאים
                DataHolder.setSelectedMiniTrainee(selected);

                // סינון התרגילים לפי המזהים שמשויכים למתאמן
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
}