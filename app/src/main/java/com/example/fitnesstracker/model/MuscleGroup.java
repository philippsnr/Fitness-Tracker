package com.example.fitnesstracker.model;

/**
 * Represents a muscle group in the fitness tracking system.
 * This class defines the anatomical muscle groups that exercises can target,
 * along with visual representation of each muscle group.
 */
public class MuscleGroup {
    private final int id;
    private final String name;
    private final String picturePath;

    /**
     * Constructs a new MuscleGroup with the specified properties.
     *
     * @param id          the unique identifier for the muscle group
     * @param name        the anatomical name of the muscle group (e.g., "Biceps", "Quadriceps")
     * @param picturePath the file path or URL to an image representing the muscle group
     */
    public MuscleGroup(int id, String name, String picturePath) {
        this.id = id;
        this.name = name;
        this.picturePath = picturePath;
    }

    /**
     * Returns the unique identifier of this muscle group.
     *
     * @return the muscle group ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the anatomical name of this muscle group.
     *
     * @return the muscle group name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the path to the visual representation of this muscle group.
     *
     * @return the file path or URL of the muscle group image
     */
    public String getPicturePath() {
        return picturePath;
    }
}