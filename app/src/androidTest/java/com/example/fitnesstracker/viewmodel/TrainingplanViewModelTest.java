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
    private final CountDownLatch latch = new CountDownLatch(1);

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

    @Test
    public void testAddAndGetAllTrainingplans() throws InterruptedException {
        // Arrange
        Trainingplan plan = new Trainingplan("Test Plan", false);

        // Act
        viewModel.addTrainingplan(plan, () -> latch.countDown(), e -> fail());
        latch.await(2, TimeUnit.SECONDS);

        // Assert
        viewModel.loadAllTrainingplans(
                plans -> {
                    assertEquals(1, plans.size());
                    assertEquals("Test Plan", plans.get(0).getName());
                    latch.countDown();
                },
                e -> fail()
        );
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    public void testSetAndGetActiveTrainingplan() throws InterruptedException {
        // Arrange
        Trainingplan plan1 = new Trainingplan("Plan 1", false);
        Trainingplan plan2 = new Trainingplan("Plan 2", false);
        repository.addTrainingplan(plan1);
        repository.addTrainingplan(plan2);

        // Act
        viewModel.setActiveTrainingplan(plan2.getId(), () -> latch.countDown(), e -> fail());
        latch.await(2, TimeUnit.SECONDS);

        // Assert
        viewModel.loadActiveTrainingplan(
                activePlan -> {
                    assertNotNull(activePlan);
                    assertEquals(plan2.getId(), activePlan.getId());
                    assertTrue(activePlan.getIsActive());
                    latch.countDown();
                },
                e -> fail()
        );
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    public void testUpdateTrainingplan() throws InterruptedException {
        // Arrange
        Trainingplan plan = new Trainingplan("Original Name", false);
        repository.addTrainingplan(plan);
        plan.setName("Updated Name");

        // Act
        viewModel.updateTrainingplan(plan, () -> latch.countDown(), e -> fail());
        latch.await(2, TimeUnit.SECONDS);

        // Assert
        viewModel.loadAllTrainingplans(
                plans -> {
                    assertEquals(1, plans.size());
                    assertEquals("Updated Name", plans.get(0).getName());
                    latch.countDown();
                },
                e -> fail()
        );
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    public void testDeleteTrainingplan() throws InterruptedException {
        // Arrange
        Trainingplan plan = new Trainingplan("To Delete", false);
        repository.addTrainingplan(plan);

        // Act
        viewModel.deleteTrainingplan(plan, () -> latch.countDown(), e -> fail());
        latch.await(2, TimeUnit.SECONDS);

        // Assert
        viewModel.loadAllTrainingplans(
                plans -> {
                    assertTrue(plans.isEmpty());
                    latch.countDown();
                },
                e -> fail()
        );
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    public void testOnlyOneActivePlan() throws InterruptedException {
        // Arrange
        Trainingplan plan1 = new Trainingplan("Plan 1", true);
        Trainingplan plan2 = new Trainingplan("Plan 2", false);
        repository.addTrainingplan(plan1);
        repository.addTrainingplan(plan2);

        // Act
        viewModel.setActiveTrainingplan(plan2.getId(), () -> latch.countDown(), e -> fail());
        latch.await(2, TimeUnit.SECONDS);

        // Assert
        viewModel.loadAllTrainingplans(
                plans -> {
                    for (Trainingplan p : plans) {
                        if (p.getId() == plan2.getId()) {
                            assertTrue(p.getIsActive());
                        } else {
                            assertFalse(p.getIsActive());
                        }
                    }
                    latch.countDown();
                },
                e -> fail()
        );
        latch.await(2, TimeUnit.SECONDS);
    }
}