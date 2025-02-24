package com.example.fitnesstracker.model;

public class MuscleGroup {
    private final int id;
    private final String name;
    private final String picturePath;

    public MuscleGroup(int id, String name, String picturePath) {
        this.id = id;
        this.name = name;
        this.picturePath = picturePath;
    }

    // Getter
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPicturePath() {
        return picturePath;
    }
}
