package com.example.myfinalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListOfTrainees extends Fragment {

    private RecyclerView recyclerView;
    private TraineeAdapter adapter;
    private Button btnAddTrainee;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_of_trainees, container, false);

        recyclerView = view.findViewById(R.id.recyclerTrainees);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnAddTrainee = view.findViewById(R.id.btnAddTrainee);
        btnAddTrainee.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, new AddNewTrainee())
                    .addToBackStack(null)
                    .commit();
        });

        loadTrainees();

        return view;
    }

    private void loadTrainees() {

        adapter = new TraineeAdapter(
                SplashActivity.traineesList,
                trainee -> loadFullTrainee(trainee.getId())
        );

        recyclerView.setAdapter(adapter);
    }

    private void loadFullTrainee(String id) {

        DBref.TraineesRef.child(String.valueOf(id))
                .get()
                .addOnSuccessListener(snapshot -> {

                    Trainee full = snapshot.getValue(Trainee.class);

                    if (full != null) {
                        DataHolder.selectedTraineeFull = full;

                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment_content_main, new ViewTraineeFragment())
                                .addToBackStack(null)
                                .commit();
                    }
                });
    }
}