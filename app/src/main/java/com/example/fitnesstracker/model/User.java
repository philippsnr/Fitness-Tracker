package com.example.fitnesstracker.model;

public class User {
    private int id;
    private String name;
    private String birthDate;
    private String goal;
    private int trainingDaysPerWeek;

    public User(int id, String name, String birthDate, String goal, int trainingDaysPerWeek) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.goal = goal;
        this.trainingDaysPerWeek = trainingDaysPerWeek;
    }

    // Getter
    public int getId() { return id; }
    public String getName() { return name; }
    public String getBirthDate() { return birthDate; }
    public String getGoal() { return goal; }
    public int getTrainingDaysPerWeek() { return trainingDaysPerWeek; }

    // Setter
    public void setGoal(String goal) { this.goal = goal; }
    public void setTrainingDaysPerWeek(int trainingDaysPerWeek) { this.trainingDaysPerWeek = trainingDaysPerWeek; }
}
