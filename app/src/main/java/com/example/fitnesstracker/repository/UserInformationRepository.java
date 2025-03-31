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
     * Ruft die zuletzt gespeicherten Benutzerinformationen ab.
     * Falls in der neuesten Zeile Höhe oder KFA 0 sind, werden die zuletzt gültigen Werte aus vorherigen Einträgen ermittelt.
     *
     * @return Das neueste {@link UserInformation} oder {@code null}, wenn keine Daten vorhanden sind.
     */
    public UserInformation getLatestUserInformation() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM UserInformation ORDER BY date DESC, id DESC LIMIT 1", null);
        UserInformation userInfo = cursor.moveToFirst() ? createUserInformationFromCursor(cursor, db) : null;
        cursor.close();
        db.close();
        return userInfo;
    }

    /**
     * Erzeugt ein {@link UserInformation}-Objekt aus der aktuellen Cursor-Zeile.
     * Falls in der Zeile optionale Werte (Höhe oder KFA) mit 0 eingetragen sind,
     * werden die jeweils zuletzt gültigen Werte aus der Datenbank ermittelt.
     *
     * @param cursor Der Cursor, der auf die aktuelle Zeile zeigt.
     * @param db     Die geöffnete Datenbankinstanz, die für weitere Abfragen benötigt wird.
     * @return Das erstellte {@link UserInformation}-Objekt.
     */
    private UserInformation createUserInformationFromCursor(Cursor cursor, SQLiteDatabase db) {
        int id = cursor.getInt(0);
        int userId = cursor.getInt(1);
        String dateStr = cursor.getString(2);
        int height = cursor.getInt(3);
        double weight = cursor.getDouble(4);
        int kfa = cursor.getInt(5);
        if (height == 0) {
            height = getLatestNonZeroHeight(db);
        }
        if (kfa == 0) {
            kfa = getLatestNonZeroKfa(db);
        }
        return new UserInformation(id, userId, dateStr, height, weight, kfa);
    }

    /**
     * Ermittelt die zuletzt eingegebene, gültige (nicht 0) Höhe.
     *
     * @param db Die geöffnete Datenbankinstanz.
     * @return Die zuletzt gültige Höhe oder 0, falls kein gültiger Eintrag gefunden wurde.
     */
    private int getLatestNonZeroHeight(SQLiteDatabase db) {
        int height = 0;
        Cursor c = db.rawQuery("SELECT height FROM UserInformation WHERE height != 0 ORDER BY date DESC, id DESC LIMIT 1", null);
        if (c.moveToFirst()) {
            height = c.getInt(0);
        }
        c.close();
        return height;
    }

    /**
     * Ermittelt den zuletzt eingegebenen, gültigen (nicht 0) KFA.
     *
     * @param db Die geöffnete Datenbankinstanz.
     * @return Den zuletzt gültigen KFA oder 0, falls kein gültiger Eintrag gefunden wurde.
     */
    private int getLatestNonZeroKfa(SQLiteDatabase db) {
        int kfa = 0;
        Cursor c = db.rawQuery("SELECT KFA FROM UserInformation WHERE KFA != 0 ORDER BY date DESC, id DESC LIMIT 1", null);
        if (c.moveToFirst()) {
            kfa = c.getInt(0);
        }
        c.close();
        return kfa;
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
