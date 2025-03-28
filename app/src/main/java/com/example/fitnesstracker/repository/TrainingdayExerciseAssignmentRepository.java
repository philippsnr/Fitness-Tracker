package com.example.fitnesstracker.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.TrainingdayExerciseAssignment;

import java.util.ArrayList;
import java.util.List;

public class TrainingdayExerciseAssignmentRepository {

    private final DatabaseHelper dbHelper;

    public TrainingdayExerciseAssignmentRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * Gibt die erste TrainingdayExerciseAssignment für die angegebene Trainingday-ID zurück.
     *
     * @param trainingdayId Die ID des Trainingdays nach dem gesucht werden soll.
     * @return Die passende TrainingdayExerciseAssignment oder null, falls keine gefunden wurde.
     */
    public TrainingdayExerciseAssignment getTrainingdayExcerciseAssignments(int trainingdayId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM TrainingdayExerciseAssignment WHERE trainingday_id = ?",
                new String[]{String.valueOf(trainingdayId)}
        );

        TrainingdayExerciseAssignment result = null;
        if (cursor.moveToFirst()) {
            result = mapCursorToAssignment(cursor);
        }

        cursor.close();
        db.close();
        return result;
    }

    /**
     * Liefert alle Exercise-IDs, die mit einem bestimmten Trainingday verknüpft sind.
     *
     * @param trainingdayId Die ID des Trainingdays, für das die Übungen abgefragt werden sollen.
     * @return Liste der Exercise-IDs, die mit dem Trainingday verknüpft sind.
     */
    public List<Integer> getExerciseIdsForTrainingday(int trainingdayId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Integer> ids = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "SELECT Exercise_id FROM TrainingdayExerciseAssignment WHERE Trainingday_id = ?",
                new String[]{String.valueOf(trainingdayId)}
        );

        while (cursor.moveToNext()) {
            ids.add(cursor.getInt(cursor.getColumnIndexOrThrow("Exercise_id")));
        }

        cursor.close();
        db.close();
        return ids;
    }

    /**
     * Löscht eine bestimmte TrainingdayExerciseAssignment anhand ihrer ID.
     *
     * @param assignmentId Die ID der Zuordnung, die gelöscht werden soll.
     */
    public void deleteTrainingdayExerciseAssignment(int assignmentId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("TrainingdayExerciseAssignment", "id = ?", new String[]{String.valueOf(assignmentId)});
        db.close();
    }

    /**
     * Erstellt eine neue Zuordnung zwischen einem Trainingday und einer Übung.
     *
     * @param trainingdayId Die ID des Trainingdays.
     * @param exerciseId    Die ID der Übung.
     * @return Die Zeilen-ID der neu eingefügten Zuordnung oder -1 bei einem Fehler.
     */
    public long addTrainingExerciseAssignment(int trainingdayId, int exerciseId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();

        try {
            validateExerciseExists(db, exerciseId);
            validateTrainingdayExists(db, trainingdayId); // New validation
            long newId = insertAssignment(db, trainingdayId, exerciseId);
            db.setTransactionSuccessful();
            return newId;
        } catch (SQLiteConstraintException e) {
            Log.e("DB", "Constraint Error: " + e.getMessage());
            return -1;
        } catch (RuntimeException e) {
            Log.e("DB", "Validation Error: " + e.getMessage());
            return -1;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * Wandelt einen Cursor in ein TrainingdayExerciseAssignment-Objekt um.
     *
     * @param cursor Der Cursor mit den Daten.
     * @return Das erstellte TrainingdayExerciseAssignment-Objekt.
     */
    private TrainingdayExerciseAssignment mapCursorToAssignment(Cursor cursor) {
        return new TrainingdayExerciseAssignment(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getInt(cursor.getColumnIndexOrThrow("Trainingday_id")),
                cursor.getInt(cursor.getColumnIndexOrThrow("Exercise_id"))
        );
    }

    /**
     * Überprüft, ob eine Übung mit der angegebenen ID existiert.
     *
     * @param db Die Datenbankverbindung.
     * @param exerciseId Die zu überprüfende Übungs-ID.
     * @throws RuntimeException Wenn die Übung nicht existiert.
     */
    private void validateExerciseExists(SQLiteDatabase db, int exerciseId) {
        if (!exerciseExists(db, exerciseId)) {
            throw new RuntimeException("Exercise does not exist");
        }
    }

    /**
     * Fügt eine neue Zuordnung in die Datenbank ein.
     *
     * @param db Die Datenbankverbindung.
     * @param trainingdayId Die ID des Trainingdays.
     * @param exerciseId Die ID der Übung.
     * @return Die ID der neu eingefügten Zeile.
     */
    private long insertAssignment(SQLiteDatabase db, int trainingdayId, int exerciseId) {
        ContentValues values = new ContentValues();
        values.put("Trainingday_id", trainingdayId);
        values.put("Exercise_id", exerciseId);
        return db.insertOrThrow("TrainingdayExerciseAssignment", null, values);
    }

    /**
     * Prüft, ob eine Übung mit der angegebenen ID in der Datenbank existiert.
     *
     * @param db Die Datenbankverbindung.
     * @param exerciseId Die zu prüfende Übungs-ID.
     * @return true, wenn die Übung existiert, sonst false.
     */
    private boolean exerciseExists(SQLiteDatabase db, int exerciseId) {
        Cursor c = db.rawQuery("SELECT 1 FROM Exercise WHERE id = ?",
                new String[]{String.valueOf(exerciseId)});
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }


    /**
     * Überprüft, ob ein Trainingstag mit der angegebenen ID existiert.
     *
     * @param db Die Datenbankverbindung
     * @param trainingdayId Die ID des zu überprüfenden Trainingstags
     * @throws RuntimeException Wenn der Trainingstag nicht existiert
     */
    private void validateTrainingdayExists(SQLiteDatabase db, int trainingdayId) {
        if (!trainingdayExists(db, trainingdayId)) {
            throw new RuntimeException("Trainingday does not exist");
        }
    }

    /**
     * Prüft die Existenz eines Trainingstags in der Datenbank.
     *
     * @param db Die Datenbankverbindung
     * @param trainingdayId Die ID des zu prüfenden Trainingstags
     * @return true wenn der Trainingstag existiert, false wenn nicht
     */
    private boolean trainingdayExists(SQLiteDatabase db, int trainingdayId) {
        Cursor c = db.rawQuery("SELECT 1 FROM Trainingday WHERE id = ?",
                new String[]{String.valueOf(trainingdayId)});
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }

    /**
     * Gibt die DatabaseHelper-Instanz zurück, die für den Datenbankzugriff verwendet wird.
     *
     * @return Die DatabaseHelper-Instanz
     */
    protected DatabaseHelper getDbHelper() {
        return dbHelper;
    }
}