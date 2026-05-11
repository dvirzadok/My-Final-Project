package com.example.myfinalproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ProgressTracking extends Fragment {

    private Spinner spinnerProgressTrainee;

    private Button btnAddProgress;

    private ArrayList<Trainee> trainees;

    private Trainee selectedTrainee;

    public ProgressTracking() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_progress_tracking,
                container,
                false
        );

        spinnerProgressTrainee =
                view.findViewById(R.id.spinnerProgressTrainee);

        btnAddProgress =
                view.findViewById(R.id.btnAddProgress);

        trainees = new ArrayList<>();

        loadTrainees();

        spinnerProgressTrainee.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            android.widget.AdapterView<?> adapterView,
                            View view,
                            int position,
                            long l
                    ) {

                        selectedTrainee =
                                trainees.get(position);
                    }

                    @Override
                    public void onNothingSelected(
                            android.widget.AdapterView<?> adapterView
                    ) {

                    }
                }
        );

        btnAddProgress.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        showAddWeightDialog();
                    }
                }
        );

        return view;
    }

    private void loadTrainees() {

        DBref.TraineesRef.addListenerForSingleValueEvent(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(
                            @NonNull DataSnapshot snapshot
                    ) {

                        trainees.clear();

                        ArrayList<String> traineeNames =
                                new ArrayList<>();

                        for (DataSnapshot data :
                                snapshot.getChildren()) {

                            Trainee trainee =
                                    data.getValue(Trainee.class);

                            if (trainee != null) {

                                trainees.add(trainee);

                                traineeNames.add(
                                        trainee.getName()
                                );
                            }
                        }

                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<>(
                                        requireContext(),
                                        android.R.layout.simple_spinner_item,
                                        traineeNames
                                );

                        adapter.setDropDownViewResource(
                                android.R.layout.simple_spinner_dropdown_item
                        );

                        spinnerProgressTrainee.setAdapter(
                                adapter
                        );

                        if (!trainees.isEmpty()) {

                            selectedTrainee =
                                    trainees.get(0);
                        }
                    }

                    @Override
                    public void onCancelled(
                            @NonNull DatabaseError error
                    ) {

                    }
                }
        );
    }

    private void showAddWeightDialog() {

        if (selectedTrainee == null) {

            Toast.makeText(
                    getContext(),
                    "לא נבחר מתאמן",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getContext());

        builder.setTitle("הוסף שקילה חדשה");

        final EditText input =
                new EditText(getContext());

        input.setHint("הכנס משקל");

        input.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        builder.setView(input);

        builder.setPositiveButton(
                "שמור",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(
                            DialogInterface dialogInterface,
                            int i
                    ) {

                        String weightStr =
                                input.getText()
                                        .toString()
                                        .trim();

                        if (weightStr.isEmpty()) {

                            Toast.makeText(
                                    getContext(),
                                    "הכנס משקל",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        double newWeight =
                                Double.parseDouble(weightStr);

                        String currentDate =
                                new SimpleDateFormat(
                                        "yyyy-MM-dd",
                                        Locale.getDefault()
                                ).format(new Date());

                        WeightEntry newEntry =
                                new WeightEntry(
                                        newWeight,
                                        currentDate
                                );

                        // אם הרשימה ריקה
                        if (selectedTrainee
                                .getWeightTracking() == null) {

                            selectedTrainee.setWeightTracking(
                                    new ArrayList<WeightEntry>()
                            );
                        }

                        // הוספת השקילה
                        selectedTrainee
                                .getWeightTracking()
                                .add(newEntry);

                        // עדכון משקל נוכחי
                        selectedTrainee
                                .setWeight(newWeight);

                        // עדכון Firebase
                        DBref.TraineesRef
                                .child(selectedTrainee.getId())
                                .setValue(selectedTrainee)

                                .addOnSuccessListener(aVoid -> {

                                    Toast.makeText(
                                            getContext(),
                                            "השקילה נוספה בהצלחה",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                })

                                .addOnFailureListener(e -> {

                                    Toast.makeText(
                                            getContext(),
                                            "שגיאה: " + e.getMessage(),
                                            Toast.LENGTH_LONG
                                    ).show();
                                });
                    }
                }
        );

        builder.setNegativeButton(
                "ביטול",
                null
        );

        builder.show();
    }
}