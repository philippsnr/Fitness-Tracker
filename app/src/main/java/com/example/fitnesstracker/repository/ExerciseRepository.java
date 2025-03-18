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
     * Führt die SQL-Abfrage aus, um Übungen für eine Muskelgruppe zu laden.
     */
    private Cursor queryExercisesForMuscleGroup(SQLiteDatabase db, int muscleGroupId) {
        String query = "SELECT e.* FROM Exercise e " +
                "JOIN ExerciseMuscleGroupAssignment emg ON e.id = emg.Exercise_id " +
                "WHERE emg.MuscleGroup_id = ?";
        return db.rawQuery(query, new String[]{String.valueOf(muscleGroupId)});
    }

    /**
     * Erstellt eine Liste von Exercise-Objekten aus dem Cursor.
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

    public List<Exercise> getExercisesByIds(List<Integer> ids) {
        List<Exercise> exercises = new ArrayList<>();
        if (ids.isEmpty()) return exercises;  // Falls keine IDs gefunden wurden

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        StringBuilder query = new StringBuilder("SELECT * FROM Exercise WHERE id IN (");

        // Platzhalter für die IDs
        String[] idArray = new String[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            query.append("?");
            if (i < ids.size() - 1) query.append(", ");
            idArray[i] = String.valueOf(ids.get(i));
        }
        query.append(")");

        Cursor cursor = db.rawQuery(query.toString(), idArray);
        if (cursor.moveToFirst()) {
            do {
                exercises.add(new Exercise(
                        cursor.getInt(0),  // id
                        cursor.getString(1),  // name
                        cursor.getInt(2),  // difficulty
                        cursor.getString(3),  // info
                        cursor.getString(4)  // picture_path
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return exercises;
    }

}
