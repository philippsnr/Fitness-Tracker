package com.example.fitnesstracker.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.User;

public class UserRepository {
    private final DatabaseHelper dbHelper;

    public UserRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public String getUserGoal() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String goal = null;

        Cursor cursor = db.query(
                "User",
                new String[] {"goal"}, null, null, null, null, null, "1");

        if (cursor != null && cursor.moveToFirst()) {
            // Prüfen, ob der Spaltenindex gültig ist
            int columnIndex = cursor.getColumnIndex("goal");
            if (columnIndex >= 0) {
                goal = cursor.getString(columnIndex);
            }
            cursor.close();
        }

        db.close();
        return goal;
    }

    public void updateUserGoal(String goal) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("goal", goal);

        int rowsAffected = db.update("User", values, null, null);

        db.close();
    }

}

