package com.example.myfinalproject;

import java.util.ArrayList;

public class Exercise {

    private String id;
    private String name;
    private int sets;
    private int reps;
    private double weight;
    private String notes;

    public Exercise() {
    }

    public Exercise(String id, String name, int sets, int reps, double weight, String notes) {

        this.id = id;
        this.name = name;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.notes = notes;


    }

    public String getId() {
        return id;
    }

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



    public void setId(String id) {
        this.id = id;
    }

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