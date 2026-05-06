package com.example.myfinalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends Activity {

    private static final int SPLASH_DURATION = 3000;

    public static ArrayList<Exercise> exercisesList = new ArrayList<>();
    public static ArrayList<MiniTrainee> traineesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadExercises();
        loadTrainees();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity2.class));
                finish();
            }
        }, SPLASH_DURATION);
    }

    private void loadExercises() {
        DBref.ExercisesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                exercisesList.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    Exercise exercise = data.getValue(Exercise.class);

                    if (exercise != null) {
                        exercisesList.add(exercise);
                    }
                }

                Log.d("Splash", "Exercises loaded: " + exercisesList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Splash", "Error loading exercises", error.toException());
            }
        });
    }

    private void loadTrainees() {
        DBref.TraineesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                traineesList.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    Trainee trainee = data.getValue(Trainee.class);

                    if (trainee != null) {


                        MiniTrainee miniTrainee = new MiniTrainee(
                                trainee.getId(),
                                trainee.getName(),
                                trainee.getGoal(),
                                trainee.getExerciseIds()
                        );

                        traineesList.add(miniTrainee);
                    }
                }

                Log.d("Splash", "Trainees loaded: " + traineesList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Splash", "Error loading trainees", error.toException());
            }
        });
    }
}