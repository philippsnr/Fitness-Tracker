package com.example.fitnesstracker.repository;

import static org.junit.Assert.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.Trainingday;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TrainingdayRepositoryTest {

    private TrainingdayRepository repository;
    private DatabaseHelper dbHelper;
    private static final int TEST_PLAN_ID = 1;
    private static final String TEST_DAY_NAME = "Test Training Day";

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        dbHelper = new DatabaseHelper(context);

        // Clean database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM Trainingday");
        db.execSQL("DELETE FROM Trainingplan");

        // Insert test training plan
        db.execSQL("INSERT INTO Trainingplan(id, name, isActive) VALUES (" + TEST_PLAN_ID + ", 'Test Plan', 0)");
        db.close();

        repository = new TrainingdayRepository(context);
    }

    @After
    public void tearDown() {
        dbHelper.close();
    }

    @Test
    public void testCreateAndGetTrainingdays() {
        // Create test training day
        Trainingday newDay = new Trainingday(0, TEST_DAY_NAME, TEST_PLAN_ID);
        repository.createTrainingday(newDay);

        // Retrieve training days
        List<Trainingday> days = repository.getTrainingdaysForPlan(TEST_PLAN_ID);

        assertFalse(days.isEmpty());
        assertEquals(TEST_DAY_NAME, days.get(0).getName());
        assertTrue(days.get(0).getId() > 0);
    }

    @Test
    public void testUpdateTrainingday() {
        // Create and retrieve day
        Trainingday day = new Trainingday(0, "Old Name", TEST_PLAN_ID);
        repository.createTrainingday(day);
        day = repository.getTrainingdaysForPlan(TEST_PLAN_ID).get(0);

        // Update day
        day.setName("New Name");
        repository.updateTrainingday(day);

        // Verify update
        Trainingday updatedDay = repository.getTrainingdaysForPlan(TEST_PLAN_ID).get(0);
        assertEquals("New Name", updatedDay.getName());
    }

    @Test
    public void testDeleteTrainingday() {
        // Create and verify day
        Trainingday day = new Trainingday(0, TEST_DAY_NAME, TEST_PLAN_ID);
        repository.createTrainingday(day);
        assertFalse(repository.getTrainingdaysForPlan(TEST_PLAN_ID).isEmpty());

        // Delete day
        day = repository.getTrainingdaysForPlan(TEST_PLAN_ID).get(0);
        repository.deleteTrainingday(day);

        // Verify deletion
        assertTrue(repository.getTrainingdaysForPlan(TEST_PLAN_ID).isEmpty());
    }

    @Test
    public void testGetTrainingdaysForNonExistentPlan() {
        List<Trainingday> result = repository.getTrainingdaysForPlan(999);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testMultipleTrainingdaysForPlan() {
        repository.createTrainingday(new Trainingday(0, "Day 1", TEST_PLAN_ID));
        repository.createTrainingday(new Trainingday(0, "Day 2", TEST_PLAN_ID));

        List<Trainingday> result = repository.getTrainingdaysForPlan(TEST_PLAN_ID);
        assertEquals(2, result.size());
    }
}