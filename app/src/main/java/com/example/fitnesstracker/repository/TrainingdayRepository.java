package com.example.fitnesstracker.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.Trainingday;

import java.util.ArrayList;
import java.util.List;

public class TrainingdayRepository {
    private final DatabaseHelper dbHelper;

    public TrainingdayRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public List<Trainingday> getTrainingdaysForPlan(int trainingplanId) {
        List<Trainingday> trainingdays = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM Trainingday WHERE Trainingplan_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(trainingplanId)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int planId = cursor.getInt(2);

                trainingdays.add(new Trainingday(id, name, planId));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return trainingdays;
    }
    public void createTrainingday(Trainingday trainingday) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", trainingday.getName());
        values.put("Trainingplan_id", trainingday.getTrainingplanId());

        db.insert("Trainingday", null, values);
        db.close();
    }

    public void updateTrainingday(Trainingday trainingday) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", trainingday.getName());
        values.put("Trainingplan_id", trainingday.getTrainingplanId());

        db.update("Trainingday", values, "id = ?", new String[]{String.valueOf(trainingday.getId())});
        db.close();
    }

    public void deleteTrainingday(Trainingday trainingday) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Trainingday", "id = ?", new String[]{String.valueOf(trainingday.getId())});
        db.close();
    }
}
