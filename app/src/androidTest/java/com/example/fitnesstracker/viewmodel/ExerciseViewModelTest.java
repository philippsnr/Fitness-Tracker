package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.Exercise;
import com.example.fitnesstracker.viewmodel.ExerciseViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ExerciseViewModelTest {

    private Application application;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Before
    public void setUp() {
        application = (Application) ApplicationProvider.getApplicationContext();
        dbHelper = new DatabaseHelper(application);
        db = dbHelper.getWritableDatabase();

        db.execSQL("DELETE FROM Exercise");
        db.execSQL("DELETE FROM ExerciseMuscleGroupAssignment");

        db.execSQL("INSERT INTO Exercise (id, name, difficulty, info, picture_path) VALUES (1, 'Squat', 2, 'Testbeschreibung für Squat', 'url_squat')");
        db.execSQL("INSERT INTO ExerciseMuscleGroupAssignment (Exercise_id, MuscleGroup_id) VALUES (1, 1)");
    }

    @After
    public void tearDown() {
        db.execSQL("DELETE FROM Exercise");
        db.execSQL("DELETE FROM ExerciseMuscleGroupAssignment");
        db.close();
    }

    @Test
    public void testLoadExercisesForMuscleGroup() throws InterruptedException {
        ExerciseViewModel viewModel = new ExerciseViewModel(application);
        final CountDownLatch latch = new CountDownLatch(1);

        viewModel.loadExercisesForMuscleGroup(1, exercises -> {
            try {
                assertNotNull("Die Übungsliste sollte nicht null sein", exercises);
                assertFalse("Die Übungsliste sollte mindestens einen Eintrag enthalten", exercises.isEmpty());

                Exercise exercise = exercises.get(0);
                assertEquals("Die ID der Übung stimmt nicht überein", 1, exercise.getId());
                assertEquals("Der Name der Übung stimmt nicht überein", "Squat", exercise.getName());
                assertEquals("Die Schwierigkeit stimmt nicht überein", 2, exercise.getDifficulty());
                assertEquals("Die Beschreibung stimmt nicht überein", "Testbeschreibung für Squat", exercise.getInfo());
                assertEquals("Die Bild-URL stimmt nicht überein", "url_squat", exercise.getPicturePath());
            } finally {
                latch.countDown();
            }
        });

        boolean awaitSuccess = latch.await(5, TimeUnit.SECONDS);
        assertTrue("Callback wurde nicht innerhalb des Zeitlimits aufgerufen", awaitSuccess);
    }
}
