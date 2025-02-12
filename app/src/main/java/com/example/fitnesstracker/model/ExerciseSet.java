package com.example.fitnesstracker.model;

public class ExerciseSet {
    private final int id;
    private final int TrainingdayExerciseAssignment_id;
    private int setNumber;
    private int repetition;
    private int weight;

    public ExerciseSet(int id, int trainingdayExerciseAssignment_id, int setNumber, int repetition, int weight) {
        this.id = id;
        this.TrainingdayExerciseAssignment_id = trainingdayExerciseAssignment_id;
        this.setNumber = setNumber;
        this.repetition = repetition;
        this.weight = weight;
    }
    //Getter
    public int getId() { return id; }
    public int getTrainingdayExerciseAssignment_id() { return TrainingdayExerciseAssignment_id; }
    public int getSetNumber() { return setNumber; }
    public int getRepetition() { return repetition; }
    public int getWeight() { return weight; }

    //Setter
    public void setSetNumber(int setNumber) { this.setNumber = setNumber; }
    public void setRepetition(int repetition) { this.repetition = repetition; }
    public void setWeight(int weight) { this.weight = weight; }
}
