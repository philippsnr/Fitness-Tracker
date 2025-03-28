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
     * Retrieves the first TrainingdayExerciseAssignment found for the specified trainingday ID.
     *
     * @param trainingdayId The ID of the trainingday to search for.
     * @return The matching TrainingdayExerciseAssignment or null if none found.
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
     * Fetches all exercise IDs associated with a specific trainingday.
     *
     * @param trainingdayId The ID of the trainingday to query.
     * @return List of exercise IDs linked to the trainingday.
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
     * Deletes a specific TrainingdayExerciseAssignment by its ID.
     *
     * @param assignmentId The ID of the assignment to delete.
     */
    public void deleteTrainingdayExerciseAssignment(int assignmentId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("TrainingdayExerciseAssignment", "id = ?", new String[]{String.valueOf(assignmentId)});
        db.close();
    }

    /**
     * Creates a new assignment between a trainingday and an exercise.
     *
     * @param trainingdayId The ID of the trainingday.
     * @param exerciseId    The ID of the exercise.
     * @return The row ID of the newly inserted assignment, or -1 on error.
     */
    public long addTrainingExerciseAssignment(int trainingdayId, int exerciseId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();

        try {
            validateExerciseExists(db, exerciseId);
            long newId = insertAssignment(db, trainingdayId, exerciseId);
            db.setTransactionSuccessful();
            return newId;
        } catch (SQLiteConstraintException e) {
            Log.e("DB", "Constraint Error: " + e.getMessage());
            return -1;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private TrainingdayExerciseAssignment mapCursorToAssignment(Cursor cursor) {
        return new TrainingdayExerciseAssignment(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getInt(cursor.getColumnIndexOrThrow("Trainingday_id")),
                cursor.getInt(cursor.getColumnIndexOrThrow("Exercise_id"))
        );
    }

    private void validateExerciseExists(SQLiteDatabase db, int exerciseId) {
        if (!exerciseExists(db, exerciseId)) {
            throw new RuntimeException("Exercise does not exist");
        }
    }

    private long insertAssignment(SQLiteDatabase db, int trainingdayId, int exerciseId) {
        ContentValues values = new ContentValues();
        values.put("Trainingday_id", trainingdayId);
        values.put("Exercise_id", exerciseId);
        return db.insertOrThrow("TrainingdayExerciseAssignment", null, values);
    }

    private boolean exerciseExists(SQLiteDatabase db, int exerciseId) {
        Cursor c = db.rawQuery("SELECT 1 FROM Exercise WHERE id = ?",
                new String[]{String.valueOf(exerciseId)});
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }
}