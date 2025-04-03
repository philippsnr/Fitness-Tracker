package com.example.fitnesstracker.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.Trainingplan;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository-Klasse für den Zugriff auf die Trainingspläne in der SQLite-Datenbank.
 * Diese Klasse bietet Methoden zum Abrufen, Erstellen, Aktualisieren und Löschen
 * von Trainingsplänen sowie zur Verwaltung des aktiven Trainingsplans.
 */
public class TrainingplanRepository {
    private final DatabaseHelper dbHelper;

    /**
     * Initialisiert eine neue Repository-Instanz zur Verwaltung von Trainingsplänen.
     *
     * @param context Der Anwendungskontext für den Datenbankzugriff
     */
    public TrainingplanRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * Ruft alle Trainingspläne aus der Datenbank ab.
     *
     * @return Liste aller gespeicherten Trainingplan-Objekte
     */
    public List<Trainingplan> getAllTrainingplans() {
        List<Trainingplan> trainingplans = new ArrayList<>();
        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM Trainingplan", null)) {

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    boolean isActive = cursor.getInt(cursor.getColumnIndexOrThrow("isActive")) == 1;
                    trainingplans.add(new Trainingplan(id, name, isActive));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("TrainingplanRepository", "Fehler beim Abrufen aller Trainingspläne", e);
        }
        return trainingplans;
    }

    /**
     * Ruft den aktuell aktiven Trainingsplan ab.
     *
     * @return Aktives Trainingplan-Objekt oder null, falls keiner existiert
     */
    public Trainingplan getActiveTrainingplan() {
        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM Trainingplan WHERE isActive = 1", null)) {

            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                boolean isActive = cursor.getInt(cursor.getColumnIndexOrThrow("isActive")) == 1;
                return new Trainingplan(id, name, isActive);
            }
        } catch (Exception e) {
            Log.e("TrainingplanRepository", "Fehler beim Abrufen des aktiven Trainingsplans", e);
        }
        return null;
    }

    /**
     * Fügt einen neuen Trainingsplan zur Datenbank hinzu.
     *
     * @param trainingplan Das hinzuzufügende Trainingplan-Objekt (ID wird automatisch generiert)
     */
    public void addTrainingplan(Trainingplan trainingplan) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put("name", trainingplan.getName());
            values.put("isActive", trainingplan.getIsActiveAsInt());
            long id = db.insert("Trainingplan", null, values);
            trainingplan.setId(id);
        } catch (Exception e) {
            Log.e("TrainingplanRepository", "Fehler beim Hinzufügen des Trainingsplans", e);
        }
    }

    /**
     * Aktualisiert den Namen eines bestehenden Trainingsplans.
     *
     * @param trainingplan Das Trainingplan-Objekt mit aktualisiertem Namen und ursprünglicher ID
     */
    public void updateTrainingplanName(Trainingplan trainingplan) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put("name", trainingplan.getName());
            db.update("Trainingplan", values, "id = ?", new String[]{String.valueOf(trainingplan.getId())});
        } catch (Exception e) {
            Log.e("TrainingplanRepository", "Fehler beim Aktualisieren des Trainingsplans", e);
        }
    }

    /**
     * Löscht einen bestimmten Trainingsplan aus der Datenbank.
     *
     * @param trainingplan Das zu löschende Trainingplan-Objekt
     */
    public void deleteTrainingplan(Trainingplan trainingplan) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            db.delete("Trainingplan", "id = ?", new String[]{String.valueOf(trainingplan.getId())});
        } catch (Exception e) {
            Log.e("TrainingplanRepository", "Fehler beim Löschen des Trainingsplans", e);
        }
    }

    /**
     * Setzt einen neuen aktiven Trainingsplan und deaktiviert alle anderen.
     *
     * @param newActiveTrainingPlanId Die ID des zu aktivierenden Trainingsplans
     */
    public void setNewActiveTrainingPlan(int newActiveTrainingPlanId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("UPDATE Trainingplan SET isActive = 0");
            ContentValues values = new ContentValues();
            values.put("isActive", 1);
            db.update("Trainingplan", values, "id = ?", new String[]{String.valueOf(newActiveTrainingPlanId)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("TrainingplanRepository", "Fehler beim Setzen des aktiven Trainingsplans", e);
        } finally {
            db.endTransaction();
        }
    }
}