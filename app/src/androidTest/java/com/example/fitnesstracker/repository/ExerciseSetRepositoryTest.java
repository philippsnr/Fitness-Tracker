package com.example.fitnesstracker.repository;

import static junit.framework.TestCase.assertTrue;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class ExerciseSetRepositoryTest {

    private ExerciseSetRepository repository;
    private DatabaseHelper dbHelper;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        dbHelper = new DatabaseHelper(context);

        // Datenbank vor jedem Test leeren
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("ExerciseSet", null, null);
        db.close();

        repository = new ExerciseSetRepository(context);
    }

    @After
    public void tearDown() {
        dbHelper.close();
    }


    @Test
    public void testSaveNewSet() {
        // Testdaten erstellen
        ExerciseSet testSet = new ExerciseSet(
                1,
                1,
                10,
                50.0,
                "2023-10-01"
        );

        // Methode testen
        repository.saveNewSet(testSet);

        // Daten direkt aus der Datenbank lesen
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM ExerciseSet WHERE exercise_id = 1",
                null
        );

        // Assertions
        assertThat(cursor.getCount(), is(1));
        cursor.moveToFirst();

        assertThat(cursor.getInt(cursor.getColumnIndexOrThrow("exercise_id")), is(1));
        assertThat(cursor.getInt(cursor.getColumnIndexOrThrow("set_number")), is(1));
        assertThat(cursor.getInt(cursor.getColumnIndexOrThrow("repetitions")), is(10));
        assertThat(cursor.getDouble(cursor.getColumnIndexOrThrow("weight")), is(50.0));
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("date")), is("2023-10-01"));

        cursor.close();
        db.close();
    }

    @Test
    public void testGetLastSets() {
        // 6 Test-Sätze erstellen
        for (int i = 1; i <= 6; i++) {
            ExerciseSet set = new ExerciseSet(
                    1,
                    i,
                    10 + i,
                    50.0 + i,
                    String.format("2023-10-%02d", i)
            );
            repository.saveNewSet(set);
        }

        // Methode testen
        List<ExerciseSet> result = repository.getLastSets(1);

        // Assertions
        assertThat(result.size(), is(5));

        String[] expectedDates = {
                "2023-10-06",
                "2023-10-05",
                "2023-10-04",
                "2023-10-03",
                "2023-10-02"
        };

        for (int i = 0; i < 5; i++) {
            assertThat(result.get(i).getDate().toString(), is(expectedDates[i]));
        }
    }

    @Test
    public void testGetSetsPerWeek() {
        // Daten für 2 Wochen erstellen
        repository.saveNewSet(new ExerciseSet(1, 1, 10, 50.0, "2023-10-02")); // Woche 40
        repository.saveNewSet(new ExerciseSet(1, 2, 10, 50.0, "2023-10-03")); // Woche 40
        repository.saveNewSet(new ExerciseSet(1, 1, 10, 50.0, "2023-10-09")); // Woche 41
        repository.saveNewSet(new ExerciseSet(1, 2, 10, 50.0, "2023-10-10")); // Woche 41
        repository.saveNewSet(new ExerciseSet(1, 3, 10, 50.0, "2023-10-11")); // Woche 41

        // Methode testen
        Map<Integer, Integer> result = repository.getSetsPerWeek();

        // Assertions
        assertThat(result.size(), is(2));
        assertThat(result.get(40), is(2));
        assertThat(result.get(41), is(3));
    }

    @Test
    public void testGetLastSetNumber() {
        // Testdaten erstellen
        repository.saveNewSet(new ExerciseSet(1, 1, 10, 50.0, "2023-10-01"));
        repository.saveNewSet(new ExerciseSet(1, 2, 10, 50.0, "2023-10-01"));
        repository.saveNewSet(new ExerciseSet(1, 3, 10, 50.0, "2023-10-01"));

        // Methode testen
        int result1 = repository.getLastSetNumber("2023-10-01", 1);
        int result2 = repository.getLastSetNumber("2023-10-02", 1);
        int result3 = repository.getLastSetNumber("2023-10-01", 2);

        // Assertions
        assertThat(result1, is(3));
        assertThat(result2, is(0));
        assertThat(result3, is(0));
    }
}