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

    // Setter
    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setDate(String date) { this.date = date; }
    public void setHeight(int height) { this.height = height; }
    public void setWeight(int weight) { this.weight = weight; }
    public void setKfa(int kfa) { this.kfa = kfa; }
}
