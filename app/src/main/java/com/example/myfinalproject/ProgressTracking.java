package com.example.myfinalproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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

    private ListView listViewProgress;

    private LineChart lineChart;

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

        listViewProgress =
                view.findViewById(R.id.listViewProgress);

        lineChart =
                view.findViewById(R.id.lineChart);

        trainees = new ArrayList<>();

        loadTrainees();

        spinnerProgressTrainee.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> adapterView,
                            View view,
                            int position,
                            long l
                    ) {

                        selectedTrainee =
                                trainees.get(position);

                        showWeightTracking();

                        showChart();
                    }

                    @Override
                    public void onNothingSelected(
                            AdapterView<?> adapterView
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

                            showWeightTracking();

                            showChart();
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

    private void showWeightTracking() {

        if (selectedTrainee == null) {
            return;
        }

        ArrayList<String> trackingList =
                new ArrayList<>();

        if (selectedTrainee.getWeightTracking() == null ||
                selectedTrainee.getWeightTracking().isEmpty()) {

            trackingList.add("אין נתוני שקילה");

        } else {

            for (WeightEntry entry :
                    selectedTrainee.getWeightTracking()) {

                String row =
                        "תאריך: "
                                + entry.getDate()
                                + " | משקל: "
                                + entry.getWeight()
                                + " ק\"ג";

                trackingList.add(row);
            }
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        trackingList
                );

        listViewProgress.setAdapter(adapter);
    }

    private void showChart() {

        if (selectedTrainee == null) {
            return;
        }

        ArrayList<Entry> entries =
                new ArrayList<>();

        ArrayList<WeightEntry> weights =
                (ArrayList<WeightEntry>)
                        selectedTrainee.getWeightTracking();

        if (weights == null || weights.isEmpty()) {

            lineChart.clear();

            return;
        }

        for (int i = 0; i < weights.size(); i++) {

            WeightEntry entry =
                    weights.get(i);

            entries.add(
                    new Entry(
                            i,
                            (float) entry.getWeight()
                    )
            );
        }

        LineDataSet dataSet =
                new LineDataSet(entries, "משקל");

        LineData lineData =
                new LineData(dataSet);

        lineChart.setData(lineData);

        lineChart.invalidate();
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

                        if (selectedTrainee
                                .getWeightTracking() == null) {

                            selectedTrainee.setWeightTracking(
                                    new ArrayList<WeightEntry>()
                            );
                        }

                        selectedTrainee
                                .getWeightTracking()
                                .add(newEntry);

                        selectedTrainee
                                .setWeight(newWeight);

                        DBref.TraineesRef
                                .child(selectedTrainee.getId())
                                .setValue(selectedTrainee)

                                .addOnSuccessListener(aVoid -> {

                                    Toast.makeText(
                                            getContext(),
                                            "השקילה נוספה בהצלחה",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    showWeightTracking();

                                    showChart();
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