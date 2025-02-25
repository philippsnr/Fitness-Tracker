package com.example.fitnesstracker.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserInformation {
    private int id;
    private int userId;
    private Date date;
    private int height;
    private int weight;
    private int kfa; // Körperfettanteil in Prozent

    // Konstruktoren
    public UserInformation(int id, int userId, Date date, int height, int weight, int kfa) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.height = height;
        this.weight = weight;
        this.kfa = kfa;
    }

    public UserInformation(int id, int userId, String dateString, int height, int weight, int kfa) {
        this.id = id;
        this.userId = userId;
        this.date = parseDate(dateString);  // Datum konvertieren
        this.height = height;
        this.weight = weight;
        this.kfa = kfa;
    }

    private Date parseDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Getter
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public Date getDate() { return date; }
    public int getHeight() { return height; }
    public int getWeight() { return weight; }
    public int getKfa() { return kfa; }
}
