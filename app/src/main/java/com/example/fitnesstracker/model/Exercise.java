package com.example.fitnesstracker.model;

public class Exercise {
    private int id;
    private String name;
    private String difficulty;
    private String info;
    private String picturePath;

    public Exercise(int id, String name, String difficulty, String info, String picturePath) {
        this.id = id;
        this.name = name;
        this.difficulty = difficulty;
        this.info = info;
        this.picturePath = picturePath;
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getInfo() { return info; }
    public void setInfo(String info) { this.info = info; }

    public String getPicturePath() { return picturePath; }
    public void setPicturePath(String picturePath) { this.picturePath = picturePath; }
}
