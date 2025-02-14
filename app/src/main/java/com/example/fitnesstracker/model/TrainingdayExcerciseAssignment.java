package com.example.fitnesstracker.model;

public class TrainingdayExcerciseAssignment {
    private final int id;
    private int trainingdayId;
    private int excerciseId;

    public TrainingdayExcerciseAssignment (int id, int trainingdayId, int excerciseId) {
        this.id = id;
        this.trainingdayId = trainingdayId;
        this.excerciseId = excerciseId;
    }

    public int getId() { return id; }
    public int getTrainingplanId() { return trainingdayId; }
    public int getExcerciseId() { return excerciseId; }
    public void setTrainingdayId( int trainingdayId ) { this.trainingdayId = trainingdayId; }
    public void setExcerciseId ( int excerciseId ) { this.excerciseId = excerciseId; }
}
