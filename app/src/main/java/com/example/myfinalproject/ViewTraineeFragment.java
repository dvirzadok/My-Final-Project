package com.example.myfinalproject;

import android.os.Bundle;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.Button;

import android.widget.TextView;

import android.widget.Toast;



import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;

import androidx.fragment.app.Fragment;



public class ViewTraineeFragment extends Fragment {



    private TextView tvName, tvAge, tvPhone, tvWeight, tvHeight;

    private TextView tvGoal, tvMonthlyCost, tvDebt, tvPaymentDay, tvEndDate;

    private TextView tvExercises;



    private Button btnEdit, btnDelete, btnBack;



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

        btnDelete = view.findViewById(R.id.btnDelete);

        btnBack = view.findViewById(R.id.btnBackk);



        Trainee t = DataHolder.selectedTraineeFull;



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



            if (t.getExerciseIds() == null || t.getExerciseIds().isEmpty()) {

                tvExercises.setText("עדיין אין אימונים");

            } else {

                StringBuilder result = new StringBuilder();

                for (String id : t.getExerciseIds()) {

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



// מחיקת מתאמן

            btnDelete.setOnClickListener(v -> new AlertDialog.Builder(requireContext())

                    .setTitle("מחיקת מתאמן")

                    .setMessage("האם אתה בטוח שברצונך למחוק את " + t.getName() + "?")

                    .setPositiveButton("מחק", (dialog, which) -> {

                        deleteTrainee(t);

                    })

                    .setNegativeButton("ביטול", null)

                    .show()

            );

        }



        btnEdit.setOnClickListener(v -> getParentFragmentManager().beginTransaction()

                .replace(R.id.nav_host_fragment_content_main, new EditTraineeFragment())

                .addToBackStack(null)

                .commit());



// חזור למסך הקודם

        btnBack.setOnClickListener(v -> {

            if (getParentFragmentManager().getBackStackEntryCount() > 0) {

                getParentFragmentManager().popBackStack();

            }

        });



        return view;

    }



    private void deleteTrainee(Trainee t) {

        DBref.TraineesRef.child(t.getId())

                .removeValue()

                .addOnSuccessListener(unused -> {

                    Toast.makeText(getContext(), "המתאמן נמחק בהצלחה!", Toast.LENGTH_SHORT).show();



// מחיקה מהרשימה המקומית כדי שהעדכון ישתקף ברשימת המתאמנים הראשית

                    for (int i = 0; i < SplashActivity.traineesList.size(); i++) {

                        if (SplashActivity.traineesList.get(i).getId().equals(t.getId())) {

                            SplashActivity.traineesList.remove(i);

                            break;

                        }

                    }



// חזרה למסך הקודם

                    getParentFragmentManager().popBackStack();

                })

                .addOnFailureListener(e ->

                        Toast.makeText(getContext(), "שגיאה במחיקה: " + e.getMessage(), Toast.LENGTH_LONG).show()

                );

    }

}