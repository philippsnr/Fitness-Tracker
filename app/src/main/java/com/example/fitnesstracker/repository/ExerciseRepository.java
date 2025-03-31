package com.example.fitnesstracker.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExerciseRepository {
    private final DatabaseHelper dbHelper;

    /**
     * Konstruktor für das ExerciseRepository.
     *
     * @param context Der Anwendungs-Kontext.
     */
    public ExerciseRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * Holt eine Liste von Übungen für eine bestimmte Muskelgruppe aus der Datenbank.
     *
     * @param muscleGroupId Die ID der Muskelgruppe.
     * @return Eine Liste von Exercise-Objekten, die zu der Muskelgruppe gehören.
     */
    public List<Exercise> getExercisesForMuscleGroup(int muscleGroupId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = queryExercisesForMuscleGroup(db, muscleGroupId);
        List<Exercise> exercises = extractExercises(cursor);
        cursor.close();
        db.close();
        return exercises;
    }

    /**
     * Führt die SQL-Abfrage aus, um Übungen für eine bestimmte Muskelgruppe zu laden.
     *
     * @param db            Die Datenbank.
     * @param muscleGroupId Die ID der Muskelgruppe.
     * @return Den Cursor mit den Ergebnissen der Abfrage.
     */
    private Cursor queryExercisesForMuscleGroup(SQLiteDatabase db, int muscleGroupId) {
        String query = "SELECT e.* FROM Exercise e " +
                "JOIN ExerciseMuscleGroupAssignment emg ON e.id = emg.Exercise_id " +
                "WHERE emg.MuscleGroup_id = ?";
        return db.rawQuery(query, new String[]{String.valueOf(muscleGroupId)});
    }

    /**
     * Erstellt eine Liste von Exercise-Objekten aus dem Cursor.
     *
     * @param cursor Der Cursor mit den Abfrageergebnissen.
     * @return Eine Liste von Exercise-Objekten.
     */
    private List<Exercise> extractExercises(Cursor cursor) {
        List<Exercise> exercises = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                exercises.add(new Exercise(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4)
                ));
            } while (cursor.moveToNext());
        }
        return exercises;
    }

    /**
     * Holt eine Liste von Übungen anhand einer Liste von IDs.
     *
     * @param ids Die Liste der Übungs-IDs.
     * @return Eine Liste von Exercise-Objekten.
     */
    public List<Exercise> getExercisesByIds(List<Integer> ids) {
        if (ids.isEmpty()) return new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = executeExerciseQuery(db, ids);
        List<Exercise> exercises = extractExercises(cursor);
        cursor.close();
        db.close();
        return exercises;
    }

    /**
     * Führt eine SQL-Abfrage aus, um Übungen anhand einer Liste von IDs abzurufen.
     *
     * @param db  Die Datenbank-Instanz.
     * @param ids Die Liste der IDs.
     * @return Ein Cursor mit den gefundenen Übungen.
     */
    private Cursor executeExerciseQuery(SQLiteDatabase db, List<Integer> ids) {
        String query = buildInClauseQuery(ids.size());
        String[] idArray = buildIdArray(ids);
        return db.rawQuery(query, idArray);
    }


    /**
     * Baut den SQL-Query-String mit Platzhaltern für die IN-Klausel.
     *
     * @param count Die Anzahl der IDs.
     * @return Den vollständigen Query-String.
     */
    private String buildInClauseQuery(int count) {
        StringBuilder query = new StringBuilder("SELECT * FROM Exercise WHERE id IN (");
        for (int i = 0; i < count; i++) {
            query.append("?");
            if (i < count - 1) {
                query.append(", ");
            }
        }
        query.append(")");
        return query.toString();
    }

    /**
     * Erstellt ein Array von String-IDs aus einer Liste von Integer-IDs.
     *
     * @param ids Die Liste der IDs.
     * @return Ein String-Array, das die IDs enthält.
     */
    private String[] buildIdArray(List<Integer> ids) {
        String[] idArray = new String[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            idArray[i] = String.valueOf(ids.get(i));
        }
        return idArray;
    }
}
