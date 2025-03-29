package com.example.fitnesstracker.repository;

import com.example.fitnesstracker.model.ExerciseSet;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.fitnesstracker.database.DatabaseHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExerciseSetRepository {
    protected DatabaseHelper dbHelper;

    /**
     * Konstruktor für das ExerciseSetRepository.
     *
     * @param context Der Anwendungs-Kontext.
     */
    public ExerciseSetRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * Holt die letzten Sätze für eine gegebene Übungs-ID.
     *
     * @param exerciseId Die ID der Übung.
     * @return Eine Liste von ExerciseSet-Objekten.
     */
    public List<ExerciseSet> getLastSets(int exerciseId) {
        List<ExerciseSet> lastSets = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ExerciseSet WHERE exercise_id = ? ORDER BY date DESC",
                new String[]{String.valueOf(exerciseId)});
        if (cursor.moveToFirst()) {
            do {
                int setNumber = cursor.getInt(cursor.getColumnIndexOrThrow("set_number"));
                int repetition = cursor.getInt(cursor.getColumnIndexOrThrow("repetitions"));
                double weight = cursor.getInt(cursor.getColumnIndexOrThrow("weight"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                lastSets.add(new ExerciseSet(exerciseId, setNumber, repetition, weight, date));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        Log.d("DB_QUERY", "Total sets loaded: " + lastSets.size());
        return lastSets;
    }

    /**
     * Speichert einen neuen Satz für eine Übung.
     *
     * @param newSet Das zu speichernde ExerciseSet-Objekt.
     */
    public void saveNewSet(ExerciseSet newSet) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        // Hier wird nun direkt die exercise_id verwendet
        values.put("exercise_id", newSet.getExerciseId());
        values.put("set_number", newSet.getSetNumber());
        values.put("repetitions", newSet.getRepetition());
        values.put("weight", newSet.getWeight());
        values.put("date", newSet.getDate().toString());
        db.insert("ExerciseSet", null, values);
        db.close();
    }

    /**
     * Ermittelt die Anzahl der Sätze pro Kalenderwoche.
     *
     * @return Eine Map, die für jede Woche die Anzahl der Sätze enthält.
     */
    public Map<Integer, Integer> getSetsPerWeek() {
        Map<Integer, Integer> setsPerWeek = new HashMap<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT (strftime('%W', date)) AS week, COUNT(*) AS count "
                + "FROM ExerciseSet GROUP BY week ORDER BY week";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int week = cursor.getInt(cursor.getColumnIndexOrThrow("week"));
                int count = cursor.getInt(cursor.getColumnIndexOrThrow("count"));
                setsPerWeek.put(week, count);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return setsPerWeek;
    }

    /**
     * Gibt die Nummer des letzten Satzes zurück
     *
     * @param date Datum, an dem die lastSetNumber ermittelt werden soll
     * @param exerciseId Übung bei der die lastSetNumber ermittelt werden soll
     * @return setNumber. Die Nummer des letzten Satzes, um den nächsten Satz dann korrekt zu erstellen
     */
    public int getLastSetNumber(String date, int exerciseId) {
        int setNumber = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT set_number FROM ExerciseSet WHERE date = ? AND exercise_id = ? ORDER BY set_number DESC LIMIT 1", new String[]{date, String.valueOf(exerciseId)});

        if (cursor.moveToFirst()) {
            setNumber = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return setNumber;
    }
}
