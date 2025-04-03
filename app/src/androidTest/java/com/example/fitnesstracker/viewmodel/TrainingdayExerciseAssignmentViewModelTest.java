package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.TrainingdayExerciseAssignment;
import com.example.fitnesstracker.repository.TrainingdayExerciseAssignmentRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class TrainingdayExerciseAssignmentViewModelTest {
    private TrainingdayExerciseAssignmentViewModel viewModel;
    private DatabaseHelper dbHelper;
    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        dbHelper = new DatabaseHelper(context);

        resetDatabase();
        viewModel = new TrainingdayExerciseAssignmentViewModel((Application) context);
    }

    private void resetDatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM TrainingdayExerciseAssignment");
        db.execSQL("DELETE FROM Trainingday");
        db.execSQL("DELETE FROM Exercise");
        db.execSQL("DELETE FROM Trainingplan");

        db.execSQL("INSERT INTO Trainingplan (id, name, isActive) VALUES (1, 'Test Plan', 1)");
        db.execSQL("INSERT INTO Exercise (id, name, difficulty, info, picture_path) VALUES (1, 'Test Exercise', 1, 'Test info', 'test.jpg')");
        db.execSQL("INSERT INTO Trainingday (id, name, Trainingplan_id) VALUES (1, 'Test Day', 1)");
        db.close();
    }

    @After
    public void tearDown() {
        dbHelper.close();
    }

    @Test
    public void testAddAndGetAssignment() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        viewModel.addTrainingExerciseAssignment(1, 1, newId -> {
            assertTrue(newId > 0);

            viewModel.getAssignmentsForTrainingday(1, assignments -> {
                assertEquals(1, assignments.size());
                assertEquals(1, assignments.get(0).getExerciseId());
                latch.countDown();
            });
        });
        assertTrue(latch.await(2, TimeUnit.SECONDS));
    }

    @Test
    public void testDeleteAssignment() throws InterruptedException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES (1, 1)");
        Cursor c = db.rawQuery("SELECT id FROM TrainingdayExerciseAssignment", null);
        c.moveToFirst();
        int id = c.getInt(0);
        c.close();
        db.close();

        CountDownLatch latch = new CountDownLatch(1);
        viewModel.deleteTrainingdayExerciseAssignment(id, () -> {
            viewModel.getAssignmentsForTrainingday(1, assignments -> {
                assertTrue(assignments.isEmpty());
                latch.countDown();
            });
        });
        assertTrue(latch.await(2, TimeUnit.SECONDS));
    }

    @Test
    public void testInvalidAssignment() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);

        viewModel.addTrainingExerciseAssignment(999, 1, newId -> {
            assertEquals(-1L, newId.longValue());
            latch.countDown();
        });

        viewModel.addTrainingExerciseAssignment(1, 999, newId -> {
            assertEquals(-1L, newId.longValue());
            latch.countDown();
        });

        assertTrue(latch.await(2, TimeUnit.SECONDS));
    }
}