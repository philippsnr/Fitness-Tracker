package com.example.fitnesstracker.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.Trainingplan;

import java.util.ArrayList;
import java.util.List;

public class TrainingplanRepository {
    private final DatabaseHelper dbHelper;

    public TrainingplanRepository(Context context) { this.dbHelper = new DatabaseHelper(context); }
    public List<Trainingplan> getTrainingplans()
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
}
