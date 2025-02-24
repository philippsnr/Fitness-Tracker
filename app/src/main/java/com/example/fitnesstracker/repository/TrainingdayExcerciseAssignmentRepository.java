package com.example.fitnesstracker.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.TrainingdayExcerciseAssignment;

public class TrainingdayExcerciseAssignmentRepository {

    private final DatabaseHelper dbHelper;

    public TrainingdayExcerciseAssignmentRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public TrainingdayExcerciseAssignment getTrainingdayExcerciseAssignments(int trainingdayId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TrainingdayExerciseAssignment WHERE trainingday_id = ?", new String[]{String.valueOf(trainingdayId)});

        if (cursor.moveToFirst()) {
            TrainingdayExcerciseAssignment trainingdayExcerciseAssignment = new TrainingdayExcerciseAssignment(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("trainingday_id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("excercise_id"))
            );
            cursor.close();
            db.close();
            return trainingdayExcerciseAssignment;
        }
        cursor.close();
        db.close();
        return null;
    }
}
