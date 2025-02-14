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

    public List<Exercise> getExercisesForMuscleGroup(int muscleGroupId) {
        List<Exercise> exercises = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT e.* FROM Exercise e " +
                "JOIN ExerciseMuscleGroupAssignment emg ON e.id = emg.Exercise_id " +
                "WHERE emg.MuscleGroup_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(muscleGroupId)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int difficulty = cursor.getInt(2);
                String info = cursor.getString(3);
                String picturePath = cursor.getString(4);

                exercises.add(new Exercise(id, name, difficulty, info, picturePath));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return exercises;
    }
}
