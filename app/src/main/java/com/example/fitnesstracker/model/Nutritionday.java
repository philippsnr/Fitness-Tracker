package com.example.fitnesstracker.model;

/**
 * Represents a nutrition day entry in the fitness tracking system.
 * This class models a specific day's nutrition data with its unique identifier
 * and the corresponding date.
 */
public class Nutritionday {
    private final int id;
    private final String date;

    /**
     * Constructs a new Nutritionday with the specified ID and date.
     *
     * @param id   the unique identifier for the nutrition day entry
     * @param date the date of the nutrition entry in string format (e.g., "YYYY-MM-DD")
     */
    public Nutritionday(int id, String date) {
        this.id = id;
        this.date = date;
    }

    /**
     * Constructs a new unsaved Nutritionday for testing purposes.
     * Uses -1 as a placeholder ID for objects not yet persisted.
     *
     * @param date the date of the nutrition entry in string format (e.g., "YYYY-MM-DD")
     */
    public Nutritionday(String date) {
        this(-1, date);
    }

    /**
     * Returns the unique identifier of this nutrition day entry.
     *
     * @return the nutrition day ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the date of this nutrition day entry.
     *
     * @return the date string in format "YYYY-MM-DD"
     */
    public String getDate() {
        return date;
    }
}