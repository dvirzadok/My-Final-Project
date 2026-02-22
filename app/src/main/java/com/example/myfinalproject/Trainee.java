package com.example.myfinalproject;

import java.util.List;

public class Trainee {


    private String id;         //מזהה ייחודי
    private String name;
    private int age;
    private String phone;

    private double weight;
    private double height;

    private double monthlyCost;      // כמה משלם כל חודש
    private double remainingDebt;    // כמה נשאר לשלם

    private int dayOfPayment;
    private String subscriptionEndDate; // תאריך סיום מנוי (לצורך התראות)

    private List<Double> weightTracking; //בכל סוף שבוע הוא מעדכן את המשקל הנתון האחרון


    public Trainee() {
    }


    public Trainee(String id, String name, int age, String phone,
                   double weight, double height,
                   double monthlyCost, double remainingDebt,
                   int dayOfPayment, String subscriptionEndDate,
                   List<Double> weightTracking) {

        this.id = id;
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.weight = weight;
        this.height = height;
        this.monthlyCost = monthlyCost;
        this.remainingDebt = remainingDebt;
        this.dayOfPayment = dayOfPayment;
        this.subscriptionEndDate = subscriptionEndDate;
        this.weightTracking = weightTracking;
    }

    //Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getPhone() {
        return phone;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    public double getMonthlyCost() {
        return monthlyCost;
    }

    public double getRemainingDebt() {
        return remainingDebt;
    }

    public int getDayOfPayment() {
        return dayOfPayment;
    }

    public String getSubscriptionEndDate() {
        return subscriptionEndDate;
    }

    public List<Double> getWeightTracking() {
        return weightTracking;
    }


    //Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setMonthlyCost(double monthlyCost) {
        this.monthlyCost = monthlyCost;
    }

    public void setRemainingDebt(double remainingDebt) {
        this.remainingDebt = remainingDebt;
    }

    public void setDayOfPayment(int dayOfPayment) {
        this.dayOfPayment = dayOfPayment;
    }

    public void setSubscriptionEndDate(String subscriptionEndDate) {
        this.subscriptionEndDate = subscriptionEndDate;
    }

    public void setWeightTracking(List<Double> weightTracking) {
        this.weightTracking = weightTracking;
    }
}