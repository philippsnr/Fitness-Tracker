package com.example.fitnesstracker.viewmodel;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.MuscleGroup;
import com.example.fitnesstracker.viewmodel.MuscleGroupViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MuscleGroupViewModelTest {

    private Application application;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Before
    public void setUp() {
        application = ApplicationProvider.getApplicationContext();
        dbHelper = new DatabaseHelper(application);
        db = dbHelper.getWritableDatabase();

        db.execSQL("DELETE FROM MuscleGroup");

        db.execSQL("INSERT INTO MuscleGroup (id, name, picture_path) VALUES (1, 'Brust', 'path_brust')");
    }

    @After
    public void tearDown() {
        db.execSQL("DELETE FROM MuscleGroup");
        db.close();
    }

    @Test
    public void testLoadMuscleGroups() throws InterruptedException {
        MuscleGroupViewModel viewModel = new MuscleGroupViewModel(application);
        final CountDownLatch latch = new CountDownLatch(1);

        viewModel.loadMuscleGroups(muscleGroups -> {
            try {
                assertNotNull("Die MuscleGroup-Liste sollte nicht null sein", muscleGroups);
                assertFalse("Die MuscleGroup-Liste sollte mindestens einen Eintrag enthalten", muscleGroups.isEmpty());

                MuscleGroup muscleGroup = muscleGroups.get(0);
                assertEquals("Die ID der MuscleGroup stimmt nicht überein", 1, muscleGroup.getId());
                assertEquals("Der Name der MuscleGroup stimmt nicht überein", "Brust", muscleGroup.getName());
                assertEquals("Der picturePath stimmt nicht überein", "path_brust", muscleGroup.getPicturePath());
            } finally {
                latch.countDown();
            }
        });

        boolean awaitSuccess = latch.await(5, TimeUnit.SECONDS);
        assertTrue("Callback wurde nicht innerhalb des Zeitlimits aufgerufen", awaitSuccess);
    }
}
