package com.example.myfinalproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SplashActivity extends Activity {

    private static final int SPLASH_DURATION = 3000; // 3 שניות המתנה
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // מסך הלוגו

        // הפעלת הורדת הנתונים והשמירה
        downloadAndSaveData();

        // מעבר למסך הבא אחרי 3 שניות
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity2.class));
            finish();
        }, SPLASH_DURATION);
    }

    private void downloadAndSaveData() {
        // הכנת ה-SharedPreferences לעבודה
        SharedPreferences sharedPreferences = getSharedPreferences("MyProjectPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 1. טיפול בנתוני מתאמנים (שם ומטרה)
        DBref.TraineesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> tempTrainees = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    String name = data.child("name").getValue(String.class);
                    String goal = data.child("goal").getValue(String.class);
                    if (name != null && goal != null) {
                        tempTrainees.add(name + " - " + goal);
                    }
                }
                // המרה ל-JSON ושמירה מיידית
                String json = gson.toJson(tempTrainees);
                editor.putString("trainees_list", json).apply();
                Log.d("Splash", "Trainees saved as JSON");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Splash", "Error loading trainees", error.toException());
            }
        });

        // 2. טיפול בנתוני תרגילים (שמות בלבד)
        DBref.ExercisesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> tempExercises = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    String exName = data.child("name").getValue(String.class);
                    if (exName != null) {
                        tempExercises.add(exName);
                    }
                }
                // המרה ל-JSON ושמירה מיידית
                String json = gson.toJson(tempExercises);
                editor.putString("exercises_list", json).apply();
                Log.d("Splash", "Exercises saved as JSON");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Splash", "Error loading exercises", error.toException());
            }
        });
    }
}