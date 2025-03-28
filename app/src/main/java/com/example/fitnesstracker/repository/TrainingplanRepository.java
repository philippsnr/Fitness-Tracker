package com.example.fitnesstracker.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.Trainingplan;

import java.util.ArrayList;
import java.util.List;

public class TrainingplanRepository {
    private final DatabaseHelper dbHelper;

    /**
     * Initializes a new repository instance for managing training plans.
     *
     * @param context The application context for database access
     */
    public TrainingplanRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * Retrieves all training plans from the database.
     *
     * @return List of all stored Trainingplan objects
     */
    public List<Trainingplan> getAllTrainingplans() {
        List<Trainingplan> trainingplans = new ArrayList<>();
        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM Trainingplan", null)) {

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    boolean isActive = cursor.getInt(cursor.getColumnIndexOrThrow("isActive")) == 1;
                    trainingplans.add(new Trainingplan(id, name, isActive));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("TrainingplanRepository", "Error getting all training plan", e);
        }
        return trainingplans;
    }

    /**
     * Retrieves the currently active training plan.
     *
     * @return Active Trainingplan object or null if none exists
     */
    public Trainingplan getActiveTrainingplan() {
        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM Trainingplan WHERE isActive = 1", null)) {

            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                boolean isActive = cursor.getInt(cursor.getColumnIndexOrThrow("isActive")) == 1;
                return new Trainingplan(id, name, isActive);
            }
        } catch (Exception e) {
            Log.e("TrainingplanRepository", "Error get active training plan", e);
        }
        return null;
    }

    /**
     * Adds a new training plan to the database.
     *
     * @param trainingplan The Trainingplan object to add (ID will be auto-generated)
     */
    public void addTrainingplan(Trainingplan trainingplan) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put("name", trainingplan.getName());
            values.put("isActive", trainingplan.getIsActiveAsInt());
            long id = db.insert("Trainingplan", null, values);
            trainingplan.setId(id);
        } catch (Exception e) {
            Log.e("TrainingplanRepository", "Error adding training plan", e);
        }
    }

    /**
     * Updates the name of an existing training plan.
     *
     * @param trainingplan The Trainingplan object with updated name and original ID
     */
    public void updateTrainingplanName(Trainingplan trainingplan) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put("name", trainingplan.getName());
            db.update("Trainingplan", values, "id = ?", new String[]{String.valueOf(trainingplan.getId())});
        } catch (Exception e) {
            Log.e("TrainingplanRepository", "Error updating training plan", e);
        }
    }

    /**
     * Deletes a specific training plan from the database.
     *
     * @param trainingplan The Trainingplan object to delete
     */
    public void deleteTrainingplan(Trainingplan trainingplan) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            db.delete("Trainingplan", "id = ?", new String[]{String.valueOf(trainingplan.getId())});
        } catch (Exception e) {
            Log.e("TrainingplanRepository", "Error deleting training plan", e);
        }
    }

    /**
     * Sets a new active training plan and deactivates all others.
     *
     * @param newActiveTrainingPlanId The ID of the training plan to activate
     */
    public void setNewActiveTrainingPlan(int newActiveTrainingPlanId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("UPDATE Trainingplan SET isActive = 0");
            ContentValues values = new ContentValues();
            values.put("isActive", 1);
            db.update("Trainingplan", values, "id = ?", new String[]{String.valueOf(newActiveTrainingPlanId)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("TrainingplanRepository", "Error setting active training plan", e);
        } finally {
            db.endTransaction();
        }
    }
}