package com.example.fitnesstracker.model;

/**
 * Represents the association between an exercise and a muscle group.
 * This class models the many-to-many relationship between exercises
 * and muscle groups in the fitness tracking system.
 */
public class ExerciseMuscleGroupAssignment {
    private int id;
    private int exerciseId;
    private int muscleGroupId;

    /**
     * Default constructor for creating an empty assignment.
     * Primarily used by persistence frameworks.
     */
    public ExerciseMuscleGroupAssignment() {}

    /**
     * Constructs a new exercise-muscle group assignment with the given IDs.
     *
     * @param exerciseId    the ID of the associated exercise
     * @param muscleGroupId the ID of the associated muscle group
     */
    public ExerciseMuscleGroupAssignment(int exerciseId, int muscleGroupId) {
        this.exerciseId = exerciseId;
        this.muscleGroupId = muscleGroupId;
    }

    /**
     * Gets the unique identifier of this assignment.
     *
     * @return the assignment ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the ID of the associated exercise.
     *
     * @return the exercise ID
     */
    public int getExerciseId() {
        return exerciseId;
    }

    /**
     * Gets the ID of the associated muscle group.
     *
     * @return the muscle group ID
     */
    public int getMuscleGroupId() {
        return muscleGroupId;
    }
}