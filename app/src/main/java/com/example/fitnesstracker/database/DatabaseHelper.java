package com.example.fitnesstracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Helper class for managing the SQLite database in the fitness tracker application.
 * This class extends {@link SQLiteOpenHelper} and ensures that the database is properly copied from assets if needed.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;
    private final Context context;
    private static String DB_PATH;

    /**
     * Constructs a DatabaseHelper instance.
     * The constructor initializes the database path and ensures that the database is copied from assets if it does not exist.
     *
     * @param context the application context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        DB_PATH = context.getDatabasePath(DATABASE_NAME).getPath();
        copyDatabaseIfNeeded();
    }

    /**
     * Called when the database is first created.
     *
     * @param db the database instance
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Database creation logic
    }

    /**
     * Called when the database needs to be upgraded.
     *
     * @param db         the database instance
     * @param oldVersion the old database version
     * @param newVersion the new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Database upgrade logic
    }

    /**
     * Copies the database from the assets folder to the app's internal storage if it does not already exist.
     */
    private void copyDatabaseIfNeeded() {
        File dbFile = new File(DB_PATH);
        if (!dbFile.exists()) {
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
