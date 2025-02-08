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

    // Holt alle Übungen aus der DB
    public List<Exercise> getAllExercises() {
        List<Exercise> exercises = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Exercise", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String difficulty = cursor.getString(2);
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
