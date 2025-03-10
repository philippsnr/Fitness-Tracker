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

    public TrainingplanRepository(Context context) { this.dbHelper = new DatabaseHelper(context); }
    public List<Trainingplan> getAllTrainingplans()
    {

        List<Trainingplan> trainingplans = new ArrayList<Trainingplan>();
        try (SQLiteDatabase db = dbHelper.getReadableDatabase(); Cursor cursor = db.rawQuery("SELECT * FROM Trainingplan", null)) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    int isActiveInt = cursor.getInt(cursor.getColumnIndexOrThrow("isActive"));
                    boolean isActive = (isActiveInt == 1);
                    trainingplans.add(new Trainingplan(id, name, isActive));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("TrainingplanRepository", "Error getting all training plan", e);
        }
        return trainingplans;
    }

    public Trainingplan getActiveTrainingplan()
    {
        try (SQLiteDatabase db = dbHelper.getReadableDatabase(); Cursor cursor = db.rawQuery("SELECT * FROM Trainingplan WHERE isActive = 1", null)) {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                int isActiveInt = cursor.getInt(cursor.getColumnIndexOrThrow("isActive"));
                boolean isActive = (isActiveInt == 1);
                return new Trainingplan(id, name, isActive);
            }
        } catch (Exception e) {
            Log.e("TrainingplanRepository", "Error get active training plan", e);
        }
        return null;
    }

    public void addTrainingplan(Trainingplan trainingplan)
    {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase())
        {
            ContentValues values = new ContentValues();
            values.put("name", trainingplan.getName());
            values.put("isActive", trainingplan.getIsActiveAsInt());
            long id = db.insert("Trainingplan", null, values);
            trainingplan.setId(id);
        } catch (Exception e) {
            Log.e("TrainingplanRepository", "Error adding training plan", e);
        }
    }
    public void updateTrainingplanName(Trainingplan trainingplan)
    {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase())
        {
            ContentValues values = new ContentValues();
            values.put("name", trainingplan.getName());
            String whereClause = "id = ?";
            String[] whereArgs = {String.valueOf(trainingplan.getId())};
            db.update("Trainingplan", values, whereClause, whereArgs);
        } catch (Exception e) {
            Log.e("TrainingplanRepository", "Error updating training plan", e);
        }
    }
    public void deleteTrainingplan(Trainingplan trainingplan)
    {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase())
        {
            String whereClause = "id = ?";
            String[] whereArgs = {String.valueOf(trainingplan.getId())};
            db.delete("Trainingplan", whereClause, whereArgs);
        } catch (Exception e) {
            Log.e("TrainingplanRepository", "Error deleting training plan", e);
        }
    }
    public void setNewActiveTrainingPlan(int newActiveTrainingPlanId)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try
        {
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
