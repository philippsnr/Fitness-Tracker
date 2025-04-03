package com.example.fitnesstracker.model;

/**
 * Represents an exercise in the fitness tracking application.
 * This class encapsulates all the relevant information about an exercise,
 * including its identification, difficulty level, description, and visual representation.
 */
public class Exercise {
    private final int id;
    private final String name;
    private final int difficulty;
    private final String info;
    private final String picturePath;

    /**
     * Constructs a new Exercise with the specified parameters.
     *
     * @param id          the unique identifier for the exercise
     * @param name        the name of the exercise
     * @param difficulty  the difficulty level of the exercise (typically on a numerical scale)
     * @param info        descriptive information about the exercise
     * @param picturePath the file path or URL to an image representing the exercise
     */
    public Exercise(int id, String name, int difficulty, String info, String picturePath) {
        this.id = id;
        this.name = name;
        this.difficulty = difficulty;
        this.info = info;
        this.picturePath = picturePath;
    }

    /**
     * Returns the unique identifier of the exercise.
     *
     * @return the exercise ID
     */
    public int getId() { return id; }

    /**
     * Returns the name of the exercise.
     *
     * @return the exercise name
     */
    public String getName() { return name; }

    /**
     * Returns the difficulty level of the exercise.
     *
     * @return the exercise difficulty level
     */
    public int getDifficulty() { return difficulty; }

    /**
     * Returns descriptive information about the exercise.
     *
     * @return the exercise description
     */
    public String getInfo() { return info; }

    /**
     * Returns the path to the exercise's image representation.
     *
     * @return the file path or URL of the exercise image
     */
    public String getPicturePath() { return picturePath; }
}