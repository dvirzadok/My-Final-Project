package com.example.myfinalproject;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

public class PaymentWorker extends Worker {

    public PaymentWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // קבלת היום הנוכחי בחודש
        Calendar calendar = Calendar.getInstance();
        int todayDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // שימוש ב-CountDownLatch כדי לחכות לסיום הפעולה מול Firebase
        CountDownLatch latch = new CountDownLatch(1);
        final boolean[] isSuccess = {true};

        DBref.TraineesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Trainee trainee = data.getValue(Trainee.class);

                    if (trainee != null && trainee.getDayOfPayment() == todayDayOfMonth) {
                        // חישוב החוב החדש
                        double newDebt = trainee.getRemainingDebt() + trainee.getMonthlyCost();

                        // עדכון החוב ב-Firebase עבור מתאמן זה
                        data.getRef().child("remainingDebt").setValue(newDebt);
                        Log.d("PaymentWorker", "Updated debt for: " + trainee.getName());
                    }
                }
                latch.countDown(); // שחרור ההשהיה, הפעולה הסתיימה
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PaymentWorker", "Database error: " + error.getMessage());
                isSuccess[0] = false;
                latch.countDown(); // שחרור ההשהיה גם במקרה של שגיאה
            }
        });

        try {
            // המתנה עד ש-Firebase יסיים את הפעולה
            latch.await();
        } catch (InterruptedException e) {
            return Result.failure();
        }

        if (isSuccess[0]) {
            return Result.success();
        } else {
            return Result.retry(); // ינסה שוב מאוחר יותר במקרה של שגיאת רשת
        }
    }
}