package com.example.fitnesstracker.repository;

import static org.junit.Assert.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.TrainingdayExerciseAssignment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TrainingdayExerciseAssignmentRepositoryTest {

    private TrainingdayExerciseAssignmentRepository repository;
    private DatabaseHelper dbHelper;
    private static final int TEST_TRAININGDAY_ID = 1;
    private static final int TEST_EXERCISE_ID = 10;
    private static final int NON_EXISTENT_ID = 999;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        dbHelper = new DatabaseHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM TrainingdayExerciseAssignment");
        db.execSQL("DELETE FROM Trainingday");
        db.execSQL("DELETE FROM Exercise");
        db.execSQL("DELETE FROM Trainingplan");

        db.execSQL("INSERT INTO Trainingplan(id, name, isActive) VALUES (5, 'Test Plan', 1)");
        db.execSQL("INSERT INTO Trainingday(id, name, Trainingplan_id) VALUES (" + TEST_TRAININGDAY_ID + ", 'Test Day', 5)");
        db.execSQL("INSERT INTO Exercise(id, name, difficulty, info, picture_path) VALUES (10, 'Exercise 1', 2, 'Info', 'path')");
        db.execSQL("INSERT INTO TrainingdayExerciseAssignment(Trainingday_id, Exercise_id) VALUES (" + TEST_TRAININGDAY_ID + ", " + TEST_EXERCISE_ID + ")");

        db.close();
        repository = new TrainingdayExerciseAssignmentRepository(context);
    }

    @After
    public void tearDown() {
        dbHelper.close();
    }

    @Test
    public void testGetAssignmentsForTrainingday() {
        List<TrainingdayExerciseAssignment> assignments = repository.getAssignmentsForTrainingday(TEST_TRAININGDAY_ID);
        assertEquals(1, assignments.size());
        assertEquals(TEST_TRAININGDAY_ID, assignments.get(0).getTrainingdayId());
        assertEquals(TEST_EXERCISE_ID, assignments.get(0).getExerciseId());
    }

    @Test
    public void testGetExerciseIdsForTrainingday() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("INSERT INTO TrainingdayExerciseAssignment(Trainingday_id, Exercise_id) VALUES (1, 11), (1, 12)");
        db.close();

        List<Integer> ids = repository.getExerciseIdsForTrainingday(TEST_TRAININGDAY_ID);
        assertEquals(3, ids.size());
        assertTrue(ids.containsAll(Arrays.asList(TEST_EXERCISE_ID, 11, 12)));
    }

    @Test
    public void testDeleteAssignment() {
        List<TrainingdayExerciseAssignment> assignments = repository.getAssignmentsForTrainingday(TEST_TRAININGDAY_ID);
        assertEquals(1, assignments.size());

        repository.deleteTrainingdayExerciseAssignment(assignments.get(0).getId());

        List<TrainingdayExerciseAssignment> afterDeletion = repository.getAssignmentsForTrainingday(TEST_TRAININGDAY_ID);
        assertTrue(afterDeletion.isEmpty());
    }

    @Test
    public void testAddValidAssignment() {
        long result = repository.addTrainingExerciseAssignment(TEST_TRAININGDAY_ID, TEST_EXERCISE_ID);
        assertTrue(result > 0);
    }

    @Test
    public void testAddInvalidTrainingdayAssignment() {
        long result = repository.addTrainingExerciseAssignment(NON_EXISTENT_ID, TEST_EXERCISE_ID);
        assertEquals(-1, result);
    }

    @Test
    public void testAddInvalidExerciseAssignment() {
        long result = repository.addTrainingExerciseAssignment(TEST_TRAININGDAY_ID, NON_EXISTENT_ID);
        assertEquals(-1, result);
    }
}