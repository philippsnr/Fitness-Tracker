package com.example.fitnesstracker.model;

public class Exercise {
    private final int id;
    private final String name;
    private final String difficulty;
    private final String info;
    private final String picturePath;

    public Exercise(int id, String name, String difficulty, String info, String picturePath) {
        this.id = id;
        this.name = name;
        this.difficulty = difficulty;
        this.info = info;
        this.picturePath = picturePath;
    }

    // Getter
    public int getId() { return id; }

    public String getName() { return name; }

    public String getDifficulty() { return difficulty; }

    public String getInfo() { return info; }

    public String getPicturePath() { return picturePath; }
}
