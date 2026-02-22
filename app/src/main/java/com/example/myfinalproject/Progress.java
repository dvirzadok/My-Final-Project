package com.example.myfinalproject;

public class Progress {

    private String traineeId;  // למי זה שייך
    private String date;       // תאריך הקלטת התקדמות
    private double weight;     // משקל הנמדד
    private String note;       // הערה

    public Progress() {
    }

    public Progress(String traineeId, String date, double weight, String note) {
        this.traineeId = traineeId;
        this.date = date;
        this.weight = weight;
        this.note = note;
    }

    //Getters
    public String getTraineeId() {
        return traineeId;
    }

    public String getDate() {
        return date;
    }

    public double getWeight() {
        return weight;
    }

    public String getNote() {
        return note;
    }

    //Setters
    public void setTraineeId(String traineeId) {
        this.traineeId = traineeId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

