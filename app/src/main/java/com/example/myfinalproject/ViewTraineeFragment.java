package com.example.myfinalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class ViewTraineeFragment extends Fragment {

    private TextView tvName, tvAge, tvPhone, tvWeight, tvHeight;
    private TextView tvGoal, tvMonthlyCost, tvDebt, tvPaymentDay, tvEndDate;
    private TextView tvExercises;

    private Button btnEdit;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_trainee, container, false);

        tvName = view.findViewById(R.id.tvName);
        tvAge = view.findViewById(R.id.tvAge);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvWeight = view.findViewById(R.id.tvWeight);
        tvHeight = view.findViewById(R.id.tvHeight);
        tvGoal = view.findViewById(R.id.tvGoal);
        tvMonthlyCost = view.findViewById(R.id.tvMonthlyCost);
        tvDebt = view.findViewById(R.id.tvDebt);
        tvPaymentDay = view.findViewById(R.id.tvPaymentDay);
        tvEndDate = view.findViewById(R.id.tvEndDate);

        tvExercises = view.findViewById(R.id.tvExercises);

        btnEdit = view.findViewById(R.id.btnEdit);

        Trainee t = DataHolder.selectedTrainee;

        if (t != null) {

            tvName.setText("שם: " + t.getName());
            tvAge.setText("גיל: " + t.getAge());
            tvPhone.setText("טלפון: " + t.getPhone());
            tvWeight.setText("משקל: " + t.getWeight());
            tvGoal.setText("מטרה: " + t.getGoal());
            tvMonthlyCost.setText("תשלום חודשי: " + t.getMonthlyCost());
            tvDebt.setText("חוב: " + t.getRemainingDebt());
            tvPaymentDay.setText("יום תשלום: " + t.getDayOfPayment());
            tvEndDate.setText("סיום מנוי: " + t.getSubscriptionEndDate());

            // =========================
            // אימונים של המתאמן
            // =========================

            if (Trainee.getExerciseIds() == null || Trainee.getExerciseIds().isEmpty()) {

                tvExercises.setText("עדיין אין אימונים");

            } else {

                StringBuilder result = new StringBuilder();

                for (String id : Trainee.getExerciseIds()) {

                    for (Exercise ex : SplashActivity.exercisesList) {

                        if (ex.getId().equals(id)) {

                            result.append("שם: ").append(ex.getName()).append("\n");
                            result.append("סטים: ").append(ex.getSets()).append("\n");
                            result.append("חזרות: ").append(ex.getReps()).append("\n");


                            if (ex.getWeight() > 0) {
                                result.append("משקל: ").append(ex.getWeight()).append("\n");
                            }

                            if (ex.getNotes() != null && !ex.getNotes().isEmpty()) {
                                result.append(ex.getNotes()).append("\n");
                            }

                            result.append("\n");
                        }
                    }
                }

                tvExercises.setText(result.toString());
            }
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main, new EditTraineeFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}