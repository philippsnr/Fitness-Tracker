package com.example.fitnesstracker.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.Nutritionday;

public class NutritiondayRepository {

    private final DatabaseHelper dbHelper;

    public NutritiondayRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public Nutritionday getNutritionday(String date) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Nutritionday WHERE date = ?", new String[]{String.valueOf(date)});

        if (cursor.moveToFirst()) {
            Nutritionday nutritionday = new Nutritionday(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("date"))
            );
            cursor.close();
            db.close();
            return nutritionday;
        }
        cursor.close();
        db.close();
        return null;
    }

    public void setNutritionday(Nutritionday nutritionday) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", nutritionday.getDate());

        db.insert("Nutritionday", null, values);
        db.close();
    }
}