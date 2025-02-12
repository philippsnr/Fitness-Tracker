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

    // **User holen (es gibt nur einen User)**
    public User getUser() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM User LIMIT 1", null);

        if (cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(4)
            );
            cursor.close();
            db.close();
            return user;
        }

        cursor.close();
        db.close();
        return null; // Falls kein User existiert
    }

    // **User speichern (einfügen oder updaten)**
    public void setUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("birth_date", user.getBirthDate());
        values.put("goal", user.getGoal());
        values.put("trainingdaysPerWeek", user.getTrainingDaysPerWeek());

        if (getUser() == null) {
            db.insert("User", null, values);
        } else {
            db.update("User", values, "id = ?", new String[]{"1"});
        }

        db.close();
    }
}

