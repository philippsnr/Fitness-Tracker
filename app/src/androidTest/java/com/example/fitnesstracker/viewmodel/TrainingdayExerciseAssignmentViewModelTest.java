package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnesstracker.database.DatabaseHelper;
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

    private static class TestableRepository extends TrainingdayExerciseAssignmentRepository {
        private DatabaseHelper testDbHelper;

        public TestableRepository(Context context) {
            super(context);
        }

        public void setTestDbHelper(DatabaseHelper helper) {
            this.testDbHelper = helper;
        }

        @Override
        protected DatabaseHelper getDbHelper() {
            return testDbHelper != null ? testDbHelper : super.getDbHelper();
        }
    }

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        dbHelper = new DatabaseHelper(context) {
            @Override
            public void onConfigure(SQLiteDatabase db) {
                super.onConfigure(db);
                db.setForeignKeyConstraintsEnabled(true);
            }
        };

        resetDatabase();

        TestableRepository testRepo = new TestableRepository(context);
        testRepo.setTestDbHelper(dbHelper);

        viewModel = new TrainingdayExerciseAssignmentViewModel((Application) context) {
            @Override
            protected TrainingdayExerciseAssignmentRepository createRepository(Application application) {
                return testRepo;
            }
        };
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
    public void testAddAndGetExerciseAssignment() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        viewModel.addTrainingExerciseAssignment(1, 1, newId -> {
            assertTrue(newId > 0);
            latch.countDown();
        });
        assertTrue(latch.await(2, TimeUnit.SECONDS));

        CountDownLatch getLatch = new CountDownLatch(1);
        viewModel.getExerciseIdsForTrainingday(1, ids -> {
            assertEquals(1, ids.size());
            assertEquals(Integer.valueOf(1), ids.get(0));
            getLatch.countDown();
        });
        assertTrue(getLatch.await(2, TimeUnit.SECONDS));
    }

    @Test
    public void testDeleteExerciseAssignment() throws InterruptedException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES (1, 1)");
        Cursor c = db.rawQuery("SELECT id FROM TrainingdayExerciseAssignment", null);
        c.moveToFirst();
        int id = c.getInt(0);
        c.close();
        db.close();

        CountDownLatch deleteLatch = new CountDownLatch(1);
        viewModel.deleteTrainingdayExerciseAssignment(id, () -> deleteLatch.countDown());
        assertTrue(deleteLatch.await(2, TimeUnit.SECONDS));

        CountDownLatch verifyLatch = new CountDownLatch(1);
        viewModel.getExerciseIdsForTrainingday(1, ids -> {
            assertTrue(ids.isEmpty());
            verifyLatch.countDown();
        });
        assertTrue(verifyLatch.await(2, TimeUnit.SECONDS));
    }

    @Test
    public void testAddAssignmentWithInvalidTrainingday() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        viewModel.addTrainingExerciseAssignment(999, 1, newId -> {
            assertEquals(-1L, newId.longValue());
            latch.countDown();
        });
        assertTrue(latch.await(2, TimeUnit.SECONDS));
    }

    @Test
    public void testAddAssignmentWithInvalidExercise() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        viewModel.addTrainingExerciseAssignment(1, 999, newId -> {
            assertEquals(-1L, newId.longValue());
            latch.countDown();
        });
        assertTrue(latch.await(2, TimeUnit.SECONDS));
    }
}