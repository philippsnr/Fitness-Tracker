package com.example.fitnesstracker.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.ExerciseMuscleGroupAssignment;
import java.util.ArrayList;
import java.util.List;

public class ExerciseMuscleGroupRepository {
    private final DatabaseHelper dbHelper;

    public ExerciseMuscleGroupRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public List<Integer> getMuscleGroupsForExercise(int exerciseId) {
        List<Integer> muscleGroups = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT MuscleGroup_id FROM ExerciseMuscleGroupAssignment WHERE Exercise_id = ?",
                new String[]{String.valueOf(exerciseId)}
        );

        if (cursor.moveToFirst()) {
            do {
                muscleGroups.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return muscleGroups;
    }
}
