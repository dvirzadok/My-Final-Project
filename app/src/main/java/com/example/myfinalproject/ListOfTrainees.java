package com.example.myfinalproject;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ListOfTrainees extends Fragment {

    private RecyclerView recyclerView;
    private TraineeAdapter adapter;
    private ArrayList<String> traineeList;
    private Button btnAddTrainee;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_of_trainees, container, false);

        // 1. קישור ה-RecyclerView
        recyclerView = view.findViewById(R.id.recyclerTrainees);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 2. הגדרת כפתור הוספה ומעבר פרגמנט
        btnAddTrainee = view.findViewById(R.id.btnAddTrainee);
        btnAddTrainee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main, new AddNewTrainee())
                        .addToBackStack(null)
                        .commit();
            }
        });

        // 3. טעינת הנתונים מה-SharedPreferences
        loadTraineesFromSP();

        return view;
    }

    private void loadTraineesFromSP() {
        if (getContext() != null) {
            SharedPreferences sp = getContext().getSharedPreferences("MyProjectPrefs", MODE_PRIVATE);
            String json = sp.getString("trainees_list", "[]");

            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>(){}.getType();
            traineeList = gson.fromJson(json, type);

            if (traineeList == null) {
                traineeList = new ArrayList<>();
            }

            adapter = new TraineeAdapter(traineeList);
            recyclerView.setAdapter(adapter);
        }
    }
}