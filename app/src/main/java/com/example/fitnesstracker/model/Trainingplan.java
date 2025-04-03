package com.example.fitnesstracker.model;

/**
 * Represents a training plan in the fitness tracker application.
 * A training plan consists of an ID, a name, and an active status.
 * Since SQLite does not support booleans, the active status can be retrieved as an integer (1 for true, 0 for false).
 */
public class Trainingplan {
    private int id;
    private String name;
    private final boolean isActive;

    /**
     * Constructs a Trainingplan with a specified ID, name, and active status.
     *
     * @param id        the unique identifier of the training plan
     * @param name      the name of the training plan
     * @param isActive  the active status of the training plan
     */
    public Trainingplan(int id, String name, boolean isActive) {
        this.id = id;
        this.name = name;
        this.isActive = isActive;
    }

    /**
     * Constructs a Trainingplan without an ID.
     * This constructor is used when the ID is assigned later (e.g., by a database).
     *
     * @param name      the name of the training plan
     * @param isActive  the active status of the training plan
     */
    public Trainingplan(String name, boolean isActive) {
        this.name = name;
        this.isActive = isActive;
    }

    /**
     * Gets the ID of the training plan.
     *
     * @return the training plan ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the training plan.
     *
     * @return the training plan name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the active status of the training plan.
     *
     * @return true if the training plan is active, false otherwise
     */
    public boolean getIsActive() {
        return isActive;
    }

    /**
     * Sets the name of the training plan.
     *
     * @param name the new name of the training plan
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Converts the active status to an integer (1 for true, 0 for false).
     * This is useful for storing the value in an SQLite database.
     *
     * @return 1 if the training plan is active, 0 otherwise
     */
    public int getIsActiveAsInt() {
        return isActive ? 1 : 0;
    }

    /**
     * Sets the ID of the training plan.
     * Since SQLite typically returns long values for IDs, this method handles the conversion.
     *
     * @param id the new ID of the training plan
     */
    public void setId(long id) {
        this.id = (int) id;
    }
}
