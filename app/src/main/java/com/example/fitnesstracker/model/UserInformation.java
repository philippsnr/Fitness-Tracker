package com.example.fitnesstracker.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserInformation {
    private int id;
    private int userId;
    private Date date;
    private int height;
    private int weight;
    private int kfa; // Körperfettanteil in Prozent

    // Konstruktor, der ein Date akzeptiert
    public UserInformation(int id, int userId, Date date, int height, int weight, int kfa) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.height = height;
        this.weight = weight;
        this.kfa = kfa;
    }

    // Konstruktor, der einen Date-String akzeptiert und ihn in ein Date umwandelt
    public UserInformation(int id, int userId, String dateString, int height, int weight, int kfa) {
        this.id = id;
        this.userId = userId;
        // Datum von String in Date umwandeln
        this.date = parseDate(dateString);
        this.height = height;
        this.weight = weight;
        this.kfa = kfa;
    }

    // Hilfsmethode, um das Datum aus einem String zu parsen
    private Date parseDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  // Format anpassen, falls notwendig
        try {
            return dateFormat.parse(dateString);  // String in Date umwandeln
        } catch (ParseException e) {
            e.printStackTrace();
            return null;  // Falls das Parsing fehlschlägt, null zurückgeben
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
