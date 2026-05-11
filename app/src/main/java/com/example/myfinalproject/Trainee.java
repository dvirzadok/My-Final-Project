package com.example.myfinalproject;

import java.util.ArrayList;
import java.util.List;

public class Trainee {

    private String id;
    private String name;
    private int age;
    private String phone;
    private double weight;
    private String goal;

    private ArrayList<String> exerciseIds; // רשימת מזהי תרגילים של המתאמן

    private double monthlyCost;
    private double remainingDebt;

    private int dayOfPayment;
    private String subscriptionEndDate;

    private List<WeightEntry> weightTracking;

    public Trainee() {
        // חשוב לפיירבייס
    }

    public Trainee(String id, String name, int age, String phone,
                   double weight,
                   double monthlyCost, double remainingDebt,
                   int dayOfPayment, String subscriptionEndDate, String goal,
                   List<WeightEntry> weightTracking,
                   ArrayList<String> exerciseIds) {

        this.id = id;
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.weight = weight;
        this.goal = goal;
        this.monthlyCost = monthlyCost;
        this.remainingDebt = remainingDebt;
        this.dayOfPayment = dayOfPayment;
        this.subscriptionEndDate = subscriptionEndDate;
        this.weightTracking = weightTracking;

        // אם אין נתונים מפיירבייס → נאתחל ריק
        if (exerciseIds != null) {
            this.exerciseIds = exerciseIds;
        } else {
            this.exerciseIds = new ArrayList<>();
        }
    }

    // Getters

    public String getId() { return id; }

    public String getName() { return name; }

    public int getAge() { return age; }

    public String getPhone() { return phone; }

    public double getWeight() { return weight; }

    public String getGoal() { return goal; }

    public double getMonthlyCost() { return monthlyCost; }

    public double getRemainingDebt() { return remainingDebt; }

    public int getDayOfPayment() { return dayOfPayment; }

    public String getSubscriptionEndDate() { return subscriptionEndDate; }

    public List<WeightEntry> getWeightTracking() { return weightTracking; }

    public  ArrayList<String> getExerciseIds() { return exerciseIds; }

    // Setters
    public void setId(String id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setAge(int age) { this.age = age; }

    public void setPhone(String phone) { this.phone = phone; }

    public void setWeight(double weight) { this.weight = weight; }

    public void setGoal(String goal) { this.goal = goal; }

    public void setMonthlyCost(double monthlyCost) { this.monthlyCost = monthlyCost; }

    public void setRemainingDebt(double remainingDebt) { this.remainingDebt = remainingDebt; }

    public void setDayOfPayment(int dayOfPayment) { this.dayOfPayment = dayOfPayment; }

    public void setSubscriptionEndDate(String subscriptionEndDate) { this.subscriptionEndDate = subscriptionEndDate; }

    public void setWeightTracking(List<WeightEntry> weightTracking) { this.weightTracking = weightTracking; }

    public void setExerciseIds(ArrayList<String> exerciseIds) {
        this.exerciseIds = exerciseIds;
    }

    // פעולות על התרגילים

    public void addExerciseId(String exerciseId) {
        if (exerciseIds == null) {
            exerciseIds = new ArrayList<>();
        }
        exerciseIds.add(exerciseId);
    }

    public void clearExerciseIds() {
        if (exerciseIds != null) {
            exerciseIds.clear();
        }
    }
}