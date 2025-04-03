package com.example.fitnesstracker.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.Nutritionday;

/**
 * Repository class for managing Nutritionday persistence operations.
 * Handles all database interactions for nutrition day records including
 * creation and retrieval of daily nutrition entries.
 */
public class NutritiondayRepository {

    private final DatabaseHelper dbHelper;

    /**
     * Constructs a new repository instance.
     *
     * @param context the application context used for database access
     */
    public NutritiondayRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * Retrieves a nutrition day entry by its date.
     *
     * @param date the date string in format "YYYY-MM-DD" to search for
     * @return the found Nutritionday object, or null if no entry exists for the given date
     * @throws IllegalArgumentException if required columns are missing in the database
     */
    public Nutritionday getNutritionday(String date) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM Nutritionday WHERE date = ?",
                new String[]{date}
        );

        Nutritionday nutritionday = null;
        if (cursor.moveToFirst()) {
            nutritionday = new Nutritionday(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("date"))
            );
        }

        cursor.close();
        db.close();
        return nutritionday;
    }

    /**
     * Stores a new nutrition day entry in the database.
     * The ID will be automatically generated by the database.
     *
     * @param nutritionday the Nutritionday object to persist
     */
    public void setNutritionday(Nutritionday nutritionday) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", nutritionday.getDate());

        db.insert("Nutritionday", null, values);
        db.close();
    }
}