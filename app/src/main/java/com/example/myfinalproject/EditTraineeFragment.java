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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class EditTraineeFragment extends Fragment {

    private EditText etName, etAge, etWeight, etSubscription, etGoal, etPhone;
    private Spinner spinnerChargeDay;
    private Button btnSaveTrainee, btnBack;
    private Trainee currentTrainee;

    public EditTraineeFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_trainee, container, false);

        initViews(view);
        setupSpinner();
        loadTraineeData();

        btnBack.setOnClickListener(v -> {
            if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                getParentFragmentManager().popBackStack();
            }
        });

        // כפתור עדכון מתאמן
        btnSaveTrainee.setOnClickListener(v -> updateTraineeData());

        return view;
    }

    private void initViews(View view) {
        etName = view.findViewById(R.id.etName);
        etAge = view.findViewById(R.id.etAge);
        etWeight = view.findViewById(R.id.etWeight);
        etSubscription = view.findViewById(R.id.etSubscription);
        etGoal = view.findViewById(R.id.etGoal);
        etPhone = view.findViewById(R.id.etPhone);
        spinnerChargeDay = view.findViewById(R.id.spinnerChargeDay);
        btnSaveTrainee = view.findViewById(R.id.btnSaveTrainee);
        btnBack = view.findViewById(R.id.btnBack);
    }

    private void setupSpinner() {
        List<Integer> days = new ArrayList<>();
        for (int i = 1; i <= 31; i++) days.add(i);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, days);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChargeDay.setAdapter(adapter);
    }

    private void loadTraineeData() {
        currentTrainee = DataHolder.getSelectedTrainee();
        if (currentTrainee != null) {
            etName.setText(currentTrainee.getName());
            etAge.setText(String.valueOf(currentTrainee.getAge()));
            etWeight.setText(String.valueOf(currentTrainee.getWeight()));
            etSubscription.setText(String.valueOf(currentTrainee.getMonthlyCost()));
            etGoal.setText(currentTrainee.getGoal());
            etPhone.setText(currentTrainee.getPhone());

            int dayValue = currentTrainee.getDayOfPayment();
            if (dayValue >= 1 && dayValue <= 31) {
                spinnerChargeDay.setSelection(dayValue - 1);
            }
        }
    }

    private void updateTraineeData() {
        // 1. קריאת הנתונים מהשדות
        String name = etName.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();
        String subStr = etSubscription.getText().toString().trim();
        String goal = etGoal.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        int day = (int) spinnerChargeDay.getSelectedItem();

        // בדיקת תקינות בסיסית
        if (name == "" || phone == "" || goal == "") {
            Toast.makeText(getContext(), "נא למלא את כל השדות", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentTrainee != null) {
            // 2. עדכון האובייקט המקומי (currentTrainee)
            currentTrainee.setName(name);
            currentTrainee.setAge(Integer.parseInt(ageStr));
            currentTrainee.setWeight(Double.parseDouble(weightStr));
            currentTrainee.setMonthlyCost(Double.parseDouble(subStr));
            currentTrainee.setGoal(goal);
            currentTrainee.setPhone(phone);
            currentTrainee.setDayOfPayment(day);

            // 3. עדכון ב-Firebase לפי ה-ID של המתאמן
            DBref.TraineesRef.child(currentTrainee.getId()).setValue(currentTrainee)
                    .addOnSuccessListener(aVoid -> {

                        // 4. עדכון רשימת ה-MiniTrainee ב-SplashActivity (בשביל התצוגה המהירה בלובי)
                        updateLocalMiniList(currentTrainee.getId(), name, goal);

                        Toast.makeText(getContext(), "הנתונים עודכנו בשרת", Toast.LENGTH_SHORT).show();

                        // חזרה למסך הקודם
                        getParentFragmentManager().popBackStack();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "שגיאה בעדכון: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        }
    }

    private void updateLocalMiniList(String id, String newName, String newGoal) {
        // עוברים על הרשימה הסטטית ב-SplashActivity ומחפשים את המתאמן המתאים לפי ID
        for (MiniTrainee mini : SplashActivity.traineesList) {
            if (mini.getId().equals(id)) {
                mini.setName(newName);
                mini.setGoal(newGoal);
                break; // מצאנו ועדכנו, אפשר לצאת מהלולאה
            }
        }
    }
}