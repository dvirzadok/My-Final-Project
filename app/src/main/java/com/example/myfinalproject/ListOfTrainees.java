package com.example.myfinalproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListOfTrainees#newInstance} factory method to
 * create an instance of this fragment.
 */


    public class ListOfTrainees extends Fragment {

        private RecyclerView recyclerView;
        private TraineeAdapter adapter;
        private List<Trainee> traineeList;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_list_of_trainees, container, false);

            recyclerView = view.findViewById(R.id.recyclerTrainees);

            // יצירת נתונים זמניים לבדיקה
            traineeList = new ArrayList<>();

            Trainee t1 = new Trainee();
            t1.setName("דביר");
            t1.setGoal("מסה");

            Trainee t2 = new Trainee();
            t2.setName("יוסי");
            t2.setGoal("חיטוב");

            Trainee t3 = new Trainee();
            t3.setName("אורי");
            t3.setGoal("כוח");

            traineeList.add(t1);
            traineeList.add(t2);
            traineeList.add(t3);

            // יצירת adapter
            adapter = new TraineeAdapter(traineeList, getContext());

            // איך הרשימה תוצג
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            // חיבור
            recyclerView.setAdapter(adapter);

            return view;
        }
    }