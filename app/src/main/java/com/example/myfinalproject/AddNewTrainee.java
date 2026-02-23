package com.example.myfinalproject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class AddNewTrainee extends Fragment {

    // רכיבי מסך
    private EditText etName;
    private EditText etAge;
    private EditText etWeight;
    private EditText etSubscription;
    private EditText etGoal;
    private EditText etPhone;
    private Spinner spinnerChargeDay;
    private Button btnSave;

    public AddNewTrainee() {
        // בנאי ריק חובה לפרגמנט
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_new_trainee, container, false);

        // חיבור בין משתנים ל־XML
        etName = view.findViewById(R.id.etName);
        etAge = view.findViewById(R.id.etAge);
        etWeight = view.findViewById(R.id.etWeight);
        etSubscription = view.findViewById(R.id.etSubscription);
        etGoal = view.findViewById(R.id.etGoal);
        etPhone = view.findViewById(R.id.etPhone);
        spinnerChargeDay = view.findViewById(R.id.spinnerChargeDay);
        btnSave = view.findViewById(R.id.btnSaveTrainee);

        setupSpinner(); // יצירת רשימת ימים

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTrainee(); // שמירת מתאמן
            }
        });

        return view;
    }

    // יצירת רשימת ימים 1-31
    private void setupSpinner() {

        List<Integer> days = new ArrayList<>();

        for (int i = 1; i <= 31; i++) {
            days.add(i);
        }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                days
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChargeDay.setAdapter(adapter);
    }

    // שמירת מתאמן חדש
    private void saveTrainee() {

        // קריאת נתונים מהשדות
        String name = etName.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();
        String subscriptionStr = etSubscription.getText().toString().trim();
        String goal = etGoal.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        // בדיקה שכל השדות מלאים
        if (TextUtils.isEmpty(name) ||
                TextUtils.isEmpty(ageStr) ||
                TextUtils.isEmpty(weightStr) ||
                TextUtils.isEmpty(subscriptionStr) ||
                TextUtils.isEmpty(phone)) {

            Toast.makeText(getContext(), "מלא את כל השדות", Toast.LENGTH_SHORT).show();
            return;
        }

        // המרה לסוגים מספריים
        int age = Integer.parseInt(ageStr);
        double weight = Double.parseDouble(weightStr);
        double monthlyCost = Double.parseDouble(subscriptionStr);

        // יום גבייה מה־Spinner
        int dayOfPayment = (int) spinnerChargeDay.getSelectedItem();

        // יצירת מזהה ייחודי בפיירבייס
        String id = DBref.TraineesRef.push().getKey();

        // יצירת רשימת מעקב משקל ראשונית
        List<Double> weightTracking = new ArrayList<>();
        weightTracking.add(weight);

        // יצירת אובייקט מתאמן
        Trainee trainee = new Trainee(
                id,
                name,
                age,
                phone,
                weight,
                0,                 // גובה כרגע 0
                monthlyCost,
                0,                 // חוב התחלתי 0
                dayOfPayment,
                "",                // אין תאריך סיום כרגע
                weightTracking
        );

        // שמירה בפיירבייס תחת Trainees
        DBref.TraineesRef.child(id).setValue(trainee);

        Toast.makeText(getContext(), "המתאמן נשמר בהצלחה", Toast.LENGTH_SHORT).show();

        clearFields(); // ניקוי שדות אחרי שמירה
    }

    // איפוס שדות
    private void clearFields() {
        etName.setText("");
        etAge.setText("");
        etWeight.setText("");
        etSubscription.setText("");
        etGoal.setText("");
        etPhone.setText("");
    }
}