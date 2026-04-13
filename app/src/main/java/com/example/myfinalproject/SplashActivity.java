package com.example.myfinalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

    private static final int SPLASH_DURATION = 3000; // 3 שניות

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //המסך עם הלוגו

        // לעבור ל-MainActivity2 אחרי 3 שניות
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity2.class));
            finish(); // סוגר את הספלש
        }, SPLASH_DURATION);
    }
}