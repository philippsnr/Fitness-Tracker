package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.Trainingplan;
import com.example.fitnesstracker.repository.TrainingplanRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class TrainingplanViewModelTest {
    private TrainingplanViewModel viewModel;
    private TrainingplanRepository repository;
    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        repository = new TrainingplanRepository(context);
        clearDatabase();
        viewModel = new TrainingplanViewModel((Application) context.getApplicationContext());
    }

    @After
    public void tearDown() {
        clearDatabase();
    }

    private void clearDatabase() {
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        db.delete("Trainingplan", null, null);
        db.close();
    }

    private Trainingplan addTestPlanToDb(String name, boolean active) {
        Trainingplan plan = new Trainingplan(name, active);
        repository.addTrainingplan(plan);
        return plan;
    }

    @Test
    public void testAddAndGetAllTrainingplans() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);

        // Arrange
        Trainingplan plan = new Trainingplan("Test Plan", false);

        // Act
        viewModel.addTrainingplan(plan,
                () -> latch.countDown(),
                e -> fail("Add failed: " + e.getMessage())
        );

        // Assert
        viewModel.loadAllTrainingplans(
                plans -> {
                    assertEquals(1, plans.size());
                    assertEquals("Test Plan", plans.get(0).getName());
                    latch.countDown();
                },
                e -> fail("Load failed: " + e.getMessage())
        );

        assertTrue(latch.await(2, TimeUnit.SECONDS));
    }

    @Test
    public void testSetAndGetActiveTrainingplan() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);

        // Arrange
        Trainingplan plan1 = addTestPlanToDb("Plan 1", false);
        Trainingplan plan2 = addTestPlanToDb("Plan 2", false);

        // Act
        viewModel.setActiveTrainingplan(plan2.getId(),
                () -> latch.countDown(),
                e -> fail("Activation failed: " + e.getMessage())
        );

        // Assert
        viewModel.loadActiveTrainingplan(
                activePlan -> {
                    assertNotNull(activePlan);
                    assertEquals(plan2.getId(), activePlan.getId());
                    assertTrue(activePlan.getIsActive());
                    latch.countDown();
                },
                e -> fail("Load active failed: " + e.getMessage())
        );

        assertTrue(latch.await(2, TimeUnit.SECONDS));
    }

    @Test
    public void testUpdateTrainingplan() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);

        // Arrange
        Trainingplan plan = addTestPlanToDb("Original Name", false);
        plan.setName("Updated Name");

        // Act
        viewModel.updateTrainingplan(plan,
                () -> latch.countDown(),
                e -> fail("Update failed: " + e.getMessage())
        );

        // Assert
        viewModel.loadAllTrainingplans(
                plans -> {
                    assertEquals(1, plans.size());
                    assertEquals("Updated Name", plans.get(0).getName());
                    latch.countDown();
                },
                e -> fail("Load failed: " + e.getMessage())
        );

        assertTrue(latch.await(2, TimeUnit.SECONDS));
    }

    @Test
    public void testDeleteTrainingplan() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);

        // Arrange
        Trainingplan plan = addTestPlanToDb("To Delete", false);

        // Act
        viewModel.deleteTrainingplan(plan,
                () -> latch.countDown(),
                e -> fail("Delete failed: " + e.getMessage())
        );

        // Assert
        viewModel.loadAllTrainingplans(
                plans -> {
                    assertTrue(plans.isEmpty());
                    latch.countDown();
                },
                e -> fail("Load failed: " + e.getMessage())
        );

        assertTrue(latch.await(2, TimeUnit.SECONDS));
    }

    @Test
    public void testOnlyOneActivePlan() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);

        // Arrange
        Trainingplan plan1 = addTestPlanToDb("Plan 1", true);
        Trainingplan plan2 = addTestPlanToDb("Plan 2", false);

        // Act
        viewModel.setActiveTrainingplan(plan2.getId(),
                () -> {
                    // Assert after activation
                    viewModel.loadAllTrainingplans(
                            plans -> {
                                assertEquals(2, plans.size());
                                for (Trainingplan p : plans) {
                                    if (p.getId() == plan2.getId()) {
                                        assertTrue("Plan 2 should be active", p.getIsActive());
                                    } else {
                                        assertFalse("Plan 1 should be inactive", p.getIsActive());
                                    }
                                }
                                latch.countDown();
                            },
                            e -> fail("Load failed: " + e.getMessage())
                    );
                    latch.countDown();
                },
                e -> fail("Activation failed: " + e.getMessage())
        );

        assertTrue(latch.await(4, TimeUnit.SECONDS));
    }
}