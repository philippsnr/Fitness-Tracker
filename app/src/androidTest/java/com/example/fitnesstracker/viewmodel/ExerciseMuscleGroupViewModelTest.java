package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.viewmodel.ExerciseMuscleGroupViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ExerciseMuscleGroupViewModelTest {

    private Application application;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Before
    public void setUp() {
        application = ApplicationProvider.getApplicationContext();
        dbHelper = new DatabaseHelper(application);
        db = dbHelper.getWritableDatabase();

        db.execSQL("DELETE FROM ExerciseMuscleGroupAssignment");

        db.execSQL("INSERT INTO ExerciseMuscleGroupAssignment (Exercise_id, MuscleGroup_id) VALUES (1, 1)");
    }

    @After
    public void tearDown() {
        db.execSQL("DELETE FROM ExerciseMuscleGroupAssignment");
        db.close();
    }

    @Test
    public void testLoadMuscleGroups() throws InterruptedException {
        ExerciseMuscleGroupViewModel viewModel = new ExerciseMuscleGroupViewModel(application);
        final CountDownLatch latch = new CountDownLatch(1);

        viewModel.loadMuscleGroups(1, muscleGroups -> {
            try {
                assertNotNull("Die MuscleGroup-Liste sollte nicht null sein", muscleGroups);
                assertFalse("Die MuscleGroup-Liste sollte mindestens einen Eintrag enthalten", muscleGroups.isEmpty());

                assertEquals("Es sollte genau ein Eintrag vorhanden sein", 1, muscleGroups.size());
                assertEquals("Die MuscleGroup-ID sollte 1 sein", Integer.valueOf(1), muscleGroups.get(0));
            } finally {
                latch.countDown();
            }
        });

        boolean awaitSuccess = latch.await(5, TimeUnit.SECONDS);
        assertTrue("Callback wurde nicht innerhalb des Zeitlimits aufgerufen", awaitSuccess);
    }
}
