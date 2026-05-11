package com.example.myfinalproject;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddNewTrainee extends Fragment {

    private EditText etName;
    private EditText etAge;
    private EditText etWeight;
    private EditText etSubscription;
    private EditText etGoal;
    private EditText etPhone;

    private Spinner spinnerChargeDay;

    private Button btnSave;

    public AddNewTrainee() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_add_new_trainee,
                container,
                false
        );

        etName = view.findViewById(R.id.etName);
        etAge = view.findViewById(R.id.etAge);
        etWeight = view.findViewById(R.id.etWeight);
        etSubscription = view.findViewById(R.id.etSubscription);
        etGoal = view.findViewById(R.id.etGoal);
        etPhone = view.findViewById(R.id.etPhone);

        spinnerChargeDay =
                view.findViewById(R.id.spinnerChargeDay);

        btnSave =
                view.findViewById(R.id.btnSaveTrainee);

        setupSpinner();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTrainee();
            }
        });

        return view;
    }

    private void setupSpinner() {

        List<Integer> days = new ArrayList<>();

        for (int i = 1; i <= 31; i++) {
            days.add(i);
        }

        ArrayAdapter<Integer> adapter =
                new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        days
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerChargeDay.setAdapter(adapter);
    }

    private void saveTrainee() {

        String name =
                etName.getText().toString().trim();

        String goal =
                etGoal.getText().toString().trim();

        String ageStr =
                etAge.getText().toString().trim();

        String weightStr =
                etWeight.getText().toString().trim();

        String subscriptionStr =
                etSubscription.getText().toString().trim();

        String phone =
                etPhone.getText().toString().trim();

        // בדיקה שכל השדות מלאים
        if (TextUtils.isEmpty(name) ||
                TextUtils.isEmpty(phone) ||
                TextUtils.isEmpty(goal) ||
                TextUtils.isEmpty(ageStr) ||
                TextUtils.isEmpty(weightStr) ||
                TextUtils.isEmpty(subscriptionStr)) {

            Toast.makeText(
                    getContext(),
                    "אנא מלא את כל השדות",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String id =
                DBref.TraineesRef.push().getKey();

        if (id == null) {
            return;
        }

        int age =
                Integer.parseInt(ageStr);

        double weight =
                Double.parseDouble(weightStr);

        double monthlyCost =
                Double.parseDouble(subscriptionStr);

        // תאריך נוכחי
        String currentDate =
                new SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                ).format(new Date());

        // יצירת רשימת שקילות
        List<WeightEntry> weightTracking =
                new ArrayList<>();

        // הוספת שקילה ראשונה
        weightTracking.add(
                new WeightEntry(
                        weight,
                        currentDate
                )
        );

        // רשימת תרגילים ריקה
        ArrayList<String> exerciseIds =
                new ArrayList<>();

        // יצירת מתאמן
        Trainee trainee = new Trainee(
                id,
                name,
                age,
                phone,
                weight,
                monthlyCost,
                0,
                (int) spinnerChargeDay.getSelectedItem(),
                "",
                goal,
                weightTracking,
                exerciseIds
        );

        // שמירה ל-Firebase
        DBref.TraineesRef
                .child(id)
                .setValue(trainee)

                .addOnSuccessListener(aVoid -> {

                    Log.d(
                            "FirebaseCheck",
                            "Success! Data saved."
                    );

                    MiniTrainee newMini =
                            new MiniTrainee(
                                    id,
                                    name,
                                    goal,
                                    exerciseIds
                            );

                    if (SplashActivity.traineesList != null) {

                        SplashActivity.traineesList.add(
                                newMini
                        );
                    }

                    Toast.makeText(
                            getContext(),
                            "המתאמן נשמר בהצלחה",
                            Toast.LENGTH_SHORT
                    ).show();

                    clearFields();
                })

                .addOnFailureListener(e -> {

                    Log.e(
                            "FirebaseCheck",
                            "Failed: " + e.getMessage()
                    );

                    Toast.makeText(
                            getContext(),
                            "שגיאת שמירה: " + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }

    private void clearFields() {

        etName.setText("");
        etAge.setText("");
        etWeight.setText("");
        etSubscription.setText("");
        etGoal.setText("");
        etPhone.setText("");
    }
}