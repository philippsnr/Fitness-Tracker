package com.example.fitnesstracker.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.UserInformation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserInformationRepository {
    private final DatabaseHelper dbHelper;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public UserInformationRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    // Alle gespeicherten User-Informationen abrufen
    public List<UserInformation> getAllUserInformation() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM UserInformation ORDER BY date ASC", null);

        List<UserInformation> userInfoList = getUserInfoList(cursor);

        cursor.close();
        db.close();
        return userInfoList;
    }

    private List<UserInformation> getUserInfoList(Cursor cursor) {
        List<UserInformation> userInfoList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                UserInformation userInfo = new UserInformation(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getDouble(4),
                        cursor.getInt(5)
                );
                userInfoList.add(userInfo);
            } while (cursor.moveToNext());
        }
        return userInfoList;
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
                    cursor.getDouble(4),
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

        String dateString = dateFormat.format(userInfo.getDate());
        values.put("date", dateString);

        values.put("height", userInfo.getHeight());
        values.put("weight", userInfo.getWeight());
        values.put("KFA", userInfo.getKfa());

        db.insert("UserInformation", null, values);
        db.close();
    }
}
