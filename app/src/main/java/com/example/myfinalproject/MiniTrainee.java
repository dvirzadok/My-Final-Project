package com.example.myfinalproject;

public class MiniTrainee {

    private String id;
    private String name;
    private String goal;

    public MiniTrainee(String id, String name, String goal){

        this.id = id;
        this.name = name;
        this.goal = goal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
}
