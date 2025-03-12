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

    public NutritiondayNutritionAssignment getNutritionday(int nutritiondayId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM NutritiondayNutritionAssignmant WHERE nutritionday_id = ?", new String[]{String.valueOf(nutritiondayId)});

        if (cursor.moveToFirst()) {
            NutritiondayNutritionAssignment nutritiondayNutritionAssignment = new NutritiondayNutritionAssignment(
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
            nutritiondayNutritionAssignment.setNutritionNameGerman(cursor.getString(cursor.getColumnIndexOrThrow("nutrtion_name_german")));
            nutritiondayNutritionAssignment.setNutritionPicturePath(cursor.getString(cursor.getColumnIndexOrThrow("nutrition_picture_path")));
            cursor.close();
            db.close();
            return nutritiondayNutritionAssignment;
        }

        cursor.close();
        db.close();
        return null;
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
