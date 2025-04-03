package com.example.fitnesstracker.model;

/**
 * Represents an assignment between a training day and an exercise in the fitness tracker application.
 * This class links a specific exercise to a training day using their respective IDs.
 */
public class TrainingdayExerciseAssignment {
    private final int id;
    private final int trainingdayId;
    private final int exerciseId;

    /**
     * Constructs a TrainingdayExerciseAssignment with the given IDs.
     *
     * @param id             the unique identifier of the assignment
     * @param trainingdayId  the ID of the associated training day
     * @param exerciseId     the ID of the associated exercise
     */
    public TrainingdayExerciseAssignment(int id, int trainingdayId, int exerciseId) {
        this.id = id;
        this.trainingdayId = trainingdayId;
        this.exerciseId = exerciseId;
    }

    /**
     * Gets the ID of the assignment.
     *
     * @return the assignment ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the ID of the associated training day.
     *
     * @return the training day ID
     */
    public int getTrainingdayId() {
        return trainingdayId;
    }

    /**
     * Gets the ID of the associated exercise.
     *
     * @return the exercise ID
     */
    public int getExerciseId() {
        return exerciseId;
    }
}
