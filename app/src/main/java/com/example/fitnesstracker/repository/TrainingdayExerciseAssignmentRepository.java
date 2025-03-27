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

    public TrainingdayExerciseAssignment getTrainingdayExcerciseAssignments(int trainingdayId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TrainingdayExerciseAssignment WHERE trainingday_id = ?", new String[]{String.valueOf(trainingdayId)});

        if (cursor.moveToFirst()) {
            TrainingdayExerciseAssignment trainingdayExerciseAssignment = new TrainingdayExerciseAssignment(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("Trainingday_id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("Exercise_id"))
            );
            cursor.close();
            db.close();
            return trainingdayExerciseAssignment;
        }
        cursor.close();
        db.close();
        return null;
    }

    public List<Integer> getExerciseIdsForTrainingday(int trainingdayId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Integer> ids = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "SELECT Exercise_id FROM TrainingdayExerciseAssignment WHERE Trainingday_id = ?",
                new String[]{String.valueOf(trainingdayId)}
        );

        while (cursor.moveToNext()) {
            ids.add(cursor.getInt(cursor.getColumnIndexOrThrow("Exercise_id"))); // Exercise-ID statt Assignment-ID
        }
        cursor.close();
        db.close();
        return ids;
    }

    public void deleteTrainingdayExerciseAssignment(int assignmentId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("TrainingdayExerciseAssignment", "id = ?", new String[]{String.valueOf(assignmentId)});
        db.close();
    }

    public long addTrainingExerciseAssignment(int trainingdayId, int exerciseId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            // Prüfung ob Exercise existiert
            if (!exerciseExists(db, exerciseId)) {
                throw new RuntimeException("Exercise existiert nicht");
            }

            ContentValues values = new ContentValues();
            values.put("Trainingday_id", trainingdayId);
            values.put("Exercise_id", exerciseId);

            long newId = db.insertOrThrow("TrainingdayExerciseAssignment", null, values);
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

    private boolean exerciseExists(SQLiteDatabase db, int exerciseId) {
        Cursor c = db.rawQuery("SELECT 1 FROM Exercise WHERE id = ?",
                new String[]{String.valueOf(exerciseId)});
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }
}
