package com.example.fitnesstracker.model;

public class ExerciseMuscleGroupAssignment {
    private int id;
    private int exerciseId;
    private int muscleGroupId;

    public ExerciseMuscleGroupAssignment() {}

    public ExerciseMuscleGroupAssignment(int exerciseId, int muscleGroupId) {
        this.exerciseId = exerciseId;
        this.muscleGroupId = muscleGroupId;
    }

    // Getter
    public int getId() {
        return id;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public int getMuscleGroupId() {
        return muscleGroupId;
    }
}
