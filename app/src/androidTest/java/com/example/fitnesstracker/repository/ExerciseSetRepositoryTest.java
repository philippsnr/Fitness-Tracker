package com.example.fitnesstracker.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.*; // Modelle je nach Test anpassen
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ExerciseSetRepositoryTest {
    private ExerciseSetRepository repository;
    private DatabaseHelper dbHelper;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
    }

    @After
    public void tearDown() {
        dbHelper.close();
    }

    @Test
    public void testSaveAndGetLastSets() {
        Context context = ApplicationProvider.getApplicationContext();
        ExerciseSet set = new ExerciseSet(0, 1, 1, 12, 50, "2023-01-01");
        repository = new ExerciseSetRepository(context);

        repository.saveNewSet(set);
        List<ExerciseSet> sets = repository.getLastSets(1);

        assertFalse(sets.isEmpty());
        assertEquals(12, sets.get(0).getRepetition());
    }
}