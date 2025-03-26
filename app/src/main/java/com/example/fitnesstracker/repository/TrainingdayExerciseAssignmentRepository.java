package com.example.fitnesstracker.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
        List<Integer> assignmentIds = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM TrainingdayExerciseAssignment WHERE Trainingday_id = ?",
                new String[]{String.valueOf(trainingdayId)});

        if (cursor.moveToFirst()) {
            do {
                assignmentIds.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return assignmentIds;
    }


    public void deleteTrainingdayExerciseAssignment(int assignmentId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("TrainingdayExerciseAssignment", "id = ?", new String[]{String.valueOf(assignmentId)});
        db.close();
    }

}
