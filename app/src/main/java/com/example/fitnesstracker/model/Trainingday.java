package com.example.fitnesstracker.model;

public class Trainingday {
    private int id;
    private String name;
    private int trainingplanId;

    public Trainingday(int id, String name, int trainingplanId) {
        this.id = id;
        this.name = name;
        this.trainingplanId = trainingplanId;
    }

    // Getter und Setter
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTrainingplanId() {
        return trainingplanId;
    }
}
