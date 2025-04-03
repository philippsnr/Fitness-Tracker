package com.example.fitnesstracker.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.Trainingday;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository-Klasse für den Zugriff auf die Trainingstage in der SQLite-Datenbank.
 * Diese Klasse bietet CRUD-Operationen für Trainingstage und ermöglicht das Abrufen,
 * Erstellen, Aktualisieren und Löschen von Trainingstagen, die mit einem bestimmten
 * Trainingsplan verknüpft sind.
 */
public class TrainingdayRepository {
    private final DatabaseHelper dbHelper;

    /**
     * Initialisiert eine neue Repository-Instanz mit dem Anwendungskontext.
     *
     * @param context Der Anwendungskontext für den Datenbankzugriff
     */
    public TrainingdayRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * Ruft alle Trainingstage ab, die mit einem bestimmten Trainingsplan verknüpft sind.
     *
     * @param trainingplanId Die ID des Trainingsplans, für den die Trainingstage abgefragt werden sollen
     * @return Liste von Trainingday-Objekten, die zu dem angegebenen Plan gehören
     */
    public List<Trainingday> getTrainingdaysForPlan(int trainingplanId) {
        List<Trainingday> trainingdays = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM Trainingday WHERE Trainingplan_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(trainingplanId)});

        if (cursor.moveToFirst()) {
            do {
                trainingdays.add(new Trainingday(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2)
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return trainingdays;
    }

    /**
     * Erstellt einen neuen Trainingstag in der Datenbank.
     *
     * @param trainingday Das Trainingday-Objekt mit den einzufügenden Daten
     */
    public void createTrainingday(Trainingday trainingday) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", trainingday.getName());
        values.put("Trainingplan_id", trainingday.getTrainingplanId());

        db.insert("Trainingday", null, values);
        db.close();
    }

    /**
     * Aktualisiert einen bestehenden Trainingstag in der Datenbank.
     *
     * @param trainingday Das Trainingday-Objekt mit aktualisierten Werten.
     *                    Die ID des Objekts wird zur Identifikation des Datenbankeintrags verwendet.
     */
    public void updateTrainingday(Trainingday trainingday) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", trainingday.getName());
        values.put("Trainingplan_id", trainingday.getTrainingplanId());

        db.update("Trainingday", values, "id = ?", new String[]{String.valueOf(trainingday.getId())});
        db.close();
    }

    /**
     * Löscht einen bestimmten Trainingstag aus der Datenbank.
     *
     * @param trainingday Das zu löschende Trainingday-Objekt.
     *                    Die ID des Objekts wird zur Identifikation des Datenbankeintrags verwendet.
     */
    public void deleteTrainingday(Trainingday trainingday) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Trainingday", "id = ?", new String[]{String.valueOf(trainingday.getId())});
        db.close();
    }
}