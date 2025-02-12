package com.example.fitnesstracker.model;

public class UserInformation {
    private int id;
    private int userId;
    private String date;
    private int height;
    private int weight;
    private int kfa; // Körperfettanteil in Prozent

    public UserInformation(int id, int userId, String date, int height, int weight, int kfa) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.height = height;
        this.weight = weight;
        this.kfa = kfa;
    }

    // Getter
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getDate() { return date; }
    public int getHeight() { return height; }
    public int getWeight() { return weight; }
    public int getKfa() { return kfa; }
}
