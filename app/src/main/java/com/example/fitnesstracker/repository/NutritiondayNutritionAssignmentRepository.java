package com.example.fitnesstracker.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.NutritiondayNutritionAssignment;

/**
 * Repository class for managing NutritiondayNutritionAssignment persistence operations.
 * Handles all database interactions for nutrition day assignments including
 * creation, retrieval, and storage of nutrition records.
 */
public class NutritiondayNutritionAssignmentRepository {

    private final DatabaseHelper dbHelper;

    /**
     * Constructs a new repository instance.
     *
     * @param context the application context used for database access
     */
    public NutritiondayNutritionAssignmentRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * Retrieves a nutrition day assignment by its nutrition day ID.
     *
     * @param nutritiondayId the ID of the nutrition day to search for
     * @return the found NutritiondayNutritionAssignment object, or null if not found
     */
    public NutritiondayNutritionAssignment getNutritionday(int nutritiondayId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM NutritiondayNutritionAssignment WHERE nutritionday_id = ?",
                new String[]{String.valueOf(nutritiondayId)}
        );

        NutritiondayNutritionAssignment assignment = null;
        if (cursor.moveToFirst()) {
            assignment = createNutritionAssignment(cursor);
        }

        cursor.close();
        db.close();
        return assignment;
    }

    /**
     * Executes a query to find nutrition day assignments by nutrition day ID.
     *
     * @param db the readable database instance
     * @param nutritiondayId the ID of the nutrition day to query
     * @return a Cursor containing the query results
     */
    private Cursor queryNutritionday(SQLiteDatabase db, int nutritiondayId) {
        return db.rawQuery(
                "SELECT * FROM NutritiondayNutritionAssignment WHERE nutritionday_id = ?",
                new String[]{String.valueOf(nutritiondayId)}
        );
    }

    /**
     * Creates a NutritiondayNutritionAssignment object from database cursor.
     *
     * @param cursor the database cursor positioned at the desired record
     * @return a populated NutritiondayNutritionAssignment object
     * @throws IllegalArgumentException if required columns are missing
     */
    private NutritiondayNutritionAssignment createNutritionAssignment(Cursor cursor) {
        NutritiondayNutritionAssignment assignment = new NutritiondayNutritionAssignment(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getInt(cursor.getColumnIndexOrThrow("nutritionday_id")),
                cursor.getString(cursor.getColumnIndexOrThrow("time")),
                cursor.getString(cursor.getColumnIndexOrThrow("nutrition_name_english")),
                cursor.getInt(cursor.getColumnIndexOrThrow("nutrition_mass")),
                cursor.getInt(cursor.getColumnIndexOrThrow("nutrition_cals")),
                cursor.getInt(cursor.getColumnIndexOrThrow("nutrition_carbs")),
                cursor.getInt(cursor.getColumnIndexOrThrow("nutrition_fats")),
                cursor.getInt(cursor.getColumnIndexOrThrow("nutrition_proteins"))
        );

        assignment.setNutritionNameGerman(
                cursor.getString(cursor.getColumnIndexOrThrow("nutrition_name_german")));
        assignment.setNutritionPicturePath(
                cursor.getString(cursor.getColumnIndexOrThrow("nutrition_picture_path")));
        return assignment;
    }

    /**
     * Stores a new nutrition day assignment in the database.
     *
     * @param nutritiondayNutritionAssignment the assignment object to persist
     */
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