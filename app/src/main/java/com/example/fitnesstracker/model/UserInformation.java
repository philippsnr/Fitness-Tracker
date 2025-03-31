package com.example.fitnesstracker.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Repräsentiert die Informationen eines Benutzers, einschließlich Größe, Gewicht, Körperfettanteil und Datum der Erfassung.
 */
public class UserInformation {
    private final int id;
    private final int userId;
    private final Date date;
    private final int height;
    private final double weight;
    private final int kfa;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    /**
     * Erstellt eine neue Instanz der Benutzerinformationen.
     *
     * @param id     Die eindeutige ID der Benutzerinformation.
     * @param userId Die ID des Benutzers, zu dem diese Informationen gehören.
     * @param date   Das Datum, an dem die Informationen erfasst wurden.
     * @param height Die Größe des Benutzers in cm.
     * @param weight Das Gewicht des Benutzers in kg.
     * @param kfa    Der Körperfettanteil des Benutzers in Prozent.
     */
    public UserInformation(int id, int userId, Date date, int height, double weight, int kfa) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.height = height;
        this.weight = weight;
        this.kfa = kfa;
    }

    /**
     * Erstellt eine neue Instanz der Benutzerinformationen mit einem Datum als String.
     *
     * @param id         Die eindeutige ID der Benutzerinformation.
     * @param userId     Die ID des Benutzers, zu dem diese Informationen gehören.
     * @param dateString Das Datum als String im Format "yyyy-MM-dd".
     * @param height     Die Größe des Benutzers in cm.
     * @param weight     Das Gewicht des Benutzers in kg.
     * @param kfa        Der Körperfettanteil des Benutzers in Prozent.
     */
    public UserInformation(int id, int userId, String dateString, int height, double weight, int kfa) {
        this.id = id;
        this.userId = userId;
        this.date = parseDate(dateString);  // Datum konvertieren
        this.height = height;
        this.weight = weight;
        this.kfa = kfa;
    }

    /**
     * Konvertiert einen String in ein {@link Date}-Objekt.
     *
     * @param dateString Das Datum als String im Format "yyyy-MM-dd".
     * @return Das konvertierte {@link Date}-Objekt oder {@code null}, falls die Konvertierung fehlschlägt.
     */
    private Date parseDate(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Getter

    /**
     * Gibt die ID der Benutzerinformationen zurück.
     *
     * @return Die ID der Benutzerinformationen.
     */
    public int getId() {
        return id;
    }

    /**
     * Gibt die Benutzer-ID zurück.
     *
     * @return Die Benutzer-ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Gibt das Datum der Erfassung zurück.
     *
     * @return Das Datum der Erfassung.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Gibt die Größe des Benutzers in cm zurück.
     *
     * @return Die Größe des Benutzers in cm.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gibt das Gewicht des Benutzers in kg zurück.
     *
     * @return Das Gewicht des Benutzers in kg.
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Gibt den Körperfettanteil des Benutzers in Prozent zurück.
     *
     * @return Der Körperfettanteil in Prozent.
     */
    public int getKfa() {
        return kfa;
    }
}
