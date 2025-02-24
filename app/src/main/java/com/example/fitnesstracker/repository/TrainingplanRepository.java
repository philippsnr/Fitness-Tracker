package com.example.fitnesstracker.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.Trainingplan;

import java.util.ArrayList;
import java.util.List;

public class TrainingplanRepository {
    private final DatabaseHelper dbHelper;

    public TrainingplanRepository(Context context) { this.dbHelper = new DatabaseHelper(context); }
    public List<Trainingplan> getAllTrainingplans()
    {
        List<Trainingplan> trainingplans = new ArrayList<Trainingplan>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Trainingplan", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                int isActive = cursor.getInt(cursor.getColumnIndexOrThrow("isActive"));
                trainingplans.add(new Trainingplan(id, name, isActive));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return trainingplans;
    }

    public void addTrainingplan(Trainingplan trainingplan)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", trainingplan.getName());
        values.put("isActive", trainingplan.getIsActiveAsInt());
        long id = db.insert("Trainingplan", null, values);
        trainingplan.setId(id);
        db.close();
    }
    public void updateTrainingplan(Trainingplan trainingplan) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", trainingplan.getName());
        values.put("isActive", trainingplan.getIsActiveAsInt());  // Umwandlung von boolean zu int (1 oder 0)
        String whereClause = "id = ?";
        String[] whereArgs = { String.valueOf(trainingplan.getId()) };
        db.update("Trainingplan", values, whereClause, whereArgs);
        db.close();
    }
    public void deleteTrainingplan(Trainingplan trainingplan) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = "id = ?";
        String[] whereArgs = { String.valueOf(trainingplan.getId()) };
        db.delete("Trainingplan", whereClause, whereArgs);
        db.close();
    }
}
