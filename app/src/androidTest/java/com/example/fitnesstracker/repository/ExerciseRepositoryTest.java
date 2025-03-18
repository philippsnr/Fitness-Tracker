package com.example.fitnesstracker.repository;

import static org.junit.Assert.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.Exercise;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ExerciseRepositoryTest {

    private ExerciseRepository repository;
    private DatabaseHelper dbHelper;
    private static final int TEST_MUSCLE_GROUP_ID = 3;
    private static final int NON_EXISTENT_MUSCLE_GROUP_ID = 999;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        dbHelper = new DatabaseHelper(context);

        // Clean and prepare database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM Exercise");
        db.execSQL("DELETE FROM ExerciseMuscleGroupAssignment");

        // Insert test data
        db.execSQL("INSERT INTO Exercise(id, name, difficulty, info, picture_path) VALUES " +
                "(1, 'Bench Press', 3, 'Chest exercise', 'bench.jpg')," +
                "(2, 'Squat', 2, 'Leg exercise', 'squat.jpg')," +
                "(3, 'Deadlift', 4, 'Back exercise', 'deadlift.jpg')");

        db.execSQL("INSERT INTO ExerciseMuscleGroupAssignment(Exercise_id, MuscleGroup_id) VALUES " +
                "(1, 3), (2, 4), (3, 3)");

        repository = new ExerciseRepository(context);
    }

    @After
    public void tearDown() {
        dbHelper.close();
    }

    @Test
    public void testGetExercisesForMuscleGroup() {
        List<Exercise> exercises = repository.getExercisesForMuscleGroup(TEST_MUSCLE_GROUP_ID);

        assertEquals(2, exercises.size());
        assertExerciseExists(exercises, 1); // Bench Press
        assertExerciseExists(exercises, 3); // Deadlift
    }

    @Test
    public void testGetExercisesForNonExistentMuscleGroup() {
        List<Exercise> exercises = repository.getExercisesForMuscleGroup(NON_EXISTENT_MUSCLE_GROUP_ID);
        assertTrue(exercises.isEmpty());
    }

    @Test
    public void testGetExercisesByIds() {
        List<Integer> ids = Arrays.asList(1, 2, 3);
        List<Exercise> exercises = repository.getExercisesByIds(ids);

        assertEquals(3, exercises.size());
        assertExerciseExists(exercises, 1);
        assertExerciseExists(exercises, 2);
        assertExerciseExists(exercises, 3);
    }

    @Test
    public void testGetExercisesByPartialIds() {
        List<Integer> ids = Arrays.asList(1, 99); // One valid, one invalid
        List<Exercise> exercises = repository.getExercisesByIds(ids);

        assertEquals(1, exercises.size());
        assertExerciseExists(exercises, 1);
    }

    @Test
    public void testGetExercisesByEmptyIds() {
        List<Exercise> exercises = repository.getExercisesByIds(Collections.emptyList());
        assertTrue(exercises.isEmpty());
    }

    private void assertExerciseExists(List<Exercise> exercises, int expectedId) {
        assertTrue(exercises.stream().anyMatch(e -> e.getId() == expectedId));
    }

    @Test
    public void testExerciseDetails() {
        Exercise exercise = repository.getExercisesByIds(Collections.singletonList(1)).get(0);

        assertEquals("Bench Press", exercise.getName());
        assertEquals(3, exercise.getDifficulty());
        assertEquals("Chest exercise", exercise.getInfo());
        assertEquals("bench.jpg", exercise.getPicturePath());
    }
}