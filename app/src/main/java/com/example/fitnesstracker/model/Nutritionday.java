package com.example.fitnesstracker.model;

public class Nutritionday {
    private final int id;
    private final String date;
    public Nutritionday ( int id, String date) {
        this.id = id;
        this.date = date;
    }
    // für Tests
    public Nutritionday(String date) {
        this(-1, date); // -1 als Platzhalter für nicht gespeicherte Objekte
    }


    public int getId() { return id; }
    public String getDate() { return date; }

}
