package com.example.fitnesstracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;
    private final Context context;
    private static String DB_PATH;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        DB_PATH = context.getDatabasePath(DATABASE_NAME).getPath();
        copyDatabaseIfNeeded();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // on create
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on update/upgrade
    }

    private void copyDatabaseIfNeeded() {
        File dbFile = new File(DB_PATH);
        if (true) {
            try {
                InputStream input = context.getAssets().open("SQLite_DB/" + DATABASE_NAME);
                OutputStream output = new FileOutputStream(DB_PATH);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
                output.flush();
                output.close();
                input.close();
                Log.d("DatabaseHelper", "Datenbank erfolgreich aus assets/SQLite_DB kopiert.");
            } catch (Exception e) {
                Log.e("DatabaseHelper", "Fehler beim Kopieren der Datenbank: " + e.getMessage());
            }
        }
    }
}
