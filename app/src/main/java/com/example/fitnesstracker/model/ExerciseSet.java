package com.example.fitnesstracker.model;

import java.time.LocalDate;

public class ExerciseSet {
    private final int id;
    private final int exerciseId;
    private int setNumber;
    private int repetition;
    private int weight;
    private final LocalDate date;

    public ExerciseSet(int id, int exerciseId, int setNumber, int repetition, int weight, LocalDate date) {
        this.id = id;
        this.exerciseId = exerciseId;
        this.setNumber = setNumber;
        this.repetition = repetition;
        this.weight = weight;
        this.date = date;
    }
    public ExerciseSet(int id, int exerciseId, int setNumber, int repetition, int weight, String date) {
        this.id = id;
        this.exerciseId = exerciseId;
        this.setNumber = setNumber;
        this.repetition = repetition;
        this.weight = weight;
        this.date = LocalDate.parse(date);
    }
    //Getter
    public int getId() { return id; }
    public int getExerciseId() { return exerciseId; }
    public int getSetNumber() { return setNumber; }
    public int getRepetition() { return repetition; }
    public int getWeight() { return weight; }
    public LocalDate getDate() { return date; }
    //Setter
    public void setSetNumber(int setNumber) { this.setNumber = setNumber; }
    public void setRepetition(int repetition) { this.repetition = repetition; }
    public void setWeight(int weight) { this.weight = weight; }
}
