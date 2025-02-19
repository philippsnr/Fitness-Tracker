package com.example.fitnesstracker.repository;

import com.example.fitnesstracker.model.ExerciseSet;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.fitnesstracker.database.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class ExerciseSetRepository
{
    private final DatabaseHelper dbHelper;

    public ExerciseSetRepository(Context context) { this.dbHelper = new DatabaseHelper(context); }

    public List<ExerciseSet> getLastSets(int trainingdayExerciseAssignmentId)
    {
        List<ExerciseSet> lastSets = new ArrayList<ExerciseSet>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ExerciseSet WHERE trainingdayExerciseAssignment_id = ? ORDER BY date DESC LIMIT 3", new String[]{String.valueOf(trainingdayExerciseAssignmentId)});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int setNumber = cursor.getInt(cursor.getColumnIndexOrThrow("setNumber"));
                int repetition = cursor.getInt(cursor.getColumnIndexOrThrow("repetition"));
                int weight = cursor.getInt(cursor.getColumnIndexOrThrow("weight"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                lastSets.add(new ExerciseSet(id, trainingdayExerciseAssignmentId, setNumber, repetition, weight, date));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lastSets;
    }
    public void saveNewSet(ExerciseSet newSet)
    {

    }
}
