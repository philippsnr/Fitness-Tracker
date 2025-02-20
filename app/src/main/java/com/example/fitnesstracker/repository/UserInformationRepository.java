package com.example.fitnesstracker.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.UserInformation;

public class UserInformationRepository {
    private final DatabaseHelper dbHelper;

    public UserInformationRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    // **Letzte gespeicherte User-Information abrufen**
    public UserInformation getLatestUserInformation() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM UserInformation ORDER BY date DESC LIMIT 1", null);

        if (cursor.moveToFirst()) {
            UserInformation userInfo = new UserInformation(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getInt(5)
            );
            cursor.close();
            db.close();
            return userInfo;
        }

        cursor.close();
        db.close();
        return null; // Falls keine Daten vorhanden sind
    }

    public UserInformation getUserInformationDate(String date) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM UserInformation WHERE date = ? ORDER BY date DESC LIMIT 1",
                new String[]{date}
        );

        if (cursor.moveToFirst()) {
            UserInformation userInfo = new UserInformation(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getInt(5)
            );
            cursor.close();
            db.close();
            return userInfo;
        }

        cursor.close();
        db.close();
        return null; // Falls keine Daten vorhanden sind
    }

    // **Neue User-Information speichern oder aktualisieren**
    public void writeUserInformation(UserInformation userInfo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userInfo.getUserId());
        values.put("date", userInfo.getDate());
        values.put("height", userInfo.getHeight());
        values.put("weight", userInfo.getWeight());
        values.put("KFA", userInfo.getKfa());

        db.insert("UserInformation", null, values);
        db.close();
    }
}
