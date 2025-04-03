package com.example.fitnesstracker.model;

/**
 * Represents a training day in the fitness tracker application.
 * A training day belongs to a specific training plan and has a unique ID and a name.
 */
public class Trainingday {
    private final int id;
    private String name;
    private final int trainingplanId;

    /**
     * Constructs a Trainingday with the given ID, name, and training plan ID.
     *
     * @param id             the unique identifier of the training day
     * @param name           the name of the training day
     * @param trainingplanId the ID of the associated training plan
     */
    public Trainingday(int id, String name, int trainingplanId) {
        this.id = id;
        this.name = name;
        this.trainingplanId = trainingplanId;
    }

    /**
     * Gets the ID of the training day.
     *
     * @return the training day ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the training day.
     *
     * @return the name of the training day
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the ID of the associated training plan.
     *
     * @return the training plan ID
     */
    public int getTrainingplanId() {
        return trainingplanId;
    }

    /**
     * Sets the name of the training day.
     *
     * @param name the new name of the training day
     */
    public void setName(String name) {
        this.name = name;
    }
}
