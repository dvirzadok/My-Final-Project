package com.example.myfinalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
// Imports חדשים עבור ה-WorkManager
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class SplashActivity extends Activity {

    private static final int SPLASH_DURATION = 3000;

    public static ArrayList<Exercise> exercisesList = new ArrayList<>();
    public static ArrayList<MiniTrainee> traineesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // טעינת נתונים קיימת
        loadExercises();
        loadTrainees();

        // הפעלת תזמון בדיקת התשלומים בחצות
        scheduleDailyPaymentCheck();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity2.class));
                finish();
            }
        }, SPLASH_DURATION);
    }

    /**
     * פונקציה האחראית לתזמן את ה-Worker שירוץ בכל חצות
     */
    private void scheduleDailyPaymentCheck() {
        Calendar currentDate = Calendar.getInstance();
        Calendar dueDate = Calendar.getInstance();

        // הגדרת זמן היעד ל-12 בלילה הקרוב
        dueDate.set(Calendar.HOUR_OF_DAY, 0);
        dueDate.set(Calendar.MINUTE, 0);
        dueDate.set(Calendar.SECOND, 0);

        // אם השעה 12 בלילה כבר עברה היום, נתזמן למחר
        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24);
        }

        // חישוב ההפרש בזמן (מתי להריץ את הבדיקה הראשונה)
        long timeDiff = dueDate.getTimeInMillis() - currentDate.getTimeInMillis();

        // יצירת בקשת עבודה מחזורית של פעם ב-24 שעות
        PeriodicWorkRequest paymentWorkRequest = new PeriodicWorkRequest.Builder(
                PaymentWorker.class, 24, TimeUnit.HOURS)
                .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                .build();

        // רישום המשימה ב-WorkManager
        // KEEP אומר שאם המשימה כבר קיימת בתור, אל תדרוס אותה (כדי לא לאפס את הטיימר כל פעם שפותחים אפליקציה)
        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork(
                "DailyPaymentCheck",
                ExistingPeriodicWorkPolicy.KEEP,
                paymentWorkRequest
        );
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