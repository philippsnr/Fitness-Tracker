package com.example.fitnesstracker.model;

import java.time.LocalDate;

/**
 * Represents a set of an exercise performed on a specific date.
 * Each set belongs to an exercise and contains details about repetitions, weight, and order within the session.
 */
public class ExerciseSet {

    private final int exerciseId;
    private final int setNumber;
    private final int repetition;
    private double weight;
    private final LocalDate date;

    /**
     * Constructs an ExerciseSet with the given parameters.
     *
     * @param exerciseId  the ID of the associated exercise
     * @param setNumber   the number of the set within the exercise session
     * @param repetition  the number of repetitions performed in the set
     * @param weight      the weight used for the set
     * @param date        the date of the exercise session in ISO-8601 format (YYYY-MM-DD)
     */
    public ExerciseSet(int exerciseId, int setNumber, int repetition, double weight, String date) {
        this.exerciseId = exerciseId;
        this.setNumber = setNumber;
        this.repetition = repetition;
        this.weight = weight;
        this.date = LocalDate.parse(date);
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
     * Gets the set number within the exercise session.
     *
     * @return the set number
     */
    public int getSetNumber() {
        return setNumber;
    }

    /**
     * Gets the number of repetitions performed in the set.
     *
     * @return the number of repetitions
     */
    public int getRepetition() {
        return repetition;
    }

    /**
     * Gets the weight used for the set.
     *
     * @return the weight in kilograms
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Gets the date of the exercise session.
     *
     * @return the date of the set
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the weight used for the set.
     *
     * @param weight the new weight value in kilograms
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }
}
