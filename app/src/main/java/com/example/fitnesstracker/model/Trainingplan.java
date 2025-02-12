package com.example.fitnesstracker.model;

public class Trainingplan {
    private final int id;
    private String name;
    private boolean isActive;
    public Trainingplan(int id, String name, int isActive) { // Constructor Overload since SQLite doesnt support booleans
        this.id = id;
        this.name = name;
        this.isActive = (isActive == 1);
    }
    public Trainingplan(int id, String name, boolean isActive)
    {
        this.id = id;
        this.name = name;
        this.isActive = isActive;
    }

    //Getter
    public int getId() { return id; }
    public String getName() { return name; }
    public boolean getIsActive() { return isActive; }

    //Setter
    public void setName(String name) { this.name = name; }
    public void setIsActive(boolean isActive) {this.isActive = isActive; }

    public int getIsActiveAsInt() { return isActive ? 1 : 0; }
}
