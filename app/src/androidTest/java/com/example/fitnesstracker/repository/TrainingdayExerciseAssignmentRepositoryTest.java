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

        // Clean and prepare database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM TrainingdayExerciseAssignment");
        db.execSQL("DELETE FROM Trainingday");
        db.execSQL("DELETE FROM Exercise");

        // Insert test data
        db.execSQL("INSERT INTO Trainingday(id, name, Trainingplan_id) VALUES (" + TEST_TRAININGDAY_ID + ", 'Trainingplan1', 5)");
        db.execSQL("INSERT INTO Exercise(id, name, difficulty, info, picture_path) VALUES (10, 'Übung1', 2, 'info1', 'path1')");
        db.execSQL("INSERT INTO TrainingdayExerciseAssignment(Trainingday_id, Exercise_id) " +
                "VALUES (" + TEST_TRAININGDAY_ID + ", " + TEST_EXERCISE_ID + ")");

        repository = new TrainingdayExerciseAssignmentRepository(context);
    }

    @After
    public void tearDown() {
        dbHelper.close();
    }

    @Test
    public void testGetTrainingdayExerciseAssignments() {
        TrainingdayExerciseAssignment assignment = repository.getTrainingdayExcerciseAssignments(TEST_TRAININGDAY_ID);

        assertNotNull(assignment);
        assertEquals(TEST_TRAININGDAY_ID, assignment.getTrainingdayId());
        assertEquals(TEST_EXERCISE_ID, assignment.getExerciseId());
    }

    @Test
    public void testGetNonExistentAssignment() {
        TrainingdayExerciseAssignment assignment = repository.getTrainingdayExcerciseAssignments(NON_EXISTENT_ID);
        assertNull(assignment);
    }

    @Test
    public void testGetExerciseIdsForTrainingday() {
        // Add more test data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("INSERT INTO TrainingdayExerciseAssignment(Trainingday_id, Exercise_id) " +
                "VALUES (" + TEST_TRAININGDAY_ID + ", 11), (" + TEST_TRAININGDAY_ID + ", 12)");
        db.close();

        List<Integer> expectedIds = Arrays.asList(TEST_EXERCISE_ID, 11, 12);
        List<Integer> result = repository.getExerciseIdsForTrainingday(TEST_TRAININGDAY_ID);

        assertEquals(expectedIds.size(), result.size());
        assertTrue(result.containsAll(expectedIds));
    }

    @Test
    public void testGetExerciseIdsForNonExistentTrainingday() {
        List<Integer> result = repository.getExerciseIdsForTrainingday(NON_EXISTENT_ID);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testMultipleAssignmentsForSameExercise() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Add duplicate exercise assignment
        db.execSQL("INSERT INTO TrainingdayExerciseAssignment(trainingday_id, exercise_id) " +
                "VALUES (" + TEST_TRAININGDAY_ID + ", " + TEST_EXERCISE_ID + ")");
        db.close();

        List<Integer> result = repository.getExerciseIdsForTrainingday(TEST_TRAININGDAY_ID);
        assertEquals(2, result.stream().filter(id -> id == TEST_EXERCISE_ID).count());
    }

    @Test
    public void testDeleteTrainingdayExerciseAssignment() {
        // Sicherstellen, dass der Eintrag existiert
        TrainingdayExerciseAssignment assignment = repository.getTrainingdayExcerciseAssignments(TEST_TRAININGDAY_ID);
        assertNotNull(assignment);

        // Eintrag löschen
        repository.deleteTrainingdayExerciseAssignment(assignment.getId());

        // Sicherstellen, dass der Eintrag nicht mehr existiert
        TrainingdayExerciseAssignment deletedAssignment = repository.getTrainingdayExcerciseAssignments(TEST_TRAININGDAY_ID);
        assertNull(deletedAssignment);
    }

    @Test
    public void testAddTrainingExerciseAssignment() {
        // Test mit existierender Übung
        long result = repository.addTrainingExerciseAssignment(TEST_TRAININGDAY_ID, TEST_EXERCISE_ID);
        assertTrue("Insert sollte erfolgreich sein", result > 0);

        // Test mit nicht existierender Übung
        long invalidResult = repository.addTrainingExerciseAssignment(TEST_TRAININGDAY_ID, NON_EXISTENT_ID);
        assertEquals("Insert sollte fehlschlagen", -1, invalidResult);

        // Überprüfung der Datenkonsistenz
        List<Integer> exercises = repository.getExerciseIdsForTrainingday(TEST_TRAININGDAY_ID);
        assertTrue("Neue Übung sollte hinzugefügt sein", exercises.contains(TEST_EXERCISE_ID));
    }
}