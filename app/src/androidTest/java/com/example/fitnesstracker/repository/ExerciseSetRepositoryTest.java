package com.example.fitnesstracker.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.ExerciseSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ExerciseSetRepositoryTest {
    private ExerciseSetRepository repository;
    private TestDatabaseHelper dbHelper;
    private SQLiteDatabase database;

    static class TestDatabaseHelper extends DatabaseHelper {
        TestDatabaseHelper(Context context) {
            super(context);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS ExerciseSet (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "exercise_id INTEGER NOT NULL," +
                    "set_number INTEGER NOT NULL," +
                    "repetitions INTEGER NOT NULL," +
                    "weight INTEGER NOT NULL," +
                    "date TEXT NOT NULL)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS ExerciseSet");
            onCreate(db);
        }
    }

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        dbHelper = new TestDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        repository = new ExerciseSetRepository(context);


        try {
            Field dbHelperField = ExerciseSetRepository.class.getDeclaredField("dbHelper");
            dbHelperField.setAccessible(true);
            dbHelperField.set(repository, dbHelper);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject test database helper", e);
        }
    }


    @After
    public void tearDown() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    @Test
    public void testGetLastSets() {

        database.execSQL("DELETE FROM ExerciseSet");

        insertTestData(1, 1, 10, 50, "2023-01-01");
        insertTestData(1, 2, 8, 50, "2023-01-02");
        insertTestData(2, 1, 12, 40, "2023-01-03");

        List<ExerciseSet> sets = repository.getLastSets(1);
        assertEquals(2, sets.size());
        assertEquals(2, sets.get(0).getSetNumber());
        assertEquals("2023-01-02", sets.get(0).getDate().toString());
    }

    @Test
    public void testSaveNewSet() {

        SQLiteDatabase resetDb = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(resetDb, 1, 2);
        resetDb.close();

        ExerciseSet newSet = new ExerciseSet(1, 1, 10, 50, "2023-01-01");
        repository.saveNewSet(newSet);


        SQLiteDatabase verifyDb = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = verifyDb.rawQuery("SELECT COUNT(*) FROM ExerciseSet", null);
            assertTrue(cursor.moveToFirst());
            assertEquals(1, cursor.getInt(0));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            verifyDb.close();
        }
    }

    @Test
    public void testGetSetsPerWeek() {
        database.execSQL("DELETE FROM ExerciseSet");

        insertTestData(1, 1, 10, 50, "2023-01-02"); // Woche 1
        insertTestData(1, 2, 8, 50, "2023-01-09");  // Woche 2
        insertTestData(1, 3, 8, 50, "2023-01-10");   // Woche 2

        Map<Integer, Integer> result = repository.getSetsPerWeek();
        assertEquals(2, result.size());
        assertEquals(2, (int) result.get(2)); // Zwei Sätze in Woche 2
    }

    @Test
    public void testGetLastSetNumber() {
        database.execSQL("DELETE FROM ExerciseSet");

        insertTestData(1, 1, 10, 50, "2023-01-01");
        insertTestData(1, 3, 8, 50, "2023-01-01");
        insertTestData(1, 2, 12, 40, "2023-01-02");

        int lastSetNumber = repository.getLastSetNumber("2023-01-01", 1);
        assertEquals(3, lastSetNumber);
    }

    private void insertTestData(int exerciseId, int setNumber, int reps, int weight, String date) {
        ContentValues values = new ContentValues();
        values.put("exercise_id", exerciseId);
        values.put("set_number", setNumber);
        values.put("repetitions", reps);
        values.put("weight", weight);
        values.put("date", date);
        database.insert("ExerciseSet", null, values);
    }
}