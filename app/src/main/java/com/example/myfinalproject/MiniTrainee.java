package com.example.myfinalproject;

import java.util.ArrayList;

public class MiniTrainee {

    private String id;
    private String name;
    private String goal;
    private ArrayList<String> exerciseIds;

    // קונסטרקטור מלא
    public MiniTrainee(String id, String name, String goal, ArrayList<String> exerciseIds) {
        this.id = id;
        this.name = name;
        this.goal = goal;

        if (exerciseIds != null) {
            this.exerciseIds = exerciseIds;
        } else {
            this.exerciseIds = new ArrayList<>();
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGoal() {
        return goal;
    }

    public ArrayList<String> getExerciseIds() {
        return exerciseIds;
    }

    // Setters (אם תצטרך בעתיד)
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public void setExerciseIds(ArrayList<String> exerciseIds) {
        this.exerciseIds = exerciseIds;
    }

    // חשוב מאוד בשביל ה-Spinner
    @Override
    public String toString() {
        return name;
    }
}