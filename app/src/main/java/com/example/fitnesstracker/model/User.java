package com.example.fitnesstracker.model;

/**
 * Repräsentiert den Benutzer.
 */
public class User {
    private int id;
    private String name;
    private String birthDate;
    private String goal;
    private int trainingDaysPerWeek;

    /**
     * Erstellt ein neues User-Objekt.
     *
     * @param id                  Die ID des Benutzers.
     * @param name                Der Name des Benutzers.
     * @param birthDate           Das Geburtsdatum des Benutzers im Format "YYYY-MM-DD".
     * @param goal                Das Fitnessziel des Benutzers.
     * @param trainingDaysPerWeek Die Anzahl der Trainingstage pro Woche.
     */
    public User(int id, String name, String birthDate, String goal, int trainingDaysPerWeek) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.goal = goal;
        this.trainingDaysPerWeek = trainingDaysPerWeek;
    }

    /**
     * Gibt die ID des Benutzers zurück.
     *
     * @return Die Benutzer-ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gibt den Namen des Benutzers zurück.
     *
     * @return Der Name des Benutzers.
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt das Geburtsdatum des Benutzers zurück.
     *
     * @return Das Geburtsdatum als String.
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * Gibt das Fitnessziel des Benutzers zurück.
     *
     * @return Das Fitnessziel.
     */
    public String getGoal() {
        return goal;
    }

    /**
     * Gibt die Anzahl der Trainingstage pro Woche zurück.
     *
     * @return Die Anzahl der Trainingstage.
     */
    public int getTrainingDaysPerWeek() {
        return trainingDaysPerWeek;
    }

    /**
     * Setzt das Fitnessziel des Benutzers.
     *
     * @param goal Das neue Fitnessziel.
     */
    public void setGoal(String goal) {
        this.goal = goal;
    }

    /**
     * Setzt die Anzahl der Trainingstage pro Woche.
     *
     * @param trainingDaysPerWeek Die neue Anzahl der Trainingstage.
     */
    public void setTrainingDaysPerWeek(int trainingDaysPerWeek) {
        this.trainingDaysPerWeek = trainingDaysPerWeek;
    }
}
