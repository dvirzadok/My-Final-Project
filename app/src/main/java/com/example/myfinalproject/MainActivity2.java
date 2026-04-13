package com.example.myfinalproject;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class MainActivity2 extends AppCompatActivity {

    // כפתורים
    private View btTrainees;
    private View btPlans;
    private View btProgress;
    private View btPayments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Toolbarחיבור ל
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // חיבור לכפתורים
        btTrainees = findViewById(R.id.nav_trainees);
        btPlans = findViewById(R.id.nav_plans);
        btProgress = findViewById(R.id.nav_progress);
        btPayments = findViewById(R.id.nav_payments);

        // מסך ראשון
        openFragment(new ListOfTrainees());

        // לחיצות
        btTrainees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new ListOfTrainees());
            }
        });

        btPlans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new TrainingPrograms());
            }
        });

        btProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new ProgressTracking());
            }
        });

        btPayments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new payments());
            }
        });
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, fragment)
                .commit();
    }
}