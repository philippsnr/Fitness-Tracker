package com.example.fitnesstracker.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.Trainingday;

import java.util.ArrayList;
import java.util.List;

public class TrainingdayRepository {
    private final DatabaseHelper dbHelper;

    /**
     * Initializes a new repository instance with the application context.
     *
     * @param context The application context for database access
     */
    public TrainingdayRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * Retrieves all training days associated with a specific training plan.
     *
     * @param trainingplanId The ID of the training plan to query
     * @return List of Trainingday objects belonging to the specified plan
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
     * Creates a new training day entry in the database.
     *
     * @param trainingday The Trainingday object containing the data to insert
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
     * Updates an existing training day entry in the database.
     *
     * @param trainingday The Trainingday object with updated values.
     *                    Uses the object's ID to identify the database entry.
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
     * Deletes a specific training day from the database.
     *
     * @param trainingday The Trainingday object to delete.
     *                    Uses the object's ID to identify the database entry.
     */
    public void deleteTrainingday(Trainingday trainingday) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Trainingday", "id = ?", new String[]{String.valueOf(trainingday.getId())});
        db.close();
    }
}