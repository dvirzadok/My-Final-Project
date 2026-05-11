package com.example.myfinalproject;

public class WeightEntry {

    private double weight;
    private String date;

    public WeightEntry() {
        // חובה לפיירבייס
    }

    public WeightEntry(double weight, String date) {
        this.weight = weight;
        this.date = date;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}