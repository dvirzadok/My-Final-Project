package com.example.myfinalproject;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AddNewTrainee extends Fragment {

    private EditText etName, etAge, etWeight, etSubscription, etGoal, etPhone;
    private Spinner spinnerChargeDay;
    private Button btnSave;

    public AddNewTrainee() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_trainee, container, false);

        etName = view.findViewById(R.id.etName);
        etAge = view.findViewById(R.id.etAge);
        etWeight = view.findViewById(R.id.etWeight);
        etSubscription = view.findViewById(R.id.etSubscription);
        etGoal = view.findViewById(R.id.etGoal);
        etPhone = view.findViewById(R.id.etPhone);
        spinnerChargeDay = view.findViewById(R.id.spinnerChargeDay);
        btnSave = view.findViewById(R.id.btnSaveTrainee);

        setupSpinner();

        btnSave.setOnClickListener(v -> saveTrainee());

        return view;
    }

    private void setupSpinner() {
        List<Integer> days = new ArrayList<>();
        for (int i = 1; i <= 31; i++) days.add(i);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, days);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChargeDay.setAdapter(adapter);
    }

    private void saveTrainee() {
        String name = etName.getText().toString().trim();
        String goal = etGoal.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();
        String subscriptionStr = etSubscription.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(goal)) {
            Toast.makeText(getContext(), "מלא את כל השדות", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. שמירה ל-Firebase
        String id = DBref.TraineesRef.push().getKey();
        double weight = Double.parseDouble(weightStr);
        List<Double> weightTracking = new ArrayList<>();
        weightTracking.add(weight);

        ArrayList<String> exerciseIds = new ArrayList<>();

        Trainee trainee = new Trainee(
                id,
                name,
                Integer.parseInt(ageStr),
                phone,
                weight,
                Double.parseDouble(subscriptionStr),
                0,
                (int) spinnerChargeDay.getSelectedItem(),
                "",
                goal,
                weightTracking,
                exerciseIds
        );

        DBref.TraineesRef.child(id).setValue(trainee)
                .addOnSuccessListener(aVoid -> {
                    // אם זה הצליח
                    Log.d("FirebaseCheck", "Success! Data saved.");
                })
                .addOnFailureListener(e -> {
                    // אם זה נכשל - זה ידפיס לך ב-Logcat בדיוק למה
                    Log.e("FirebaseCheck", "Failed: " + e.getMessage());
                    Toast.makeText(getContext(), "שגיאת שמירה: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });

        // 2. עדכון ה-JSON המקומי ב-SharedPreferences
        updateJsonInSP(name, goal);

        Toast.makeText(getContext(), "המתאמן נשמר בהצלחה", Toast.LENGTH_SHORT).show();
        clearFields();
    }

    private void updateJsonInSP(String name, String goal) {
        if (getContext() != null) {
            SharedPreferences sp = getContext().getSharedPreferences("MyProjectPrefs", MODE_PRIVATE);
            Gson gson = new Gson();

            // שליפת הרשימה הקיימת
            String json = sp.getString("trainees_list", "[]");
            Type type = new TypeToken<ArrayList<String>>(){}.getType();
            ArrayList<String> currentList = gson.fromJson(json, type);

            if (currentList == null) currentList = new ArrayList<>();

            // הוספת החדש (לפי הפורמט של האדפטר)
            currentList.add(name + " - " + goal);

            // שמירה דורסת
            sp.edit().putString("trainees_list", gson.toJson(currentList)).apply();
        }
    }

    private void clearFields() {
        etName.setText(""); etAge.setText(""); etWeight.setText("");
        etSubscription.setText(""); etGoal.setText(""); etPhone.setText("");
    }
}