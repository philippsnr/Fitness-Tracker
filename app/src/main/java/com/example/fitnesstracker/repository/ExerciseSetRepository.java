package com.example.fitnesstracker.repository;

import com.example.fitnesstracker.model.ExerciseSet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.fitnesstracker.database.DatabaseHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                int setNumber = cursor.getInt(cursor.getColumnIndexOrThrow("set_number"));
                int repetition = cursor.getInt(cursor.getColumnIndexOrThrow("repetitions"));
                int weight = cursor.getInt(cursor.getColumnIndexOrThrow("weight"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                lastSets.add(new ExerciseSet(id, trainingdayExerciseAssignmentId, setNumber, repetition, weight, date));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        Log.d("DB_QUERY", "Total sets loaded: " + lastSets.size());
        return lastSets;
    }
    public void saveNewSet(ExerciseSet newSet)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TrainingdayExerciseAssignment_id", newSet.getTrainingdayExerciseAssignmentId());
        values.put("set_number", newSet.getSetNumber());
        values.put("repetitions", newSet.getRepetition());
        values.put("weight", newSet.getWeight());
        values.put("date", newSet.getDate().toString());
        db.insert("ExerciseSet", null, values);
        db.close();
    }

    public Map<Integer, Integer> getSetsPerWeek() {
        Map<Integer, Integer> setsPerWeek = new HashMap<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT CAST(strftime('%W', substr(date,7,4) || '-' || substr(date,4,2) || '-' || substr(date,1,2)) AS INTEGER) AS week, COUNT(*) AS count " +
                "FROM ExerciseSet GROUP BY week ORDER BY week";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int week = cursor.getInt(cursor.getColumnIndexOrThrow("week"));
                int count = cursor.getInt(cursor.getColumnIndexOrThrow("count"));
                setsPerWeek.put(week, count);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return setsPerWeek;
    }
}
