package com.example.fitnesstracker.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.User;

/**
 * Repository-Klasse zur Verwaltung von Benutzerdaten in der SQLite-Datenbank.
 */
public class UserRepository {
    private final DatabaseHelper dbHelper;

    /**
     * Konstruktor für das UserRepository.
     *
     * @param context Der Anwendungskontext.
     */
    public UserRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * Ruft das Fitnessziel des gespeicherten Benutzers aus der Datenbank ab.
     *
     * @return Das Fitnessziel als String oder null, falls kein Benutzer vorhanden ist.
     */
    public String getUserGoal() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String goal = null;

        Cursor cursor = db.query(
                "User",
                new String[] {"goal"}, null, null, null, null, null, "1");

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("goal");
            if (columnIndex >= 0) {
                goal = cursor.getString(columnIndex);
            }
            cursor.close();
        }

        db.close();
        return goal;
    }

    /**
     * Speichert einen neuen Benutzer in der Datenbank.
     *
     * @param user Das User-Objekt, das gespeichert werden soll.
     */
    public void saveUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", user.getId());
        values.put("name", user.getName());
        values.put("birth_date", user.getBirthDate());
        values.put("goal", user.getGoal());
        values.put("trainingDaysPerWeek", user.getTrainingDaysPerWeek());

        db.insert("User", null, values);
        db.close();
    }

    /**
     * Aktualisiert das Fitnessziel des Benutzers in der Datenbank.
     *
     * @param goal Das neue Fitnessziel.
     */
    public void updateUserGoal(String goal) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("goal", goal);

        db.update("User", values, null, null);
        db.close();
    }
}
