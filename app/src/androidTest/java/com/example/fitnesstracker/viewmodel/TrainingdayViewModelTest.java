package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.Trainingday;
import com.example.fitnesstracker.viewmodel.TrainingdayViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class TrainingdayViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private TrainingdayViewModel viewModel;
    private Context context;
    private DatabaseHelper dbHelper;

    private static final String DATABASE_NAME = "database.db";

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        // Lösche und erstelle eine neue Datenbank für jeden Test
        context.deleteDatabase(DatabaseHelper.DATABASE_NAME);
        dbHelper = new DatabaseHelper(context);
        viewModel = new TrainingdayViewModel((Application) context.getApplicationContext());
    }

    @After
    public void tearDown() {
        dbHelper.close();
    }

    @Test
    public void testCreateAndGetTrainingdays() throws InterruptedException {
        // Testdaten
        int planId = (int) System.currentTimeMillis();
        Trainingday testDay = new Trainingday(0,"Test Tag", planId);

        // Erstelle Trainingstag
        CountDownLatch latch = new CountDownLatch(1);
        viewModel.createTrainingday(testDay, new TrainingdayViewModel.OnOperationCompleteListener() {
            @Override
            public void onComplete() {
                latch.countDown();
            }

            @Override
            public void onError(Exception exception) {
                fail("Operation failed: " + exception.getMessage());
            }
        });
        latch.await(2, TimeUnit.SECONDS);

        // Hole Trainingstage
        CountDownLatch dataLatch = new CountDownLatch(1);
        viewModel.getTrainingdaysForPlan(planId, new TrainingdayViewModel.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(List<Trainingday> trainingdays) {
                assertEquals(1, trainingdays.size());
                Trainingday savedDay = trainingdays.get(0);
                assertEquals("Test Tag", savedDay.getName());
                assertEquals(planId, savedDay.getTrainingplanId());
                dataLatch.countDown();
            }
        });
        dataLatch.await(2, TimeUnit.SECONDS);
    }

    @Test
    public void testUpdateTrainingday() throws InterruptedException {
        // Vorbereitung
        Trainingday testDay = createTestDay("Vor Update", 1);

        // Update durchführen
        testDay.setName("Nach Update");
        CountDownLatch latch = new CountDownLatch(1);
        viewModel.updateTrainingday(testDay, new TrainingdayViewModel.OnOperationCompleteListener() {
            @Override
            public void onComplete() {
                latch.countDown();
            }

            @Override
            public void onError(Exception exception) {
                fail("Update failed: " + exception.getMessage());
            }
        });
        latch.await(2, TimeUnit.SECONDS);

        // Überprüfung
        verifyDayName(testDay.getId(), "Nach Update");
    }

    @Test
    public void testDeleteTrainingday() throws InterruptedException {
        // Vorbereitung mit eindeutiger Plan-ID
        int uniquePlanId = (int) System.currentTimeMillis();
        Trainingday testDay = createTestDay("Zu löschend " + System.currentTimeMillis(), uniquePlanId);

        // Löschung durchführen
        CountDownLatch deleteLatch = new CountDownLatch(1);
        viewModel.deleteTrainingday(testDay, new TrainingdayViewModel.OnOperationCompleteListener() {
            @Override
            public void onComplete() {
                deleteLatch.countDown();
            }

            @Override
            public void onError(Exception exception) {
                fail("Delete failed: " + exception.getMessage());
            }
        });
        assertTrue(deleteLatch.await(5, TimeUnit.SECONDS));

        // Überprüfung
        CountDownLatch verifyLatch = new CountDownLatch(1);
        viewModel.getTrainingdaysForPlan(uniquePlanId, new TrainingdayViewModel.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(List<Trainingday> trainingdays) {
                // Prüfe spezifisch ob unser Test-Tag gelöscht wurde
                boolean found = false;
                for (Trainingday day : trainingdays) {
                    if (day.getId() == testDay.getId()) {
                        found = true;
                        break;
                    }
                }
                assertFalse("Der gelöschte Tag sollte nicht mehr in der Datenbank sein", found);
                verifyLatch.countDown();
            }
        });
        assertTrue(verifyLatch.await(5, TimeUnit.SECONDS));
    }

    private Trainingday createTestDay(String name, int planId) throws InterruptedException {
        Trainingday testDay = new Trainingday(0, name, planId);
        CountDownLatch createLatch = new CountDownLatch(1);
        viewModel.createTrainingday(testDay, new TrainingdayViewModel.OnOperationCompleteListener() {
            @Override
            public void onComplete() {
                createLatch.countDown();
            }

            @Override
            public void onError(Exception exception) {
                fail("Setup failed: " + exception.getMessage());
            }
        });
        createLatch.await(2, TimeUnit.SECONDS);

        // Hole die generierte ID aus der Datenbank
        List<Trainingday> days = getDaysFromDb(planId);
        return days.get(0);
    }

    private void verifyDayName(int dayId, String expectedName) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        viewModel.getTrainingdaysForPlan(1, new TrainingdayViewModel.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(List<Trainingday> trainingdays) {
                Trainingday updatedDay = trainingdays.stream()
                        .filter(d -> d.getId() == dayId)
                        .findFirst()
                        .orElse(null);
                assertNotNull(updatedDay);
                assertEquals(expectedName, updatedDay.getName());
                latch.countDown();
            }
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    private List<Trainingday> getDaysFromDb(int planId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Trainingday> days = new ArrayList<>();

        Cursor cursor = db.query("Trainingday",
                null,
                "Trainingplan_id = ?",
                new String[]{String.valueOf(planId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                days.add(new Trainingday(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("Trainingplan_id"))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return days;
    }
}