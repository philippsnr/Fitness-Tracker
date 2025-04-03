package com.example.fitnesstracker.repository;

import static org.junit.Assert.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.Trainingplan;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TrainingplanRepositoryTest {

    private TrainingplanRepository repository;
    private DatabaseHelper dbHelper;
    private static final String TEST_PLAN_NAME = "Test Plan";
    private static final String UPDATED_NAME = "Updated Plan";

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        dbHelper = new DatabaseHelper(context);
        repository = new TrainingplanRepository(context);
        cleanDatabase();
    }

    @After
    public void tearDown() {
        dbHelper.close();
    }

    private void cleanDatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM Trainingplan");
        db.close();
    }

    @Test
    public void testAddAndGetAllTrainingplans() {
        Trainingplan plan = new Trainingplan(TEST_PLAN_NAME, false);
        repository.addTrainingplan(plan);

        List<Trainingplan> plans = repository.getAllTrainingplans();

        assertFalse(plans.isEmpty());
        assertEquals(TEST_PLAN_NAME, plans.get(0).getName());
        assertTrue(plans.get(0).getId() > 0);
    }

    @Test
    public void testUpdateTrainingplanName() {
        Trainingplan plan = new Trainingplan(TEST_PLAN_NAME, false);
        repository.addTrainingplan(plan);
        plan = repository.getAllTrainingplans().get(0);

        plan.setName(UPDATED_NAME);
        repository.updateTrainingplanName(plan);

        Trainingplan updatedPlan = repository.getAllTrainingplans().get(0);
        assertEquals(UPDATED_NAME, updatedPlan.getName());
    }

    @Test
    public void testDeleteTrainingplan() {
        Trainingplan plan = new Trainingplan(TEST_PLAN_NAME, false);
        repository.addTrainingplan(plan);
        assertFalse(repository.getAllTrainingplans().isEmpty());

        plan = repository.getAllTrainingplans().get(0);
        repository.deleteTrainingplan(plan);

        assertTrue(repository.getAllTrainingplans().isEmpty());
    }

    @Test
    public void testSetActiveTrainingPlan() {
        Trainingplan plan1 = new Trainingplan("Plan 1", false);
        Trainingplan plan2 = new Trainingplan("Plan 2", true);
        repository.addTrainingplan(plan1);
        repository.addTrainingplan(plan2);

        List<Trainingplan> plans = repository.getAllTrainingplans();
        long plan1Id = plans.get(0).getId();
        long plan2Id = plans.get(1).getId();

        repository.setNewActiveTrainingPlan((int) plan1Id);
        Trainingplan activePlan = repository.getActiveTrainingplan();
        assertNotNull(activePlan);
        assertEquals(plan1Id, activePlan.getId());

        List<Trainingplan> trainingplans = repository.getAllTrainingplans();
        Trainingplan secondPlan = trainingplans.get(1);
        assertNotNull(secondPlan);
        assertFalse(secondPlan.getIsActive());
    }

    @Test
    public void testGetActiveTrainingplanWhenNoneExists() {
        assertNull(repository.getActiveTrainingplan());
    }
}