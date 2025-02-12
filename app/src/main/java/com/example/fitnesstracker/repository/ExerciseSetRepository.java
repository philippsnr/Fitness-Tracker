package com.example.fitnesstracker.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.ExerciseSet;

import java.util.ArrayList;
import java.util.List;

public class ExerciseSetRepository {
    private final DatabaseHelper dbHelper;

    public ExerciseSetRepository(Context context) { this.dbHelper = dbHelper; }

    public List<ExerciseSet> getLastSets() {
        List<ExerciseSet> lastSets= new ArrayList<ExerciseSet>;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int trainingdayExerciseAssignment_id = getTrainingdayExerciseAssigment_id();
        Cursor cursor = db.rawQuery("SELECT * FROM ExerciseSet WHERE trainingdayExerciseAssigment_id = ? ORDER BY date LIMIT 3", trainingdayExerciseAssignment_id);
        return lastSets;
    }
}
