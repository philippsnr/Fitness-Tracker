package com.example.fitnesstracker.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.NutritiondayNutritionAssignment;

public class NutritiondayNutritionAssignmentRepository {

    private final DatabaseHelper dbHelper;

    public NutritiondayNutritionAssignmentRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * Holt eine NutritiondayNutritionAssignment aus der Datenbank anhand der nutritiondayId.
     *
     * @param nutritiondayId Die ID des Nutritiondays.
     * @return Das zugehörige NutritiondayNutritionAssignment oder null, falls nicht gefunden.
     */
    public NutritiondayNutritionAssignment getNutritionday(int nutritiondayId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = queryNutritionday(db, nutritiondayId);

        NutritiondayNutritionAssignment assignment = null;
        if (cursor.moveToFirst()) {
            assignment = createNutritionAssignment(cursor);
        }

        cursor.close();
        db.close();
        return assignment;
    }

    /**
     * Führt die SQL-Abfrage für einen Nutritionday aus.
     */
    private Cursor queryNutritionday(SQLiteDatabase db, int nutritiondayId) {
        return db.rawQuery(
                "SELECT * FROM NutritiondayNutritionAssignmant WHERE nutritionday_id = ?",
                new String[]{String.valueOf(nutritiondayId)}
        );
    }

    /**
     * Erstellt ein NutritiondayNutritionAssignment-Objekt aus dem Cursor.
     */
    private NutritiondayNutritionAssignment createNutritionAssignment(Cursor cursor) {
        NutritiondayNutritionAssignment assignment = new NutritiondayNutritionAssignment(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getInt(cursor.getColumnIndexOrThrow("nutritionday_id")),
                cursor.getString(cursor.getColumnIndexOrThrow("time")),
                cursor.getString(cursor.getColumnIndexOrThrow("nutrtion_name_english")),
                cursor.getInt(cursor.getColumnIndexOrThrow("nutrition_mass")),
                cursor.getInt(cursor.getColumnIndexOrThrow("nutrition_cals")),
                cursor.getInt(cursor.getColumnIndexOrThrow("nutrition_carbs")),
                cursor.getInt(cursor.getColumnIndexOrThrow("nutrition_fats")),
                cursor.getInt(cursor.getColumnIndexOrThrow("nutrition_proteins"))
        );

        assignment.setNutritionNameGerman(cursor.getString(cursor.getColumnIndexOrThrow("nutrtion_name_german")));
        assignment.setNutritionPicturePath(cursor.getString(cursor.getColumnIndexOrThrow("nutrition_picture_path")));
        return assignment;
    }


    public void setNutritiondayNutritionAssignment(NutritiondayNutritionAssignment nutritiondayNutritionAssignment) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nutritionday_id", nutritiondayNutritionAssignment.getNutritiondayId());
        values.put("time", nutritiondayNutritionAssignment.getTime());
        values.put("nutrition_name_english", nutritiondayNutritionAssignment.getNutritionNameEnglish());
        values.put("nutrition_mass", nutritiondayNutritionAssignment.getNutritionMass());
        values.put("nutrition_cals", nutritiondayNutritionAssignment.getNutritionCals());
        values.put("nutrition_carbs", nutritiondayNutritionAssignment.getNutritionCarbs());
        values.put("nutrition_fats", nutritiondayNutritionAssignment.getNutritionFats());
        values.put("nutrition_proteins", nutritiondayNutritionAssignment.getNutritionProteins());
        if (nutritiondayNutritionAssignment.getNutritionNameGerman() != null) {
            values.put("nutrition_name_german", nutritiondayNutritionAssignment.getNutritionNameGerman());
        }
        if (nutritiondayNutritionAssignment.getNutritionPicturePath() != null) {
            values.put("nutrition_picture_path", nutritiondayNutritionAssignment.getNutritionPicturePath());
        }
        db.insert("NutritiondayNutritionAssignment", null, values);
        db.close();
    }
}
