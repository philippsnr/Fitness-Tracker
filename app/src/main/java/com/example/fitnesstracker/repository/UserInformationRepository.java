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

/**
 * Repository-Klasse zur Verwaltung von Benutzerinformationen in einer SQLite-Datenbank.
 */
public class UserInformationRepository {
    private final DatabaseHelper dbHelper;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    /**
     * Konstruktor, der eine Instanz der {@link UserInformationRepository} erstellt.
     *
     * @param context Der Kontext der Anwendung, der für den Zugriff auf die Datenbank erforderlich ist.
     */
    public UserInformationRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * Ruft alle gespeicherten Benutzerinformationen aus der Datenbank ab.
     *
     * @return Eine Liste von {@link UserInformation}, die alle Benutzerinformationen enthält.
     */
    public List<UserInformation> getAllUserInformation() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM UserInformation ORDER BY date ASC", null);

        List<UserInformation> userInfoList = getUserInfoList(cursor);

        cursor.close();
        db.close();
        return userInfoList;
    }

    /**
     * Konvertiert die Ergebnisse des Cursors in eine Liste von {@link UserInformation}.
     *
     * @param cursor Der Cursor, der die Datenbankergebnisse enthält.
     * @return Eine Liste von {@link UserInformation}.
     */
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

    /**
     * Ruft die zuletzt gespeicherten Benutzerinformationen aus der Datenbank ab.
     *
     * @return Das neueste {@link UserInformation} oder {@code null}, wenn keine Daten vorhanden sind.
     */
    public UserInformation getLatestUserInformation() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM UserInformation ORDER BY date DESC, id DESC LIMIT 1", null);
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

    /**
     * Speichert oder aktualisiert die Benutzerinformationen in der Datenbank.
     *
     * @param userInfo Die Benutzerinformationen, die gespeichert oder aktualisiert werden sollen.
     */
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
