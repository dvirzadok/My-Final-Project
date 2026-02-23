package com.example.myfinalproject;

public class Exercise {

    private String id;
    private String name;       // שם התרגיל
    private int sets;          // מספר סטים
    private int reps;          // מספר חזרות
    private double weight;     // משקל אם יש
    private String notes;      // הערות נוספות

    public Exercise() {
    }

    public Exercise(String name, int sets, int reps, double weight, String notes) {
        this.name = name;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.notes = notes;
    }

    //Getters
    public String getName() {

        return name;
    }

    public int getSets() {

        return sets;
    }

    public int getReps() {

        return reps;
    }

    public double getWeight() {

        return weight;
    }

    public String getNotes() {

        return notes;
    }

    //Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setSets(int sets) {

        this.sets = sets;
    }

    public void setReps(int reps) {

        this.reps = reps;
    }

    public void setWeight(double weight) {

        this.weight = weight;
    }

    public void setNotes(String notes) {

        this.notes = notes;
    }
}
