package com.example.fitnesstracker.model;

import java.time.LocalDate;

public class ExerciseSet {

    private final int exerciseId;
    private int setNumber;
    private int repetition;
    private double weight;
    private final LocalDate date;

    public ExerciseSet(int exerciseId, int setNumber, int repetition, double weight, LocalDate date) {
        this.exerciseId = exerciseId;
        this.setNumber = setNumber;
        this.repetition = repetition;
        this.weight = weight;
        this.date = date;
    }
    public ExerciseSet(int exerciseId, int setNumber, int repetition, double weight, String date) {
        this.exerciseId = exerciseId;
        this.setNumber = setNumber;
        this.repetition = repetition;
        this.weight = weight;
        this.date = LocalDate.parse(date);
    }
    //Getter
    public int getExerciseId() { return exerciseId; }
    public int getSetNumber() { return setNumber; }
    public int getRepetition() { return repetition; }
    public double getWeight() { return weight; }
    public LocalDate getDate() { return date; }
    //Setter
    public void setSetNumber(int setNumber) { this.setNumber = setNumber; }
    public void setRepetition(int repetition) { this.repetition = repetition; }
    public void setWeight(int weight) { this.weight = weight; }
}
