package com.example.myfinalproject;

import java.util.List;

public class WorkoutPlan {

    private String id;
    private String traineeId;
    private String planName;
    private String description;
    private List<Exercise> exercises; //  רשימת תרגילים הכלולים בתוכנית מסוג exercise
    private String creationDate;      // תאריך יצירת התוכנית

    public WorkoutPlan(){
    }

    public WorkoutPlan(String id, String traineeId, String planName,
                       String description, List<Exercise> exercises,
                       String creationDate) {
        this.id = id;
        this.traineeId = traineeId;
        this.planName = planName;
        this.description = description;
        this.exercises = exercises;
        this.creationDate = creationDate;
    }

    //Getters
    public String getId() {
        return id;
    }

    public String getTraineeId() {
        return traineeId;
    }

    public String getPlanName() {
        return planName;
    }

    public String getDescription() {
        return description;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public String getCreationDate() {
        return creationDate;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setTraineeId(String traineeId) {
        this.traineeId = traineeId;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
