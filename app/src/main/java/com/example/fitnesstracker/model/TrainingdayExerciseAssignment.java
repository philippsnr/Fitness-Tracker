package com.example.fitnesstracker.model;

public class TrainingdayExerciseAssignment {
    private final int id;
    private final int trainingdayId;
    private final int exerciseId;

    public TrainingdayExerciseAssignment(int id, int trainingdayId, int exerciseId) {
        this.id = id;
        this.trainingdayId = trainingdayId;
        this.exerciseId = exerciseId;
    }

    public int getId() { return id; }
    public int getTrainingdayId() { return trainingdayId; }

    public int getExerciseId() { return exerciseId; }
}
