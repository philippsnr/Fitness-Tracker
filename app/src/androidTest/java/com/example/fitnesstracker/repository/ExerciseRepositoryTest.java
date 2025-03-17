package com.example.fitnesstracker.repository;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.*; // Modelle je nach Test anpassen
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ExerciseRepositoryTest {
    private ExerciseRepository repository;
    private DatabaseHelper dbHelper;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        dbHelper = new DatabaseHelper(context);
        dbHelper.getWritableDatabase().execSQL("DELETE FROM Exercise");
        dbHelper.getWritableDatabase().execSQL(
                "INSERT INTO Exercise (id, name, difficulty, info, picture_path) VALUES (1, 'Push Up', 2, 'Info1', 'Path1')"
        );
        repository = new ExerciseRepository(context);
    }

    @Test
    public void testGetExercisesForMuscleGroup() {
        List<Exercise> exercises = repository.getExercisesForMuscleGroup(3);
        assertTrue(exercises.isEmpty());
        exercises = repository.getExercisesForMuscleGroup(1);
        assertEquals(1, exercises.size());
    }

    @Test
    public void testGetExercisesByIds() {
        List<Integer> ids = Arrays.asList(1, 2);
        List<Exercise> result = repository.getExercisesByIds(ids);
        assertEquals(2, result.size());
    }
}
