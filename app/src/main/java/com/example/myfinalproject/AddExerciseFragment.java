package com.example.myfinalproject;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddExerciseFragment extends Fragment {

    private EditText etName, etSets, etReps, etWeight, etNotes;
    private Button btnSave, btnBack; // הוספת btnBack

    public AddExerciseFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_exercise, container, false);

        etName = view.findViewById(R.id.etName);
        etSets = view.findViewById(R.id.etSets);
        etReps = view.findViewById(R.id.etReps);
        etWeight = view.findViewById(R.id.etWeight);
        etNotes = view.findViewById(R.id.etNotes);
        btnSave = view.findViewById(R.id.btnSave);
        btnBack = view.findViewById(R.id.btnBackToView); // קישור הכפתור מה-XML

        // הגדרת לחיצה על כפתור חזור
        btnBack.setOnClickListener(v -> {
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
        });

        btnSave.setOnClickListener(v -> saveExercise());

        return view;
    }

    private void saveExercise() {
        String name = etName.getText().toString().trim();
        String setsStr = etSets.getText().toString().trim();
        String repsStr = etReps.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(setsStr) || TextUtils.isEmpty(repsStr)) {
            Toast.makeText(getContext(), "מלא את כל השדות החובה", Toast.LENGTH_SHORT).show();
            return;
        }

        int sets, reps;
        double weight = 0;

        try {
            sets = Integer.parseInt(setsStr);
            reps = Integer.parseInt(repsStr);

            if (!TextUtils.isEmpty(weightStr)) {
                weight = Double.parseDouble(weightStr);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "ערכים לא תקינים במספרים", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = DBref.ExercisesRef.push().getKey();

        if (id == null) {
            Toast.makeText(getContext(), "שגיאה ביצירת מזהה", Toast.LENGTH_SHORT).show();
            return;
        }

        Exercise exercise = new Exercise(id, name, sets, reps, weight, notes);

        DBref.ExercisesRef.child(id).setValue(exercise)
                .addOnSuccessListener(aVoid -> {
                    SplashActivity.exercisesList.add(exercise);

                    if (getContext() != null) {
                        Toast.makeText(getContext(), "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
                    }

                    // חזרה אוטומטית למסך הקודם לאחר שמירה מוצלחת
                    if (getFragmentManager() != null) {
                        getFragmentManager().popBackStack();
                    }
                })
                .addOnFailureListener(e -> {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "שגיאה בשמירה", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}